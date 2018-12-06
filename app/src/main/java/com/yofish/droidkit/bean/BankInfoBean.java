package com.yofish.droidkit.bean;

import java.io.Serializable;

/**
 * 银行信息
 *
 * Created by hch on 2017/3/29.
 */

public class BankInfoBean implements Serializable {
    /** 银行logo地址 */
    private String bankLogoUrl;
    /** 银行Id */
    private String bankId;
    /** 银行名称 */
    private String bankName;

    public String getBankLogoUrl() {
        return bankLogoUrl;
    }

    public void setBankLogoUrl(String bankLogoUrl) {
        this.bankLogoUrl = bankLogoUrl;
    }

    public String getBankId() {
        return bankId;
    }

    public void setBankId(String bankId) {
        this.bankId = bankId;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }
}
