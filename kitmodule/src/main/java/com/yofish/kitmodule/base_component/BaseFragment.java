package com.yofish.kitmodule.base_component;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.trello.rxlifecycle.components.support.RxFragment;

/**
 * Fragment基类 ，
 * 可使用Rxjava捕获fragment的生命周期
 *
 * Created by hch on 2017/8/3.
 */

public abstract class BaseFragment extends RxFragment {

    /**
     * 根view
     */
    protected View mRootView;

    /**
     * 是否对用户可见
     */
    protected boolean mIsVisible;

    /**
     * 是否需要懒加载
     */
    protected boolean mShouldLazyLoad;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(setLayoutId(), container, false);
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
    protected abstract int setLayoutId();

    /**
     * 初始化views
     */
    protected abstract void initViews(ViewGroup rootView);

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

}
