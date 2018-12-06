package com.yofish.kitmodule.router;

import android.content.Context;
import android.os.Bundle;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.facade.annotation.Interceptor;
import com.alibaba.android.arouter.facade.callback.InterceptorCallback;
import com.alibaba.android.arouter.facade.template.IInterceptor;

/**
 * 登录拦截器
 * <p>
 * Created by hch on 2018/9/26.
 */
@Interceptor(priority = 1, name = "LoginInterceptor")
public class LoginInterceptor implements IInterceptor {
    @Override
    public void process(Postcard postcard, InterceptorCallback callback) {
        Bundle bundle = postcard.getExtras();
        callback.onContinue(postcard);
    }

    @Override
    public void init(Context context) {

    }
}
