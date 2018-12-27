package com.yofish.kitmodule.loginUtil;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;

import com.yofish.kitmodule.base_component.BaseApp;
import com.yofish.kitmodule.router.CommonRouter;

import org.greenrobot.eventbus.EventBus;

/**
 * 登录处理工具类  用于测试路由拦截登录
 * Created by wyn on 2018/12/10.
 */
public class AppLoginMgr {

    private static volatile AppLoginMgr appLoginMgr;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;

    private AppLoginMgr() {
        this.sp = BaseApp.getContext().getSharedPreferences("SP_VERIFYUSER_INFO", Context.MODE_PRIVATE);
        this.editor = this.sp.edit();
    }


    public static AppLoginMgr getInstance() {
        if (appLoginMgr == null) {
            synchronized (AppLoginMgr.class) {
                if (appLoginMgr == null) {
                    appLoginMgr = new AppLoginMgr();
                }
            }
        }
        return appLoginMgr;
    }


    public void doTarget(Context var1, String var2) {
        this.doTarget(var1, var2, false);
    }

    public void doTarget(Context var1, String var2, boolean var3) {
        if (this.isLogin()) {
            EventBus.getDefault().post(new LoginSuccessEvent(var2));
        } else {
//            Intent var4 = new Intent(var1, LoginActivity.class);
//            var4.addFlags(536870912);
//            var4.addFlags(67108864);
//            var4.putExtra("LOGINACTION_KEY", var2);
//            var4.putExtra("LOGIN_HIDEREG_KEY", var3);
//            var1.startActivity(var4);
        }
    }

    public void doTargetRouter(Context var1, String var2, Bundle var3) {
        this.doTargetRouter(var1, var2, var3, false);
    }

    public void doTargetRouter(Context context, String url, Bundle bundle, boolean var4) {
        if (this.isLogin()) {
            CommonRouter.router2PagerByUrl(context, url, bundle);
        } else {
//            Intent var5 = new Intent(var1, LoginActivity.class);
//            var5.addFlags(536870912);
//            var5.addFlags(67108864);
//            var5.putExtra("LOGINACTION_PATH_KEY", var2);
//            var5.putExtra("LOGINACTION_BUNDLE_KEY", var3);
//            var5.putExtra("LOGIN_HIDEREG_KEY", var4);
//            var1.startActivity(var5);
        }
    }

    public void doUserInfoOption(Context var1, int var2) {
        if (!this.isLogin()) {
            this.doTarget(var1, "");
        } else {
//            Intent var3 = new Intent(var1, UserSettingActivity.class);
//            var3.putExtra("anbcm_usersetting_deflog_key", var2);
//            var1.startActivity(var3);
        }
    }

    /**
     * 设置登录信息
     *
     * @param token token
     * @param appId appId
     */
    public void setLoginInfo(String token, String appId) {
        this.setVerifyUserToken(token);
        this.setVerifyUserAppId(appId);
    }

    /**
     * 是否登录
     *
     * @return boolean
     */
    public boolean isLogin() {
        return !TextUtils.isEmpty(this.getVerifyUserToken()) && !TextUtils.isEmpty(this.getVerifyUserAppId());
    }

    public void clearLogin() {
        this.setVerifyUserToken("");
        this.setVerifyUserAppId("");
        this.setUserPhoneNum("");
        this.setUserAvatar("");
        this.setUserNickName("");
        this.setUserSex("");
        this.setUserBirthday("");
        this.setUserPub("");
        EventBus.getDefault().post(new LoginOutEvent());
    }

    /**
     * 获取token
     *
     * @return String
     */
    public String getVerifyUserToken() {
        return this.sp.getString("VERIFYUSER_TOKEN_KEY", "");
    }

    /**
     * 设置token
     *
     * @param token token
     */
    private void setVerifyUserToken(String token) {
        this.editor.putString("VERIFYUSER_TOKEN_KEY", token);
        this.editor.commit();
    }

    /**
     * 获取appId
     *
     * @return String
     */
    public String getVerifyUserAppId() {
        return this.sp.getString("VERIFYUSER_APPID_KEY", "");
    }

    /**
     * 设置appId
     *
     * @param appId appId
     */
    private void setVerifyUserAppId(String appId) {
        this.editor.putString("VERIFYUSER_APPID_KEY", appId);
        this.editor.commit();

    }

    /**
     * 获取用户手机号
     *
     * @return String
     */
    public String getUserPhoneNum() {
        return this.sp.getString("VERIFYUSER_PHONENUM_KEY", "");
    }

    /**
     * 获取用户手机号掩码
     *
     * @return String
     */
    public String getMaskUserPhoneNum() {
        StringBuilder mask = new StringBuilder();
        String phone = this.sp.getString("VERIFYUSER_PHONENUM_KEY", "");
        if (!TextUtils.isEmpty(phone)) {
            char[] array = phone.toCharArray();
            for (int i = 0; i < array.length; i++) {
                if (i >= 3 && i <= 6) {
                    mask.append("*");
                } else {
                    mask.append(array[i]);
                }
            }
        }
        return mask.toString();
    }

    /**
     * 设置用户手机号
     *
     * @param phoneNum
     */
    public void setUserPhoneNum(String phoneNum) {
        this.editor.putString("VERIFYUSER_PHONENUM_KEY", phoneNum);
        this.editor.commit();
    }

    /**
     * 获取用户头像
     *
     * @return String
     */
    public String getUserAvatar() {
        return this.sp.getString("VERIFYUSER_AVATAR_KEY", "");
    }

    /**
     * 设置用户头像
     *
     * @param avatarUrl
     */
    public void setUserAvatar(String avatarUrl) {
        this.editor.putString("VERIFYUSER_AVATAR_KEY", avatarUrl);
        this.editor.commit();
    }

    /**
     * 获取用户昵称
     *
     * @return String
     */
    public String getUserNickName() {
        return this.sp.getString("VERIFYUSER_NICKNAME_KEY", "");
    }

    /**
     * 设置用户昵称
     *
     * @param nickName
     */
    public void setUserNickName(String nickName) {
        this.editor.putString("VERIFYUSER_NICKNAME_KEY", nickName);
        this.editor.commit();
    }

    public String getUserProfile() {
        return this.sp.getString("VERIFYUSER_PROFILE_KEY", "");
    }

    public void setUserProfile(String var1) {
        this.editor.putString("VERIFYUSER_PROFILE_KEY", var1);
        this.editor.commit();
    }

    /**
     * 获取用户性别
     *
     * @return String
     */
    public String getUserSex() {
        return this.sp.getString("VERIFYUSER_SEX_KEY", "0");
    }

    /**
     * 设置用户性别
     *
     * @param sex sex
     */
    public void setUserSex(String sex) {
        this.editor.putString("VERIFYUSER_SEX_KEY", sex);
        this.editor.commit();
    }

    /**
     * 获取用户生日
     *
     * @return String
     */
    public String getUserBirthday() {
        return this.sp.getString("VERIFYUSER_BIRTHDAY_KEY", "");
    }

    /**
     * 设置用户生日
     *
     * @param birthday
     */
    public void setUserBirthday(String birthday) {
        this.editor.putString("VERIFYUSER_BIRTHDAY_KEY", birthday);
        this.editor.commit();
    }


    public String getUserPub() {
        return this.sp.getString("VERIFYUSER_PUB_KEY", "1");
    }

    public void setUserPub(String var1) {
        this.editor.putString("VERIFYUSER_PUB_KEY", var1);
        this.editor.commit();
    }

}
