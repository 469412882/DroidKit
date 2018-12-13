package com.yofish.droidkit.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.yofish.droidkit.R;
import com.yofish.droidkit.repository.bean.BankData;
import com.yofish.droidkit.repository.bean.BankInfoBean;
import com.yofish.kitmodule.baseAdapter.recyclerview.CommonAdapter;
import com.yofish.kitmodule.base_component.BaseActivity;
import com.yofish.kitmodule.wedget.recyclerview.RecycleViewItemDiver;
import com.yofish.kitmodule.wedget.refresh.pull2Refresh.PtrDefaultHandler;
import com.yofish.kitmodule.wedget.refresh.pull2Refresh.PtrFrameLayout;
import com.yofish.kitmodule.wedget.refresh.pull2Refresh.PtrHandler;
import com.yofish.kitmodule.wedget.refresh.pull2Refresh.PtrShimmerFrameLayout;
import com.yofish.netmodule.NetClient;
import com.yofish.netmodule.callback.BaseCallBack;

/**
 * Created by wyn on 2018/12/12.
 */
public class PullRefreshRecyclerViewActivity extends BaseActivity {

    private CommonAdapter<BankInfoBean> mAdapter;

    private PtrShimmerFrameLayout ptrMetialFrameLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initToolbar("下拉刷新头部");
    }

    @Override
    protected int setLayoutId(Bundle savedInstanceState) {
        return R.layout.pullrorefresh_activity;
    }

    @Override
    protected void initViews() {
        ptrMetialFrameLayout = findViewById(R.id.ptr);

        /** 设置下拉刷新 */
        ptrMetialFrameLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                ptrMetialFrameLayout.autoRefresh(true);
            }
        },100);
        /**设置下拉刷新响应*/
        ptrMetialFrameLayout.setPtrHandler(new PtrHandler() {

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
               loadData(false);
            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
            }
        });

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.pull_rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new RecycleViewItemDiver(this, LinearLayoutManager.HORIZONTAL));
        recyclerView.setAdapter(mAdapter = new CommonAdapter<BankInfoBean>(this, R.layout.banklist_item) {
            @Override
            protected void convert(com.yofish.kitmodule.baseAdapter.recyclerview.base.ViewHolder holder, BankInfoBean bankInfoBean, int position) {
                holder.setImageResource(R.id.bank_logo, R.mipmap.ic_launcher_round);
                holder.setText(R.id.bank_name, bankInfoBean.getBankName());
            }
        });


    }


    private void loadData(final boolean isLoadMore) {
        NetClient.newBuilder(this).baseUrl("http://credit.youyuwo.com/notcontrol/manage/")
                .method("queryCardImportBankList.go").callBack(new BaseCallBack<BankData>() {
            @Override
            public void onSuccess(BankData bankData) {
                //加载完成
                ptrMetialFrameLayout.refreshComplete();

                if (isLoadMore) {
                    mAdapter.addData(bankData.getBankList());
                } else {
                    mAdapter.resetData(bankData.getBankList());
                }
              //  container.getLoadMoreFooterUtils().updatePages(1, 2);
            }

            @Override
            public void onFailed(String code, String errors) {

            }

            @Override
            public void onComplete() {
                super.onComplete();
              //  container.loadComplete();
            }
        }).sendPost();
    }
}
