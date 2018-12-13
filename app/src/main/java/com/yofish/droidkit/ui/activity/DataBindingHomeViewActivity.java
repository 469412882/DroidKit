package com.yofish.droidkit.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.yofish.droidkit.BR;
import com.yofish.droidkit.R;
import com.yofish.droidkit.databinding.ActivityDatabindingHomeBinding;
import com.yofish.droidkit.viewmodule.DBExampleViewModel;
import com.yofish.kitmodule.base_component.BaseBindingActivity;

/**
 * RecyclerView页面
 * <p>
 * Created by hch on 2017/12/1.
 */

public class DataBindingHomeViewActivity extends BaseBindingActivity<ActivityDatabindingHomeBinding, DBExampleViewModel> {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initToolbar("MVVM");
    }

    @Override
    protected int initVariableId() {
        return BR.viewModel;
    }

    @Override
    protected DBExampleViewModel initViewModel() {
        return createViewModel(this, DBExampleViewModel.class);
    }


    @Override
    protected int setLayoutId(Bundle savedInstanceState) {
        return R.layout.activity_databinding_home;
    }

    protected void initBindingViews() {
    }

}
