package com.yofish.droidkit.utils;


import com.yofish.droidkit.R;
import com.yofish.droidkit.bean.MainConfigData;
import com.yofish.droidkit.fragment.FragmentFind;
import com.yofish.droidkit.fragment.FragmentHome;
import com.yofish.droidkit.fragment.FragmentUser;

import java.util.HashMap;

/**
 * 页面配置信息
 *
 * Created by hch on 2018/9/28.
 */

public class TabConfig {
    /**
     * 页面配置
     */
    private static HashMap<String, MainConfigData> mainConfigMap = new HashMap<>();

    static {
        mainConfigMap.put("main_home", new MainConfigData(FragmentHome.class, R.drawable.main_tab_home_icon_selector, "首页"));
        mainConfigMap.put("main_fx", new MainConfigData(FragmentFind.class, R.drawable.main_tab_fx_icon_selector, "发现"));
        mainConfigMap.put("main_user", new MainConfigData(FragmentUser.class, R.drawable.main_tab_user_icon_selector, "我的"));

    }

    /**
     * 根据页面配置key返回页面配置信息
     *
     * @param key 页面key
     * @return 页面配置信息
     */
    public static MainConfigData getMainConfigDataByKey(String key) {
        MainConfigData configData = mainConfigMap.get(key);
        return configData;
    }

}
