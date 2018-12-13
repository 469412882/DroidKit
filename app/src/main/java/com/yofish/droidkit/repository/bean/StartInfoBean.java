package com.yofish.droidkit.repository.bean;

import java.io.Serializable;

/**
 * app启动信息
 * <p>
 * Created by hch on 2018/9/28.
 */

public class StartInfoBean implements Serializable {
    private String majiaSwitch;

    public String getMajiaSwitch() {
        return majiaSwitch;
    }

    public void setMajiaSwitch(String majiaSwitch) {
        this.majiaSwitch = majiaSwitch;
    }
}
