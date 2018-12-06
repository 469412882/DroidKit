package com.yofish.netmodule.callback;

import android.content.Context;

/**
 * 回调
 *
 * Created by hch on 2017/6/28.
 */

public abstract class BaseCallBack<T> implements ICallBack<T> {

    public Context mContext;

    public BaseCallBack(){

    }

    @Override
    public void setContext(Context context){
        this.mContext = context;
    }

    @Override
    public abstract void onSuccess(T t);

    @Override
    public abstract void onFailed(String errors);

    @Override
    public void onProgress(int progress, long total) {

    }

    @Override
    public void onSpeed(long speed) {

    }

    @Override
    public void onPridictFinish(long pridictFinishSecond) {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onStart() {

    }

    @Override
    public void onComplete() {

    }
}
