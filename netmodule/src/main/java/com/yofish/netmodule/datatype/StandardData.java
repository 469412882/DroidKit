package com.yofish.netmodule.datatype;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.yofish.netmodule.callback.ICallBack;
import com.yofish.netmodule.retrofit.api.BaseResultEntity;
import com.yofish.netmodule.utils.Utility;

import java.lang.reflect.Type;

/**
 * 数据规范
 * <p>
 * Created by hch on 2018/12/7.
 */
public class StandardData {

    public static void successData(ICallBack callBack, String data){
        Type type = Utility.getGenericTypeFromClass(callBack.getClass());
        //需要所有的responseBody字符串
        if (type == AllString.class) {
            callBack.onSuccess(new AllString(data));
        }
        //需要所有的responseBody转换成json串
        if (type == AllJsonObject.class) {
            JsonObject jsonObject = new JsonParser().parse(data).getAsJsonObject();
            callBack.onSuccess(new AllJsonObject(jsonObject));
        }
        //按标准过滤信息
        Gson gson = new Gson();
        BaseResultEntity entity = gson.fromJson(data, BaseResultEntity.class);
        int code = Integer.valueOf(entity.getCode());
        /* 过滤接口的成功标识 */
        if (1 == code) {
            String content = gson.toJson(entity.getData());
            callBack.onSuccess(Utility.parseDataByType(content, callBack.getClass()));
        }
    }
}
