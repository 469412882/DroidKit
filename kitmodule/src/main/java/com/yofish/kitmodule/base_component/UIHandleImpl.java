package com.yofish.kitmodule.base_component;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Toast;

import com.yofish.kitmodule.util.PagerInfo;

/**
 * UI事件处理接口实现类
 * <p>
 * Created by hch on 2018/12/14.
 */
public class UIHandleImpl implements IUIHandle {

    private Context mContext;
    /** 提示对话框 */
    private AlertDialog mAlertDialog;
    /** 请求时的对话框 */
    private ProgressDialog mLoadingDialog;

    public UIHandleImpl(Context context){
        this.mContext = context;
    }

    @Override
    public void showAlertDialog(String content) {
        if (mAlertDialog == null) {
            mAlertDialog = new AlertDialog.Builder(mContext).setTitle("提示")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mAlertDialog.dismiss();
                        }
                    }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mAlertDialog.dismiss();
                        }
                    }).create();
        }
        mAlertDialog.setMessage(content);
        if (!mAlertDialog.isShowing()) {
            mAlertDialog.show();
        }
    }

    @Override
    public void dismissAlertDialog() {
        if (mAlertDialog != null && mAlertDialog.isShowing()) {
            mAlertDialog.dismiss();
        }
    }

    @Override
    public void showLoadingDialog(boolean cancleable) {
        if (mLoadingDialog == null) {
            mLoadingDialog = new ProgressDialog(mContext);
        }
        mLoadingDialog.setCancelable(cancleable);
        if (!mLoadingDialog.isShowing()) {
            mLoadingDialog.show();
        }
    }

    @Override
    public void dismissLoadingDialog() {
        if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
        }
    }

    @Override
    public void showToast(String msg) {
        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showSnackBar(String content, View anchor) {
        Snackbar.make(anchor, content, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void startActivity(Class<?> clz, Bundle bundle) {
        Intent intent = new Intent(mContext, clz);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        mContext.startActivity(intent);
    }

    @Override
    public void updatePage(PagerInfo pagerInfo) {

    }

    @Override
    public void loadingComplete() {

    }
}
