package com.yofish.kitmodule.base_component.repository;

/**
 * 数据仓库，用来获取数据（通过网络或者数据库、亦或是缓存等），MVVM中的M层
 * <p>
 * Created by hch on 2018/12/12.
 */
public interface IRepository {
    /** 加载数据 */
    void loadData();
    /** 请求网络数据 */
    void requestNetData();
    /** 假数据，用于开发阶段的测试 */
    void loadFakeData();
    /** 缓存数据 */
    void loadCacheData();
}
