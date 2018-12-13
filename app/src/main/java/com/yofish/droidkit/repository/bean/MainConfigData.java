package com.yofish.droidkit.repository.bean;

/**
 * Created by zhang on 2016/9/21.
 * Des 首页配置数据 用于打包成不同样式的首页
 */

public class MainConfigData {
    /**页面的fragment*/
    private Class fg;
    /**tab的indicator selector*/
    private int tabSelector;
    /**页面名字*/
    private String tabName;
    /**params*/
    private String params;

    public MainConfigData(Class fg, int tabSelector, String tabName) {
        this.fg = fg;
        this.tabSelector = tabSelector;
        this.tabName = tabName;
    }
    public MainConfigData(Class fg, int tabSelector, String tabName, String params) {
        this.fg = fg;
        this.tabSelector = tabSelector;
        this.tabName = tabName;
        this.params = params;
    }

    public Class getFg() {
        return fg;
    }

    public void setFg(Class fg) {
        this.fg = fg;
    }

    public int getTabSelector() {
        return tabSelector;
    }

    public void setTabSelector(int tabSelector) {
        this.tabSelector = tabSelector;
    }

    public String getTabName() {
        return tabName;
    }

    public void setTabName(String tabName) {
        this.tabName = tabName;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }
}
