package com.yofish.netmodule.retrofit.download;

import android.content.Context;
import android.widget.Toast;

import com.trello.rxlifecycle.android.ActivityEvent;
import com.yofish.netmodule.GlobalAppContext;
import com.yofish.netmodule.download.DownloadInfo;
import com.yofish.netmodule.download.DownloadState;
import com.yofish.netmodule.request.RequestConfig;
import com.yofish.netmodule.retrofit.Interceptor.HeaderInterceptor;
import com.yofish.netmodule.retrofit.api.ApiService;
import com.yofish.netmodule.retrofit.download.DownLoadListener.DownloadInterceptor;
import com.yofish.netmodule.retrofit.func.RetryWhenNetworkException;
import com.yofish.netmodule.retrofit.func.RxActivityTransformer;
import com.yofish.netmodule.retrofit.subscriber.DownloadSubscriber;
import com.yofish.netmodule.utils.Utility;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * 下载文件的manager
 * 
 * Created by hch on 2017/7/4.
 */

public class HttpDownloadManager {
    /* 单例对象 */
    private volatile static HttpDownloadManager INSTANCE;
    /* 记录下载数据 */
    private Set<DownloadInfo> downloadInfos;
    /* 回调sub队列 */
    private HashMap<String, DownloadSubscriber> subMap;
    /* 数据库类 */
    private DbDownloadUtil db;

    private HttpDownloadManager() {
        downloadInfos = new HashSet<>();
        subMap = new HashMap<>();
        db = DbDownloadUtil.getInstance();
    }

    public static HttpDownloadManager getInstance() {
        if (INSTANCE == null) {
            synchronized (HttpDownloadManager.class) {
                if (INSTANCE == null) {
                    INSTANCE = new HttpDownloadManager();
                }
            }
        }
        return INSTANCE;
    }

    /***
     * 开始下载
     * 
     * @param config
     *            config
     * @param subscriber
     *            subscriber
     */
    public void startDownload(RequestConfig config, DownloadSubscriber subscriber) {
        if (config == null) {
            return;
        }
        final DownloadInfo downloadInfo = config.getDownloadInfo();
        if (downloadInfo == null) {
            return;
        }
        /** 正在下载的不能重复下载 */
        if (subMap.get(downloadInfo.getUrl()) != null) {
            Toast.makeText(GlobalAppContext.getContext(), "正在下载...", Toast.LENGTH_SHORT).show();
            return;
        }
        if (downloadInfo.getState().equals(DownloadState.FINISH)
                || (downloadInfo.getCountLength() > 0 && downloadInfo.getCountLength() - downloadInfo.getReadLength() == 0)) {
            subscriber.onNext(downloadInfo);
            return;
        }
        subscriber.setDownloadInfo(downloadInfo);
        subMap.put(downloadInfo.getUrl(), subscriber);
        ApiService downloadService;
        if (downloadInfos.contains(downloadInfo) && downloadInfo.getService() != null) {
            downloadService = downloadInfo.getService();
        } else {
            OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
            clientBuilder.connectTimeout(config.getTimeout(), TimeUnit.SECONDS);
            clientBuilder.readTimeout(config.getTimeout(), TimeUnit.SECONDS);
            clientBuilder.writeTimeout(config.getTimeout(), TimeUnit.SECONDS);
            clientBuilder.addInterceptor(new DownloadInterceptor(subscriber));
            clientBuilder.addInterceptor(new HeaderInterceptor(config.getHeaders()));

            Retrofit retrofit = new Retrofit.Builder().client(clientBuilder.build())
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .baseUrl(Utility.getBasUrl(downloadInfo.getUrl())).build();
            downloadService = retrofit.create(ApiService.class);
            downloadInfo.setService(downloadService);
            downloadInfos.add(downloadInfo);
        }

        downloadService.downloadFile("bytes=" + downloadInfo.getReadLength() + "-", downloadInfo.getUrl())
                .retryWhen(new RetryWhenNetworkException(config.getRetryTime(), config.getRetryDelay()))
                .compose(new RxActivityTransformer<ResponseBody>(config.getRxAppCompatActivity(), ActivityEvent.DESTROY))
                .subscribeOn(Schedulers.io()).unsubscribeOn(Schedulers.io())
                .map(new Func1<ResponseBody, DownloadInfo>() {
                    @Override
                    public DownloadInfo call(ResponseBody responseBody) {
                        try {
                            Utility.writeCache(responseBody, downloadInfo);
                        } catch (IOException e) {
                            throw new RuntimeException(e.getMessage());
                        }
                        return downloadInfo;
                    }
                }).observeOn(AndroidSchedulers.mainThread()).subscribe(subscriber);

    }

