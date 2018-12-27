package com.yofish.kitmodule.util;

import java.util.regex.Pattern;

/**
 * file description
 * <p>
 * Created by hch on 2018/12/25.
 */
public class PatternsUtil {
    /**
     * 校验手机号
     */
    public static Pattern mobilePhonePattern = Pattern.compile("^1[3|4|5|7|8][0-9]\\d{8}$");
    /**
     * 校验身份证
     */
    public static Pattern idNumPattern = Pattern.compile("(\\d{14}[0-9a-zA-Z])|(\\d{17}[0-9a-zA-Z])");
    /**
     * 校验卡号
     */
    public static Pattern cardNumPattern = Pattern.compile("^[1-9]\\d{8,19}$");
    /**
     * 校验六位数字密码
     */
    public static Pattern sixpwdPattern = Pattern.compile("^\\d{6}$");
    /**
     * 8到20位密码，字母 数字 符号至少两种
     */
    public static Pattern complexPwdPattern = Pattern.compile("^(?![\\d]+$)(?![a-zA-Z]+$)(?![^\\da-zA-Z]+$).{8,20}$");
}
