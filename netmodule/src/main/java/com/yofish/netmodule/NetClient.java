package com.yofish.netmodule;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;


import com.yofish.netmodule.callback.ICallBack;
import com.yofish.netmodule.download.DownloadInfo;
import com.yofish.netmodule.request.RequestConfig;
import com.yofish.netmodule.request.RequestDelegate;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * 网络工具
 * 
 * Created by hch on 2017/6/28.
 */

public class NetClient {

    public static Builder newBuilder(Context context){
        GlobalAppContext.setContext(context);
        return new Builder(context);
    }

    public static class Builder {
        /** baseurl */
        private String baseUrl;
        /** 请求方法 */
        private String method;
        /** 请求参数 */
        private Map<String, Object> params;
        /** 文件列表 */
        private Map<String, File> files;
        /** context */
        private Context context;
        /** 下载信息 */
        private DownloadInfo downloadInfo;
        /** 请求的配置信息 */
        private RequestConfig config;
        /** 回调 */
        private ICallBack callBack;
        /** 超时时间,单位：秒 */
        private long timeout;
        /** 重连次数 */
        private int retryTime;
        /** 重连的延迟 */
        private int retryDelay;
        /** 请求头部信息 */
        private Map<String, String> headers;


        public Builder(Context context) {
            this.context = context;
            config = new RequestConfig(context);
        }

        public Builder baseUrl(String url) {
            this.baseUrl = url;
            config.setBaseUrl(this.baseUrl);
            return this;
        }

        public Builder method(String method) {
            this.method = method;
            config.setMethod(this.method);
            return this;
        }

        public Builder downloadInfo(DownloadInfo info) {
            this.downloadInfo = info;
            config.setDownloadInfo(this.downloadInfo);
            return this;
        }

        public Builder params(Map<String, Object> params) {
            this.params = params;
            config.setParameters(this.params);
            return this;
        }

        public Builder timeout(long timeout) {
            this.timeout = timeout;
            config.setTimeout(this.timeout);
            return this;
        }

        public Builder headers(Map<String, String> headers) {
            this.headers = headers;
            config.setHeaders(this.headers);
            return this;
        }

        public Builder retryTime(int retryTime) {
            this.retryTime = retryTime;
            config.setRetryTime(this.retryTime);
            return this;
        }

        public Builder retryDelay(int retryDelay) {
            this.retryDelay = retryDelay;
            config.setRetryDelay(this.retryDelay);
            return this;
        }


        public Builder addFile(File file) {
            if (files == null) {
                files = new HashMap<>();
            }
            if (file == null) {
                Toast.makeText(context, "您还没有选择文件", Toast.LENGTH_SHORT).show();
                return this;
            }
            files.put(file.getName(), file);
            config.setFiles(this.files);
            return this;
        }

        public Builder callBack(ICallBack callBack){
            this.callBack = callBack;
            config.setCallBack(this.callBack);
            return this;
        }


        public void sendPost(){
            checkArgs();
            RequestDelegate.getInstance().getRequest().excutePost(config);
        }

        public void sendGet(){
            checkArgs();
            RequestDelegate.getInstance().getRequest().excuteGet(config);
        }

        public void uploadFile(){
            checkArgs();
            RequestDelegate.getInstance().getRequest().uploadFile(config);
        }

        public void uploadFiles(){
            checkArgs();
            RequestDelegate.getInstance().getRequest().uploadFiles(config);
        }

        /**
         * 下载文件
         */
        public void downloadFile() {
            checkDownloadArgs();
            RequestDelegate.getInstance().getRequest().downloadFile(config);
        }

        /**
         * 校验参数
         */
        private void checkArgs(){
            if (TextUtils.isEmpty(baseUrl)) {
                throw new IllegalArgumentException("baseUrl cannot be null");
            }
            if (callBack == null) {
                throw new IllegalArgumentException("callBack cannot be null");
            }
        }


        /**
         * 校验参数
         */
        private void checkDownloadArgs(){
            if (callBack == null) {
                throw new IllegalArgumentException("callBack cannot be null");
            }
        }

    }
}
