package com.yofish.kitmodule.baseAdapter.recyclerview;

import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * file description
 * <p>
 * Created by hch on 2018/12/13.
 */
public class DBViewHolder extends RecyclerView.ViewHolder {
    private ViewDataBinding viewDataBinding;

    public DBViewHolder(View view) {
        super(view);
    }

    public void setBinding(ViewDataBinding viewDataBinding){
        this.viewDataBinding = viewDataBinding;
    }

    public ViewDataBinding getBinding(){
        return viewDataBinding;
    }
}
