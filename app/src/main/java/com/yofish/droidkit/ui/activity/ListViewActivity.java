package com.yofish.droidkit.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.ListView;

import com.yofish.kitmodule.baseAdapter.abslistview.CommonAdapter;
import com.yofish.kitmodule.baseAdapter.abslistview.ViewHolder;
import com.yofish.kitmodule.base_component.BaseActivity;
import com.yofish.kitmodule.wedget.refresh.RefreshContainer;
import com.yofish.droidkit.R;
import com.yofish.droidkit.repository.bean.BankData;
import com.yofish.droidkit.repository.bean.BankInfoBean;
import com.yofish.netmodule.NetClient;
import com.yofish.netmodule.callback.BaseCallBack;

/**
 * listview页面
 * <p>
 * Created by hch on 2017/9/6.
 */

public class ListViewActivity extends BaseActivity {
    private CommonAdapter<BankInfoBean> mAdapter;
    private RefreshContainer container;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initToolbar("ListView下拉加载和加载更多");
    }

    @Override
    protected int setLayoutId(Bundle savedInstanceState) {
        return R.layout.activity_listview;
    }

    @Override
    protected void initViews() {
        ListView listView = (ListView) findViewById(R.id.list);
        listView.setAdapter(mAdapter = new CommonAdapter<BankInfoBean>(this, R.layout.banklist_item) {
            @Override
            protected void convert(ViewHolder viewHolder, BankInfoBean item, int position) {
                viewHolder.setImageResource(R.id.bank_logo, R.mipmap.ic_launcher_round);
                viewHolder.setText(R.id.bank_name, item.getBankName());
            }
        });
        container = (RefreshContainer) findViewById(R.id.container);
        container.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData(false);
            }
        });
        container.setAutoRefreshing();
        container.setOnLoadMoreListener(new RefreshContainer.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                loadData(true);
            }
        });
    }

    @Override
    protected void loadData() {

    }

    private void loadData(final boolean isLoadMore) {
        NetClient.newBuilder(this).baseUrl("http://credit.youyuwo.com/notcontrol/manage/")
                .method("queryCardImportBankList.go").callBack(new BaseCallBack<BankData>() {
            @Override
            public void onSuccess(BankData bankData) {
                if (isLoadMore) {
                    mAdapter.addData(bankData.getBankList());
                } else {
                    mAdapter.resetData(bankData.getBankList());
                }
                container.getLoadMoreFooterUtils().updatePages(1, 1);
            }

            @Override
            public void onFailed(String code, String errors) {

            }

            @Override
            public void onComplete() {
                super.onComplete();
                container.loadComplete();
            }
        }).sendPost();
    }
}
