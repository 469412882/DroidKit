package com.yofish.kitmodule.wedget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * gif图片圆形处理，抗锯齿
 *
 * Created by hch on 2018/10/8.
 */

@SuppressLint("AppCompatCustomView")
public class GifCircleImageView extends ImageView {
    //画笔
    private Paint mPaint;
    //圆形图片的半径
    private int mRadius;
    //
    private Rect rect;
    //
    private boolean isGif = false;
    public GifCircleImageView(Context context) {
        this(context, null);
    }

    public GifCircleImageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GifCircleImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void setGif(boolean gif){
        isGif = gif;
    }
    private void init(){
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        rect = new Rect();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        getDrawingRect(rect);
        Drawable drawable = getDrawable();
        if (null != drawable && isGif) {
            mRadius = Math.min(rect.width() / 2, rect.height() / 2);
            try {
                //创建一个新的bitmap，大小跟imageview一样
                Bitmap target = Bitmap.createBitmap(rect.width(), rect.height(), Bitmap.Config.ARGB_8888);
                //下面所有的绘图操作均在这个target上面完成
                Canvas canvas2 = new Canvas(target);
                super.onDraw(canvas2);
                //初始化BitmapShader，传入bitmap对象
                BitmapShader bitmapShader = new BitmapShader(target, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
                mPaint.setShader(bitmapShader);
                //画圆形，指定好坐标，半径，画笔
                canvas.drawCircle(rect.centerX(), rect.centerY(), mRadius, mPaint);
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            super.onDraw(canvas);
        }
    }

}
