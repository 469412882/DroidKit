package com.yofish.droidkit.viewmodule;

import android.app.Application;
import android.content.Intent;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;
import android.view.View;

import com.yofish.droidkit.R;
import com.yofish.droidkit.repository.BankInfoRepository;
import com.yofish.droidkit.repository.bean.BankData;
import com.yofish.droidkit.repository.bean.BankInfoBean;
import com.yofish.droidkit.ui.activity.BindingRecyclerViewActivity;
import com.yofish.kitmodule.baseAdapter.recyclerview.CommonAdapter;
import com.yofish.kitmodule.baseAdapter.recyclerview.base.ViewHolder;
import com.yofish.kitmodule.base_component.repository.IRepositoryCallBack;
import com.yofish.kitmodule.base_component.viewmodel.BaseViewModel;
import com.yofish.kitmodule.util.PagerInfo;

/**
 * file description
 * <p>
 * Created by hch on 2018/12/13.
 */
public class DBExampleViewModel extends BaseViewModel {


    public DBExampleViewModel(@NonNull Application application) {
        super(application);
    }

    public void onRecyclerViewClick(View view){
        startActivity(BindingRecyclerViewActivity.class);
    }

}
