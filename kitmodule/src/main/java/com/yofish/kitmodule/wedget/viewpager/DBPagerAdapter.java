package com.yofish.kitmodule.wedget.viewpager;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yofish.kitmodule.base_component.viewmodel.ItemViewModel;

import java.util.ArrayList;
import java.util.List;


public class DBPagerAdapter<T extends ItemViewModel> extends PagerAdapter {
    private int itemLayoutId;
    private int itemVarId;
    private ArrayList<T> dataList = new ArrayList();

    public DBPagerAdapter(int itemLayoutId, int itemVarId) {
        this.itemLayoutId = itemLayoutId;
        this.itemVarId = itemVarId;
    }

    public int getCount() {
        return this.dataList.size();
    }

    public Object instantiateItem(ViewGroup container, int position) {
        if (position >= this.getCount()) {
            return null;
        } else {
            ViewDataBinding viewDataBinding = DataBindingUtil.inflate(LayoutInflater.from(container.getContext()), this.itemLayoutId, container, false);
            viewDataBinding.setVariable(this.itemVarId, this.dataList.get(position));
            container.addView(viewDataBinding.getRoot());
            return viewDataBinding.getRoot();
        }
    }

    public void destroyItem(ViewGroup container, int position, Object obj) {
        container.removeView((View) obj);
    }

    public boolean isViewFromObject(View view, Object obj) {
        return obj == view;
    }

    public void resetData(List<T> data) {
        this.dataList.clear();
        this.dataList.addAll(data);
    }

    public void addData(List<T> data) {
        this.dataList.addAll(data);
    }
}