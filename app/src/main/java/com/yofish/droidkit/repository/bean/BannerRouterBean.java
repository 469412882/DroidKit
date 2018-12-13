package com.yofish.droidkit.repository.bean;

import java.io.Serializable;

/**
 * 路由bean
 *
 * Created by hch on 2018/8/3.
 */
public class BannerRouterBean implements Serializable {

    private String actionUrl;
    private String picUrl;
    private String subtitle;
    private String title;

    public String getActionUrl() {
        return actionUrl;
    }

    public void setActionUrl(String actionUrl) {
        this.actionUrl = actionUrl;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
