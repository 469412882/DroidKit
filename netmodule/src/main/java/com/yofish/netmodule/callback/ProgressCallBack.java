package com.yofish.netmodule.callback;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * 有等待对话框的CallBack
 *
 * Created by hch on 2017/7/31.
 */

public abstract class ProgressCallBack<T> extends BaseCallBack<T> {

    private ProgressDialog dialog;
    private boolean cancleable = true;

    public ProgressCallBack(){

    }

    public ProgressCallBack(boolean cancleable){
        this.cancleable = cancleable;
    }

    @Override
    public void setContext(Context context) {
        super.setContext(context);
        initProgressDialog();
    }

    @Override
    public void onStart() {
        super.onStart();
        showDialog();
    }

    @Override
    public void onComplete() {
        super.onComplete();
        dismissDialog();
    }

    @Override
    public void onFailed(String code, String errors) {
        dismissDialog();
    }

    private void dismissDialog() {
        if (dialog == null)
            return;
        dialog.dismiss();
    }

    private void showDialog() {
        if (dialog == null)
            return;
        dialog.show();
    }

    private void initProgressDialog() {
        if (mContext == null) {
            return;
        }
        dialog = new ProgressDialog(mContext);
        dialog.setCancelable(cancleable);
    }
}
