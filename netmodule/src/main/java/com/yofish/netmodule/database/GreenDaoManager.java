package com.yofish.netmodule.database;


import com.yofish.netmodule.GlobalAppContext;
import com.yofish.netmodule.download.DaoMaster;
import com.yofish.netmodule.download.DaoSession;

/**
 * greenDao管理类
 *
 * notice：
 * 1、新建数据库表需要再Entity中加入@Entity注解
 * 2、建完表之后可以直接通过GreenDaoManager.getInstance().getNewDaoSession().getDownloadInfoDao()获取对应的DAO类，直接可以进行增删改查操作
 *    也可以写一个单独的DbUtil类进行复杂操作
 *
 * 3、通过修改gradle中的schemaVersion来升级数据库
 *
 *
 * Created by hch on 2017/8/8.
 */

public class GreenDaoManager {
    /* daomaster */
    private DaoMaster daoMaster;
    /* daoSession */
    private DaoSession daoSession;

    private GreenDaoManager() {
        DaoMaster.DevOpenHelper openHelper = new DaoMaster.DevOpenHelper(GlobalAppContext.getContext(), "db_name", null);
        daoMaster = new DaoMaster(openHelper.getWritableDatabase());
        daoSession = daoMaster.newSession();
    }

    public static GreenDaoManager getInstance() {
        return Singleton.INSTANCE.getInstance();
    }

    private enum Singleton {
        INSTANCE;
        private GreenDaoManager manager;

        Singleton() {
            manager = new GreenDaoManager();
        }

        private GreenDaoManager getInstance() {
            return manager;
        }
    }

    public DaoMaster getDaoMaster() {
        return daoMaster;
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }

    public DaoSession getNewDaoSession() {
        return daoMaster.newSession();
    }
}
