package com.yofish.netmodule.request;

/**
 * 请求factory
 *
 * Created by hch on 2017/6/28.
 */

public class RequestDelegate {

    private static volatile RequestDelegate mInstance;

    private RequestDelegate(){

    }

    public static RequestDelegate getInstance(){
        if (mInstance == null) {
            synchronized (RequestDelegate.class) {
                if (mInstance == null) {
                    mInstance = new RequestDelegate();
                }
            }
        }
        return mInstance;
    }

    public IRequestStrategy getRequest() {
        return getRetrofit();
    }

    private RetrofitRequestStrategy getRetrofit() {
        return new RetrofitRequestStrategy();
    }
}
