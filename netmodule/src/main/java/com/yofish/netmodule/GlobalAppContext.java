package com.yofish.netmodule;

import android.app.Activity;
import android.app.Service;
import android.content.Context;

/**
 * file description
 * <p>
 * Created by hch on 2018/12/6.
 */
public class GlobalAppContext {
    private static Context mContext;

    public static Context getContext(){
        if (mContext == null) {
            throw new NullPointerException("mContext could not be null");
        }
        return mContext;
    }

    public static void setContext(Context context){
        if (mContext != null) {
            return;
        }
        mContext = context.getApplicationContext();
    }
}
