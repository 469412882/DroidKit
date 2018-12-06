package com.yofish.imagemodule;

import android.widget.ImageView;

/**
 * 图片加载的配置选项
 *
 * Created by hch on 2017/8/1.
 */

public class ImgOptions {

    private ImageView imageView;
    private String url;
    private int placeHolder;
    private String path;
    private int radius;
    public enum ImgType{
        NORMAL,CIRCLE, ROUND;
    }
    private ImgType type;
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

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
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

    public ImgLoaderListener getListener() {
        return listener;
    }

    public void setListener(ImgLoaderListener listener) {
        this.listener = listener;
    }
}
