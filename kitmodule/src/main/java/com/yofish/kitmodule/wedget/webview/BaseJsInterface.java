package com.yofish.kitmodule.wedget.webview;

import android.app.Activity;
import android.content.Context;
import android.webkit.JavascriptInterface;


/**
 * js回调类
 *
 * Created by hch on 2017/8/4.
 */

public class BaseJsInterface {

    private Context context;

    public BaseJsInterface(Context context) {
        this.context = context;
    }

    @JavascriptInterface
    public String jsGetAppVersionCode() {
        return "";
    }

    @JavascriptInterface
    public String jsGetAppVersionName() {
        return "";
    }

    @JavascriptInterface
    public String jsGetAppPkgName() {
        return "";
    }

    @JavascriptInterface
    public String jsGetCityName(){
        return "";
    }

    @JavascriptInterface
    public String jsGetCityCode(){
        return "";
    }


    @JavascriptInterface
    public String jsGetGps() {
        return "";
    }

    @JavascriptInterface
    public String jsGetUserToken() {
        return "";
    }

    @JavascriptInterface
    public String jsGetUserAppId() {
        return "";
    }



    @JavascriptInterface
    public void jsCallNative(String var1) {

    }

    @JavascriptInterface
    public void jsCallClose() {
        if(this.context instanceof Activity) {
            ((Activity)this.context).finish();
        }

    }

    @JavascriptInterface
    public String jsGetOSType() {
        return "android";
    }

    @JavascriptInterface
    public String jsGetOSVersion() {
        return "";
    }

    @JavascriptInterface
    public String jsGetIMEI() {
        return "";
    }


    @JavascriptInterface
    public String jsGetPhoneName() {
        return "";
    }

    @JavascriptInterface
    public String jsGetTelephonyCode() {
        return "";
    }

    @JavascriptInterface
    public String jsGetTelephonyName() {
        return "";
    }


}
