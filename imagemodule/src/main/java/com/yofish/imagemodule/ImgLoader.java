package com.yofish.imagemodule;

import android.content.Context;
import android.widget.ImageView;

/**
 * 图片加载
 *
 * Created by hch on 2017/8/1.
 */

public class ImgLoader {

    private ImgLoader() {

    }

    /**
     * 单元素的枚举类型已经成为实现Singleton的最佳方法
     */
    private enum ImgSingleton {
        INSTANCE;
        private ImgLoader loader;

        // JVM会保证此方法绝对只调用一次
        ImgSingleton() {
            loader = new ImgLoader();
        }

        public ImgLoader getInstance() {
            return loader;
        }
    }

    public static ImgLoader getInstance() {
        return ImgSingleton.INSTANCE.getInstance();
    }

    /**
     * 加载图片
     * 
     * @param options
     *            图片配置选项
     */
    public void load(ImgOptions options) {
        ImgLoaderDelegate.getInstance().getLoader().loadImg(options);
    }

    /**
     * 加载图片
     * 
     * @param url
     *            url
     * @param imageView
     *            imageView
     */
    public void load(String url, ImageView imageView) {
        ImgLoaderDelegate.getInstance().getLoader().loadImg(new ImgOptions(url, imageView));
    }

    /**
     * 清除磁盘缓存
     * 
     * @param context
     *            context
     */
    public void clearImgDiskCache(Context context) {
        ImgLoaderDelegate.getInstance().getLoader().clearImgDiskCache(context);
    }

    /**
     * 清除内存缓存
     * 
     * @param context
     *            context
     */
    public void clearImgMemoryCache(Context context) {
        ImgLoaderDelegate.getInstance().getLoader().clearImgMemoryCache(context);
    }

    /**
     * 获取缓存大小
     * 
     * @param context
     *            context
     * @return String
     */
    public String getCacheSize(Context context) {
        return ImgLoaderDelegate.getInstance().getLoader().getCacheSize(context);
    }

    public static class Builder {

        ImgOptions options;

        public Builder() {
            options = new ImgOptions();
        }

        public Builder url(String url) {
            options.setUrl(url);
            return this;
        }

        public Builder placeHolder(int placeHolder) {
            options.setPlaceHolder(placeHolder);
            return this;
        }

        public Builder path(String path) {
            options.setPath(path);
            return this;
        }

        public Builder imgType(ImgOptions.ImgType type) {
            options.setType(type);
            return this;
        }

        public Builder radius(int radius) {
            options.setRadius(radius);
            return this;
        }

        public Builder listener(ImgLoaderListener listener) {
            options.setListener(listener);
            return this;
        }

        public void into(ImageView imageView) {
            options.setImageView(imageView);
            getInstance().load(options);
        }

    }
}
