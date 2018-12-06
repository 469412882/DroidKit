package com.yofish.kitmodule.wedget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.style.ImageSpan;

/**
 * 垂直居中的imageSpan，用于处理表情不居中的问题
 *
 * Created by hch on 2018/10/8.
 */

public class CenterVerticalImageSpan extends ImageSpan {

    private float lineSpacingExtra;

    public CenterVerticalImageSpan(Context context, Bitmap b) {
        super(context, b);
    }

    public CenterVerticalImageSpan(Drawable d) {
        super(d);
    }

    public CenterVerticalImageSpan(Drawable d, String source, float lineSpacingExtra) {
        super(d, source);
        this.lineSpacingExtra = lineSpacingExtra;
    }

    @Override
    public int getSize(Paint paint, CharSequence text, int start, int end, Paint.FontMetricsInt fontMetricsInt) {
        Drawable drawable = getDrawable();
        Rect rect = drawable.getBounds();
        if (fontMetricsInt != null) {
            Paint.FontMetricsInt fmPaint = paint.getFontMetricsInt();
            int fontHeight = fmPaint.bottom - fmPaint.top;
            int drHeight = rect.bottom - rect.top;

            int top = drHeight / 2 - fontHeight / 4;
            int bottom = drHeight / 2 + fontHeight / 4;

            fontMetricsInt.ascent = -bottom;
            fontMetricsInt.top = -bottom;
            fontMetricsInt.bottom = top;
            fontMetricsInt.descent = top;
        }
        return rect.right;
    }

    @Override
    public void draw(Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom,
                     Paint paint) {
        Drawable drawable = getDrawable();
        int transY = (int) (((bottom - lineSpacingExtra - top) - drawable.getBounds().bottom) / 2
                        + top);
        canvas.save();
        canvas.translate(x, transY);
        drawable.draw(canvas);
        canvas.restore();
    }
}
