package com.yofish.droidkit.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by hch on 2017/6/29.
 */

public class BankData implements Serializable {
    private List<BankInfoBean> bankList;

    public List<BankInfoBean> getBankList() {
        return bankList;
    }

    public void setBankList(List<BankInfoBean> bankList) {
        this.bankList = bankList;
    }
}
