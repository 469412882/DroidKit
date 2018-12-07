package com.yofish.netmodule.datatype;

import java.io.Serializable;

/**
 * 返回responseBody的String字符串，未加工的数据
 * <p>
 * Created by hch on 2018/12/7.
 */
public class AllString implements Serializable {
    private String responseBody;

    public AllString(String result){
        this.responseBody = result;
    }

    public String getResponseBody() {
        return responseBody;
    }

    public void setResponseBody(String responseBody) {
        this.responseBody = responseBody;
    }
}
