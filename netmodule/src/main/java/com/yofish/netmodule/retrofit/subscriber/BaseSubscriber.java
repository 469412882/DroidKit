package com.yofish.netmodule.retrofit.subscriber;

import android.content.Context;


import com.yofish.netmodule.retrofit.func.RxDataMap;
import com.yofish.netmodule.utils.Utility;

import rx.Subscriber;

/**
 * BaseSubscriber
 *
 * Created by hch on 2017/6/30.
 */

public abstract class BaseSubscriber<T> extends Subscriber<T> {

    private RxDataMap<T> rxDataMap;

    protected Context mContext;

    public BaseSubscriber() {
        rxDataMap = new RxDataMap<>(Utility.getGenericTypeFromClass(this.getClass()));
    }

    public void setContext(Context context) {
        this.mContext = context;
    }

    public RxDataMap<T> getRxDataMap() {
        return rxDataMap;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        onCompleted();
    }

    @Override
    public abstract void onNext(T t);
}
