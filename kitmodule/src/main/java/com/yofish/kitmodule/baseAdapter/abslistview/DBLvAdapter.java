package com.yofish.kitmodule.baseAdapter.abslistview;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.yofish.kitmodule.baseAdapter.recyclerview.DBRCViewType;
import com.yofish.kitmodule.base_component.viewmodel.ItemViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DBLvAdapter<T extends ItemViewModel> extends BaseAdapter {
    /**布局id*/
    private int itemLayoutId;
    /**item中的BR id*/
    private int itemVarId;
    /**数据集合*/
    private ArrayList<T> dataList;
    /**多布局类型*/
    private HashMap<Integer, DBRCViewType> types;

    public DBLvAdapter(int itemLayoutId, int itemVarId) {
        this.itemLayoutId = itemLayoutId;
        this.itemVarId = itemVarId;
        this.dataList = new ArrayList();
    }

    public int getCount() {
        return this.dataList.size();
    }

    public Object getItem(int position) {
        return this.dataList.get(position);
    }

    public long getItemId(int id) {
        return (long)id;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewDataBinding viewDataBinding;
        if (convertView == null) {
            viewDataBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), this.getItemLayoutIdByType(position), parent, false);
        } else {
            viewDataBinding = DataBindingUtil.getBinding(convertView);
        }

        viewDataBinding.setVariable(this.getItemVarIdByType(position), this.dataList.get(position));
        return viewDataBinding.getRoot();
    }

    public void resetData(List<T> var1) {
        this.dataList.clear();
        this.dataList.addAll(var1);
        this.notifyDataSetChanged();
    }

    public void addData(List<T> var1) {
        this.dataList.addAll(var1);
        this.notifyDataSetChanged();
    }

    public void setViewTypes(HashMap<Integer, DBRCViewType> types) {
        this.types = types;
    }

    private int getItemLayoutIdByType(int var1) {
        if (null != this.types) {
            DBRCViewType var2 = this.types.get(var1);
            if (null != var2) {
                return var2.getLayoutId();
            }
        }

        return this.itemLayoutId;
    }

    private int getItemVarIdByType(int var1) {
        if (null != this.types) {
            DBRCViewType var2 = this.types.get(var1);
            if (null != var2) {
                return var2.getVarId();
            }
        }

        return this.itemVarId;
    }
}
