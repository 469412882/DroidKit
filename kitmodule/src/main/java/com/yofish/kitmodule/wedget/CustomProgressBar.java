package com.yofish.kitmodule.wedget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.yofish.kitmodule.R;


/**
 * 自定义进度条，支持圆形的和直线型、实心的和描边的
 * 
 * @author hch
 * @since 2015-7-22
 */
public class CustomProgressBar extends View {

    /** TAG */
    private static final String TAG = "CustomProgressBar";
    /** 进度条的颜色 */
    private int pBarColor;
    /** 进度条的宽度 */
    private float pBarWidth;
    /** 进度条的变化速度，按倍数增加 */
    private int pBarSpeed;
    /** 形状：0、圆形 1、直线 */
    private int shape;
    /** 圆环的颜色 */
    private int annulusColor;
    /** 圆环的宽度 */
    private float annulusWidth;
    /** 描边的颜色 */
    private int strokeColor;
    /** 描边的宽度 */
    private float strokeWidth;
    /** 左内边距 */
    private float paddingLeft;
    /** 上内边距 */
    private float paddingTop;
    /** 右内边距 */
    private float paddingRight;
    /** 下内边距 */
    private float paddingBottom;
    /** view的宽度 */
    private int layoutWidth;
    /** view的高度 */
    private int layoutHeight;
    /** 默认的进度条宽度 */
    private float defaultPbarWidth = 10.0f;
    /** 圆环的矩阵 */
    private RectF annulusBounds;
    /** 外描边的矩阵 */
    private RectF outerStrokeBounds;
    /** 内描边的矩阵 */
    private RectF innerStrokeBounds;
    /** 圆的矩阵 */
    private RectF circleBounds;
    /** 圆环的画笔 */
    private Paint annulusPaint;
    /** 进度条的画笔 */
    private Paint pBarPaint;
    /** 描边的画笔 */
    private Paint storkePaint;
    /** 直线的画笔 */
    private Paint linePaint;
    /** 直线描边的画笔 */
    private Paint lineStrokePaint;
    /** 圆的画笔 */
    private Paint circlePaint;
    /** 圆形进度条的起始角度 */
    private int startDegrees = -90;
    /** 进度刻度 */
    private int progress;
    /** 直线的起始X坐标 */
    private float lineStartX;
    /** 直线的起始Y坐标 */
    private float lineStartY;
    /** 直线结束X的坐标 */
    private float lineStopX;
    /** 直线结束Y的坐标 */
    private float lineStopY;
    /** 直线的长度 */
    private float lineLength;
    /** 字体的颜色 */
    private int textColor;
    /** 字体大小 */
    private float textSize;
    /** %字符大小 */
    private float percentTextSize;
    /** 直线的画笔 */
    private Paint textPaint;
    /** %字符的画笔 */
    private Paint percentTextPaint;
    /** 是否显示百分数 */
    private boolean showText;
    /** 是否是实心的 */
    private boolean isSolid;
    /** 是否要有圆角效果 */
    private boolean isBorderRadius;
    /** text的起始X坐标 */
    private float textStartX;
    /** text的起始Y坐标 */
    private float textStartY;
    /** %和text的间隙 */
    private float percentSpace;
    /** 进度完成后显示的实心圆标识 */
    private boolean showCompleteSolidCircle = false;
    /** 当前角度 */
    private float mCurrentDegree;

    /** 圆形 */
    private static final int SHAPE_CIRCLE = 0;
    /** 直线 */
    private static final int SHAPE_LINE = 1;

    /**
     * 构造
     * 
     * @param context
     * @param attrs
     */
    public CustomProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        parseAttrs(context.obtainStyledAttributes(attrs, R.styleable.CustomProgressBar));
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        layoutWidth = w;
        layoutHeight = h;

        initBounds();
        initPaint();
        initLine();

        invalidate();

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        layoutWidth = getMeasuredWidth();
        layoutHeight = getMeasuredHeight();

