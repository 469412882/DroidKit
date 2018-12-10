package com.yofish.kitmodule.router;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.alibaba.android.arouter.launcher.ARouter;
import com.yofish.kitmodule.util.LogUtils;
import com.yofish.kitmodule.util.Utility;

import java.util.Iterator;
import java.util.Set;

/**
 * 公共的路由
 *
 * Created by hch on 2018/9/26.
 */

public class CommonRouter {

    public  CommonRouter(){

    }

    public static void router2PagerByUrl(Context context, String url){
        Uri uri = Uri.parse(url);
        Set keySet = Utility.getQueryParameterNames(uri);
        Bundle bundle = new Bundle();
        String key;
        if(!keySet.isEmpty()) {
            LogUtils.i("params is not null");
            Iterator iterator = keySet.iterator();

            while(iterator.hasNext()) {
                key = (String)iterator.next();

                try {
                    String value = uri.getQueryParameter(key);
                    bundle.putString(key, value);
                    LogUtils.i("param=" + key);
                    LogUtils.i("paramValue=" + value);
                } catch (Exception var9) {
                    ;
                }
            }
        }
        LogUtils.i("path=" + uri.getPath());
        router2PagerByUrl(context, uri.getPath(), bundle);
    }

    public static void router2PagerByUrl(Context context, String path, Bundle bundle){
        ARouter.getInstance().build(path).with(bundle).navigation(context);
    }

    /**
     * 获取注册路由的Fragment实例
     * @param routerUrl
     * @return
     */
    public static Fragment getRouterFragment(String routerUrl) {
        try {
            return (Fragment)ARouter.getInstance().build(routerUrl).navigation();
        } catch (Exception var2) {
            LogUtils.i( "no page exception");
            return null;
        }
    }

}
