package com.yofish.netmodule.request;

/**
 * 请求接口
 *
 * Created by hch on 2017/6/28.
 */

public interface IRequestStrategy {

    /**
     * 发送get请求
     *
     * @param config 请求对象
     */
    void excuteGet(RequestConfig config);

    /**
     * 发送post请求，包含多文件上传方式的传文件
     *
     * @param config 请求对象
     */
    void excutePost(RequestConfig config);

    /**
     * 上传文件
     *
     * @param config 请求对象
     */
    void uploadFile(RequestConfig config);

    /**
     * 上传多文件
     *
     * @param config 请求对象
     */
    void uploadFiles(RequestConfig config);

    /**
     * 下载文件
     *
     * @param config  请求对象
     */
    void downloadFile(RequestConfig config);

}
