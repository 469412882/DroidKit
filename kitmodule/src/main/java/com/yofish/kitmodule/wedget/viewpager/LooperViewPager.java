package com.yofish.kitmodule.wedget.viewpager;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

/**
 * ViewPager无限循环实现
 * 1、实现一个无限大的PagerAdapter
 * 2、继承ViewPager重写setAdapter方法
 * 3、实现一个自动滚屏的控制器 注意：数据刷新调用NewLooperPager类里的notifyDataSetChanged
 * 
 * @author hch
 *
 */
public class LooperViewPager extends ViewPager implements Runnable {

    /** 代理adapter */
    private LoopPagerAdapter loopAdapter;
    /** 真实的adapter */
    private PagerAdapter adapter;
    /** 自动滚屏时间间隔 */
    private int POST_DELAYED_TIME = 4000;
    /** 自动滚屏开关 */
    private boolean isLoop = true;
    /** 解决notifyDataSetChanged不刷新数据问题 */
    private boolean isNotify = false;

    public LooperViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setAdapter(PagerAdapter adapter) {
        if (adapter != null) {
            // 使用代理adapter
            loopAdapter = new LoopPagerAdapter(adapter);
            this.adapter = adapter;
        }
        super.setAdapter(loopAdapter);
        startLoop();
    }

    @Override
    public PagerAdapter getAdapter() {
        return adapter;
    }

    public LoopPagerAdapter getLoopAdapter() {
        return loopAdapter;
    }

    /**
     * 解决数据刷新问题
     */
    public void notifyDataSetChanged() {
        isNotify = true;
        loopAdapter.notifyDataSetChanged();
        if (isLoop) {
            stopLoop();
            startLoop();
        }
    }

    /**
     * 设置自动滚屏时间
     * 
     * @param time
     *            time
     */
    public void setLoopTime(int time) {
        POST_DELAYED_TIME = time;
    }

    /**
     * 开启和关闭滚屏
     * 
     * @param isLoop
     *            isLoop
     */
    public void setLoop(boolean isLoop) {
        this.isLoop = isLoop;
        if (isLoop) {
            startLoop();
        } else {
            startLoop();
        }
    }

    private void startLoop() {
        stopLoop();
        postDelayed(this, POST_DELAYED_TIME);
    }

    private void stopLoop() {
        removeCallbacks(this);
    }

    public boolean isLoop() {
        return isLoop;
    }

    /**
     * 获取当前的item
     * 
     * @return int
     */
    public int getLoopCurrentItem() {
        int curent_item = 0;
        if (adapter != null && getCurrentItem() != 0 && adapter.getCount() != 0) {
            if (getCurrentItem() >= adapter.getCount()) {
                curent_item = getCurrentItem() % adapter.getCount();
            }
        }
        return curent_item;
    }

    @Override
    // 自动滚动关键
    public void run() {
        if (!isLoop) {
            return;
        }
        if (getAdapter() != null && getAdapter().getCount() > 1) {
            setCurrentItem(getCurrentItem() + 1);
        }
        startLoop();
    }

    /**
     * 当touch时取消自动滚动
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            stopLoop();
        } else if (ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_CANCEL) {
            if (isLoop) {
                startLoop();
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 无限大的pagerAdapter
     * 
     * @author sdsw
     *
     */
    class LoopPagerAdapter extends PagerAdapter {

        private PagerAdapter adapter;

        public LoopPagerAdapter(PagerAdapter adapter) {
            this.adapter = adapter;
        }

        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return adapter.isViewFromObject(arg0, arg1);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            int count = adapter.getCount();
            int new_position = 0;
            if (count != 0)
                new_position = position % adapter.getCount();
            adapter.destroyItem(container, new_position, object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            int count = adapter.getCount();
            int new_position = 0;
            if (count != 0)
                new_position = position % adapter.getCount();
            return adapter.instantiateItem(container, new_position);
        }

        @Override
        public int getItemPosition(Object object) {
            if (isNotify) {
                isNotify = false;
                return POSITION_NONE;
            }
            return super.getItemPosition(object);
        }
    }

}