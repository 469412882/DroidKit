package com.yofish.kitmodule.wedget.refresh;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yofish.kitmodule.R;

/**
 * 加载更多Footer工具类
 * <p>
 * Created by hch on 2017/11/30.
 */

public class LoadMoreFooterUtils {
    /** 底部Footer */
    private View mFooterView;
    /** 总页数 */
    private int mTotalPageNum;
    /** 当前页数 */
    private int mCurrentPageNum;
    /** context */
    private Context mContext;
    /** 状态值 */
    private LoadMoreStatus mLoadMoreStatus;
    /** 进度条 */
    private View mPbar;
    /** 提示语 */
    private TextView mHint;

    public LoadMoreFooterUtils(Context context, ViewGroup parent) {
        this(context, parent, R.layout.loadmore_footer);
    }

    private LoadMoreFooterUtils(Context context, ViewGroup parent, int layoutId) {
        this.mContext = context;
        mFooterView = LayoutInflater.from(mContext).inflate(layoutId, parent, false);
        mPbar = mFooterView.findViewById(R.id.progressBar);
        mHint = (TextView) mFooterView.findViewById(R.id.tip_hint);
        mLoadMoreStatus = LoadMoreStatus.INIT;
        refreshFooterView();
    }

    public View getFooterView() {
        return mFooterView;
    }

    public void setFooterStatus(LoadMoreStatus status) {
        this.mLoadMoreStatus = status;
        refreshFooterView();
    }

    public void updatePages(String currentPagenum, String totalPageNum) {
        try {
            updatePages(Integer.parseInt(currentPagenum), Integer.parseInt(totalPageNum));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updatePages(int currentPagenum, int totalPageNum) {
        this.mCurrentPageNum = currentPagenum;
        this.mTotalPageNum = totalPageNum;
        if (mCurrentPageNum <= 0 || mTotalPageNum <= 0) {
            return;
        }
        if (mCurrentPageNum == mTotalPageNum) {
            mLoadMoreStatus = LoadMoreStatus.NOMORE;
        } else if (mCurrentPageNum < mTotalPageNum) {
            mLoadMoreStatus = LoadMoreStatus.READY;
        }
        refreshFooterView();
    }

    public boolean canLoadMore(){
        return LoadMoreStatus.NOMORE != mLoadMoreStatus;
    }

    /**
     * 根据状态刷新Footer
     */
    private void refreshFooterView() {
        switch (mLoadMoreStatus) {
            case INIT:
                mFooterView.setVisibility(View.GONE);
                break;
            case READY:
                mFooterView.setVisibility(View.VISIBLE);
                mPbar.setVisibility(View.GONE);
                mHint.setText("准备加载数据");
                break;
            case LOADING:
                mFooterView.setVisibility(View.VISIBLE);
                mPbar.setVisibility(View.VISIBLE);
                mHint.setText("正在加载数据");
                break;
            case NOMORE:
                mFooterView.setVisibility(View.VISIBLE);
                mPbar.setVisibility(View.GONE);
                mHint.setText("没有更多数据了");
                break;
            default:
                break;
        }
    }

    public enum LoadMoreStatus {
        INIT,
        READY,
        LOADING,
        NOMORE;

        LoadMoreStatus() {
        }
    }
}
