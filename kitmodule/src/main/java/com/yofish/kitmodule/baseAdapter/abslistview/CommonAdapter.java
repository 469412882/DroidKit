package com.yofish.kitmodule.baseAdapter.abslistview;

import android.content.Context;

import com.yofish.kitmodule.baseAdapter.abslistview.base.ItemViewDelegate;

import java.util.List;

public abstract class CommonAdapter<T> extends MultiItemTypeAdapter<T> {

    public CommonAdapter(Context context, final int layoutId, List<T> datas) {
        super(context, datas);

        addItemViewDelegate(new ItemViewDelegate<T>() {
            @Override
            public int getItemViewLayoutId() {
                return layoutId;
            }

            @Override
            public boolean isForViewType(T item, int position) {
                return true;
            }

            @Override
            public void convert(ViewHolder holder, T t, int position) {
                CommonAdapter.this.convert(holder, t, position);
            }
        });
    }

    public CommonAdapter(Context context, final int layoutId) {
        this(context, layoutId, null);
    }

    protected abstract void convert(ViewHolder viewHolder, T item, int position);

}
