package com.yofish.kitmodule.wedget.viewpager;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * 通用的ViewPager适配器
 *
 * Created by hch on 2016/10/24.
 */

public abstract class CommonViewPagerAdapter<T> extends PagerAdapter {

    /** 数据 */
    private List<T> mDataList;
    /** context */
    private Context mContext;
    /** 页面ID */
    private int mLayoutId;
    /** inflater */
    private LayoutInflater mInflater;

    /**
     * 构造
     * 
     * @param context
     *            context
     * @param layoutId
     *            layoutId
     */
    public CommonViewPagerAdapter(Context context, int layoutId) {
        this.mContext = context;
        this.mLayoutId = layoutId;
        mDataList = new ArrayList<>();
        mInflater = LayoutInflater.from(context);
    }

    /**
     * 构造
     * 
     * @param context
     *            context
     * @param layoutId
     *            layoutId
     * @param datas
     *            datas
     */
    public CommonViewPagerAdapter(Context context, int layoutId, List<T> datas) {
        if (datas == null) {
            mDataList = new ArrayList<>();
        } else {
            mDataList = datas;
        }
        this.mLayoutId = layoutId;
        this.mContext = context;
        mInflater = LayoutInflater.from(context);

    }

    @Override
    public int getCount() {
        return mDataList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = mInflater.inflate(mLayoutId, container, false);
        container.addView(view);
        if (mDataList.size() < 1) return view;
        convert(view, mDataList.get(position));
        return view;
    }

    @Override
    public int getItemPosition(Object object) {
        if (getCount() > 0) {
            return PagerAdapter.POSITION_NONE;
        } else {
            return PagerAdapter.POSITION_UNCHANGED;
        }
    }

    /**
     * 实现类需要实现此方法，设置view的数据
     * 
     * @param view
     *            每个itemview
     * @param itemData
     *            数据
     */
    protected abstract void convert(View view, T itemData);

    public void resetDatas(List<T> dataList) {
        if (dataList == null) {
            return;
        }
        mDataList.clear();
        mDataList.addAll(dataList);
        notifyDataSetChanged();
    }
}
