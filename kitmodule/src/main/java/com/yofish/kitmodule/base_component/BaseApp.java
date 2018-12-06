package com.yofish.kitmodule.base_component;

import android.content.Context;
import android.support.multidex.MultiDexApplication;

/**
 * 基类APP
 *
 * Created by hch on 2017/8/4.
 */

public class BaseApp extends MultiDexApplication {
    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
    }

    public static Context getContext(){
        return mContext;
    }
}
