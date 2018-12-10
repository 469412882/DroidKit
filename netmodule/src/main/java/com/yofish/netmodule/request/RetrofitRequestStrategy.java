package com.yofish.netmodule.request;

import android.util.Log;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.trello.rxlifecycle.android.ActivityEvent;
import com.yofish.netmodule.callback.ICallBack;
import com.yofish.netmodule.datatype.StandardData;
import com.yofish.netmodule.download.DownloadInfo;
import com.yofish.netmodule.retrofit.Interceptor.HeaderInterceptor;
import com.yofish.netmodule.retrofit.api.ApiService;
import com.yofish.netmodule.retrofit.download.HttpDownloadManager;
import com.yofish.netmodule.retrofit.func.RetryWhenNetworkException;
import com.yofish.netmodule.retrofit.func.RxActivityTransformer;
import com.yofish.netmodule.retrofit.subscriber.BaseSubscriber;
import com.yofish.netmodule.retrofit.subscriber.DownloadSubscriber;
import com.yofish.netmodule.retrofit.subscriber.ProgressSubscriber;
import com.yofish.netmodule.retrofit.subscriber.UploadSubscriber;
import com.yofish.netmodule.retrofit.subscriber.upload.ProgressRequestBody;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * retrofit请求
 * <p>
 * Created by hch on 2017/6/28.
 */

public class RetrofitRequestStrategy extends BaseRequestStrategy {
    /**
     * 接口
     */
    private ApiService apiService;

    @Override
    public void excuteGet(RequestConfig config) {
        super.excuteGet(config);
        BaseSubscriber subscriber = generatorSubscriber(config.getCallBack());
        apiService = getRetrofit(config).create(ApiService.class);
        apiService
                .excuteGet(config.getBaseUrl())
                .retryWhen(new RetryWhenNetworkException(config.getRetryTime(), config.getRetryDelay()))
                .compose(
                        new RxActivityTransformer<ResponseBody>(config.getRxAppCompatActivity(), ActivityEvent.DESTROY))
                .subscribeOn(Schedulers.io()).unsubscribeOn(Schedulers.io()).map(subscriber.getRxDataMap())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(subscriber);
    }

