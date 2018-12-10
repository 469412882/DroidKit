package com.yofish.imagemodule;


import com.yofish.imagemodule.strategy.GlideStrategy;
import com.yofish.imagemodule.strategy.IImgLoaderStrategy;

/**
 * 图片加载委托类
 *
 * Created by hch on 2017/8/1.
 */

public class ImgLoaderDelegate {

    private IImgLoaderStrategy iImgLoaderStrategy;

    /**
     * 默认使用Glide策略加载图片
     */
    private ImgLoaderDelegate() {
        iImgLoaderStrategy = getGlideStrategy();
    }

    public static ImgLoaderDelegate getInstance() {
        return Singleton.INSTANCE.getInstance();
    }

    private enum Singleton {
        INSTANCE;
        private ImgLoaderDelegate delegate;

        Singleton() {
            delegate = new ImgLoaderDelegate();
        }

        private ImgLoaderDelegate getInstance() {
            return delegate;
        }
    }

    public IImgLoaderStrategy getLoader() {
        return iImgLoaderStrategy;
    }

    public void setLoader(IImgLoaderStrategy iImgLoaderStrategy) {
        this.iImgLoaderStrategy = iImgLoaderStrategy;
    }

    public GlideStrategy getGlideStrategy() {
        return GlideStrategy.getInstance();
    }
}
