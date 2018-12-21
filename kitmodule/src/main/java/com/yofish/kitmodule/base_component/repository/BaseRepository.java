package com.yofish.kitmodule.base_component.repository;

import android.content.Context;

import com.yofish.kitmodule.util.NetClient;
import com.yofish.netmodule.callback.BaseCallBack;

import java.util.Map;

/**
 * 数据仓库，用来获取数据（通过网络或者数据库、亦或是缓存等），MVVM中的M层
 * <p>
 * Created by hch on 2018/12/12.
 */
public abstract class BaseRepository<T> extends BaseCallBack<T> implements IRepository {
    /**
     * 设置baseUrl
     */
    protected abstract String getBaseUrl();

    /**
     * 设置method
     */
    protected abstract String getMethod();

    /**
     * 设置key value类型的参数，如果存在Object参数，则此参数无效
     * 如果不需要参数，则不必重写此方法
     */
    protected Map<String, Object> getParams() {
        return null;
    }

    /**
     * 设置整个对象的参数，如果key value类型的参数和此参数同时存在，则优先使用此类型的参数，Map则无效
     * 如果不需要参数，则不必重写此方法
     */
    protected Object getObjectParams() {
        return null;
    }

    protected IRepositoryCallBack<T> callBack;

    /**
     * 是否加载假数据， 如需要，重写此方法，返回true即可
     *
     * @return
     */
    protected boolean isFake() {
        return false;
    }

    /**
     * 是否需要加载缓存数据，如需要，重写此方法，返回true即可
     *
     * @return
     */
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

    /**
     * 加载数据的入口方法
     */
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
        NetClient.newBuilder(getContext())
                .baseUrl(getBaseUrl())
                .method(getMethod())
                .params(getObjectParams())
                .params(getParams())
                .callBack(this).sendPost();
    }

    /**
     * NetClient的回调
     *
     * @param t t
     */
    @Override
    public void onSuccess(T t) {
        if (callBack != null) {
            callBack.onSuccess(t);
        }
    }

    /**
     * NetClient的回调
     *
     * @param code   code
     * @param errors errors
     */
    @Override
    public void onFailed(String code, String errors) {
        if (callBack != null) {
            callBack.onFailed(code, errors);
        }
    }

    /**
     * NetClient的回调
     */
    @Override
    public void onComplete() {
        super.onComplete();
        if (callBack != null) {
            callBack.onComplete();
        }
    }
}
