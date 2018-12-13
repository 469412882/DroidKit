package com.yofish.kitmodule.wedget.refresh.pull2Refresh;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;

import com.yofish.kitmodule.R;
import com.yofish.kitmodule.wedget.refresh.pull2Refresh.header.StoreHouseHeader;
import com.yofish.kitmodule.wedget.refresh.pull2Refresh.util.PtrLocalDisplay;

public class PtrShimmerFrameLayout extends PtrFrameLayout
{
    private StoreHouseHeader storeHouseHeader;
    private boolean mKeepHeader = false;
    //头部text
    private String mShimmerText;
    private int mShimmerTextColor;
    private String e;

    public PtrShimmerFrameLayout(Context paramContext)
    {
        this(paramContext, null, 0);
    }

    public PtrShimmerFrameLayout(Context paramContext, AttributeSet paramAttributeSet)
    {
        this(paramContext, paramAttributeSet, 0);
    }

    public PtrShimmerFrameLayout(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
    {
        super(paramContext, paramAttributeSet, paramInt);
        mShimmerTextColor = getResources().getColor(R.color.colorPrimary);
        TypedArray localTypedArray = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.PtrFrameLayout, 0, 0);
        if (null != paramAttributeSet)
        {
            mKeepHeader = localTypedArray.getBoolean(R.styleable.PtrFrameLayout_ptr_keep_header_float_content, mKeepHeader);
            mShimmerText = localTypedArray.getString(R.styleable.PtrFrameLayout_ptr_header_shimmer_text);
            mShimmerTextColor = localTypedArray.getColor(R.styleable.PtrFrameLayout_ptr_header_shimmer_text_color, getResources().getColor(R.color.colorPrimary));
            this.e = localTypedArray.getString(R.styleable.PtrFrameLayout_ptr_header_shimmer_points_array);
        }
        initHeader();
    }

    private void initHeader()
    {
        storeHouseHeader = new StoreHouseHeader(getContext());
        storeHouseHeader.setTextColor(mShimmerTextColor);
        storeHouseHeader.setPadding(0, PtrLocalDisplay.dp2px(15.0F), 0, PtrLocalDisplay.dp2px(15.0F));
        int i;
        if (TextUtils.isEmpty(this.e)) {
            i = 0;
        } else {
            i = a(getContext(), this.e);
        }
        if (0 != i)
        {
            storeHouseHeader.initWithStringArray(i);
        }
        else
        {
            if (TextUtils.isEmpty(mShimmerText)) {
                mShimmerText = "YOUYU Store";
            }
            this.storeHouseHeader.initWithString(mShimmerText);
        }
        setPinContent(mKeepHeader);
        setHeaderView(storeHouseHeader);
        addPtrUIHandler(storeHouseHeader);
    }

    public StoreHouseHeader getHeader()
    {
        return storeHouseHeader;
    }

    private int a(Context paramContext, String paramString)
    {
        return paramContext.getResources().getIdentifier(paramString, "array", paramContext
                .getPackageName());
    }
}
