package com.yofish.imagemodule;

/**
 * 图片加载的监听
 *
 * Created by hch on 2017/8/1.
 */

public interface ImgLoaderListener {
    void start();

    void finish();

    void progress(boolean isComplete, int percentage, long bytesRead, long totalBytes);

    void failed();
}
