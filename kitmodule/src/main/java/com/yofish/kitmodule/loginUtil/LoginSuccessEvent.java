package com.yofish.kitmodule.loginUtil;

/**
 * file description
 * <p>
 * Created by hch on 2018/12/20.
 */
public class LoginSuccessEvent {
    private String action;

    public LoginSuccessEvent(String action) {
        this.action = action;
    }

    public String getAction() {
        return this.action;
    }
}
