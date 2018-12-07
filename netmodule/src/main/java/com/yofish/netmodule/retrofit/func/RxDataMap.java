package com.yofish.netmodule.retrofit.func;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.yofish.netmodule.datatype.AllJsonObject;
import com.yofish.netmodule.datatype.AllString;
import com.yofish.netmodule.retrofit.api.BaseResultEntity;

import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import rx.functions.Func1;

/**
 * 用于rxjava + retrofit中将response转化为目标类型
 * <p>
 * Created by hch on 2017/6/29.
 */

public class RxDataMap<T> implements Func1<ResponseBody, T> {

    private Type type;

    public RxDataMap(Type type) {
        this.type = type;
    }

    @Override
    public T call(ResponseBody responseBody) {
        try {
            String result = responseBody.string();
            /* 处理String类型的情况 */
            if (type == String.class) {
                return (T) result;
            }
            /* 处理JsonObject类型的情况 */
            if (type == JsonObject.class) {
                return (T) new JsonParser().parse(result).getAsJsonObject();
            }
            Gson gson = new Gson();
            return gson.fromJson(result, this.type);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
