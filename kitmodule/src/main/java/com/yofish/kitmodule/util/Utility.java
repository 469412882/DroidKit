package com.yofish.kitmodule.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.ColorInt;
import android.support.v4.content.FileProvider;
import android.support.v4.graphics.ColorUtils;
import android.text.ClipboardManager;
import android.text.TextUtils;
import android.util.TypedValue;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import static android.content.Context.CLIPBOARD_SERVICE;


/**
 * Created by hch on 2017/6/29.
 */

public class Utility {
    public static Pattern idNumPattern = Pattern.compile("^[1-9][0-7]\\d{4}((19\\d{2}(0[13-9]|1[012])(0[1-9]|[12]\\d|30))|(19\\d{2}(0[13578]|1[02])31)|(19\\d{2}02(0[1-9]|1\\d|2[0-8]))|(19([13579][26]|[2468][048]|0[48])0229))\\d{3}(\\d|X|x)?$");
    public static String[] ID_JIAO_YAN = new String[]{"1", "0", "X", "9", "8", "7", "6", "5", "4", "3", "2"};
    public static int[] ID_XI_SHU = new int[]{7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};

    public static File saveFile(InputStream inputStream, String fileName) {
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return null;
        }
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "kit");
        if (!file.exists()) {
            file.mkdirs();
        }
        File uploadFile = new File(file.getAbsolutePath(), fileName);
        OutputStream outputstream = null;
        try {
            outputstream = new FileOutputStream(uploadFile);
            byte buffer[] = new byte[1024];
            int len = -1;
            while ((len = inputStream.read(buffer)) != -1) {
                outputstream.write(buffer, 0, len);
            }
            inputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return uploadFile;
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
     * 检测网络是否可用
     *
     * @param context context
     * @return 网络是否可用
     */
    public static boolean isNetworkConnected(Context context) {
        boolean flag = false;
        if (null == context) {
            return false;
        }
        try {
            ConnectivityManager connectivity = (ConnectivityManager) context.getApplicationContext().getSystemService(
                    Context.CONNECTIVITY_SERVICE);
            if (connectivity == null) {
                return false;
            }
            NetworkInfo networkInfo = connectivity.getActiveNetworkInfo();
            flag = networkInfo != null && networkInfo.isAvailable();
        } catch (Exception e) {
            return false;
        }
        return flag;
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
     * 设置格式化浮点小数位数
     *
     * @param num
     * @return
     */
    public static String formatFloat(double num) {

        String format = new DecimalFormat("0.00").format(num);
        return format;
    }

    /**
     * 设置格式化浮点小数位数
     *
     * @param num
     * @return
     */
    public static String formatFloat(String num) {
        if (null == num || num.equals("")) {
            num = "0";
        }
        String format = new DecimalFormat("0.00").format(Float.valueOf(num));
        return format;
    }

    /**
     * Convert Dp to Pixel
     */
    public static int dpToPx(float dp, Resources resources) {
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.getDisplayMetrics());
        return (int) px;
    }

    /**
     * 获取uri中的key值
     *
     * @param uri uri
     * @return Set
     */
    public static Set<String> getQueryParameterNames(Uri uri) {
        if (uri.isOpaque()) {
            throw new UnsupportedOperationException("This isn't a hierarchical URI.");
        }
        String params = uri.getEncodedQuery();
        if (params == null) {
            return Collections.emptySet();
        } else {
            LinkedHashSet keySet = new LinkedHashSet();
            int cursor = 0;

            do {
                int paramIndex = params.indexOf(38, cursor);
                int paramIndexTemp = paramIndex == -1 ? params.length() : paramIndex;
                int keyIndex = params.indexOf(61, cursor);
                if (keyIndex > paramIndexTemp || keyIndex == -1) {
                    keyIndex = paramIndexTemp;
                }

                String key = params.substring(cursor, keyIndex);
                keySet.add(Uri.decode(key));
                cursor = paramIndexTemp + 1;
            } while (cursor < params.length());

            return Collections.unmodifiableSet(keySet);
        }
    }

    /**
     * 获取versionCode
     *
     * @param context context
     * @return int
     */
    public static int getVersionCode(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * 获取versionName
     *
     * @param context context
     * @return String
     */
    public static String getVersionName(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 获取packageName
     *
     * @param context context
     * @return String
     */
    public static String getPackageName(Context context) {
        return context.getPackageName();
    }

    /**
     * 获取appName
     *
     * @param context context
     * @return String
     */
    public static String getAppName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            int lr = packageInfo.applicationInfo.labelRes;
            return context.getResources().getString(lr);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取MetaData
     *
     * @param context context
     * @return String
     */
    public static String getMetaDataFromApp(Context context, String key) {
        try {
            ApplicationInfo applicationInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            Object value = applicationInfo.metaData.get(key);
            if (value != null)
                return value.toString();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "";
        }
        return "";
    }

    /**
     * 根据名称获取drawableId
     *
     * @param context context
     * @param  drawableId drawableId
     * @return int
     */
    public static int getDrawableId(Context context, String drawableId) {
        return context.getResources().getIdentifier(drawableId, "drawable", context.getPackageName());
    }

    /**
     * 反射获取value
     * @param object 对象
     * @param fieldName 属性名称
     * @return 属性值
     * @throws Exception e
     */
    public static Object getValue(Object object, String fieldName) throws Exception {
        if (object == null) {
            return null;
        }
        if (TextUtils.isEmpty(fieldName)) {
            return null;
        }
        Field field = null;
        Class<?> clazz = object.getClass();
        for (; clazz != Object.class; clazz = clazz.getSuperclass()) {
            try {
                field = clazz.getDeclaredField(fieldName);
                field.setAccessible(true);
                return field.get(object);
            } catch (Exception e) {
                //这里甚么都不要做！并且这里的异常必须这样写，不能抛出去。
                //如果这里的异常打印或者往外抛，则就不会执行clazz = clazz.getSuperclass(),最后就不会进入到父类中了
            }
        }

        return null;
    }

    /**
     * 读取assets文件
     * @param context context
     * @param fileName fileName
     * @return JsonObject
     */
    public static JsonObject readAssetsJsonFile(Context context, String fileName) {
        InputStream inputStream = null;
        StringBuilder stringBuilder = new StringBuilder();

        try {
            inputStream = context.getAssets().open(fileName, AssetManager.ACCESS_BUFFER);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));

            String line;
            while((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if(inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        return (new JsonParser()).parse(stringBuilder.toString()).getAsJsonObject();
    }

    public static String testSpData(){
        return AppSharedPrefrences.getInstance().get("test", "");
    }

    public static boolean isDarkColor(@ColorInt int color) {
        return ColorUtils.calculateLuminance(color) >= 0.5D;
    }



    /**
     *  粘贴板
     * @param content
     * @param context
     */
    public static void copyToClipboard(String content, Context context) {
        try {
            if(Build.VERSION.SDK_INT <= 11) {
                ClipboardManager var2 = (ClipboardManager)context.getApplicationContext().getSystemService(CLIPBOARD_SERVICE);
                if(var2 != null) {
                    var2.setText(content);
                    Toast.makeText(context, "复制成功", Toast.LENGTH_SHORT).show();
                }
            } else {
                android.content.ClipboardManager var4 = (android.content.ClipboardManager)context.getApplicationContext().getSystemService(CLIPBOARD_SERVICE);
                if(var4 != null) {
                    var4.setText(content);
                    Toast.makeText(context, "复制成功", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception var3) {
            LogUtils.e( var3.toString());
        }

    }

    /**
     * 获取剪切板内容
     * @param context
     * @return
     */
    public static String getClipBoardContent(Context context) {
        try {
            if(Build.VERSION.SDK_INT <= 11) {
                ClipboardManager var3 = (ClipboardManager)context.getApplicationContext().getSystemService(CLIPBOARD_SERVICE);
                return var3.getText().toString();
            } else {
                android.content.ClipboardManager var1 = (android.content.ClipboardManager)context.getApplicationContext().getSystemService(CLIPBOARD_SERVICE);
                return var1 != null?var1.getText().toString():null;
            }
        } catch (Exception var2) {
            LogUtils.e(var2.toString());
            return null;
        }
    }

    /**
     * 打电话
     * @param context
     * @param phoneNum
     */
    public static void makeCall(Context context, String phoneNum) {
        try {
            Intent var2 = new Intent("android.intent.action.DIAL", Uri.parse("tel:" + phoneNum));
            var2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(var2);
        } catch (ActivityNotFoundException var3) {
            Toast.makeText(context, "抱歉，未找到打电话的应用", Toast.LENGTH_SHORT).show();
        } catch (Exception var4) {
            ;
        }

    }

    /**
     * 发短信
     * @param context
     * @param phoneNum
     * @param msg
     */
    public static void makeMsg(Context context, String phoneNum, String msg) {
        try {
            Uri var3 = Uri.parse("smsto:" + phoneNum);
            Intent var4 = new Intent("android.intent.action.SENDTO", var3);
            var4.putExtra("sms_body", msg);
            var4.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(var4);
        } catch (ActivityNotFoundException var5) {
            Toast.makeText(context, "抱歉，未找到短信应用", Toast.LENGTH_SHORT).show();
        } catch (Exception var6) {
            ;
        }

    }

    /**
     * 校验身份证号
     * @param context
     * @param idNum
     * @return
     */
    public static boolean checkIDcard(Context context, String idNum) {
        boolean var2 = idNumPattern.matcher(idNum).matches();
        if(!var2) {
            return false;
        } else if(idNum.length() < 18) {
            Toast.makeText(context, "请输入正确的二代身份证号码", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            char[] var3 = idNum.toCharArray();
            int var4 = 0;

            int var5;
            for(var5 = 0; var5 < var3.length - 1; ++var5) {
                var4 += Integer.parseInt(var3[var5] + "") * ID_XI_SHU[var5];
            }

            var5 = var4 % 11;
            String var6 = ID_JIAO_YAN[var5];
            String var7 = var3[var3.length - 1] + "";
            if(var6.toUpperCase().equals(var7.toUpperCase())) {
                var2 = true;
            } else {
                var2 = false;
            }

            return var2;
        }
    }

    /**
     * 判断集合是否为空
     * @param list
     * @return
     */
    public static boolean isNotEmptyList(List list) {
        return list != null && list.size() > 0;
    }




}
