package com.yofish.kitmodule.util;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Map;

/**
 * app 数据
 * <p>
 * Created by hch on 2018/9/28.
 */

public class AppSharedPrefrences {

    private static volatile AppSharedPrefrences instance;
    private static String fileName = "app_data_sp";
    private static Context mContext;
    private static SharedPreferences sp;
    private static SharedPreferences.Editor editor;

    private AppSharedPrefrences() {
    }

    public Context getContext() {
        return mContext;
    }

    public void init(Context context, String name) {
        mContext = context;
        fileName = name;
        sp = context.getSharedPreferences(fileName, 0);
        editor = sp.edit();
    }

    public static AppSharedPrefrences getInstance() {
        if (null == instance) {
            synchronized (AppSharedPrefrences.class) {
                if (null == instance) {
                    instance = new AppSharedPrefrences();
                }
            }
        }
        return instance;
    }

    /**
     * 保存数据
     * @param key
     * @param value
     */
    public void put(String key, Object value) {
        if (null != mContext) {
            if (value instanceof String) {
                editor.putString(key, (String) value);
            } else if (value instanceof Integer) {
                editor.putInt(key, ((Integer) value).intValue());
            } else if (value instanceof Boolean) {
                editor.putBoolean(key, ((Boolean) value).booleanValue());
            } else if (value instanceof Float) {
                editor.putFloat(key, ((Float) value).floatValue());
            } else if (value instanceof Long) {
                editor.putLong(key, ((Long) value).longValue());
            } else if (null == value) {
                editor.putString(key, "");
            } else {
                editor.putString(key, value.toString());
            }

            editor.commit();
        }
    }

    /**
     * 获取保存的数据
     * @param key
     * @param defaultValue
     * @param <T>
     * @return
     */
    public <T extends Object> T get(String key, T defaultValue) {
        T value = null;
        try {
            if (mContext != null) {
                if (defaultValue instanceof String) {
                    value = (T) sp.getString(key, (String) defaultValue);
                } else if (defaultValue instanceof Integer) {
                    value = (T) Integer.valueOf(sp.getInt(key, (Integer) defaultValue));
                } else if (defaultValue instanceof Boolean) {
                    value = (T) Boolean.valueOf(sp.getBoolean(key, ((Boolean) defaultValue).booleanValue()));
                } else if (defaultValue instanceof Float) {
                    value = (T) Float.valueOf(sp.getFloat(key, ((Float) defaultValue).floatValue()));
                } else if (defaultValue instanceof Long) {
                    value = (T) Long.valueOf(sp.getLong(key, ((Long) defaultValue).longValue()));
                } else {
                    value = (T) sp.getString(key, "");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }

    /**
     * 移除某个key对应的值
     * @param key
     */
    public void remove(String key) {
        if (null != mContext) {
            editor.remove(key);
            editor.commit();
        }
    }

    /**
     * 清除所有数据
     */
    public void clear() {
        if (null != mContext) {
            editor.clear();
            editor.commit();
        }
    }

    /**
     * 查询某个key是否存在
     * @param var1
     * @return
     */
    public boolean contains(String var1) {
        return null == mContext ? false : sp.contains(var1);
    }

    /**
     * 返回所有键值对
     * @return
     */
    public Map<String, ?> getAll() {
        return null == mContext ? null : sp.getAll();
    }
}
