package com.yofish.netmodule.retrofit.subscriber;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * ProgressSubscriber
 *
 * Created by hch on 2017/6/30.
 */

public abstract class ProgressSubscriber<T> extends BaseSubscriber<T> {

    private boolean showProgress = true;

    private boolean cancleable = false;

    private ProgressDialog dialog;

    public ProgressSubscriber() {
        super();
    }

    public void setContext(Context context) {
        super.setContext(context);
        if (showProgress) {
            initProgressDialog();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        showDialog();
    }

    @Override
    public void onCompleted() {
        super.onCompleted();
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
