package com.yofish.kitmodule.wedget.refresh;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;

import com.yofish.kitmodule.R;
import com.yofish.kitmodule.baseAdapter.recyclerview.CommonAdapter;
import com.yofish.kitmodule.baseAdapter.recyclerview.wrapper.HeaderAndFooterWrapper;
import com.yofish.kitmodule.wedget.refresh.pull2Refresh.PtrDefaultHandler;
import com.yofish.kitmodule.wedget.refresh.pull2Refresh.PtrFrameLayout;
import com.yofish.kitmodule.wedget.refresh.pull2Refresh.PtrHandler;
import com.yofish.kitmodule.wedget.refresh.pull2Refresh.PtrMetialFrameLayout;

import static android.support.v7.widget.RecyclerView.SCROLL_STATE_IDLE;

/**
 * 下拉刷新和加载更多的容器， 目前只兼容ListView和RecyclerView
 * <p>
 * Created by hch on 2017/6/21. modify by hch on 2017/9/6.
 */

public class RefreshContainer extends PtrMetialFrameLayout {

    private enum ScrollableViewType {
        LISTVIEW, RECYCLEVIEW, GRIDVIEW, SCROLLVIEW
    }

    /**
     * 子view的类型
     */
    private ScrollableViewType viewType;
    /**
     * 能滚动的子view
     */
    private ViewGroup mScrollableChild;
    /**
     * listview的footer
     */
    private HeaderAndFooterWrapper wrapper;
    /**
     * 是否应该加载更多
     */
    boolean shouldLoadMore = false;
    /**
     * 是否支持加载更多
     */
    boolean supportLoadMore = false;
    /**
     * 是否正在加载
     */
    boolean isLoading = false;
    /**
     * 加载Footer工具
     */
    private LoadMoreFooterUtils mLoadMoreUtils;
    /**
     * 加载更多的回调
     */
    private OnLoadMoreListener mLoadMoreListener;
    /**
     * 加载更多的回调
     */
    private OnRefreshListener mRefreshListener;

    public RefreshContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray array = getContext().obtainStyledAttributes(attrs, R.styleable.RefreshContainer);
            supportLoadMore = array.getBoolean(R.styleable.RefreshContainer_support_loadmore, false);
            array.recycle();
        }
       // setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
    }

    /**
     * 获取mLoadMoreUtils，用于加载后更新Footer状态
     *
     * @return mLoadMoreUtils
     */
    public LoadMoreFooterUtils getLoadMoreFooterUtils() {
        return mLoadMoreUtils;
    }

    public void setAutoRefreshing() {
        setPtrHandler(new PtrHandler() {

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                if (mRefreshListener != null) {
                    mRefreshListener.onRefresh();
                }
            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
            }
        });
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        int childCount = getChildCount();
        if (childCount != 2) {
            throw new IllegalStateException("childCount must be 2");
        }
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            if (child instanceof ListView) {
                viewType = ScrollableViewType.LISTVIEW;
                mScrollableChild = (ViewGroup) child;
                break;
            } else if (child instanceof RecyclerView) {
                viewType = ScrollableViewType.RECYCLEVIEW;
                mScrollableChild = (ViewGroup) child;
                break;
            }
        }
        if (mScrollableChild == null) {
            throw new IllegalStateException("there is no scrollable child");
        }
        mLoadMoreUtils = new LoadMoreFooterUtils(getContext(), mScrollableChild);
        dealScrollableView();
    }

    private void dealScrollableView() {
        switch (viewType) {
            case LISTVIEW:
                initListview();
                break;
            case RECYCLEVIEW:
                initRecyclerView();
                break;
            case GRIDVIEW:
                break;
            case SCROLLVIEW:
                break;
            default:
                break;
        }
    }

    /**
     * 设置listview的配置
     */
    private void initListview() {
        if (!supportLoadMore) {
            return;
        }
        ListView listView = (ListView) mScrollableChild;
        listView.addFooterView(mLoadMoreUtils.getFooterView());
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (shouldLoadMore && mLoadMoreUtils.canLoadMore() && scrollState == SCROLL_STATE_IDLE) {
                    if (isLoading) {
                        return;
                    }
                    mLoadMoreUtils.setFooterStatus(LoadMoreFooterUtils.LoadMoreStatus.LOADING);
                    isLoading = true;
                    if (mLoadMoreListener != null) {
                        mLoadMoreListener.onLoadMore();
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                shouldLoadMore = firstVisibleItem + visibleItemCount == totalItemCount;
            }
        });
    }

    /**
     * 设置recyclerview的配置
     */
    private void initRecyclerView() {
        if (!supportLoadMore) {
            return;
        }
        RecyclerView recyclerView = (RecyclerView) mScrollableChild;
        wrapper = new HeaderAndFooterWrapper(recyclerView.getAdapter());
        wrapper.addFootView(mLoadMoreUtils.getFooterView());
        recyclerView.setAdapter(wrapper);
        wrapper.notifyDataSetChanged();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState != SCROLL_STATE_IDLE || isLoading || !mLoadMoreUtils.canLoadMore()) {
                    return;
                }
                if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
                    LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
                    int lastItem = manager.findLastVisibleItemPosition();
                    if (lastItem == manager.getItemCount() - 1) {
                        if (mLoadMoreListener != null) {
                            mLoadMoreUtils.setFooterStatus(LoadMoreFooterUtils.LoadMoreStatus.LOADING);
                            mLoadMoreListener.onLoadMore();
                            isLoading = true;
                        }
                    }
                }
            }
        });
    }

    /**
     * 加载完成
     */
    public void loadComplete() {
        refreshComplete();
        shouldLoadMore = false;
        if (wrapper != null) {
            wrapper.notifyDataSetChanged();
        }
        isLoading = false;
    }

    public interface OnLoadMoreListener {
        void onLoadMore();
    }

    public void setOnLoadMoreListener(OnLoadMoreListener loadListener) {
        mLoadMoreListener = loadListener;
    }


    public interface OnRefreshListener {
        void onRefresh();
    }

    public void setOnRefreshListener(OnRefreshListener loadListener) {
        mRefreshListener = loadListener;
    }

}
