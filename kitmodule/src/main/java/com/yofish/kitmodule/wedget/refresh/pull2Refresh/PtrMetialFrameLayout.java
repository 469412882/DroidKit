package com.yofish.kitmodule.wedget.refresh.pull2Refresh;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import com.yofish.kitmodule.R;
import com.yofish.kitmodule.wedget.refresh.pull2Refresh.header.MaterialHeader;
import com.yofish.kitmodule.wedget.refresh.pull2Refresh.util.PtrLocalDisplay;

/**
 * MetialDesign风格的下拉刷新
 */
public class PtrMetialFrameLayout extends PtrFrameLayout
{
    private MaterialHeader materialHeader;
    //刷新时保持头部
    private boolean keepHeader = false;

    public PtrMetialFrameLayout(Context paramContext)
    {
        this(paramContext, null, 0);
    }

    public PtrMetialFrameLayout(Context paramContext, AttributeSet paramAttributeSet)
    {
        this(paramContext, paramAttributeSet, 0);
    }

    public PtrMetialFrameLayout(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
    {
        super(paramContext, paramAttributeSet, paramInt);
        TypedArray localTypedArray = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.PtrFrameLayout, 0, 0);
        if (null != paramAttributeSet) {
            keepHeader = localTypedArray.getBoolean(R.styleable.PtrFrameLayout_ptr_keep_header_float_content, keepHeader);
        }
        initHeader();
    }

    private void initHeader()
    {
        materialHeader = new MaterialHeader(getContext());
        materialHeader.setPadding(0, PtrLocalDisplay.dp2px(15.0F), 0, PtrLocalDisplay.dp2px(15.0F));
        setPinContent(keepHeader);
        setHeaderView(materialHeader);
        addPtrUIHandler(materialHeader);

        initAutoRefresh();
    }

    private void initAutoRefresh() {
        /**
         * 设置自动刷新头部
         */
        postDelayed(new Runnable() {
            @Override
            public void run() {
                autoRefresh(true);
            }
        },100);
    }

    public MaterialHeader getHeader()
    {
        return materialHeader;
    }
}
