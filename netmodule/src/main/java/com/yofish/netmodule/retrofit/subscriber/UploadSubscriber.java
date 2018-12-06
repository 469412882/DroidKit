package com.yofish.netmodule.retrofit.subscriber;


import com.yofish.netmodule.retrofit.subscriber.upload.UploadProgressListener;

/**
 * UploadSubscriber 上传的回调
 *
 * Created by hch on 2017/6/30.
 */

public abstract class UploadSubscriber<T> extends BaseSubscriber<T> implements UploadProgressListener {

    public UploadSubscriber() {
        super();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onCompleted() {
        super.onCompleted();
    }

    public void onProgress(long currentBytesCount, long totalBytesCount){
        onProgress((int) ((float)currentBytesCount / (float) totalBytesCount * 100), totalBytesCount);
    }

    public abstract void onProgress(int percent, long total);

}