        setMeasuredDimension(layoutWidth, layoutHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        switch (shape) {
        case SHAPE_CIRCLE:
            if (strokeWidth > 0) {
                canvas.drawArc(outerStrokeBounds, 360, 360, false, storkePaint);
                canvas.drawArc(innerStrokeBounds, 360, 360, false, storkePaint);
            }
            if (!isSolid) {
                canvas.drawArc(annulusBounds, 360, 360, false, annulusPaint);
                canvas.drawArc(annulusBounds, startDegrees, mCurrentDegree, false, pBarPaint);
                if (isBorderRadius) {
                    drawBorderRadius(canvas, (float) startDegrees);
                    drawBorderRadius(canvas, (float) startDegrees + mCurrentDegree);
                }

            } else {
                if (showCompleteSolidCircle) {
                    circlePaint.setColor(pBarColor);
                    canvas.drawCircle((layoutWidth - paddingLeft) / 2.0f, (layoutHeight - paddingTop) / 2.0f,
                            (layoutWidth - paddingLeft - paddingRight) / 2, circlePaint);
                } else {
                    circlePaint.setColor(annulusColor);
                    canvas.drawArc(annulusBounds, 360, 360, true, circlePaint);
                    circlePaint.setColor(pBarColor);
                    canvas.drawArc(annulusBounds, startDegrees, mCurrentDegree, true, circlePaint);
                }
            }
            if (showText) {
                float textHeight = textPaint.descent() - textPaint.ascent();
                // 计算baseLine的位置
                float verticalTextOffset = (textHeight / 2) - textPaint.descent();
                float horizontalTextOffset = textPaint.measureText(progress + "") / 2.0f
                        + percentTextPaint.measureText("%") / 2.0f;
                textStartX = getWidth() / 2 - horizontalTextOffset;
                textStartY = getHeight() / 2 + verticalTextOffset;
                canvas.drawText(progress + "", textStartX, textStartY, textPaint);
                canvas.drawText("%", this.getWidth() / 2 + textPaint.measureText(progress + "") / 2.0f, textStartY,
                        percentTextPaint);
            }
            break;

        case SHAPE_LINE:
            canvas.drawLine(lineStartX, lineStartY, lineStartX + lineLength, lineStopY, lineStrokePaint);
            canvas.drawLine(lineStartX, lineStartY, lineStartX + (progress / 100.0f) * lineLength, lineStopY, linePaint);
            break;

        default:
            break;
        }

    }

    /**
     * 圆角效果
     * 
     * @param canvas
     * @param degrees
     */
    public void drawBorderRadius(Canvas canvas, float degrees) {
        float radius = (layoutWidth - paddingLeft - paddingRight) / 2.0f - pBarWidth / 2.0f;
        float xOffset = radius * (float) Math.cos(degrees * (Math.PI / 180));
        float yOffset = radius * (float) Math.sin(degrees * (Math.PI / 180));
        float cx = (layoutWidth - paddingLeft - paddingRight) / 2.0f + xOffset;
        float cy = (layoutHeight - paddingTop - paddingBottom) / 2.0f + yOffset;
        float cRadius = pBarWidth / 2.0f;
        canvas.drawCircle(cx, cy, cRadius, circlePaint);
    }

    /**
     * 初始化边界
     */
    public void initBounds() {

        // 获取内边距
        paddingLeft = getPaddingLeft();
        paddingTop = getPaddingTop();
        paddingRight = getPaddingRight();
        paddingBottom = getPaddingBottom();

        // 如果不设置圆环和进度条的宽度，让它们都等于默认值
        if (pBarWidth == 0 && annulusWidth > 0) {
            pBarWidth = annulusWidth;
        } else if (pBarWidth > 0 && annulusWidth == 0) {
            annulusWidth = pBarWidth;
        } else if (pBarWidth == 0 && annulusWidth == 0) {
            pBarWidth = defaultPbarWidth;
            annulusWidth = defaultPbarWidth;
        }

        // 计算每个矩阵的内边距
        float annulusPadding = strokeWidth + annulusWidth / 2.0f;
        float outerStorkePadding = strokeWidth / 2.0f;
        float innerStorkePadding = strokeWidth * 1.5f + annulusWidth;

        // 初始化各个矩阵
        annulusBounds = new RectF(paddingLeft + annulusPadding, paddingTop + annulusPadding, layoutWidth - paddingRight
                - annulusPadding, layoutHeight - paddingBottom - annulusPadding);
        outerStrokeBounds = new RectF(paddingLeft + outerStorkePadding, paddingTop + outerStorkePadding, layoutWidth
                - paddingRight - outerStorkePadding, layoutHeight - paddingBottom - outerStorkePadding);
        innerStrokeBounds = new RectF(paddingLeft + innerStorkePadding, paddingTop + innerStorkePadding, layoutWidth
                - paddingRight - innerStorkePadding, layoutHeight - paddingBottom - innerStorkePadding);
        circleBounds = new RectF(paddingLeft, paddingTop, layoutWidth - paddingRight, layoutHeight - paddingBottom);

    }

