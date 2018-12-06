package com.yofish.kitmodule.base_component;

import android.app.Application;

import com.alibaba.android.arouter.launcher.ARouter;
import com.yofish.kitmodule.util.AppSharedPrefrences;
import com.yofish.kitmodule.util.LogUtils;
import com.tencent.smtt.sdk.QbSdk;

/**
 * 基础工具箱
 * <p>
 * Created by hch on 2017/8/4.
 */

public class BaseKit {

    public static void init(Application application, boolean debug, String spName) {
        try {
            QbSdk.initX5Environment(application.getApplicationContext(), null);
            if (debug) {
                ARouter.openLog();     // 打印日志
                ARouter.openDebug();
            }
            ARouter.init(application);
            AppSharedPrefrences.getInstance().init(application.getApplicationContext(), spName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        LogUtils.DEBUG = debug;
    }
}
