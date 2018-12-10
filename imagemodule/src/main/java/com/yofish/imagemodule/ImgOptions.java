package com.yofish.imagemodule;

import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.request.RequestOptions;

import java.io.File;

/**
 * 图片加载的配置选项
 *
 * Created by hch on 2017/8/1.
 */

public class ImgOptions {

    private ImageView imageView;
    /**
     * 图片地址
     */
    private String url;
    /**
     * 占位图
     */
    private int placeHolder;
    /**
     * 错误图
     */
    private int error;
    /**
     * 本地图片路径
     */
    private String path;
    /**
     * 图片Uri地址
     */
    private Uri uri;
    /**
     * 图片文件
     */
    private File file;
    /**
     * 图片资源Id
     */
    private int resourceId;
    /**
     * 字节类型图片
     */
    private byte[] imgByte;
    /**
     * 圆角 大小
     */
    private int radius;
    /**
     * 图片样式
     */
    private ImgType type;

    private LoadType loadType;

    private BitmapTransformation bitmapTransformation;

    private RequestOptions requestOptions;

    public enum ImgType{
        NORMAL, CIRCLE, ROUND
    }

    public enum LoadType{
        URL, PATH, URI, FILE, RESOURCEID, IMGBYTE
    }

    private ImgLoaderListener listener;

    public ImgOptions(){}

    public ImgOptions(String url, ImageView imageView){
        this.url = url;
        this.imageView = imageView;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getPlaceHolder() {
        return placeHolder;
    }

    public void setPlaceHolder(int placeHolder) {
        this.placeHolder = placeHolder;
    }

    public BitmapTransformation getBitmapTransformation() {
        return bitmapTransformation;
    }

    public void setBitmapTransformation(BitmapTransformation bitmapTransformation) {
        this.bitmapTransformation = bitmapTransformation;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getError() {
        return error;
    }

    public void setError(int error) {
        this.error = error;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public ImgType getType() {
        return type;
    }

    public void setType(ImgType type) {
        this.type = type;
    }

    public LoadType getLoadType() {
        return loadType;
    }

    public void setLoadType(LoadType loadType) {
        this.loadType = loadType;
    }

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public int getResourceId() {
        return resourceId;
    }

    public void setResourceId(int resourceId) {
        this.resourceId = resourceId;
    }

    public byte[] getImgByte() {
        return imgByte;
    }

    public void setImgByte(byte[] imgByte) {
        this.imgByte = imgByte;
    }

    public RequestOptions getRequestOptions() {
        return requestOptions;
    }

    public void setRequestOptions(RequestOptions requestOptions) {
        this.requestOptions = requestOptions;
    }

    public ImgLoaderListener getListener() {
        return listener;
    }

    public void setListener(ImgLoaderListener listener) {
        this.listener = listener;
    }
}