    /**
     * 初始化各个画笔
     */
    public void initPaint() {
        storkePaint = new Paint();
        storkePaint.setColor(strokeColor);
        storkePaint.setStyle(Style.STROKE);
        storkePaint.setAntiAlias(true);
        storkePaint.setStrokeWidth(strokeWidth);

        annulusPaint = new Paint();
        annulusPaint.setColor(annulusColor);
        annulusPaint.setStyle(Style.STROKE);
        annulusPaint.setStrokeWidth(annulusWidth);
        annulusPaint.setAntiAlias(true);

        pBarPaint = new Paint();
        pBarPaint.setColor(pBarColor);
        pBarPaint.setStyle(Style.STROKE);
        pBarPaint.setStrokeWidth(pBarWidth);
        pBarPaint.setAntiAlias(true);

        linePaint = new Paint();
        linePaint.setColor(pBarColor);
        linePaint.setStyle(Style.STROKE);
        linePaint.setAntiAlias(true);
        linePaint.setStrokeWidth(pBarWidth);

        lineStrokePaint = new Paint();
        lineStrokePaint.setColor(annulusColor);
        lineStrokePaint.setStyle(Style.STROKE);
        lineStrokePaint.setStrokeWidth(pBarWidth);
        lineStrokePaint.setAntiAlias(true);

        textPaint = new Paint();
        textPaint.setColor(textColor);
        textPaint.setStyle(Style.FILL);
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(textSize);

        percentTextPaint = new Paint();
        percentTextPaint.setColor(textColor);
        percentTextPaint.setStyle(Style.FILL);
        percentTextPaint.setAntiAlias(true);
        percentTextPaint.setTextSize(percentTextSize);

        circlePaint = new Paint();
        circlePaint.setColor(pBarColor);
        circlePaint.setStyle(Style.FILL);
        circlePaint.setAntiAlias(true);

    }

    /**
     * 初始化直线的起始位置
     */
    public void initLine() {
        lineStartX = paddingLeft;
        lineStartY = layoutHeight / 2.0f;
        lineStopX = lineStartX;
        lineStopY = lineStartY;
        lineLength = layoutWidth - paddingLeft - paddingRight;
    }

    /***
     * 获取XML中用户定义的属性值
     * 
     * @param array
     */
    public void parseAttrs(TypedArray array) {
        pBarColor = array.getColor(R.styleable.CustomProgressBar_pbarColor, 0xFFADADAD);// 默认颜色浅灰色
        annulusColor = array.getColor(R.styleable.CustomProgressBar_annulusColor, 0xFF00AEAE);// 默认颜色浅蓝色
        strokeColor = array.getColor(R.styleable.CustomProgressBar_strokeColor, 0xFF9D9D9D);// 默认颜色浅灰色

        pBarWidth = array.getDimension(R.styleable.CustomProgressBar_pbarWidth, 0);
        annulusWidth = array.getDimension(R.styleable.CustomProgressBar_annulusWidth, 0);
        strokeWidth = array.getDimension(R.styleable.CustomProgressBar_strokeWidth, 0);// 默认圆环边的宽度
        shape = array.getInteger(R.styleable.CustomProgressBar_shape, 0);// 默认是圆环
        pBarSpeed = array.getInteger(R.styleable.CustomProgressBar_pbarSpeed, 1);// 默认速度为1%

        startDegrees = array.getInteger(R.styleable.CustomProgressBar_startDegrees, -90);// 圆形进度条的初始角度

        textColor = array.getColor(R.styleable.CustomProgressBar_textColor, 0xFF3C3C3C);
        textSize = array.getDimension(R.styleable.CustomProgressBar_textSize, 20);
        percentTextSize = array.getDimension(R.styleable.CustomProgressBar_percentTextSize, 10);
        percentSpace = array.getDimension(R.styleable.CustomProgressBar_percentSpace, 0);
        showText = array.getBoolean(R.styleable.CustomProgressBar_pshowText, true);// 默认设置显示进度text
        isSolid = array.getBoolean(R.styleable.CustomProgressBar_isSolid, false);// 默认设置不是实心的
        isBorderRadius = array.getBoolean(R.styleable.CustomProgressBar_borderRadius, false);// 默认不设置圆角效果

        array.recycle();
    }

    /**
     * 设置进度，0--100
     * 
     * @param i
     */
    public void setProgress(int i) {
        this.progress = (i % 101);
        this.mCurrentDegree = progress * 3.6f;
        postInvalidate();
    }

    /**
     * 设置进度，0--100
     *
     * @param i
     */
    public void setProgress(float i) {
        this.progress = (int) (i % 101f);
        this.mCurrentDegree = progress * 3.6f;
        postInvalidate();
    }

    public float getProgress(){
        return this.progress;
    }

    /**
     * 设置角度0--360
     * 
     * @param degree
     */
    public void setcurrentDegree(float degree) {
        this.mCurrentDegree = degree;
        this.progress = (int) Math.ceil(mCurrentDegree / 3.6);
        postInvalidate();
    }

    /**
     * 显示实心圆
     */
    public void showCompleteSolidCircle() {
        showCompleteSolidCircle = true;
        postInvalidate();
    }

}
