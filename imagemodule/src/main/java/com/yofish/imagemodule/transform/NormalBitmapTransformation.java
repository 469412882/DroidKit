package com.yofish.imagemodule.transform;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

import java.security.MessageDigest;

/**
 * 圆形转换
 *
 * Created by hch on 2017/8/1.
 */

public class NormalBitmapTransformation extends BitmapTransformation {
    @Override
    protected Bitmap transform(@NonNull BitmapPool pool, @NonNull Bitmap toTransform, int outWidth, int outHeight) {
        return toTransform;
    }

    @Override
    public void updateDiskCacheKey(MessageDigest messageDigest) {

    }
}
