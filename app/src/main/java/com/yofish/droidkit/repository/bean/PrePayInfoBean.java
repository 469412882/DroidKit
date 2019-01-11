package com.yofish.droidkit.repository.bean;

/**
 * file description
 * <p>
 * Created by hch on 2018/12/28.
 */
public class PrePayInfoBean {

    /**
     * orderId : string
     * payState : string
     * prePayFlag : string
     * prePaySerialNo : string
     */

    private String orderId;
    private String payState;
    private String prePayFlag;
    private String prePaySerialNo;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getPayState() {
        return payState;
    }

    public void setPayState(String payState) {
        this.payState = payState;
    }

    public String getPrePayFlag() {
        return prePayFlag;
    }

    public void setPrePayFlag(String prePayFlag) {
        this.prePayFlag = prePayFlag;
    }

    public String getPrePaySerialNo() {
        return prePaySerialNo;
    }

    public void setPrePaySerialNo(String prePaySerialNo) {
        this.prePaySerialNo = prePaySerialNo;
    }
}
