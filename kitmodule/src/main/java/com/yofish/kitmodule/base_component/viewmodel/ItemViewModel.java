package com.yofish.kitmodule.base_component.viewmodel;

import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.support.annotation.NonNull;

/**
 * ItemViewModel用于recyclerview listview等
 * <p>
 * Created by hch on 2018/12/13.
 */
public class ItemViewModel<VM extends BaseViewModel> {
    /** item应该持有上级的View Model*/
    protected VM viewModel;
    /** item 类型*/
    public ObservableInt itemType = new ObservableInt(-1);

    public ItemViewModel(@NonNull VM viewModel){
        this.viewModel = viewModel;
    }
}
