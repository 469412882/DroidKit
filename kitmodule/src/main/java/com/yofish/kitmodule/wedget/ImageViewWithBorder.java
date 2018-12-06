package com.yofish.kitmodule.wedget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.yofish.kitmodule.R;


/**
 * 带边框的Imageview
 *
 * Created by hch on 2018/10/8.
 */

public class ImageViewWithBorder extends android.support.v7.widget.AppCompatImageView {

    private Paint mBorderPaint;

    private int mImageShape;

    private Rect rect = new Rect();
    private RectF rectF = new RectF();

    public ImageViewWithBorder(Context context) {
        this(context, null);
    }

    public ImageViewWithBorder(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public void setImageShape(int imageShape){
        this.mImageShape = imageShape;
    }

    private void init(AttributeSet attrs) {
        mBorderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBorderPaint.setStyle(Paint.Style.STROKE);
        mBorderPaint.setColor(getContext().getResources().getColor(R.color.colorDivider));
        mBorderPaint.setStrokeWidth(1);
        if (attrs != null) {
            TypedArray array = getContext().obtainStyledAttributes(attrs, R.styleable.ImageViewWithBorder);
            mImageShape = array.getInt(R.styleable.ImageViewWithBorder_image_shape, 0);
            array.recycle();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.getClipBounds(rect);
        rectF.left = rect.left + 0.5f;
        rectF.top = rect.top + 0.5f;
        rectF.right = rect.right - 0.5f;
        rectF.bottom = rect.bottom - 0.5f;
        super.onDraw(canvas);
        switch (mImageShape) {
            case 0:
                canvas.drawRect(rectF, mBorderPaint);
                break;
            case 1:
                float radius = Math.min(rectF.width(), rectF.height()) / (float) 2;
                canvas.drawCircle(rectF.centerX(), rectF.centerY(), radius, mBorderPaint);
                break;
            default:
                break;
        }
    }
}
