package com.yofish.kitmodule.baseAdapter.recyclerview.wrapper;

import android.databinding.ViewDataBinding;
import android.support.v4.util.SparseArrayCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.yofish.kitmodule.baseAdapter.recyclerview.DBRvAdapter;
import com.yofish.kitmodule.baseAdapter.recyclerview.DBViewHolder;
import com.yofish.kitmodule.baseAdapter.recyclerview.utils.WrapperUtils;


public class BindingHeaderAndFooterWrapper
        extends RecyclerView.Adapter<DBViewHolder> {

    private SparseArrayCompat<ViewDataBinding> headerbindings = new SparseArrayCompat();
    private SparseArrayCompat<ViewDataBinding> footerbindings = new SparseArrayCompat();
    private DBRvAdapter dbRvAdapter;

    public BindingHeaderAndFooterWrapper(DBRvAdapter paramDBRCBaseAdapter) {
        dbRvAdapter = paramDBRCBaseAdapter;
    }

    public DBViewHolder onCreateViewHolder(ViewGroup paramViewGroup, int paramInt) {
        DBViewHolder localDBViewHolder;
        if (headerbindings.get(paramInt) != null) {
            localDBViewHolder = new DBViewHolder(((ViewDataBinding) headerbindings.get(paramInt)).getRoot());
            return localDBViewHolder;
        }
        if (footerbindings.get(paramInt) != null) {
            localDBViewHolder = new DBViewHolder(((ViewDataBinding) footerbindings.get(paramInt)).getRoot());
            return localDBViewHolder;
        }
        return dbRvAdapter.onCreateViewHolder(paramViewGroup, paramInt);
    }

    public int getItemViewType(int paramInt) {
        if (a(paramInt)) {
            return headerbindings.keyAt(paramInt);
        }
        if (b(paramInt)) {
            return footerbindings.keyAt(paramInt - getHeadersCount() - a());
        }
        return dbRvAdapter.getItemViewType(paramInt - getHeadersCount());
    }

    private int a() {
        return dbRvAdapter.getItemCount();
    }

    public void onBindViewHolder(DBViewHolder paramDBViewHolder, int paramInt) {
        if (a(paramInt)) {
            return;
        }
        if (b(paramInt)) {
            return;
        }
        dbRvAdapter.onBindViewHolder(paramDBViewHolder, paramInt - getHeadersCount());
    }

    public int getItemCount() {
        return getHeadersCount() + getFootersCount() + a();
    }

    public void onAttachedToRecyclerView(RecyclerView paramRecyclerView) {
        WrapperUtils.onAttachedToRecyclerView(dbRvAdapter, paramRecyclerView, new WrapperUtils.SpanSizeCallback() {
            public int getSpanSize(GridLayoutManager paramAnonymousGridLayoutManager, GridLayoutManager.SpanSizeLookup paramAnonymousSpanSizeLookup, int paramAnonymousInt) {
                int i = getItemViewType(paramAnonymousInt);
                if (headerbindings.get(i) != null) {
                    return paramAnonymousGridLayoutManager.getSpanCount();
                } else if (footerbindings.get(i) != null) {
                    return paramAnonymousGridLayoutManager.getSpanCount();
                } else {
                    return paramAnonymousSpanSizeLookup != null ? paramAnonymousSpanSizeLookup.getSpanSize(paramAnonymousInt) : 1;
                }
            }
        });
    }

    public void onViewAttachedToWindow(DBViewHolder paramDBViewHolder) {
        dbRvAdapter.onViewAttachedToWindow(paramDBViewHolder);
        int i = paramDBViewHolder.getLayoutPosition();
        if ((a(i)) || (b(i))) {
            WrapperUtils.setFullSpan(paramDBViewHolder);
        }
    }

    private boolean a(int paramInt) {
        return paramInt < getHeadersCount();
    }

    private boolean b(int paramInt) {
        return paramInt >= getHeadersCount() + a();
    }

    public void addHeaderView(ViewDataBinding paramViewDataBinding) {
        headerbindings.put(headerbindings.size() + 100000, paramViewDataBinding);
    }

    public void addFootView(ViewDataBinding paramViewDataBinding) {
        footerbindings.put(footerbindings.size() + 200000, paramViewDataBinding);
    }

    public int getHeadersCount() {
        return headerbindings.size();
    }

    public int getFootersCount() {
        return footerbindings.size();
    }
}
