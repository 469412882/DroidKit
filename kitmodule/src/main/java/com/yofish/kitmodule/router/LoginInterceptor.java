package com.yofish.kitmodule.router;

import android.content.Context;
import android.os.Bundle;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.facade.annotation.Interceptor;
import com.alibaba.android.arouter.facade.callback.InterceptorCallback;
import com.alibaba.android.arouter.facade.template.IInterceptor;
import com.alibaba.android.arouter.launcher.ARouter;

/**
 * 登录拦截器
 * <p>
 * Created by hch on 2018/9/26.
 */
@Interceptor(priority = 1, name = "LoginInterceptor")
public class LoginInterceptor implements IInterceptor {

    private Context mContext;

    @Override
    public void process(Postcard postcard, InterceptorCallback callback) {
        /*Bundle bundle = postcard.getExtras();
        callback.onContinue(postcard);
        */
        if (postcard.getGroup().equals("needlogin")) {

            if (AppLoginMgr.getInstance().isLogin()) {
                callback.onContinue(postcard);
            }else {
                AppLoginMgr.getInstance().tologinRouter(mContext,postcard.getPath(),postcard.getExtras());
            }

        } else {
           // postcard.withString("extra", "拦截器中添加参数");
            callback.onContinue(postcard);
        }


    }

    @Override
    public void init(Context context) {
        mContext = context;
    }
}
