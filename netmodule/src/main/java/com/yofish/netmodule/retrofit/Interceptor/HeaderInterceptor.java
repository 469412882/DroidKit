package com.yofish.netmodule.retrofit.Interceptor;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 头部拦截器
 *
 * Created by hch on 2017/6/30.
 */

public class HeaderInterceptor implements Interceptor {

    private Map<String, String> headers;

    public HeaderInterceptor(Map<String, String> headers) {
        if (headers == null) {
            headers = new HashMap<>();
        }
        this.headers = headers;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request.Builder builder = chain.request().newBuilder();
        Set<String> keySet = headers.keySet();
        for (String key : keySet) {
            builder.addHeader(key, headers.get(key));
        }
        return chain.proceed(builder.build());
    }
}
