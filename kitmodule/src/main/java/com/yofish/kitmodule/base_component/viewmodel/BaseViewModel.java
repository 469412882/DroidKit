package com.yofish.kitmodule.base_component.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.Observer;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.yofish.kitmodule.util.PagerInfo;

import java.util.HashMap;
import java.util.Map;

/**
 * file description
 * <p>
 * Created by hch on 2018/12/12.
 */
public abstract class BaseViewModel extends AndroidViewModel implements IBaseViewModel {

    private UILiveData uiLiveData;

    public BaseViewModel(@NonNull Application application) {
        super(application);
    }

    /**
     * 由View层调用绑定，当UILiveData变化后可通知View
     *
     * @return UILiveData
     */
    public UILiveData getUiLiveData() {
        if (uiLiveData == null) {
            uiLiveData = new UILiveData();
        }
        return uiLiveData;
    }

    /**
     * 如果在VM层想要显示对话框的化，可调用此方法，此方法会通知View层执行相应的显示。
     *
     * @param title
     */
    public void showDialog(String title) {
        if (uiLiveData == null) {
            return;
        }
        uiLiveData.showDialogEvent.postValue(title);
    }

    /**
     * 显示snackBar，逻辑同上
     *
     * @param title
     */
    public void showSnacBar(String title) {
        if (uiLiveData == null) {
            return;
        }
        uiLiveData.snackBarEvent.postValue(title);
    }

    /**
     * 更新页数
     *
     * @param pagerInfo pagerInfo
     */
    public void updatePage(PagerInfo pagerInfo) {
        if (uiLiveData == null) {
            return;
        }
        uiLiveData.updatePageEvent.postValue(pagerInfo);
    }

    /**
     * 隐藏对话框
     */
    public void dismissDialog() {
        if (uiLiveData == null) {
            return;
        }
        uiLiveData.dismissDialogEvent.call();
    }

    /**
     * 加载完成
     */
    public void loadingComplete() {
        if (uiLiveData == null) {
            return;
        }
        uiLiveData.loadingComplete.call();
    }

    /**
     * 跳转页面
     *
     * @param clz 所跳转的目的Activity类
     */
    public void startActivity(Class<?> clz) {
        if (uiLiveData == null) {
            return;
        }
        startActivity(clz, null);
    }

    /**
     * 跳转页面
     *
     * @param clz    所跳转的目的Activity类
     * @param bundle 跳转所携带的信息
     */
    public void startActivity(Class<?> clz, Bundle bundle) {
        if (uiLiveData == null) {
            return;
        }
        Map<String, Object> params = new HashMap();
        params.put(ParameterField.CLASS, clz);
        if (bundle != null) {
            params.put(ParameterField.BUNDLE, bundle);
        }
        uiLiveData.startActivityEvent.postValue(params);
    }

    /**
     * ViewModel通知Activity要做的事情可以在此定义
     */
    public class UILiveData extends SingleLiveEvent {
        /** 显示弹窗 */
        private SingleLiveEvent<String> showDialogEvent;
        /** 隐藏弹窗 */
        private SingleLiveEvent dismissDialogEvent;
        /** 显示snackBar */
        private SingleLiveEvent<String> snackBarEvent;
        /** 更新分页的页数 */
        private SingleLiveEvent<PagerInfo> updatePageEvent;
        /** 启动activity */
        private SingleLiveEvent<Map<String, Object>> startActivityEvent;
        /** finish() */
        private SingleLiveEvent finishEvent;
        /** 返回上一级，比如fragment回退栈 */
        private SingleLiveEvent onBackPressedEvent;
        /** 加载完成 */
        private SingleLiveEvent loadingComplete;

        public SingleLiveEvent<String> getShowDialogEvent() {
            return showDialogEvent = createLiveData(showDialogEvent);
        }

        public SingleLiveEvent<String> getSnackBarEvent() {
            return snackBarEvent = createLiveData(snackBarEvent);
        }

        public SingleLiveEvent<PagerInfo> getUpdatePageEvent() {
            return updatePageEvent = createLiveData(updatePageEvent);
        }

        public SingleLiveEvent getDismissDialogEvent() {
            return dismissDialogEvent = createLiveData(dismissDialogEvent);
        }

        public SingleLiveEvent<Map<String, Object>> getStartActivityEvent() {
            return startActivityEvent = createLiveData(startActivityEvent);
        }

        public SingleLiveEvent getFinishEvent() {
            return finishEvent = createLiveData(finishEvent);
        }

        public SingleLiveEvent getOnBackPressedEvent() {
            return onBackPressedEvent = createLiveData(onBackPressedEvent);
        }

        public SingleLiveEvent getLoadingCompleteEvent() {
            return loadingComplete = createLiveData(loadingComplete);
        }

        private SingleLiveEvent createLiveData(SingleLiveEvent liveData) {
            if (liveData == null) {
                liveData = new SingleLiveEvent();
            }
            return liveData;
        }

        @Override
        public void observe(LifecycleOwner owner, Observer observer) {
            super.observe(owner, observer);
        }
    }

    public static class ParameterField {
        public static String CLASS = "CLASS";
        public static String CANONICAL_NAME = "CANONICAL_NAME";
        public static String BUNDLE = "BUNDLE";
    }

    @Override
    public void onAny(LifecycleOwner owner, Lifecycle.Event event) {

    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void onStart() {

    }

    @Override
    public void onStop() {

    }

    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

    }
}
