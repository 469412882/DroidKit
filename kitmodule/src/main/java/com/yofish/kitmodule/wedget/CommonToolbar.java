package com.yofish.kitmodule.wedget;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.TintTypedArray;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

import com.yofish.kitmodule.R;

import java.lang.reflect.Field;

/**
 * file description
 * <p>
 * Created by hch on 2018/9/30.
 */

public class CommonToolbar extends Toolbar {

    int gravity = Gravity.NO_GRAVITY;

    public CommonToolbar(Context context) {
        this(context, null);
    }

    public CommonToolbar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CommonToolbar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs, defStyleAttr);
    }

    private void init(AttributeSet attrs, int defStyleAttr){
        this.setBackgroundColor(this.getResources().getColor(R.color.colorToolbarBg));
        this.setPadding(0, this.getResources().getDimensionPixelSize(R.dimen.common_toolbar_padding_top), 0, 0);
        this.setMinimumHeight(this.getResources().getDimensionPixelSize(R.dimen.common_toolbar_height));
        this.setPopupTheme(R.style.DroidKitTheme_ToolbarOverflowMenuStyle);
        TintTypedArray array = TintTypedArray.obtainStyledAttributes(getContext(), attrs, R.styleable.CommonToolbar, defStyleAttr, 0);
        boolean titleCenter = array.getBoolean(R.styleable.CommonToolbar_titleCenter, false);
        if (titleCenter) {
            gravity = Gravity.CENTER;
        } else {
            gravity = Gravity.CENTER_VERTICAL;
        }
    }

    public void notFullScreen(){
        ViewGroup.LayoutParams lp = getLayoutParams();
        lp.height = this.getResources().getDimensionPixelSize(R.dimen.common_toolbar_height_constant);
        setLayoutParams(lp);
        this.setPadding(0, 0, 0, 0);
        this.setMinimumHeight(this.getResources().getDimensionPixelSize(R.dimen.common_toolbar_height_constant));
    }

    @Override
    protected LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        LayoutParams layoutParams = super.generateLayoutParams(p);
        layoutParams.gravity = gravity;
        return layoutParams;
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        LayoutParams layoutParams = new LayoutParams(this.getContext(), attrs);
        layoutParams.gravity = gravity;
        return layoutParams;
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        LayoutParams layoutParams = super.generateDefaultLayoutParams();
        layoutParams.gravity = gravity;
        return layoutParams;
    }

    @Override
    public void setNavigationIcon(@Nullable Drawable icon) {
        super.setNavigationIcon(icon);
        this.setGravityCenter();
    }

    public void setGravityCenter() {
        this.post(new Runnable() {
            public void run() {
                CommonToolbar.this.setCenter("mNavButtonView");
                CommonToolbar.this.setCenter("mMenuView");
            }
        });
    }

    /**
     * 反射强制居中显示
     *
     * @param fieldName 属性名
     */
    private void setCenter(String fieldName) {
        try {
            Field field = this.getClass().getSuperclass().getDeclaredField(fieldName);
            field.setAccessible(true);
            Object mFiled = field.get(this);
            if(mFiled == null) {
                return;
            }

            if(mFiled instanceof View) {
                View view = (View)mFiled;
                android.view.ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
                if(layoutParams instanceof android.support.v7.app.ActionBar.LayoutParams) {
                    android.support.v7.app.ActionBar.LayoutParams lp = (android.support.v7.app.ActionBar.LayoutParams)layoutParams;
                    lp.gravity = this.gravity;
                    view.setLayoutParams(layoutParams);
                }
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }
}
