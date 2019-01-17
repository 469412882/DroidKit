package com.yofish.kitmodule.util;

/**
 * file description
 * <p>
 * Created by hch on 2019/1/11.
 */
public class CommonEvent {
    private String action;
    private String param1;
    private String param2;

    public CommonEvent(String action) {
        this.action = action;
    }

    public CommonEvent(String action, String param1) {
        this.action = action;
        this.param1 = param1;
    }

    public CommonEvent(String action, String param1, String param2) {
        this.action = action;
        this.param1 = param1;
        this.param2 = param2;
    }

    public String getAction() {
        return this.action;
    }

    public String getParam1() {
        return this.param1;
    }

    public void setParam1(String param1) {
        this.param1 = param1;
    }

    public String getParam2() {
        return this.param2;
    }

    public void setParam2(String param2) {
        this.param2 = param2;
    }
}
