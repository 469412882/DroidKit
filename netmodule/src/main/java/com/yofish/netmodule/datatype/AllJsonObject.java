package com.yofish.netmodule.datatype;

import com.alibaba.fastjson.JSONObject;

/**
 * file description
 * <p>
 * Created by hch on 2018/12/7.
 */
public class AllJsonObject {
    private JSONObject responseBodyJson;

    public AllJsonObject(JSONObject result){
        this.responseBodyJson = result;
    }

    public JSONObject getResponseBodyJson() {
        return responseBodyJson;
    }

    public void setResponseBodyJson(JSONObject responseBodyJson) {
        this.responseBodyJson = responseBodyJson;
    }
}
