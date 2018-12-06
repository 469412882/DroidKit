package com.yofish.netmodule.callback;

import android.content.Context;

/**
 * 请求回调接口
 * 
 * Created by hch on 2017/6/28.
 */

public interface ICallBack<T> {

    void onSuccess(T t);

    void onFailed(String errors);

    void onStart();

    void onComplete();

    void onProgress(int progress, long total);

    void onPause();

    void onSpeed(long speed);

    void onPridictFinish(long pridictFinishSecond);

    void setContext(Context context);
}
