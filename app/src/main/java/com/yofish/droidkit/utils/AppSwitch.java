package com.yofish.droidkit.utils;


import com.yofish.kitmodule.util.AppSharedPrefrences;

/**
 * Created by zhangnaiqi on 2017/6/15.
 * Des 开关管理
 */

public class AppSwitch {
    /**
     * 马甲包
     */
    private static final String APP_MAJIA_SWITCH = "app_majia_switch";

    /**
     * 获取马甲包标识
     *
     * @return  String
     */
    public static String getAppMajiaSwitch() {
        String ret = "0";
        try {
            ret = AppSharedPrefrences.getInstance().get(APP_MAJIA_SWITCH, "0");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    /**
     * 设置马甲包标识
     *
     * @param flag "0" : 非马甲包  "1" : 马甲包
     */
    public static void setAppMajiaSwitch(String flag) {
        try {
            AppSharedPrefrences.getInstance().put(APP_MAJIA_SWITCH, flag);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
