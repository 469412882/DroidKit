package com.yofish.kitmodule.wedget;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.yofish.kitmodule.R;
import com.yofish.kitmodule.util.Utility;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.ValueAnimator;


/**
 * 圆形百分比view
 *
 * Created by hch on 2018/10/8.
 */

public class CirclePercentView extends View {
    /** 圆弧画笔 */
    private Paint mArcPaint;
    /** 文字画笔 */
    private Paint mTextPaint;
    /** 圆弧颜色 */
    private int mArcColor;
    /** 背景颜色 */
    private int mArcBgColor;
    /** 文字颜色 */
    private int mTextColor;
    /** 圆弧宽度 */
    private int mArcWidth;
    /** 文字大小 */
    private int mTextSize;
    /** 开始角度 */
    private int mStartAngle;
    /** 视图区域 */
    private RectF mRect;
    /** 文字测量 */
    private Rect mTextRect;
    /** 最大百分比 */
    private int mMaxPercent;
    /** 当前百分比 */
    private int mCurrentPercent;
    /** 刻度线文字画笔度量 */
    private Paint.FontMetricsInt fontMetrics;
    private float animatedValue;

    public CirclePercentView(Context context) {
        this(context, null);
    }

    public CirclePercentView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CirclePercentView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray array = getContext().obtainStyledAttributes(attrs, R.styleable.CirclePercentView);
            mArcColor = array.getColor(R.styleable.CirclePercentView_arc_color,
                    getResources().getColor(R.color.colorPrimary));
            mArcBgColor = array.getColor(R.styleable.CirclePercentView_arc_bg_color,
                    getResources().getColor(R.color.colorDivider));
            mTextColor = array.getColor(R.styleable.CirclePercentView_percent_text_color,
                    getResources().getColor(R.color.colorPrimary));
            mArcWidth = array.getDimensionPixelSize(R.styleable.CirclePercentView_arc_width, 1);
            mTextSize = array.getDimensionPixelSize(R.styleable.CirclePercentView_percent_text_size,
                    Utility.dpToPx(8, getContext().getResources()));
            mStartAngle = array.getDimensionPixelSize(R.styleable.CirclePercentView_percent_start_angle, 0-90);
            mMaxPercent = array.getDimensionPixelSize(R.styleable.CirclePercentView_max_percent, 100);
            array.recycle();
        } else {
            mArcColor = getResources().getColor(R.color.colorPrimary);
            mArcBgColor = getResources().getColor(R.color.colorDivider);
            mTextColor = getResources().getColor(R.color.colorPrimary);
            mArcWidth = 1;
            mTextSize = Utility.dpToPx(8, getContext().getResources());
            mMaxPercent = 100;
        }
        mArcPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mArcPaint.setColor(mArcColor);
        mArcPaint.setStyle(Paint.Style.STROKE);
        mArcPaint.setStrokeWidth(mArcWidth);
        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setColor(mTextColor);
        mTextPaint.setStyle(Paint.Style.STROKE);
        mTextPaint.setTextSize(mTextSize);
        mRect = new RectF();
        mTextRect = new Rect();
        fontMetrics = mTextPaint.getFontMetricsInt();
    }

    public void setPercent(int percent) {
        setAnimtion(percent % (mMaxPercent + 1));
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        int radius = Math.min(width - getPaddingLeft() - getPaddingRight(), height - getPaddingTop()
                        - getPaddingBottom()) - mArcWidth;
        int centerX = (int) (getPaddingLeft() + (width - getPaddingRight()) / (float) 2);
        int centerY = (int) (getPaddingTop() + (height - getPaddingBottom()) / (float) 2);
        int halfRadius = (int) (radius / (float) 2);
        mRect.set(centerX - halfRadius, centerY - halfRadius, centerX + halfRadius, centerY + halfRadius);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int sweepAngle = (int) (mCurrentPercent / (float) mMaxPercent * 360);
        mArcPaint.setColor(mArcBgColor);
        canvas.drawArc(mRect, mStartAngle, 360, false, mArcPaint);
        mArcPaint.setColor(mArcColor);
        canvas.drawArc(mRect, mStartAngle, sweepAngle, false, mArcPaint);
        String percentStr = mCurrentPercent + "%";
        mTextPaint.getTextBounds(percentStr, 0, percentStr.length(), mTextRect);
        float baseLine = mRect.centerY() - (fontMetrics.bottom - fontMetrics.top) / 2 - fontMetrics.top;
        canvas.drawText(percentStr, mRect.centerX() - mTextRect.width() / (float) 2, baseLine, mTextPaint);
    }

    private void setAnimtion(int proDataFor360) {
        ValueAnimator valueAnimator = ObjectAnimator.ofInt(0, proDataFor360);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mCurrentPercent = (int) animation.getAnimatedValue();
                invalidate();
            }
        });
        valueAnimator.setDuration(1000);
        valueAnimator.start();
    }

}
