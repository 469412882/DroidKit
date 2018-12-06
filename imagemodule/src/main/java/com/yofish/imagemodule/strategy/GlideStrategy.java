package com.yofish.imagemodule.strategy;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Looper;
import android.support.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.yofish.imagemodule.ImgOptions;
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

    private static RequestOptions requestOptions;

    private GlideStrategy() {

    }

    private static void resetOptions(ImgOptions options){
        requestOptions = new RequestOptions();
        if (options.getPlaceHolder() != 0) {
            requestOptions.placeholder(options.getPlaceHolder());
        }
        if (options.getType() != null) {
            BitmapTransformation transformation = null;
            switch (options.getType()) {
                case CIRCLE:
                    transformation = new CircleBitmapTransformation();
                    break;
                case ROUND:
                    transformation = new RoundBitmapTransformation();
                    break;
                case NORMAL:
                    transformation = new NormalBitmapTransformation();
                    break;
                default:
                    transformation = new NormalBitmapTransformation();
                    break;
            }
            requestOptions.transform(transformation);
        }
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
        resetOptions(options);
        Glide.with(options.getImageView().getContext()).load(options.getUrl()).apply(requestOptions)
                .into(options.getImageView());
    }

    @Override
    public void loadImgWithPlaceHolder(ImgOptions options) {
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
    }

    @Override
    public void loadGifWithProgress(ImgOptions options) {

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
