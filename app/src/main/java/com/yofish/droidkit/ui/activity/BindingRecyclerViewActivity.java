package com.yofish.droidkit.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import com.yofish.droidkit.R;
import com.yofish.droidkit.BR;
import com.yofish.droidkit.databinding.ActivityRecyclerviewBindingBinding;
import com.yofish.droidkit.viewmodule.BankListViewModel;
import com.yofish.kitmodule.base_component.BaseBindingActivity;
import com.yofish.kitmodule.util.PagerInfo;
import com.yofish.kitmodule.wedget.recyclerview.RecycleViewItemDiver;
import com.yofish.kitmodule.wedget.refresh.RefreshContainer;

/**
 * RecyclerView页面
 * <p>
 * Created by hch on 2017/12/1.
 */

public class BindingRecyclerViewActivity extends BaseBindingActivity<ActivityRecyclerviewBindingBinding, BankListViewModel> {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initToolbar("Binding RecyclerView");
    }

    @Override
    protected int initVariableId() {
        return BR.viewModel;
    }

    @Override
    protected BankListViewModel initViewModel() {
        return createViewModel(this, BankListViewModel.class);
    }

    @Override
    protected int setLayoutId(Bundle savedInstanceState) {
        return R.layout.activity_recyclerview_binding;
    }

    protected void initBindingViews() {
        binding.recyclerview.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerview.addItemDecoration(new RecycleViewItemDiver(this, LinearLayoutManager.HORIZONTAL));
        binding.container.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                viewModel.requestRepository(false);
            }
        });
        binding.container.setAutoRefreshing();
        binding.container.setOnLoadMoreListener(new RefreshContainer.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                viewModel.requestRepository(true);
            }
        });
    }

    @Override
    public void loadingComplete() {
        super.loadingComplete();
        binding.container.loadComplete();
    }

    @Override
    public void updatePage(PagerInfo pagerInfo) {
        super.updatePage(pagerInfo);
        binding.container.getLoadMoreFooterUtils().updatePages(pagerInfo.currentPage, pagerInfo.totalPage);
    }
}
