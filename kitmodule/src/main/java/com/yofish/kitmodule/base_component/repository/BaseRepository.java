package com.yofish.kitmodule.base_component.repository;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

import com.yofish.netmodule.NetClient;
import com.yofish.netmodule.callback.BaseCallBack;

import java.util.Map;

/**
 * 数据仓库，用来获取数据（通过网络或者数据库、亦或是缓存等），MVVM中的M层
 * <p>
 * Created by hch on 2018/12/12.
 */
public abstract class BaseRepository<T> implements IRepository {
    /**
     * 设置baseUrl
     */
    protected String getBaseUrl(){
        return null;
    }

    /**
     * 设置method
     */
    protected String getMethod(){
        return null;
    }

    /**
     * 设置key value类型的参数，如果存在Object参数，则此参数无效
     */
    protected Map<String, Object> getParams() {
        return null;
    }

    /**
     * 设置整个对象的参数，如果key value类型的参数和此参数同时存在，则优先使用此类型的参数，Map则无效
     */
    protected Object getObjectParams() {
        return null;
    }

    protected IRepositoryCallBack<T> callBack;

    protected boolean isFake() {
        return false;
    }

    protected boolean shouldLoadCache() {
        return false;
    }

    private Context context;

    public BaseRepository(Context context) {
        this.context = context;
    }

    public Context getContext() {
        return context;
    }

    public void setCallBack(IRepositoryCallBack<T> callBack) {
        this.callBack = callBack;
    }

    @Override
    public void loadData() {
        if (isFake()) {
            loadFakeData();
        } else {
            if (shouldLoadCache()) {
                loadCacheData();
            }
            requestNetData();
        }
    }

    /**
     * 默认情况下requestNetData已经实现，如有其它需求，可以重写此方法
     */
    @Override
    public void requestNetData() {

    }
}
