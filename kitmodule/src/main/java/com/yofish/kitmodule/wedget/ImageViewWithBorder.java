package com.yofish.kitmodule.wedget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.yofish.kitmodule.R;


/**
 * 带边框的Imageview
 * <p>
 * Created by hch on 2018/10/8.
 */

public class ImageViewWithBorder extends android.support.v7.widget.AppCompatImageView {

    private Paint mBorderPaint;
    private Paint mPaint;

    private boolean isCircle;
    private boolean haveBorder;
    private float borderWidth;
    private int borderColor;

    private Rect rect = new Rect();
    private RectF rectF = new RectF();

    public ImageViewWithBorder(Context context) {
        this(context, null);
    }

    public ImageViewWithBorder(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray array = getContext().obtainStyledAttributes(attrs, R.styleable.ImageViewWithBorder);
            isCircle = array.getBoolean(R.styleable.ImageViewWithBorder_circle, false);
            haveBorder = array.getBoolean(R.styleable.ImageViewWithBorder_border, false);
            borderWidth = array.getDimensionPixelSize(R.styleable.ImageViewWithBorder_border_width, 1);
            borderColor = array.getColor(R.styleable.ImageViewWithBorder_border_color, getResources().getColor(android.R.color.white));
            array.recycle();
        }
        mBorderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBorderPaint.setStyle(Paint.Style.STROKE);
        mBorderPaint.setColor(borderColor);
        mBorderPaint.setStrokeWidth(borderWidth);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        getDrawingRect(rect);
        BitmapDrawable drawable = null;
        try {
            drawable = (BitmapDrawable) getDrawable();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (drawable != null && null != drawable.getBitmap() && isCircle) {
            int mRadius = Math.min(rect.width() / 2, rect.height() / 2);
            try {
                //初始化BitmapShader，传入bitmap对象
                BitmapShader bitmapShader = new BitmapShader(drawable.getBitmap(), Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
                mPaint.setShader(bitmapShader);
                //画圆形，指定好坐标，半径，画笔
                canvas.drawCircle(rect.centerX(), rect.centerY(), mRadius, mPaint);
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            super.onDraw(canvas);
        }

        if (haveBorder) {
            rectF.left = rect.left + borderWidth / 2;
            rectF.top = rect.top + borderWidth / 2;
            rectF.right = rect.right - borderWidth / 2;
            rectF.bottom = rect.bottom - borderWidth / 2;
            if (isCircle) {
                float radius = Math.min(rectF.width(), rectF.height()) / (float) 2;
                canvas.drawCircle(rectF.centerX(), rectF.centerY(), radius, mBorderPaint);
            } else {
                canvas.drawRect(rectF, mBorderPaint);
            }
        }
    }
}
