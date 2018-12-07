package com.yofish.netmodule.datatype;

import com.google.gson.JsonObject;

/**
 * file description
 * <p>
 * Created by hch on 2018/12/7.
 */
public class AllJsonObject {
    private JsonObject responseBodyJson;

    public AllJsonObject(JsonObject result){
        this.responseBodyJson = result;
    }

    public JsonObject getResponseBodyJson() {
        return responseBodyJson;
    }

    public void setResponseBodyJson(JsonObject responseBodyJson) {
        this.responseBodyJson = responseBodyJson;
    }
}
