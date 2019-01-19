package com.yofish.imagemodule.utils;

import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.WindowManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 图片处理工具
 * <p>
 * Created by hch on 2017/10/10.
 */

public class ImgUtils {
    /**
     * 规定图片最大的宽度为1500像素，此像素用于参考处理图片上传至后台，这样基本能适配市面上绝大部分手机
     */
    public static final int PIC_MAX_WIDTH = 1500;

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),
                        Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    /**
     * 计算图片的宽高
     *
     * @param picUrl  路径
     * @param picSize 宽高
     */
    public static void caculatePicSize(String picUrl, int[] picSize) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(picUrl, options);
        final int width = options.outWidth;
        final int height = options.outHeight;
        picSize[0] = width;
        picSize[1] = height;
        resetPicSizeByDegree(picSize, getBitmapDegree(picUrl));
    }

    /**
     * 根据图片角度处理图片的宽高
     *
     * @param picSize picSize
     */
    private static void resetPicSizeByDegree(int[] picSize, int degree) {
        switch (degree) {
            case 90:
            case 270:
                int temp = picSize[0];
                picSize[0] = picSize[1];
                picSize[1] = temp;
                break;
            default:
                break;
        }
    }

    /**
     * 计算图片的压缩比例
     *
     * @param options   bitmap的options
     * @param reqWidth  目标宽度
     * @param reqHeight 目标高度
     * @return 缩放比例
     */
    private static int caculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight, int degree) {
        final int width = options.outWidth;
        final int height = options.outHeight;
        int[] picSize = new int[]{width, height};
        resetPicSizeByDegree(picSize, degree);
        int inSampleSize = 1;
        if (picSize[0] > reqWidth || picSize[1] > reqHeight) {
            final int widthRatio = Math.round((float) picSize[0] / (float) reqWidth);
            final int heightRatio = Math.round((float) picSize[1] / (float) reqHeight);
            inSampleSize = widthRatio > heightRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;

    }

    /**
     * 将图片压缩到指定的大小，按最小比例压缩，图片不会变形
     *
     * @param imagePath 图片路径
     * @param reqWidth  目标宽度
     * @param reqHeight 目标高度
     * @return 压缩后的图片
     */
    public static Bitmap getCompressBitmap(String imagePath, int reqWidth, int reqHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        int degree = getBitmapDegree(imagePath);
        BitmapFactory.decodeFile(imagePath, options);
        options.inSampleSize = caculateInSampleSize(options, reqWidth, reqHeight, degree);
        options.inJustDecodeBounds = false;
        Bitmap bitmap = BitmapFactory.decodeFile(imagePath, options);
        return rotateBitmapByDegree(bitmap, getBitmapDegree(imagePath));
    }

    /**
     * 按30%的质量压缩图片并保存在本地
     *
     * @param bitmap     要保存的bitmap
     * @param targetPath 要保存的路径
     * @return 保存是否成功
     */
    public static boolean saveBitmap2Local(Bitmap bitmap, String targetPath, int quality) {
        File file = new File(targetPath);
        if (file.exists()) {
            file.delete();
        }
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, fos);
            fos.flush();
            fos.close();
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 获取图片的旋转角度
     *
     * @param path 图片路径
     * @return 角度
     */
    private static int getBitmapDegree(String path) {
        try {
            // 从指定路径下读取图片，并获取其EXIF信息
            ExifInterface exifInterface = new ExifInterface(path);
            return getBitmapDegree(exifInterface);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return 0;
    }

    private static int getBitmapDegree(ExifInterface exifInterface) {
        int degree = 0;
        int orientaion = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
        switch (orientaion) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                degree = 90;
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                degree = 180;
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                degree = 270;
                break;
            default:
                break;
        }
        return degree;
    }

    /**
     * 根据角度旋转图片
     *
     * @param bitmap bitmap
     * @param degree 要旋转的角度
     * @return 旋转后的bitmap
     */
    private static Bitmap rotateBitmapByDegree(Bitmap bitmap, int degree) {
        Bitmap returnBm = null;

        // 根据旋转角度，生产旋转矩阵
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        // 将原有图片按照旋转矩阵进行旋转，得到新的bitmap
        returnBm = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        if (returnBm == null) {
            return bitmap;
        }
        if (bitmap != returnBm) {
            bitmap.recycle();
        }
        return returnBm;
    }

    public static void setTargetPicSize(String picUrl, int[] targetSize) {
        int[] sourceSize = new int[2];
        int picMaxWidth = getPicMaxWidthByDefault();
        caculatePicSize(picUrl, sourceSize);
        int targetWidth = 0, targetHeight = 0;
        if (sourceSize[0] < picMaxWidth) {
            targetWidth = sourceSize[0];
            targetHeight = sourceSize[1];
        } else {
            targetWidth = picMaxWidth;
            targetHeight = (int) ((float) sourceSize[1] / sourceSize[0] * picMaxWidth);
        }
        targetSize[0] = targetWidth;
        targetSize[1] = targetHeight;
    }

    /**
     * 获取图片的默认最大宽度，用于帖子的编辑和显示
     *
     * @return int
     */
    public static int getPicMaxWidthByDefault() {
        return PIC_MAX_WIDTH;
    }

    /**
     * 获取图片的最大宽度，用于帖子的编辑和显示
     *
     * @param context context
     * @return int
     */
    public static int getPicMaxWidthOnScreen(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int screenWidth = wm.getDefaultDisplay().getWidth();
        return screenWidth;
    }

    /**
     * 获取图片的最小宽度，用于帖子的编辑和显示
     *
     * @param context context
     * @return int
     */
    public static int getPicMinWidth(Context context) {
        return 100;
    }

    /**
     * 将原始的宽高根据比例设置成不大于屏幕的宽高
     *
     * @param context context
     * @param size    size
     */
    public static void getFitableSize(Context context, int[] size) {
        int maxWidth = getPicMaxWidthOnScreen(context);
        int minWidth = getPicMinWidth(context);
        if (size[0] > maxWidth / 2) {
            int height = (int) ((float) size[1] / size[0] * maxWidth);
            size[0] = maxWidth;
            size[1] = height;
        } else if (size[0] < minWidth) {
            int height = (int) ((float) size[1] / size[0] * minWidth);
            size[0] = minWidth;
            size[1] = height;
        }

    }

    /**
     * 根据url获取文件名称
     *
     * @param url url
     * @return fileName
     */
    public static String getFileNameByUrl(String url) {
        String fileName = "";
        if (!TextUtils.isEmpty(url)) {
            fileName = url.substring(url.lastIndexOf("/") + 1);
        }
        return fileName;
    }

    /**
     * 根据url获取图片名称，如果图片没有后缀，则加上.jpg后缀
     *
     * @param url url
     * @return picName
     */
    public static String getPicNameByUrl(String url) {
        String picName = getFileNameByUrl(url);
        if (!TextUtils.isEmpty(picName) && !picName.contains(".")) {
            picName = picName + ".jpg";
        }
        return picName;
    }

    /**
     * 重置bitmap宽高
     *
     * @param bitmap    bitmap
     * @param newWidth  newWidth
     * @param newHeight newHeight
     * @return Bitmap
     */
    public static Bitmap resizeBitmap(Bitmap bitmap, int newWidth, int newHeight) {
        // 获得图片的宽高.
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        // 计算缩放比例.
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 取得想要缩放的matrix参数.
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // 得到新的图片.
        return Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
    }
}
