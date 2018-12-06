package com.yofish.imagemodule.utils;


import java.io.File;
import java.text.DecimalFormat;

/**
 * Created by hch on 2017/6/29.
 */

public class Utility {
    /**
     * 将byte转换为KB、MB、GB
     *
     * @param bytes bytes
     * @return String
     */
    public static String getFormatBytes(long bytes) {
        if (bytes < 1024) {
            return bytes + "B";
        } else if (bytes < 1024 * 1024) {
            return bytes / 1024 + "K";
        } else if (bytes < 1024 * 1024 * 1024) {
            return formatFloat(bytes / (float) (1024 * 1024)) + "M";
        } else {
            return formatFloat(bytes / (float) (1024 * 1024 * 1024)) + "G";
        }
    }

    /**
     * 设置格式化浮点小数位数
     *
     * @param num
     * @return
     */
    public static String formatFloat(float num) {
        String format = new DecimalFormat("0.00").format(num);
        return format;
    }

    /**
     * 获取文件夹的大小
     *
     * @param file 文件夹
     * @return long
     */
    public static long getTotalSizeOfFilesInDir(File file) {
        if (file == null)
            return 0;
        if (file.isFile())
            return file.length();
        final File[] children = file.listFiles();
        long total = 0;
        if (children != null)
            for (final File child : children)
                total += getTotalSizeOfFilesInDir(child);
        return total;
    }
}
