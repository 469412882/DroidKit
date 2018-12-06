package com.yofish.netmodule.download;


import com.yofish.netmodule.retrofit.api.ApiService;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Transient;

/**
 * apk下载请求数据基础类 Created by WZG on 2016/10/20.
 */

@Entity
public class DownloadInfo {
    @Id(autoincrement = true)
    private Long id;
    /* 存储位置 */
    private String savePath;
    /* 文件总长度 */
    private long countLength;
    /* 下载长度 */
    private long readLength;
    /* 下载Service */
    @Transient
    private ApiService service;
    /* 超时设置 */
    private int connectonTime = 60;
    /* state状态数据库保存 */
    private int stateInte;
    /* url */
    private String url;

    public DownloadInfo(String url) {
        setUrl(url);
    }

    public DownloadInfo(String url, String savePath) {
        this.savePath = savePath;
        this.url = url;
    }

    @Generated(hash = 559026354)
    public DownloadInfo(Long id, String savePath, long countLength, long readLength, int connectonTime, int stateInte,
            String url) {
        this.id = id;
        this.savePath = savePath;
        this.countLength = countLength;
        this.readLength = readLength;
        this.connectonTime = connectonTime;
        this.stateInte = stateInte;
        this.url = url;
    }

    @Generated(hash = 327086747)
    public DownloadInfo() {
    }

    public DownloadState getState() {
        switch (getStateInte()) {
            case 0:
                return DownloadState.START;
            case 1:
                return DownloadState.DOWN;
            case 2:
                return DownloadState.PAUSE;
            case 3:
                return DownloadState.STOP;
            case 4:
                return DownloadState.ERROR;
            case 5:
            default:
                return DownloadState.FINISH;
        }
    }

    public void setState(DownloadState state) {
        setStateInte(state.getState());
    }

    public int getStateInte() {
        return stateInte;
    }

    public void setStateInte(int stateInte) {
        this.stateInte = stateInte;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSavePath() {
        return savePath;
    }

    public void setSavePath(String savePath) {
        this.savePath = savePath;
    }

    public long getCountLength() {
        return countLength;
    }

    public void setCountLength(long countLength) {
        this.countLength = countLength;
    }

    public long getReadLength() {
        return readLength;
    }

    public void setReadLength(long readLength) {
        this.readLength = readLength;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getConnectonTime() {
        return this.connectonTime;
    }

    public void setConnectonTime(int connectonTime) {
        this.connectonTime = connectonTime;
    }

    public ApiService getService() {
        return service;
    }

    public void setService(ApiService service) {
        this.service = service;
    }

    public int getPercent() {
        if (countLength == 0) {
            return 0;
        }
        return (int) ((float)readLength / (float)countLength * 100);
    }
}
