package com.yofish.imagemodule.strategy;


import android.content.Context;

import com.yofish.imagemodule.ImgOptions;

/**
 * 图片加载基类
 *
 * Created by hch on 2017/8/1.
 */

public abstract class BaseImgLoaderStrategy implements IImgLoaderStrategy {

    @Override
    public void resumeRequests(Context context) {

    }

    @Override
    public void pauseRequests(Context context) {

    }

    @Override
    public void loadImgWithPlaceHolder(ImgOptions options) {

    }

    @Override
    public void loadBigImg(ImgOptions options) {

    }

    @Override
    public void loadRoundImg(ImgOptions options) {

    }

    @Override
    public void loadGif(ImgOptions options) {

    }

    @Override
    public void loadImgLocal(ImgOptions options) {

    }

    @Override
    public void loadImgWithProgress(ImgOptions options) {

    }

    @Override
    public void loadGifWithProgress(ImgOptions options) {

    }

}
