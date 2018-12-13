package com.yofish.kitmodule.base_component.repository;

/**
 * file description
 * <p>
 * Created by hch on 2018/12/12.
 */
public interface IRepositoryCallBack<T> {
    void onSuccess(T t);
    void onFailed(String code, String desc);
    void onComplete();
}
