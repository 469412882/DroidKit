package com.yofish.kitmodule.base_component;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;

import com.yofish.kitmodule.base_component.viewmodel.BaseViewModel;
import com.yofish.kitmodule.util.PagerInfo;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;

/**
 * 数据绑定Activity基类
 * <p>
 * Created by hch on 2018/12/12.
 */
public abstract class BaseBindingActivity<V extends ViewDataBinding, VM extends BaseViewModel> extends BaseActivity {
    protected V binding;
    protected VM viewModel;
    protected int viewModelId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViewDataBinding(savedInstanceState);
        registorUIChangeLiveDataCallBack();
        initBindingViews();
    }

    protected abstract int initVariableId();

    protected VM initViewModel(){
        return null;
    }



    /**
     * 此方法为不用数据绑定需要实现的方法,否则不能重写此方法
     */
    @Override
    protected final void initViews() {

    }

    /**
     * 初始化views
     */
    protected abstract void initBindingViews();

    /**
     * 注入绑定
     */
    private void initViewDataBinding(Bundle savedInstanceState) {
        //DataBindingUtil类需要在project的build中配置 dataBinding {enabled true }, 同步后会自动关联android.databinding包
        binding = DataBindingUtil.setContentView(this, setLayoutId(savedInstanceState));
        viewModelId = initVariableId();
        viewModel = initViewModel();
        if (viewModel == null) {
            Class modelClass;
            Type type = getClass().getGenericSuperclass();
            //如果没设置viewmodel则自动创建一个泛型类型的VM
            if (type instanceof ParameterizedType) {
                modelClass = (Class) ((ParameterizedType) type).getActualTypeArguments()[1];
            } else {
                //如果没有指定泛型参数，则默认使用BaseViewModel
                modelClass = BaseViewModel.class;
            }
            viewModel = (VM) createViewModel(this, modelClass);
        }
        binding.setVariable(viewModelId, viewModel);
        //让ViewModel拥有View的生命周期感应
        getLifecycle().addObserver(viewModel);
        //注入RxLifecycle生命周期

    }

    /**
     * 注册ViewModel与View的契约UI回调事件
     */
    private void registorUIChangeLiveDataCallBack() {
        //加载对话框显示
        viewModel.getUiLiveData().getShowDialogEvent().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String content) {
                showAlertDialog(content);
            }
        });
        //snackBar显示
        viewModel.getUiLiveData().getSnackBarEvent().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                showSnackBar(s, getWindow().getDecorView());
            }
        });
        //snackBar显示
        viewModel.getUiLiveData().getToolBarTitleEvent().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                setToolbarTitle(s);
            }
        });
        //页数更新
        viewModel.getUiLiveData().getUpdatePageEvent().observe(this, new Observer<PagerInfo>() {
            @Override
            public void onChanged(@Nullable PagerInfo pagerInfo) {
                updatePage(pagerInfo);
            }
        });
        //加载对话框消失
        viewModel.getUiLiveData().getDismissDialogEvent().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                dismissAlertDialog();
            }
        });
        //跳入新页面
        viewModel.getUiLiveData().getStartActivityEvent().observe(this, new Observer<Map<String, Object>>() {
            @Override
            public void onChanged(@Nullable Map<String, Object> params) {
                Class<?> clz = (Class<?>) params.get(BaseViewModel.ParameterField.CLASS);
                Bundle bundle = (Bundle) params.get(BaseViewModel.ParameterField.BUNDLE);
                startActivity(clz, bundle);
            }
        });
        //关闭界面
        viewModel.getUiLiveData().getFinishEvent().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                finish();
            }
        });
        //关闭上一层
        viewModel.getUiLiveData().getOnBackPressedEvent().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                onBackPressed();
            }
        });
        //加载结束
        viewModel.getUiLiveData().getLoadingCompleteEvent().observe(this, new Observer() {
            @Override
            public void onChanged(@Nullable Object o) {
                loadingComplete();
            }
        });
    }



    /**
     *   ViewModel需要通过这种方式创建，因为这种创建方式可以判断是否有缓存，
     *   如果不创建，则BaseBindingActivity 会自动创建泛型中的对象
     *   如果没有泛型，则BaseBindingActivity 会自动创建BaseViewModel的对象
     * @param cls
     * @param <T>
     * @return
     */
    public <T extends ViewModel> T createViewModel(FragmentActivity activity, Class<T> cls) {
        return ViewModelProviders.of(activity).get(cls);
    }
}
