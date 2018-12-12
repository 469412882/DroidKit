package com.yofish.netmodule.request;

import android.content.Context;

import com.trello.rxlifecycle.components.support.RxAppCompatActivity;
import com.yofish.netmodule.callback.ICallBack;
import com.yofish.netmodule.download.DownloadInfo;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * 请求内容
 *
 * Created by hch on 2017/6/28.
 */

public class RequestConfig {
    /**
     * 上下文
     */
    private Context context;
    /**
     * RxAppCompatActivity,用于请求绑定Activity的生命周期
     */
    private RxAppCompatActivity rxAppCompatActivity;
    /**
     * 发送请求的路径
     */
    private String baseUrl;
    /**
     * 发送请求的方法
     */
    private String method;
    /**
     * 下载文件信息
     */
    private DownloadInfo downloadInfo;

    /**
     * 请求参数，new出一个map对象，将默认的先添加进去
     */
    private Map<String, Object> parameters = new HashMap<>();

    /**
     * 请求参数，以objectParameters对象转化为一个json串的形式传递参数
     */
    private Object objectParameters;

    /**
     * 请求参数，上传的文件map
     */
    private Map<String, File> files;

    /**
     * 回调
     */
    private ICallBack callBack;

    /**
     * 超时时间
     */
    private long timeout = 60;

    /**
     * 重连次数
     */
    private int retryTime = 3;

    /**
     * 重连的延迟
     */
    private int retryDelay = 100;

    /**
     * 请求头部信息
     */
    private Map<String, String> headers;

    /**
     * 外界传入参数的构造方法，没有返回结果的请求
     */
    public RequestConfig(Context context) {
        this.context = context;
        if (context instanceof RxAppCompatActivity) {
            this.rxAppCompatActivity = (RxAppCompatActivity) context;
        }
        headers = getBaseHeaders();
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Map<String, Object> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, Object> parameters) {
        this.parameters.clear();
        if (parameters == null) {
            return;
        }
        this.parameters.putAll(parameters);
    }

    public void setObjectParameters(Object objectParameters) {
        if (objectParameters == null) {
            return;
        }
        this.objectParameters = objectParameters;
    }

    public Object getObjectParameters() {
        return objectParameters;
    }

    public ICallBack getCallBack() {
        return callBack;
    }

    public void setCallBack(ICallBack callBack) {
        this.callBack = callBack;
        this.callBack.setContext(context);
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public Map<String, File> getFiles() {
        return files;
    }

    public void setFiles(Map<String, File> files) {
        this.files = files;
    }

    public DownloadInfo getDownloadInfo() {
        return downloadInfo;
    }

    public void setDownloadInfo(DownloadInfo downloadInfo) {
        this.downloadInfo = downloadInfo;
    }

    public long getTimeout() {
        return timeout;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    public int getRetryTime() {
        return retryTime;
    }

    public void setRetryTime(int retryTime) {
        this.retryTime = retryTime;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        if (headers == null) {
            return;
        }
        this.headers.putAll(headers);
    }

    public int getRetryDelay() {
        return retryDelay;
    }

    public void setRetryDelay(int retryDelay) {
        this.retryDelay = retryDelay;
    }

    public RxAppCompatActivity getRxAppCompatActivity() {
        return rxAppCompatActivity;
    }

    public void setRxAppCompatActivity(RxAppCompatActivity rxAppCompatActivity) {
        this.rxAppCompatActivity = rxAppCompatActivity;
    }

    /**
     * 公共的的headers
     *
     * @return Map
     */
    private Map<String, String> getBaseHeaders() {
        Map<String, String> headers = new HashMap<>();
        headers.put("adCode", "310100");
        headers.put("cityCode", "021");
        headers.put("hskCityId", "101");
        headers.put("gps", "");
        headers.put(
                "token",
                "+NEflO3uj02eOaWPdCVSbDiORgxuKQuyVLKkfCeHMvtm8x4bsQ0G+o1qbuVVEDefpnhbqbWPafcFlabCA+XjEBg2yTrU6G/uNxHk7/4pl3B9OzL99OnIr1kL5ePBp2qVoZsUocUi1+CCOHygWi1U/4EU4mlDvEmjAa3NOpPUHzvjoww1CiPGqw==");
        headers.put("appId", "yuJ20D170X6R3RGOR00NRWB939V5W7070");
        return headers;
    }
}
