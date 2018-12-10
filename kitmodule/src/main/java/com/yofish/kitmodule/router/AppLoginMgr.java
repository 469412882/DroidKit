package com.yofish.kitmodule.router;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

/**
 * 登录处理工具类  用于测试路由拦截登录
 * Created by wyn on 2018/12/10.
 */
public class AppLoginMgr {

    //TODO 判断是否登录的变量,记得替换为正确方法
    private boolean hasTokenAndUserId = false;

    private static volatile AppLoginMgr appLoginMgr;

    private AppLoginMgr() {
    }


    public static AppLoginMgr getInstance() {
        if (appLoginMgr == null) {
            synchronized (AppLoginMgr.class) {
                if (appLoginMgr == null) {
                    appLoginMgr = new AppLoginMgr();
                }
            }
        }
        return appLoginMgr;
    }

    /**
     * 判断登录
     *
     * @return
     */
    public boolean isLogin() {
        return hasTokenAndUserId;
    }

    /**
     * 处理登录跳转
     * LOGIN_PATH_KEY 用于登录成功后直接跳转到目标页面，待处理
     *
     * @param context
     * @param path
     * @param bundle
     */
    public void tologinRouter(Context context, String path, Bundle bundle) {

        // Intent intent = new Intent(var1, LoginActivity.class);
        // var5.putExtra("LOGIN_PATH_KEY", path);
        // var1.startActivity(intent);

    }

}
