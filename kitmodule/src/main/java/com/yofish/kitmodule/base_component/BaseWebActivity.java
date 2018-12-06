package com.yofish.kitmodule.base_component;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.yofish.kitmodule.R;
import com.yofish.kitmodule.wedget.webview.BaseJsInterface;
import com.yofish.kitmodule.wedget.webview.X5WebView;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebView;

/**
 * web页面基类
 *
 * Created by hch on 2017/8/4.
 */
@Route(path = "/droidkit/webview")
public class BaseWebActivity extends BaseActivity {
    public static final String WEB_TITLE = "webTitle";
    public static final String WEB_URL = "webUrl";
    public static final String SHOW_CLOSE = "showClose";
    private X5WebView mWebview;
    private ProgressBar mPbar;
    private String mTitle;
    private String mUrl;
    private boolean mShowClose;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initToolbar(mTitle);
        init();
        mWebview.loadUrl(mUrl);
    }

    /**
     * 设置webView
     */
    private void init() {
        try {
            if (Integer.parseInt(android.os.Build.VERSION.SDK) >= 11) {
                getWindow().setFlags(android.view.WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                        android.view.WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        mWebview.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView webView, int i) {
                super.onProgressChanged(webView, i);
                mPbar.setProgress(i);
                if (mPbar.getMax() == i) {
                    mPbar.setVisibility(View.GONE);
                } else {
                    if (mPbar.getVisibility() == View.GONE) {
                        mPbar.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
        mWebview.addJavascriptInterface(new BaseJsInterface(this), "android");
        ImageButton close = (ImageButton) findViewById(R.id.close);
        if (mShowClose) {
            getToolbar().setTitle("");
            TextView title = (TextView) findViewById(R.id.title);
            title.setText(mTitle);
            close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        } else {
            close.setVisibility(View.GONE);
        }
    }

    @Override
    protected int setLayoutId() {
        return R.layout.base_web_activity;
    }

    @Override
    protected void receiveData(Intent intent) {
        super.receiveData(intent);
        mTitle = intent.getStringExtra(WEB_TITLE);
        mUrl = intent.getStringExtra(WEB_URL);
        mShowClose = intent.getBooleanExtra(SHOW_CLOSE, false);
    }

    @Override
    protected void initViews() {
        mWebview = (X5WebView) findViewById(R.id.base_webview);
        mPbar = (ProgressBar) findViewById(R.id.progressBar);
    }

    /**
     * 物理返回键的控制
     * 
     * @param keyCode
     *            keyCode
     * @param event
     *            event
     * @return boolean
     */
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && this.mWebview.canGoBack()) {
            this.mWebview.goBack();
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    /**
     * menu的点击
     * 
     * @param item
     *            item
     * @return boolean
     */
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (this.mWebview.canGoBack()) {
                    this.mWebview.goBack();
                    return true;
                }
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
