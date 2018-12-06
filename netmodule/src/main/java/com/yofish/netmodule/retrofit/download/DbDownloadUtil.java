package com.yofish.netmodule.retrofit.download;

import com.yofish.netmodule.database.GreenDaoManager;
import com.yofish.netmodule.download.DownloadInfo;
import com.yofish.netmodule.download.DownloadInfoDao;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

/**
 * 断点续传 数据库工具类-geendao运用
 * Created by hch on 2017/8/8.
 */

public class DbDownloadUtil {
    private static DbDownloadUtil db;
    private DownloadInfoDao downloadInfoDao;

    public DbDownloadUtil() {
        downloadInfoDao = GreenDaoManager.getInstance().getNewDaoSession().getDownloadInfoDao();
    }

    /**
     * 获取单例
     * 
     * @return
     */
    public static DbDownloadUtil getInstance() {
        if (db == null) {
            synchronized (DbDownloadUtil.class) {
                if (db == null) {
                    db = new DbDownloadUtil();
                }
            }
        }
        return db;
    }

    public void insert(DownloadInfo info) {
        downloadInfoDao.insert(info);
    }

    public void update(DownloadInfo info) {
        downloadInfoDao.update(info);
    }

    public void delete(DownloadInfo info) {
        downloadInfoDao.delete(info);
    }

    public DownloadInfo queryDownById(long Id) {
        QueryBuilder<DownloadInfo> qb = downloadInfoDao.queryBuilder();
        qb.where(DownloadInfoDao.Properties.Id.eq(Id));
        List<DownloadInfo> list = qb.list();
        if (list.isEmpty()) {
            return null;
        } else {
            return list.get(0);
        }
    }

    public DownloadInfo queryDownByUrl(String url) {
        List<DownloadInfo> list = downloadInfoDao.queryBuilder().where(DownloadInfoDao.Properties.Url.eq(url)).list();
        if (list.isEmpty()) {
            return null;
        } else {
            return list.get(0);
        }
    }

    public List<DownloadInfo> queryDownAll() {
        return downloadInfoDao.queryBuilder().list();
    }
}
