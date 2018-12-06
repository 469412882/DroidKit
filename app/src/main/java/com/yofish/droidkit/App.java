package com.yofish.droidkit;

import android.content.Context;

import com.danikula.videocache.HttpProxyCacheServer;
import com.yofish.kitmodule.base_component.BaseApp;
import com.yofish.kitmodule.base_component.BaseKit;

/**
 * APP
 *
 * Created by hch on 2017/8/4.
 */

public class App extends BaseApp {

    @Override
    public void onCreate() {
        super.onCreate();
        BaseKit.init(this, BuildConfig.LOG_DEBUG, "myApp_data");
    }

    private HttpProxyCacheServer proxy;

    public static HttpProxyCacheServer getProxy(Context context) {
        App app = (App) context.getApplicationContext();
        return app.proxy == null ? (app.proxy = app.newProxy()) : app.proxy;
    }

    private HttpProxyCacheServer newProxy() {
        return new HttpProxyCacheServer(this);
    }
}
