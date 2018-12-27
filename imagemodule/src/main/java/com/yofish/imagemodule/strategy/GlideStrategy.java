package com.yofish.imagemodule.strategy;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Looper;
import android.support.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.yofish.imagemodule.ImgOptions;
import com.yofish.imagemodule.okhttpmodule.OnProgressListener;
import com.yofish.imagemodule.okhttpmodule.ProgressManager;
import com.yofish.imagemodule.transform.CircleBitmapTransformation;
import com.yofish.imagemodule.transform.NormalBitmapTransformation;
import com.yofish.imagemodule.transform.RoundBitmapTransformation;
import com.yofish.imagemodule.utils.Utility;

/**
 * glide图片加载
 *
 * Created by hch on 2017/8/1.
 */

public class GlideStrategy extends BaseImgLoaderStrategy {

    private static volatile GlideStrategy mInstance;

    private RequestOptions requestOptions;

    private GlideStrategy() {

    }

    private void resetOptions(ImgOptions options){
        requestOptions = new RequestOptions();
        /**
         * 设置占位图
         */
        if (options.getPlaceHolder() != 0) {
            requestOptions.placeholder(options.getPlaceHolder());
        }
        /**
         * 设置加载错误图
         */
        if (options.getError() != 0) {
            requestOptions.error(options.getError());
        }

        BitmapTransformation transformation = null;
        if (options.getType() != null) {
            switch (options.getType()) {
                case CIRCLE:
                    transformation = new CircleBitmapTransformation();
                    break;
                case ROUND:
                    transformation = new RoundBitmapTransformation();
                    if (options.getRadius() != 0) {
                        ((RoundBitmapTransformation) transformation).setRadius(options.getRadius());
                    }
                    break;
                case NORMAL:
                    transformation = new NormalBitmapTransformation();
                    break;
                default:
                    transformation = new NormalBitmapTransformation();
                    break;
            }
        }
        /**
         * 设置自定义BitmapTransformation
         */
        if(options.getBitmapTransformation() != null) {
            transformation = options.getBitmapTransformation();
        }
        requestOptions.transform(transformation == null ? new NormalBitmapTransformation() : transformation);
    }

    public static GlideStrategy getInstance(){
        if (mInstance == null) {
            synchronized (GlideStrategy.class) {
                if (mInstance == null) {
                    mInstance = new GlideStrategy();
                }
            }
        }
        return mInstance;
    }

    @Override
    public void loadImg(ImgOptions options) {
        RequestOptions requestOptions = null;
        if (options.getRequestOptions() != null) {
            requestOptions = options.getRequestOptions();
        }else {
            resetOptions(options);
            requestOptions = this.requestOptions;
        }

        RequestManager with = Glide.with(options.getImageView().getContext());
        RequestBuilder<Drawable> load = with.load(options.getUrl());
        if (options.getLoadType() != null) {
            try {
                switch (options.getLoadType()) {
                    case URI:
                        load = with.load(options.getUri());
                        break;
                    case PATH:
                        load = with.load(options.getPath());
                        break;
                    case RESOURCEID:
                        load = with.load(options.getResourceId());
                        break;
                    case IMGBYTE:
                        load = with.load(options.getImgByte());
                        break;
                    case URL:
                        load = with.load(options.getUrl());
                        break;
                    case FILE:
                        load = with.load(options.getFile());
                    case BITMAP:
                        load = with.load(options.getBitmap());
                        break;
                }
            } catch (Exception e) {

            }
        }
        load.apply(requestOptions).into(options.getImageView());
    }

    @Override
    public void loadImgWithProgress(final ImgOptions options) {
        resetOptions(options);
        Glide.with(options.getImageView().getContext()).load(options.getUrl()).apply(requestOptions).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                if (options.getListener() != null) {
                    options.getListener().failed();
                }
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                if (options.getListener() != null) {
                    options.getListener().finish();
                }
                return false;
            }
        }).into(options.getImageView());

        /**
         * 利用okhttp拦截器监听图片加载进度
         */
        ProgressManager.addListener(options.getUrl(), new OnProgressListener() {
            @Override
            public void onProgress(boolean isComplete, int percentage, long bytesRead, long totalBytes) {
                if (options.getListener() != null) {
                    options.getListener().progress(isComplete,percentage,bytesRead,totalBytes);
                }
            }
        });
    }

    @Override
    public void resumeRequests(Context context) {
        Glide.with(context).resumeRequests();
    }

    @Override
    public void pauseRequests(Context context) {
        Glide.with(context).pauseRequests();
    }


    @Override
    public void clearImgDiskCache(final Context context) {
        try {
            if (Looper.myLooper() == Looper.getMainLooper()) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.get(context.getApplicationContext()).clearDiskCache();
                    }
                }).start();
            } else {
                Glide.get(context.getApplicationContext()).clearDiskCache();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void clearImgMemoryCache(Context context) {
        try {
            if (Looper.myLooper() == Looper.getMainLooper()) { //只能在主线程执行
                Glide.get(context.getApplicationContext()).clearMemory();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getCacheSize(Context context) {
        try {
            return Utility.getFormatBytes(Utility.getTotalSizeOfFilesInDir(Glide.getPhotoCacheDir(context.getApplicationContext())));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "0B";
    }
}
