package com.yofish.netmodule.retrofit.subscriber;


import com.yofish.netmodule.download.DownloadInfo;
import com.yofish.netmodule.download.DownloadState;
import com.yofish.netmodule.retrofit.download.DownLoadListener.DownloadProgressListener;
import com.yofish.netmodule.retrofit.download.HttpDownloadManager;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * 下载监听
 *
 * Created by hch on 2017/7/4.
 */

public abstract class DownloadSubscriber<T> extends BaseSubscriber<T> implements DownloadProgressListener {
    /* 下载数据 */
    private DownloadInfo downloadInfo;

    public DownloadSubscriber() {

    }

    public void setDownloadInfo(DownloadInfo info) {
        this.downloadInfo = info;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (downloadInfo != null) {
            downloadInfo.setState(DownloadState.START);
        }
    }

    @Override
    public void onCompleted() {
        if (mContext != null) {
            HttpDownloadManager.getInstance().finish(downloadInfo);
        }
    }

    @Override
    public void onError(Throwable e) {
        downloadInfo.setState(DownloadState.ERROR);
        if (mContext != null) {
            HttpDownloadManager.getInstance().error(downloadInfo.getUrl());
        }
    }

    @Override
    public void onNext(T t) {
    }

    public void onPause() {

    }

    @Override
    public void update(long read, long count, boolean done) {
        if (downloadInfo == null) {
            return;
        }
        if (downloadInfo.getCountLength() > count) {
            read = downloadInfo.getCountLength() - count + read;
        } else {
            downloadInfo.setCountLength(count);
        }
        downloadInfo.setReadLength(read);
        /* 接受进度消息，造成UI阻塞，如果不需要显示进度可去掉实现逻辑，减少压力 */
        Observable.just(read).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<Long>() {

            @Override
            public void call(Long aLong) {
                /* 如果暂停或者停止状态延迟，不需要继续发送回调，影响显示 */
                if (downloadInfo.getState() == DownloadState.PAUSE || downloadInfo.getState() == DownloadState.STOP)
                    return;
                downloadInfo.setState(DownloadState.DOWN);
                onProgress((int) ((float) aLong / (float) downloadInfo.getCountLength() * 100), downloadInfo.getCountLength());
            }
        });
    }

    @Override
    public void speedPerSecond(long speed) {
        Observable.just(speed).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<Long>() {
            @Override
            public void call(Long aLong) {
                onSpeed(aLong);
            }
        });
    }

    @Override
    public void pridictFinishSecond(long pridictFinishSecond) {
        Observable.just(pridictFinishSecond).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<Long>() {
            @Override
            public void call(Long aLong) {
                onPridictFinish(aLong);
            }
        });
    }

    public void onSpeed(long speed) {

    }

    public void onPridictFinish(long pridictFinishSecond) {

    }

    public abstract void onProgress(int percent, long total);
}
