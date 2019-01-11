package com.yofish.kitmodule.base_component;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yofish.kitmodule.base_component.viewmodel.BaseViewModel;
import com.yofish.kitmodule.util.PagerInfo;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;

/**
 * file description
 * <p>
 * Created by hch on 2018/12/12.
 */
public abstract class BaseBindingFragment<V extends ViewDataBinding, VM extends BaseViewModel> extends BaseFragment {

    protected V binding;
    protected VM viewModel;
    protected int viewModelId;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected abstract int initVariableId();

    protected void initBindingViews() {

    }

    protected VM initViewModel(){
        return null;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), setLayoutId(savedInstanceState), container, false);
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
            viewModel = (VM) createViewModel(getActivity(), modelClass);
        }
        binding.setVariable(viewModelId, viewModel);
        //让ViewModel拥有View的生命周期感应
        getLifecycle().addObserver(viewModel);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        registorUIChangeLiveDataCallBack();
        initBindingViews();
    }

    //注册ViewModel与View的契约UI回调事件
    private void registorUIChangeLiveDataCallBack() {
        //加载对话框显示
        viewModel.getUiLiveData().getShowDialogEvent().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String content) {
                showAlertDialog(content);
            }
        });
        viewModel.getUiLiveData().getSnackBarEvent().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                showSnackBar(s, getActivity().getWindow().getDecorView());
            }
        });
        viewModel.getUiLiveData().getToastEvent().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                showToast(s);
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
        viewModel.getUiLiveData().getShowLoadingDialogEvent().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                showLoadingDialog(aBoolean);
            }
        });
        viewModel.getUiLiveData().getDismissLoadingDialogEvent().observe(this, new Observer() {
            @Override
            public void onChanged(@Nullable Object o) {
                dismissLoadingDialog();
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
     * 创建ViewModel
     *
     * @param cls
     * @param <T>
     * @return
     */
    public <T extends ViewModel> T createViewModel(FragmentActivity activity, Class<T> cls) {
        return ViewModelProviders.of(activity).get(cls);
    }
    /**
     * 创建ViewModel
     *
     * @param cls
     * @param <T>
     * @return
     */
    public <T extends ViewModel> T createViewModel(Fragment fragment, Class<T> cls) {
        return ViewModelProviders.of(fragment).get(cls);
    }
}
