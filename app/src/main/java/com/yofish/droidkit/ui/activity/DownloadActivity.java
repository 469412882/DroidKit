package com.yofish.droidkit.ui.activity;

import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.yofish.kitmodule.baseAdapter.abslistview.CommonAdapter;
import com.yofish.kitmodule.baseAdapter.abslistview.ViewHolder;
import com.yofish.kitmodule.base_component.BaseActivity;
import com.yofish.kitmodule.util.Utility;
import com.yofish.droidkit.R;
import com.yofish.netmodule.NetClient;
import com.yofish.netmodule.callback.BaseCallBack;
import com.yofish.netmodule.download.DownloadInfo;
import com.yofish.netmodule.retrofit.download.HttpDownloadManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 网络测试页
 *
 * Created by hch on 2017/6/28.
 */

public class DownloadActivity extends BaseActivity implements View.OnClickListener {

    CommonAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initToolbar("断点续传下载");
    }

    @Override
    protected int setLayoutId(Bundle savedInstanceState) {
        return R.layout.activity_download;
    }

    @Override
    protected void initViews() {
        ListView listView = (ListView) findViewById(R.id.listview);
        listView.setAdapter(adapter = new CommonAdapter<DownloadInfo>(this, R.layout.download_list_item, null) {
            @Override
            protected void convert(final ViewHolder viewHolder, final DownloadInfo item, int position) {
                final ProgressBar bar = viewHolder.getView(R.id.download_progress);
                bar.setProgress(item.getPercent());
                final TextView state = viewHolder.getView(R.id.state);
                final TextView speed = viewHolder.getView(R.id.speed);
                final TextView pridictSecond = viewHolder.getView(R.id.pridictFinishSecond);
                final TextView total = viewHolder.getView(R.id.total);
                switch (item.getState()) {
                    case DOWN:
                        state.setText("下载中");
                        break;
                    case START:
                        break;
                    case STOP:
                        state.setText("停止");
                        break;
                    case FINISH:
                        state.setText("下载完成");
                        break;
                    case PAUSE:
                        state.setText("暂停");
                        break;
                    case ERROR:
                        state.setText("下载失败");
                        break;
                }
                viewHolder.getView(R.id.download).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        downloadNew(item, bar, state, speed, pridictSecond, total);
                    }
                });
                viewHolder.getView(R.id.pause).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        HttpDownloadManager.getInstance().pause(item.getUrl());
                    }
                });
                viewHolder.getView(R.id.redownload).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        HttpDownloadManager.getInstance().reset(item);
                        downloadNew(item, bar, state, speed, pridictSecond, total);
                        bar.setProgress(0);
                    }
                });
            }
        });
        adapter.resetData(initInfo());
    }

    @Override
    protected void loadData() {

    }

    @Override
    protected void onPause() {
        super.onPause();
        HttpDownloadManager.getInstance().pauseLoading();
    }

    private List<DownloadInfo> initInfo() {
        List<DownloadInfo> list = new ArrayList<>();
        DownloadInfo info1 = HttpDownloadManager.getInstance().getDownloadInfo(
                "http://down.youyuwo.com/hsk/Huishuaka_V3.5.1.apk",
                new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                        "huishuaka.apk").getAbsolutePath());
        DownloadInfo info2 = HttpDownloadManager.getInstance().getDownloadInfo(
                "http://apk.hiapk.com/appdown/com.imohoo.favorablecard",
                new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                        "favorablecard.apk").getAbsolutePath());
        DownloadInfo info3 = HttpDownloadManager.getInstance().getDownloadInfo(
                "http://apk.hiapk.com/appdown/com.qytx.zfcq.baidu",
                new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "zfcq.apk")
                        .getAbsolutePath());
        list.add(info1);
        list.add(info2);
        list.add(info3);
        return list;
    }

    private void downloadNew(DownloadInfo info, final ProgressBar bar, final TextView state, final TextView speedTxt,
            final TextView pridictTxt, final TextView totalTxt) {
        NetClient.newBuilder(getApplicationContext()).downloadInfo(info).callBack(new BaseCallBack<DownloadInfo>() {
            @Override
            public void onSuccess(DownloadInfo info) {

            }

            @Override
            public void onFailed(String code, String errors) {
                state.setText("下载失败，" + errors);
            }

            @Override
            public void onProgress(int percent, long total) {
                bar.setProgress(percent);
                state.setText("下载中：" + percent + "%");
                totalTxt.setText("共" + Utility.getFormatBytes(total));
            }

            @Override
            public void onSpeed(long speed) {
                super.onSpeed(speed);
                speedTxt.setText("下载速度：" + Utility.getFormatBytes(speed) + "/s");
            }

            @Override
            public void onPridictFinish(long pridictFinishSecond) {
                super.onPridictFinish(pridictFinishSecond);
                pridictTxt.setText("剩余时间：" + pridictFinishSecond + "秒");
            }

            @Override
            public void onPause() {
                super.onPause();
                state.setText("暂停");
            }

            @Override
            public void onComplete() {
                super.onComplete();
                state.setText("下载完成");
            }

            @Override
            public void onStart() {
                super.onStart();
                state.setText("开始下载");
            }
        }).downloadFile();
    }

    @Override
    public void onClick(View v) {
    }
}
