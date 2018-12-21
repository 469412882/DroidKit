package com.yofish.kitmodule.util;

import com.yofish.kitmodule.base_component.BaseApp;
import com.yofish.kitmodule.loginUtil.AppLoginMgr;

import java.util.HashMap;

/**
 * file description
 * <p>
 * Created by hch on 2018/12/21.
 */
public class HeaderUtil {
    private static volatile HeaderUtil headerUtil;

    private HeaderUtil() {
    }

    public static HeaderUtil getInstance() {
        if (headerUtil == null) {
            synchronized (HeaderUtil.class) {
                if (headerUtil == null) {
                    headerUtil = new HeaderUtil();
                }
            }
        }
        return headerUtil;
    }

    public HashMap<String, String> getHeaderMap() {
        HashMap<String, String> headers = new HashMap<>();
        headers.put("appId", AppLoginMgr.getInstance().getVerifyUserAppId());
        headers.put("token", AppLoginMgr.getInstance().getVerifyUserToken());
        headers.put("appPkgName", Utility.getPackageName(BaseApp.getContext()));
        headers.put("appVersionName", Utility.getVersionName(BaseApp.getContext()));
        headers.put("appVersionCode", Utility.getVersionCode(BaseApp.getContext()) + "");
        headers.put("appMgr", "");
        headers.put("source", "");
        headers.put("adCode", "");
        headers.put("hskCityId", "");
        headers.put("cityCode", "");
        headers.put("devType", "");
        headers.put("gps", "");
        return headers;
    }
}
