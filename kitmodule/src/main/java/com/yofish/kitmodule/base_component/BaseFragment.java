package com.yofish.kitmodule.base_component;

import android.app.ProgressDialog;
import android.arch.lifecycle.HolderFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.trello.rxlifecycle.components.support.RxFragment;
import com.yofish.kitmodule.util.PagerInfo;


/**
 * Fragment基类 ，
 * 可使用Rxjava捕获fragment的生命周期
 *
 * Created by hch on 2017/8/3.
 */

public abstract class BaseFragment extends RxFragment implements IUIHandle {

    /** 根view */
    protected View mRootView;

    /** 是否对用户可见 */
    protected boolean mIsVisible;

    /** 是否需要懒加载 */
    protected boolean mShouldLazyLoad;
    /** ui操作者，包装模式 */
    private UIHandleImpl uiHandleWrapper;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        uiHandleWrapper = new UIHandleImpl(getContext());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(setLayoutId(savedInstanceState), container, false);
        if (mRootView instanceof ViewGroup) {
            initViews((ViewGroup) mRootView);
        }
        return mRootView;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        this.mIsVisible = isVisibleToUser;

        if (isVisibleToUser && mShouldLazyLoad) {
            onLazyLoad();
        }

    }

    /**
     * 子类如果需要懒加载可重写此方法
     */
    protected void onLazyLoad() {

    }

    /**
     * 设置页面布局Id
     * 
     * @return
     */
    protected abstract int setLayoutId(Bundle savedInstanceState);

    /**
     * 初始化views
     */
    protected void initViews(ViewGroup rootView) {}

    /**
     * 设置懒加载开关
     * 
     * @param lazyLoad
     *            lazyLoad
     */
    protected void initViews(ViewGroup rootView, boolean lazyLoad) {
        this.mShouldLazyLoad = lazyLoad;
        initViews(rootView);
    }

    /**
     * toast提示
     *
     * @param msg
     *            msg
     */
    public void showToast(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }

    /**
     * 弹窗提示
     *
     * @param content content
     */
    public void showAlertDialog(String content) {
        uiHandleWrapper.showAlertDialog(content);
    }

    public void dismissAlertDialog() {
        uiHandleWrapper.dismissAlertDialog();
    }

    @Override
    public void showLoadingDialog(boolean cancelable) {
        uiHandleWrapper.showLoadingDialog(cancelable);
    }

    @Override
    public void dismissLoadingDialog() {
        uiHandleWrapper.dismissLoadingDialog();
    }

    public void showSnackBar(String content, View anchor){
        uiHandleWrapper.showSnackBar(content, anchor);
    }

    /**
     * 跳转页面
     *
     * @param clz    所跳转的目的Activity类
     * @param bundle 跳转所携带的信息
     */
    public void startActivity(Class<?> clz, Bundle bundle) {
        uiHandleWrapper.startActivity(clz, bundle);
    }

    public void finish(){
        if (getActivity() == null) {
            return;
        }
        getActivity().finish();
    }

    public void onBackPressed(){

    }

    /**
     * 更新页数
     *
     * @param pagerInfo pagerInfo
     */
    public void updatePage(PagerInfo pagerInfo) {
        uiHandleWrapper.updatePage(pagerInfo);
    }

    /**
     * ViewModel加载完数据后会通过LiveData通知调用此方法
     */
    public void loadingComplete() {
        uiHandleWrapper.loadingComplete();
    }

}
