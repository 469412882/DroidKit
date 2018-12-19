package com.yofish.kitmodule.base_component.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.Observer;
import android.databinding.ObservableField;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;

import com.yofish.kitmodule.R;
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

    public ObservableField<BaseViewModel.LoadStatus> loadStatus = new ObservableField<>(LoadStatus.NORMAL);
    public ObservableField<String> netErrHint = new ObservableField<>();
    public ObservableField<String> nodataHint = new ObservableField<>();
    public ObservableField<Drawable> netErrRes = new ObservableField<>();
    public ObservableField<Drawable> nodataRes = new ObservableField<>();
    public ObservableField<String> toolbarTitle = new ObservableField<>("");

    public BaseViewModel(@NonNull Application application) {
        super(application);
        netErrHint.set(application.getResources().getString(R.string.net_err));
        nodataHint.set(application.getResources().getString(R.string.no_data));
        netErrRes.set(application.getResources().getDrawable(R.drawable.load_neterr));
        nodataRes.set(application.getResources().getDrawable(R.drawable.load_nodata));
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
     * 修改toolbar的title
     *
     * @param title
     */
    public void setToolbarTitle(String title) {
        if (uiLiveData == null) {
            return;
        }
        uiLiveData.toolBarTitleEvent.postValue(title);
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
     * 显示加载对话框
     */
    public void showLoadingDialog(boolean cancleable){
        if (uiLiveData == null) {
            return;
        }
        uiLiveData.showLoadingDialogEvent.postValue(cancleable);
    }

    /**
     * 隐藏加载对话框
     */
    public void dismissLoadingDialog(){
        if (uiLiveData == null) {
            return;
        }
        uiLiveData.dismissLoadingDialogEvent.call();
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
        /**
         * 显示弹窗
         */
        private SingleLiveEvent<String> showDialogEvent;
        /**
         * 隐藏弹窗
         */
        private SingleLiveEvent dismissDialogEvent;
        /**
         * 显示加载弹窗
         */
        private SingleLiveEvent<Boolean> showLoadingDialogEvent;
        /**
         * 隐藏加载弹窗
         */
        private SingleLiveEvent dismissLoadingDialogEvent;
        /**
         * 显示snackBar
         */
        private SingleLiveEvent<String> snackBarEvent;
        /**
         * 更新分页的页数
         */
        private SingleLiveEvent<PagerInfo> updatePageEvent;
        /**
         * 启动activity
         */
        private SingleLiveEvent<Map<String, Object>> startActivityEvent;
        /**
         * finish()
         */
        private SingleLiveEvent finishEvent;
        /**
         * 返回上一级，比如fragment回退栈
         */
        private SingleLiveEvent onBackPressedEvent;
        /**
         * 加载完成
         */
        private SingleLiveEvent loadingComplete;
        /**
         * 设置toolbar的title
         */
        private SingleLiveEvent<String> toolBarTitleEvent;

        public SingleLiveEvent<String> getShowDialogEvent() {
            return showDialogEvent = createLiveData(showDialogEvent);
        }

        public SingleLiveEvent<String> getSnackBarEvent() {
            return snackBarEvent = createLiveData(snackBarEvent);
        }

        public SingleLiveEvent<String> getToolBarTitleEvent() {
            return toolBarTitleEvent = createLiveData(toolBarTitleEvent);
        }

        public SingleLiveEvent<PagerInfo> getUpdatePageEvent() {
            return updatePageEvent = createLiveData(updatePageEvent);
        }

        public SingleLiveEvent getDismissDialogEvent() {
            return dismissDialogEvent = createLiveData(dismissDialogEvent);
        }

        public SingleLiveEvent<Boolean> getShowLoadingDialogEvent(){
            return showLoadingDialogEvent = createLiveData(showLoadingDialogEvent);
        }

        public SingleLiveEvent getDismissLoadingDialogEvent(){
            return dismissLoadingDialogEvent = createLiveData(dismissLoadingDialogEvent);
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

    /**
     * 无数据
     */
    public void setNoData() {
        loadStatus.set(LoadStatus.NODATA);
    }


    /**
     * 无数据，可设置提示语和图片
     */
    public void setNoData(String tip, Drawable res) {
        setNoData();
        if (!TextUtils.isEmpty(tip)) {
            nodataHint.set(tip);
        }
        if (res != null) {
            nodataRes.set(res);
        }
    }

    /**
     * 网络异常
     */
    public void setNetErr() {
        loadStatus.set(LoadStatus.NETERR);
    }

    /**
     * 网络异常，可设置提示语和图片
     */
    public void setNetErr(String tip, Drawable res) {
        setNetErr();
        if (!TextUtils.isEmpty(tip)) {
            netErrHint.set(tip);
        }
        if (res != null) {
            netErrRes.set(res);
        }
    }

    /**
     * 点击重试
     *
     * @param view view
     */
    public void clickRetry(View view) {
        try {
            if (loadStatus.get() == null) {
                return;
            }
            switch (loadStatus.get()) {
                case NODATA:
                    this.clickNoData(view);
                    break;
                case NETERR:
                    this.clickNetErr(view);
            }

            loadStatus.set(LoadStatus.NORMAL);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 网络点击
     *
     * @param view view
     */
    public void clickNetErr(View view) {
    }

    /**
     * 无数据点击
     *
     * @param view view
     */
    public void clickNoData(View view) {
    }



    public static enum LoadStatus {
        NORMAL,
        NETERR,
        NODATA
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
