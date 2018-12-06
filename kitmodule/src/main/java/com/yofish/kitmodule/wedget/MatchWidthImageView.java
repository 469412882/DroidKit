package com.yofish.kitmodule.wedget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * 宽度固定，长度按图片比例自适用的ImageView，
 *
 * Created by hch on 2015/12/10.
 */
@SuppressLint("AppCompatCustomView")
public class MatchWidthImageView extends ImageView {

    private boolean matched = false;

    public MatchWidthImageView(Context context) {
        super(context);
    }

    public MatchWidthImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MatchWidthImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!matched) {
            matchWidth(getDrawable());
            matched = true;
        }
    }

    @Override
    public void setImageDrawable(Drawable drawable) {
        super.setImageDrawable(drawable);
        matched = false;
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        super.setImageBitmap(bm);
        matched = false;
    }

    @Override
    public void setImageResource(int resId) {
        super.setImageResource(resId);
        matched = false;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void matchWidth(Drawable drawable){
        if (drawable != null && this.getLayoutParams()!=null) {
            int h = Math.min(getMaxHeight(), (int) ((float) getWidth() / drawable.getIntrinsicWidth() * drawable.getIntrinsicHeight()));
            this.getLayoutParams().height = h;
            this.setLayoutParams(this.getLayoutParams());
        }
    }
}
