package com.yofish.droidkit.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.VideoView;

import com.danikula.videocache.HttpProxyCacheServer;
import com.yofish.kitmodule.base_component.BaseActivity;
import com.yofish.droidkit.App;
import com.yofish.droidkit.R;

/**
 * 视频页面
 * <p>
 * Created by hch on 2018/10/16.
 */

public class VideoActivity extends BaseActivity {

    private VideoView mVideoView;

    private final String VIDEO_URL = "http://66.42.73.238:8080/examples/test/video_demo.mp4";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initToolbar("视频播放");
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_video;
    }

    @Override
    protected void initViews() {
        mVideoView = (VideoView) findViewById(R.id.video_view);
        HttpProxyCacheServer proxy = App.getProxy(this);
        String proxyUrl = proxy.getProxyUrl(VIDEO_URL);
        mVideoView.setVideoPath(proxyUrl);
        mVideoView.start();
    }
}