    /**
     * 下载失败
     *
     * @param url
     *            url
     */
    public void error(String url) {
        DownloadInfo downloadInfo = getInfoFromSet(url);
        if (downloadInfo == null || downloadInfo.getState().equals(DownloadState.FINISH)) {
            return;
        }
        downloadInfo.setState(DownloadState.ERROR);
        if (subMap.containsKey(downloadInfo.getUrl())) {
            DownloadSubscriber subscriber = subMap.get(downloadInfo.getUrl());
            /** 调用unsubscribe停止下载 */
            subscriber.unsubscribe();
            subMap.remove(downloadInfo.getUrl());
        }
        db.update(downloadInfo);

    }

    /**
     * 重置下载信息，可重新下载
     * 
     * @param info
     */
    public void reset(DownloadInfo info) {
        if (info == null) {
            return;
        }
        info.setState(DownloadState.START);
        info.setReadLength(0);
        info.setCountLength(0);
        db.update(info);
        File file = new File(info.getSavePath());
        if (file.exists()) {
            file.delete();
        }
        subMap.remove(info.getUrl());
        downloadInfos.remove(info);
    }

    /**
     * 暂停下载
     * 
     * @param url
     *            url
     */
    public void pause(String url) {
        DownloadInfo downloadInfo = getInfoFromSet(url);
        if (downloadInfo == null || downloadInfo.getState().equals(DownloadState.FINISH)) {
            return;
        }
        downloadInfo.setState(DownloadState.PAUSE);
        if (subMap.containsKey(downloadInfo.getUrl())) {
            DownloadSubscriber subscriber = subMap.get(downloadInfo.getUrl());
            subscriber.onPause();
            /** 调用unsubscribe停止下载 */
            subscriber.unsubscribe();
            subMap.remove(downloadInfo.getUrl());
        }
        db.update(downloadInfo);
    }

    /**
     * 暂停正在下载的请求
     */
    public void pauseLoading() {
        for (DownloadInfo downloadInfo : downloadInfos) {
            if (downloadInfo.getState().equals(DownloadState.DOWN)) {
                pause(downloadInfo.getUrl());
            }
        }
        subMap.clear();
        downloadInfos.clear();
    }

    /**
     * 获取下载信息，获取顺序1、当前任务列表，2、数据库，3、新建
     * 
     * @param url
     *            url
     * @param savePath
     *            savePath
     * @return DownloadInfo
     */
    public DownloadInfo getDownloadInfo(String url, String savePath) {
        if (url == null)
            return null;
        DownloadInfo info = null;
        info = getInfoFromSet(url);
        if (info == null) {
            info = DbDownloadUtil.getInstance().queryDownByUrl(url);
        }
        if (info == null) {
            info = new DownloadInfo(url, savePath);
            db.insert(info);
        }
        return info;
    }

    /**
     * 从当前的任务列表中获取DownloadInfo
     * 
     * @param url
     *            url
     * @return DownloadInfo
     */
    public DownloadInfo getInfoFromSet(String url) {
        for (DownloadInfo info : downloadInfos) {
            if (info.getUrl().equals(url)) {
                return info;
            }
        }
        return null;
    }

    /**
     * 下载完成
     * 
     * @param info
     *            info
     */
    public void finish(DownloadInfo info) {
        info.setState(DownloadState.FINISH);
        db.update(info);
        subMap.remove(info.getUrl());
        downloadInfos.remove(info);
    }

}
