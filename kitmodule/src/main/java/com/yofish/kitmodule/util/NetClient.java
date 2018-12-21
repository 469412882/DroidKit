package com.yofish.kitmodule.util;

import android.content.Context;

import com.yofish.netmodule.BaseNetClient;
import com.yofish.netmodule.GlobalAppContext;

import java.util.Map;

/**
 * file description
 * <p>
 * Created by hch on 2018/12/21.
 */
public class NetClient {

    public static BaseNetClient.Builder newBuilder(Context context){
        GlobalAppContext.setContext(context);
        return new Builder(context, HeaderUtil.getInstance().getHeaderMap());
    }

    public static class Builder extends BaseNetClient.Builder{

        public Builder(Context context, Map<String, String> headers) {
            super(context);
            headers(headers);
        }
    }
}
