package com.yofish.kitmodule.wedget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * listview
 *
 * Created by hch on 2018/10/8.
 */
public class InnerListView extends ListView {

    /**
     * 
     * @param context
     *            context
     * @param attrs
     *            attrs
     */
    public InnerListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 
     * @param context
     *            context
     */
    public InnerListView(Context context) {
        super(context);
    }

    /**
     * 
     * @param context
     *            context
     * @param attrs
     *            attrs
     * @param defStyle
     *            Style
     */
    public InnerListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }

}
