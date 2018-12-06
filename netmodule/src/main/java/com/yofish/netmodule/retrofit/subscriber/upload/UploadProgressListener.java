package com.yofish.netmodule.retrofit.subscriber.upload;


/**
 * 上传进度接口
 *
 * Created by hch on 2017/7/7.
 */
public interface UploadProgressListener {
    /**
     * 上传进度
     *
     * @param currentBytesCount 当前读取的字节数
     *
     * @param totalBytesCount 总字节数
     */
    void onProgress(long currentBytesCount, long totalBytesCount);
}
