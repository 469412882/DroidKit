package com.yofish.imagemodule.strategy;

import android.content.Context;

import com.yofish.imagemodule.ImgOptions;


/**
 * 图片加载的公共方法接口，采用策略模式
 *
 * Created by hch on 2017/8/1.
 */

public interface IImgLoaderStrategy {

    void loadImg(ImgOptions options);

    void loadImgWithPlaceHolder(ImgOptions options);

    void loadRoundImg(ImgOptions options);

    void loadGif(ImgOptions options);

    void loadImgLocal(ImgOptions options);

    void loadImgWithProgress(ImgOptions options);

    void loadGifWithProgress(ImgOptions options);

    void resumeRequests(Context context);

    void pauseRequests(Context context);

    void clearImgDiskCache(Context context);

    void clearImgMemoryCache(Context context);

    String getCacheSize(Context context);
}
