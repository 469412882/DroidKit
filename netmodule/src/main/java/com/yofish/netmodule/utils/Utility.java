package com.yofish.netmodule.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.ColorInt;
import android.support.v4.graphics.ColorUtils;
import android.text.TextUtils;
import android.util.TypedValue;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.internal.$Gson$Types;
import com.yofish.netmodule.download.DownloadInfo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.text.DecimalFormat;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import okhttp3.ResponseBody;

/**
 * Created by hch on 2017/6/29.
 */

public class Utility {

    public static Type getGenericTypeFromClass(Class<?> clazz) {
        Type t = clazz.getGenericSuperclass();
        if (t instanceof Class) {
            t = clazz.getSuperclass().getGenericSuperclass();
        }
        if (!(t instanceof ParameterizedType)) {
            return null;
        }
        ParameterizedType parameterized = (ParameterizedType) t;
        return $Gson$Types.canonicalize(parameterized.getActualTypeArguments()[0]);
    }

    /**
     * 将String数据转换为泛型的数据
     *
     * @param data  接口返回的String类型的data数据
     * @param clazz callback,此callback要有泛型类型
     * @return
     */
    public static Object parseDataByType(String data, Class clazz) {
        Type type = getGenericTypeFromClass(clazz);
        if (type == null) {
            return data;
        }
        try {
            Object o = JSON.parseObject(data, type);
            return o;
        } catch (Exception e) {
            return data;
        }
    }

    /**
     * 读取baseurl
     *
     * @param url
     * @return
     */
    public static String getBasUrl(String url) {
        String head = "";
        int index = url.indexOf("://");
        if (index != -1) {
            head = url.substring(0, index + 3);
            url = url.substring(index + 3);
        }
        index = url.indexOf("/");
        if (index != -1) {
            url = url.substring(0, index + 1);
        }
        return head + url;
    }

    /**
     * 写入文件
     *
     * @param info info
     * @throws IOException IOException
     */
    public static void writeCache(ResponseBody responseBody, DownloadInfo info) throws IOException {
        File file = new File(info.getSavePath());
        if (!file.getParentFile().exists())
            file.getParentFile().mkdirs();
        long allLength;
        if (info.getCountLength() == 0) {
            allLength = responseBody.contentLength();
        } else {
            allLength = info.getCountLength();
        }
        FileChannel channelOut = null;
        RandomAccessFile randomAccessFile = null;
        randomAccessFile = new RandomAccessFile(file, "rwd");
        channelOut = randomAccessFile.getChannel();
        MappedByteBuffer mappedBuffer = channelOut.map(FileChannel.MapMode.READ_WRITE, info.getReadLength(), allLength
                - info.getReadLength());
        byte[] buffer = new byte[1024 * 8];
        int len;
        int record = 0;
        while ((len = responseBody.byteStream().read(buffer)) != -1) {
            mappedBuffer.put(buffer, 0, len);
            record += len;
        }
        responseBody.byteStream().close();
        if (channelOut != null) {
            channelOut.close();
        }
        if (randomAccessFile != null) {
            randomAccessFile.close();
        }
    }
}
