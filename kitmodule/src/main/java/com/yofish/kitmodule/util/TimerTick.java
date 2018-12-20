package com.yofish.kitmodule.util;

/**
 * 倒计时
 *
 * Created by hch on 2018/5/15.
 */
public class TimerTick implements Runnable {

    /** TAG */
    private static final String TAG = "MCTimerTick";
    /**
     * 停止线程标识
     */
    private volatile boolean stop = false;
    /**
     * 最大进程数
     */
    private int maxSeconds = 100;
    /**
     * 进度
     */
    private int currentSecond = 0;
    /**
     * 线程暂停标识
     */
    private boolean suspend = false;
    /**
     * object
     */
    private String control = "";
    /**
     * callBack
     */
    private TimeOutCallBack callBack;

    /**
     * 构造
     */
    public TimerTick(int maxSeconds, TimeOutCallBack callBack) {
        this.maxSeconds = maxSeconds;
        this.callBack = callBack;
    }


    @Override
    public void run() {
        currentSecond = maxSeconds;
        while (!stop && currentSecond >= 0) {
            // 线程暂停
            synchronized (control) {
                if (suspend) {
                    try {
                        control.wait();
                    } catch (InterruptedException e) {
                        break;
                    }
                }

            }
            try {
                Thread.sleep(1000);
                if (--currentSecond == 0) {
                    if (callBack != null) {
                        callBack.timeout();
                    }
                } else {
                    if (callBack != null) {
                        callBack.count(currentSecond);
                    }
                }
            } catch (InterruptedException e) {
                stop = true;
                break;
            }

        }
    }

    /**
     * 暂停线程
     *
     * @param suspend
     *            suspend
     */
    public void setSuspend(boolean suspend) {
        if (!suspend) {
            synchronized (control) {
                control.notifyAll();
            }
        }

        this.suspend = suspend;
    }

    /**
     * 获取进程标识
     *
     * @return stop
     */
    public boolean isStop() {
        return stop;
    }

    /**
     * 设置进程标识
     */
    public void setStop(boolean stop) {
        this.stop = stop;
    }

    /**
     * 重置
     */
    public void reset(){
        currentSecond = maxSeconds;
    }

    public interface TimeOutCallBack{
        void timeout();
        void count(int count);
    }

}