    @Override
    public void excutePost(RequestConfig config) {
        super.excutePost(config);
        BaseSubscriber subscriber = generatorSubscriber(config.getCallBack());
        apiService = getRetrofit(config).create(ApiService.class);
        String content = config.getParameters() != null ? JSON.toJSONString(config.getParameters()): "";
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), content);
        apiService
                .excutePost(config.getMethod(), body)
                .retryWhen(new RetryWhenNetworkException(config.getRetryTime(), config.getRetryDelay()))
                .compose(
                        new RxActivityTransformer<ResponseBody>(config.getRxAppCompatActivity(), ActivityEvent.DESTROY))
                .subscribeOn(Schedulers.io()).unsubscribeOn(Schedulers.io()).map(subscriber.getRxDataMap())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(subscriber);
        /*apiService
                .excutePost(config.getMethod(), config.getParameters())
                .retryWhen(new RetryWhenNetworkException(config.getRetryTime(), config.getRetryDelay()))
                .compose(
                        new RxActivityTransformer<ResponseBody>(config.getRxAppCompatActivity(), ActivityEvent.DESTROY))
                .subscribeOn(Schedulers.io()).unsubscribeOn(Schedulers.io()).map(subscriber.getRxDataMap())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(subscriber);*/
    }

    @Override
    public void uploadFile(RequestConfig config) {
        super.uploadFile(config);
        apiService = getRetrofit(config).create(ApiService.class);
        Map<String, File> fileMap = config.getFiles();
        if (fileMap == null || fileMap.size() < 1) {
            Toast.makeText(config.getContext(), "文件数量为0", Toast.LENGTH_SHORT).show();
            return;
        }
        String key = null;
        File file = null;
        for (Map.Entry<String, File> entry : fileMap.entrySet()) {
            key = entry.getKey();
            file = entry.getValue();
            break;
        }
        MultipartBody.Part part;
        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        BaseSubscriber subscriber = generatorUploadSubscriber(config.getCallBack());
        if (subscriber instanceof UploadSubscriber) {
            ProgressRequestBody progressRequestBody = new ProgressRequestBody(requestBody,
                    (UploadSubscriber) subscriber);
            part = MultipartBody.Part.createFormData(key, file.getName(), progressRequestBody);
        } else {
            part = MultipartBody.Part.createFormData(key, file.getName(), requestBody);
        }
        apiService
                .upLoadFile(config.getMethod(), part)
                .retryWhen(new RetryWhenNetworkException(config.getRetryTime(), config.getRetryDelay()))
                .compose(
                        new RxActivityTransformer<ResponseBody>(config.getRxAppCompatActivity(), ActivityEvent.DESTROY))
                .subscribeOn(Schedulers.io()).unsubscribeOn(Schedulers.io()).map(subscriber.getRxDataMap())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(subscriber);
    }

    @Override
    public void uploadFiles(RequestConfig config) {
        super.uploadFiles(config);
        apiService = getRetrofit(config).create(ApiService.class);
        Map<String, File> fileMap = config.getFiles();
        if (fileMap == null || fileMap.size() < 1) {
            Toast.makeText(config.getContext(), "文件数量为0", Toast.LENGTH_SHORT).show();
            return;
        }
        Map<String, RequestBody> bodys = new HashMap<>();
        for (Map.Entry<String, File> entry : fileMap.entrySet()) {
            String key = entry.getKey();
            File file = entry.getValue();
            RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            bodys.put(key + "\"; filename=\"" + file.getName(), requestBody);
        }
        if (config.getParameters() != null) {
            for (Map.Entry<String, Object> entry : config.getParameters().entrySet()) {
                bodys.put(entry.getKey(), RequestBody.create(null, String.valueOf(entry.getValue())));
            }
        }
        BaseSubscriber subscriber = generatorBaseSubscriber(config.getCallBack());
        apiService
                .uploadFiles(config.getMethod(), bodys)
                .retryWhen(new RetryWhenNetworkException(config.getRetryTime(), config.getRetryDelay()))
                .compose(
                        new RxActivityTransformer<ResponseBody>(config.getRxAppCompatActivity(), ActivityEvent.DESTROY))
                .subscribeOn(Schedulers.io()).unsubscribeOn(Schedulers.io()).map(subscriber.getRxDataMap())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(subscriber);
    }

    @Override
    public void downloadFile(RequestConfig config) {
        super.downloadFile(config);
        DownloadSubscriber subscriber = generatorDownloadSubscriber(config.getCallBack());
        HttpDownloadManager.getInstance().startDownload(config, subscriber);
    }

    private ProgressSubscriber generatorSubscriber(final ICallBack callBack) {
        return new ProgressSubscriber<String>() {

            @Override
            public void onStart() {
                super.onStart();
                callBack.onStart();
            }

            @Override
            public void onCompleted() {
                super.onCompleted();
                callBack.onComplete();
            }

            @Override
            public void onNext(String data) {
                Log.d("requestData", "请求列表数据完成");
                try {
                    StandardData.successData(callBack, data);
                } catch (Exception e) {
                    e.printStackTrace();
                    onError(e);
                }
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                callBack.onFailed(e.getMessage());
                onCompleted();
            }
        };
    }

    private BaseSubscriber generatorBaseSubscriber(final ICallBack callBack) {
        return new BaseSubscriber<String>() {

            @Override
            public void onStart() {
                super.onStart();
                callBack.onStart();
            }

            @Override
            public void onCompleted() {
                super.onCompleted();
                callBack.onComplete();
            }

            @Override
            public void onNext(String data) {
                try {
                    StandardData.successData(callBack, data);
                } catch (Exception e) {
                    e.printStackTrace();
                    onError(e);
                }
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                callBack.onFailed(e.getMessage());
                onCompleted();
            }
        };
    }

    private UploadSubscriber generatorUploadSubscriber(final ICallBack callBack) {
        return new UploadSubscriber<String>() {

            @Override
            public void onStart() {
                super.onStart();
                callBack.onStart();
            }

            @Override
            public void onCompleted() {
                super.onCompleted();
                callBack.onComplete();
            }

            @Override
            public void onProgress(int percent, long total) {
                callBack.onProgress(percent, total);
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                callBack.onFailed(e.getMessage());
                onCompleted();
            }

            @Override
            public void onNext(String data) {
                StandardData.successData(callBack, data);
            }
        };
    }

    private DownloadSubscriber generatorDownloadSubscriber(final ICallBack callBack) {
        return new DownloadSubscriber<DownloadInfo>() {

            @Override
            public void onStart() {
                super.onStart();
                callBack.onStart();
            }

            @Override
            public void onCompleted() {
                super.onCompleted();
                callBack.onComplete();
            }

            @Override
            public void onProgress(int percent, long total) {
                callBack.onProgress(percent, total);
            }

            @Override
            public void onSpeed(long speed) {
                super.onSpeed(speed);
                callBack.onSpeed(speed);
            }

            @Override
            public void onPridictFinish(long pridictFinishSecond) {
                super.onPridictFinish(pridictFinishSecond);
                callBack.onPridictFinish(pridictFinishSecond);
            }

            @Override
            public void onPause() {
                super.onPause();
                callBack.onPause();
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                callBack.onFailed(e.getMessage());
                onCompleted();
            }

            @Override
            public void onNext(DownloadInfo info) {
                try {
                    callBack.onSuccess(info);
                } catch (Exception e) {
                    e.printStackTrace();
                    onError(e);
                }
            }
        };
    }

    /**
     * retrofit
     *
     * @param config config
     * @return Retrofit
     */
    private Retrofit getRetrofit(RequestConfig config) {
        OkHttpClient httpClient = new OkHttpClient.Builder().addInterceptor(new HeaderInterceptor(config.getHeaders()))
                .connectTimeout(config.getTimeout(), TimeUnit.SECONDS)
                .readTimeout(config.getTimeout(), TimeUnit.SECONDS).writeTimeout(config.getTimeout(), TimeUnit.SECONDS)
                .build();
        return new Retrofit.Builder().client(httpClient).baseUrl(config.getBaseUrl())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create()).build();
    }

    /**
     * 日志输出 自行判定是否添加
     *
     * @return
     */
    private HttpLoggingInterceptor getHttpLoggingInterceptor() {
        // 日志显示级别
        HttpLoggingInterceptor.Level level = HttpLoggingInterceptor.Level.BODY;
        // 新建log拦截器
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                Log.d("RxRetrofit", "Retrofit====Message:" + message);
            }
        });
        loggingInterceptor.setLevel(level);
        return loggingInterceptor;
    }
}
