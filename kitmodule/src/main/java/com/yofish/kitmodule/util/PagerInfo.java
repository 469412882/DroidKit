package com.yofish.kitmodule.util;

/**
 * 分页数据
 * <p>
 * Created by hch on 2018/12/13.
 */
public class PagerInfo {
    public int totalPage;
    public int currentPage;
    public PagerInfo(int currentPage, int totalPage){
        this.currentPage = currentPage;
        this.totalPage = totalPage;
    }
}
