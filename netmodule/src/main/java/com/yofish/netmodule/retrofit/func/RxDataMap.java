package com.yofish.netmodule.retrofit.func;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.yofish.netmodule.retrofit.api.BaseResultEntity;

import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import rx.functions.Func1;

/**
 * 用于rxjava + retrofit中将response转化为目标类型
 *
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
            Gson gson = new Gson();
            BaseResultEntity entity = gson.fromJson(responseBody.string(), BaseResultEntity.class);
            int code = Integer.valueOf(entity.getCode());
            /* 过滤接口的成功标识 */
            if (1 == code) {
                String data = gson.toJson(entity.getData());
                /* 处理String类型的情况 */
                if (type == String.class) {
                    return (T) data;
                }
                /* 处理JsonObject类型的情况 */
                if (type == JsonObject.class) {
                    return (T) new JsonParser().parse(data).getAsJsonObject();
                }
                return gson.fromJson(data, this.type);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
