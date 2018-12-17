package com.yofish.droidkit.viewmodule.item;

import android.databinding.ObservableField;
import android.support.annotation.NonNull;

import com.yofish.droidkit.viewmodule.BindingImageViewModel;
import com.yofish.kitmodule.base_component.viewmodel.ItemViewModel;

/**
 * file description
 * <p>
 * Created by hch on 2018/12/13.
 */
public class ImgItemViewModel extends ItemViewModel<BindingImageViewModel> {

    public ObservableField<String> bankName = new ObservableField<>("test");
    public ObservableField<String> bankIconURL = new ObservableField<>();

    public ImgItemViewModel(@NonNull BindingImageViewModel viewModel) {
        super(viewModel);
    }

}
