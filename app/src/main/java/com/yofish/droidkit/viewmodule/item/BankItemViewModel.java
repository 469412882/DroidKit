package com.yofish.droidkit.viewmodule.item;

import android.databinding.ObservableField;
import android.support.annotation.NonNull;

import com.yofish.droidkit.viewmodule.BankListViewModel;
import com.yofish.kitmodule.base_component.viewmodel.ItemViewModel;

/**
 * file description
 * <p>
 * Created by hch on 2018/12/13.
 */
public class BankItemViewModel extends ItemViewModel<BankListViewModel> {

    public ObservableField<String> bankName = new ObservableField<>();
    public ObservableField<String> bankIconURL = new ObservableField<>();

    public BankItemViewModel(@NonNull BankListViewModel viewModel) {
        super(viewModel);
    }

}
