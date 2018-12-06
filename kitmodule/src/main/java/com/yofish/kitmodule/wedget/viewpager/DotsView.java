package com.yofish.kitmodule.wedget.viewpager;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

import com.yofish.kitmodule.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 附着在viewpager的小圆点view
 *
 * Created by hch on 2016/10/24.
 */

public class DotsView extends View {
    /** 圆点数 */
    private int mSize;
    /** 命中位置 */
    private int mHitPosition;
    /** 画笔 */
    private Paint mPaint;
    /** 圆点半径 */
    private int mRadius;
    /** 默认颜色 */
    private int mDefaultColor;
    /** 命中颜色 */
    private int mHitColor;
    /** 圆点样式，命中圆点一直都是solid样式，此样式决定未命中圆点 */
    private int mDefaultDotStyle;
    /** 圆点数 */
    private List<Point> mPoints;

    public DotsView(Context context) {
        this(context, null);
    }

    public DotsView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DotsView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mRadius = (int) getContext().getResources().getDimension(R.dimen.dot_radius);
        mPoints = new ArrayList<>();
        if (attrs != null) {
            TypedArray array = getContext().obtainStyledAttributes(attrs, R.styleable.DotsView);
            mDefaultColor = array.getColor(R.styleable.DotsView_default_color, Color.GRAY);
            mHitColor = array.getColor(R.styleable.DotsView_hit_color, Color.WHITE);
            mDefaultDotStyle = array.getInt(R.styleable.DotsView_dot_style, 0);
            array.recycle();
        }
    }

    public void setSize(int size) {
        this.mSize = size;
        if(size < 2) {
            setVisibility(GONE);
        } else {
            setVisibility(VISIBLE);
        }
        invalidate();
    }

    public int getSize() {
        return mSize;
    }

    public void setPosition(int position) {
        if (position > mSize - 1)
            position = mSize - 1;
        if (position < 0)
            position = 0;
        this.mHitPosition = position;
        invalidate();
    }

    public void setViewPager(android.support.v4.view.ViewPager viewPager){
        if (viewPager == null) return;
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (getSize() < 1) {
                    return;
                }
                setPosition(position % getSize());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        /** 设置最小的宽高 */
        int minHeight = 2 * mRadius;
        int minWidth = (2 * mSize - 1) * 2 * mRadius;
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        super.onMeasure(MeasureSpec.makeMeasureSpec(minWidth, widthMode), MeasureSpec.makeMeasureSpec(minHeight, heightMode));

        mPoints.clear();
        int startX = getMeasuredWidth() / 2 - (minWidth - 2 * mRadius) / 2;
        for (int i = 0; i < mSize; i++) {
            mPoints.add(new Point(startX + 2 * i * 2 * mRadius, getMeasuredHeight() / 2));
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i = 0; i < mPoints.size(); i++) {
            if (i == mHitPosition) {
                mPaint.setColor(mHitColor);
                mPaint.setStyle(Paint.Style.FILL);
            } else {
                mPaint.setColor(mDefaultColor);
                switch (mDefaultDotStyle) {
                case 0:
                    mPaint.setStyle(Paint.Style.STROKE);
                    mPaint.setStrokeWidth(1);
                    break;
                case 1:
                    mPaint.setStyle(Paint.Style.FILL);
                    break;
                }
            }
            canvas.drawCircle(mPoints.get(i).x, mPoints.get(i).y, mRadius, mPaint);
        }
    }

}
