package com.yofish.netmodule.datatype;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
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
    /** 运行异常错误 */
    public static final String RUNTIME_EXCEPTION_CODE = "250";

    public static void successData(ICallBack callBack, String data) {
        //获取接口中的泛型
        Type type = Utility.getGenericTypeFromClass(callBack.getClass());
        //需要所有的responseBody字符串
        if (AllString.class == type) {
            callBack.onSuccess(new AllString(data));
            return;
        }
        //需要所有的responseBody转换成json串
        if (AllJsonObject.class == type) {
            JSONObject jsonObject = JSON.parseObject(data);
            callBack.onSuccess(new AllJsonObject(jsonObject));
            return;
        }
        //按标准过滤信息
        BaseResultEntity entity = JSON.parseObject(data, BaseResultEntity.class);
        int code = Integer.valueOf(entity.getCode());
        /* 过滤接口的成功标识 */
        if (1 == code) {
            String content = JSON.toJSONString(entity.getData());
            if (type == null) {
                callBack.onSuccess(content);
                return;
            }
            callBack.onSuccess(Utility.parseDataByType(content, callBack.getClass()));
        } else {
            callBack.onFailed(entity.getCode(), entity.getDesc());
        }
    }
}
