package com.yofish.netmodule.retrofit.func;

import com.trello.rxlifecycle.android.ActivityEvent;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import rx.Observable;

/**
 * 对rxAppCompatActivity判空的转换
 *
 * Created by hch on 2017/7/31.
 */

public class RxActivityTransformer<T> implements Observable.Transformer<T, T> {

    private RxAppCompatActivity rxAppCompatActivity;
    private ActivityEvent event;

    public RxActivityTransformer(RxAppCompatActivity rxAppCompatActivity, ActivityEvent event){
        this.rxAppCompatActivity = rxAppCompatActivity;
        this.event = event;
    }
    @Override
    public Observable<T> call(Observable<T> tObservable) {
        if (rxAppCompatActivity == null) {
            return tObservable;
        }
        return (Observable<T>) tObservable.compose(rxAppCompatActivity.bindUntilEvent(event));
    }
}
