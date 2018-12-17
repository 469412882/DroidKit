package com.yofish.droidkit.ui.activity;

import android.os.Bundle;

import com.yofish.droidkit.BR;
import com.yofish.droidkit.R;
import com.yofish.droidkit.databinding.ActivityDatabindingImgBinding;
import com.yofish.droidkit.viewmodule.BindingImageViewModel;
import com.yofish.kitmodule.base_component.BaseBindingActivity;

public class BindingImageViewActivity extends BaseBindingActivity<ActivityDatabindingImgBinding,BindingImageViewModel> {
    @Override
    protected int initVariableId() {
        return BR.imgVM;
    }

    @Override
    protected BindingImageViewModel initViewModel() {
        return createViewModel(this,BindingImageViewModel.class);
    }

    @Override
    protected void initBindingViews() {

    }

    @Override
    protected int setLayoutId(Bundle savedInstanceState) {
        return R.layout.activity_databinding_img;
    }

    @Override
    public void loadingComplete() {
        super.loadingComplete();
        binding.refresh.loadComplete();
    }
}
