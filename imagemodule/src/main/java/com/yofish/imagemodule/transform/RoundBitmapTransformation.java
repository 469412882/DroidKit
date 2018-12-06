package com.yofish.imagemodule.transform;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.support.annotation.NonNull;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

import java.security.MessageDigest;

/**
 * 圆角转换
 *
 * Created by hch on 2017/8/1.
 */

public class RoundBitmapTransformation extends BitmapTransformation {

    private float radius;

    public RoundBitmapTransformation(){
        this(4);
    }

    public RoundBitmapTransformation(int radius){
        super();
        this.radius = Resources.getSystem().getDisplayMetrics().density * radius;
    }

    @Override
    protected Bitmap transform(@NonNull BitmapPool pool, @NonNull Bitmap toTransform, int outWidth, int outHeight) {
        return roundCrop(pool, toTransform);
    }

    @Override
    public void updateDiskCacheKey(MessageDigest messageDigest) {

    }

    private Bitmap roundCrop(BitmapPool pool, Bitmap source){
        if (source == null) return null;
        Bitmap result = pool.get(source.getWidth(), source.getHeight(), Bitmap.Config.RGB_565);
        if (result == null) {
            result = Bitmap.createBitmap(source.getWidth(), source.getHeight(), Bitmap.Config.RGB_565);
        }
        Canvas canvas = new Canvas(result);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setShader(new BitmapShader(source, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
        RectF rectF = new RectF(0, 0, source.getWidth(), source.getHeight());
        canvas.drawRoundRect(rectF, radius, radius, paint);
        return result;
    }
}
