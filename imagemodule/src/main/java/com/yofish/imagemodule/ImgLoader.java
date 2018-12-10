package com.yofish.imagemodule;

import android.content.Context;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.request.RequestOptions;
import com.yofish.imagemodule.strategy.IImgLoaderStrategy;

import java.io.File;

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
     * 图片配置选项
     */
    public void load(ImgOptions options) {
        ImgLoaderDelegate.getInstance().getLoader().loadImg(options);
    }

    /**
     * 加载图片 带有进度回调
     * @param options
     */
    public void loadWithListener(ImgOptions options) {
        ImgLoaderDelegate.getInstance().getLoader().loadImgWithProgress(options);
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
     * 加载图片 带有进度回调
     * @param url
     * @param imageView
     */
    public void loadWithListener(String url, ImageView imageView) {
        ImgLoaderDelegate.getInstance().getLoader().loadImgWithProgress(new ImgOptions(url, imageView));
    }

    /**
     * 暂停加载
     */
    public void pauseRequests(Context context) {
        ImgLoaderDelegate.getInstance().getLoader().pauseRequests(context);
    }

    /**
     * 恢复加载
     */
    public void resumeRequests(Context context) {
        ImgLoaderDelegate.getInstance().getLoader().resumeRequests(context);
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

    /**
     * 设置使用某种加载器加载图片 默认Glide加载
     * @param iImgLoaderStrategy
     */
    public void setImgLoader(IImgLoaderStrategy iImgLoaderStrategy) {
        ImgLoaderDelegate.getInstance().setLoader(iImgLoaderStrategy);
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

        public Builder path(String path) {
            options.setPath(path);
            return this;
        }

        public Builder uri(Uri uri) {
            options.setUri(uri);
            return this;
        }

        public Builder file(File file) {
            options.setFile(file);
            return this;
        }

        public Builder imgByte(byte[] imgByte) {
            options.setImgByte(imgByte);
            return this;
        }

        public Builder resourceId(int resourceId) {
            options.setResourceId(resourceId);
            return this;
        }

        public Builder placeHolder(int placeHolder) {
            options.setPlaceHolder(placeHolder);
            return this;
        }

        public Builder error(int error) {
            options.setError(error);
            return this;
        }

        public Builder bitmapTransformation(BitmapTransformation bitmapTransformation) {
            options.setBitmapTransformation(bitmapTransformation);
            return this;
        }

        /**
         * 图片裁剪类型
         * @param type
         * @return
         */
        public Builder imgType(ImgOptions.ImgType type) {
            options.setType(type);
            return this;
        }

        /**
         * 加载某种图片 默认加载网络图片
         * @param loadType
         * @return
         */
        public Builder loadType(ImgOptions.LoadType loadType) {
            options.setLoadType(loadType);
            return this;
        }

        public Builder radius(int radius) {
            options.setRadius(radius);
            return this;
        }

        public Builder requestOptions(RequestOptions requestOptions) {
            options.setRequestOptions(requestOptions);
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

        public void intoWithListener(ImageView imageView) {
            options.setImageView(imageView);
            getInstance().loadWithListener(options);
        }

    }
}
