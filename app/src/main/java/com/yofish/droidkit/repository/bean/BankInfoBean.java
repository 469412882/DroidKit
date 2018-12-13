package com.yofish.droidkit.repository.bean;

import java.io.Serializable;

/**
 * 银行信息
 *
 * Created by hch on 2017/3/29.
 */

public class BankInfoBean implements Serializable {
    /** 银行logo地址 */
    private String bankIconURL;
    /** 银行Id */
    private String bankId;
    /** 银行名称 */
    private String bankName;

    public String getBankIconURL() {
        return bankIconURL;
    }

    public void setBankIconURL(String bankIconURL) {
        this.bankIconURL = bankIconURL;
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
