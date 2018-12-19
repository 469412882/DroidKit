package com.yofish.kitmodule.base_component;

import android.os.Bundle;
import android.view.View;

import com.yofish.kitmodule.util.PagerInfo;

/**
 * UI事件处理接口
 * <p>
 * Created by hch on 2018/12/14.
 */
public interface IUIHandle {
    void showAlertDialog(String content);

    void dismissAlertDialog();

    void showLoadingDialog(boolean cancelable);

    void dismissLoadingDialog();

    void showToast(String msg);

    void showSnackBar(String content, View anchor);

    void startActivity(Class<?> clz, Bundle bundle);

    void updatePage(PagerInfo pagerInfo);

    void loadingComplete();
}
