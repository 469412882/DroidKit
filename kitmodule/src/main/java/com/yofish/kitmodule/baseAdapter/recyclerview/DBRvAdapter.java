package com.yofish.kitmodule.baseAdapter.recyclerview;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.yofish.kitmodule.base_component.viewmodel.ItemViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 数据绑定的RecyclerView.Adapter
 * <p>
 * Created by hch on 2018/12/13.
 */
public class DBRvAdapter<T extends ItemViewModel> extends RecyclerView.Adapter<DBViewHolder> {
    /** 数据 */
    private List<T> dataList;
    /**item布局id*/
    private int itemLayoutId;
    /**数据绑定 item布局中的变量id*/
    private int itemVarId;
    /**指定多类型*/
    private HashMap<Integer, DBRCViewType> types;

    public List<T> getDataList(){
        return dataList;
    }

    public DBRvAdapter(int itemLayoutId, int itemVarId){
        this.itemLayoutId = itemLayoutId;
        this.itemVarId = itemVarId;
        dataList = new ArrayList<>();
    }

    public DBRvAdapter(int itemLayoutId, int itemVarId, List<T> dataList){
        this(itemLayoutId, itemVarId);
        this.dataList.addAll(dataList);
    }


    @NonNull
    @Override
    public DBViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int type) {
        ViewDataBinding binding = DataBindingUtil.inflate(LayoutInflater.from(viewGroup.getContext()),
                getItemLayoutIdByType(type), viewGroup, false);
        DBViewHolder holder = new DBViewHolder(binding.getRoot());
        holder.setBinding(binding);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull DBViewHolder dbViewHolder, int position) {
        dbViewHolder.getBinding().setVariable(getItemVarIdByType(getItemViewType(position)), dataList.get(position));
        dbViewHolder.getBinding().executePendingBindings();
    }

    @Override
    public int getItemViewType(int position) {
        return this.dataList.get(position).itemType.get();
    }
    /**
     * 重置所有数据
     * @param data 重置目标数据
     */
    public void resetData(List<T> data) {
        this.dataList.clear();
        this.dataList.addAll(data);
    }

    /**
     * 追加数据
     * @param exdata 追加数据
     */
    public void addData(List<T> exdata) {
        this.dataList.addAll(exdata);
    }

    /**
     * 设置多类型
     * @param types
     */
    public void setViewTypes(HashMap<Integer, DBRCViewType> types) {
        this.types = types;
    }

    /**
     * 根据类型获取layoutid
     * @param type
     * @return
     */
    private int getItemLayoutIdByType(int type) {

        if (null != types) {
            DBRCViewType viewType = types.get(type);
            if (null != viewType) {
                return viewType.getLayoutId();
            }
        }
        return this.itemLayoutId;
    }

    /**
     * 根据类型获取变量id
     * @param type
     * @return
     */
    private int getItemVarIdByType(int type) {
        if (null != types) {
            DBRCViewType viewType = types.get(type);
            if (null != viewType) {
                return viewType.getVarId();
            }
        }
        return this.itemVarId;
    }


    @Override
    public int getItemCount() {
        return dataList.size();
    }
}
