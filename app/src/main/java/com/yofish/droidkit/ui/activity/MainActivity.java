package com.yofish.droidkit.ui.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yofish.kitmodule.base_component.BaseActivity;
import com.yofish.kitmodule.router.CommonRouter;
import com.yofish.kitmodule.util.AppSharedPrefrences;
import com.yofish.kitmodule.wedget.NoRecyclerFragmentTabHost;
import com.yofish.droidkit.R;
import com.yofish.droidkit.repository.bean.MainConfigData;
import com.yofish.droidkit.utils.TabConfig;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    public static final String FRAGMENT_KEY = "main_fragment_args_key";
    private NoRecyclerFragmentTabHost mTabHost;
    /**
     * 按两次退出
     */
    private long mExitStartTime = 0;
    /**
     * 两秒内连续按两次退出APP.
     */
    private static final int EXIT_WAIT_TIME = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initToolbar("首页", false);
        initTabs();
        dealAction();
    }

    @Override
    protected void initConfig() {
        super.initConfig();
        setFullScreenFlag(false);
    }

    @Override
    protected int setLayoutId(Bundle savedInstanceState) {
        return R.layout.activity_home;
    }

    @Override
    protected void initViews() {
        AppSharedPrefrences.getInstance().put("test", "我是App层存的数据");
    }

    @Override
    protected void loadData() {

    }

    private void initTabs(){
        mTabHost = (NoRecyclerFragmentTabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.main_tabcontent);
        addPage("main_home");
        addPage("main_fx");
        addPage("main_user");
    }

    private void addPage(String key) {
        MainConfigData data = TabConfig.getMainConfigDataByKey(key);
        Bundle args = null;
        if (!TextUtils.isEmpty(data.getParams())) {
            args = new Bundle();
            args.putString(FRAGMENT_KEY, data.getParams());
        }
        if (null != data) {
            View indicator = LayoutInflater.from(this).inflate(R.layout.tab_indicator, null);
            ((TextView) indicator.findViewById(R.id.tab_indicator_title)).setText(data.getTabName());
            ((ImageView) indicator.findViewById(R.id.tab_indicator_img))
                    .setImageResource(data.getTabSelector());
            mTabHost.addTab(mTabHost.newTabSpec(key).setIndicator(indicator), data.getFg(), args);
        }
        mTabHost.getTabWidget().setDividerDrawable(null);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mTabHost.getCurrentTab() != 0) {
                mTabHost.setCurrentTab(0);
                return true;
            }
            if (System.currentTimeMillis() - mExitStartTime > EXIT_WAIT_TIME) {
                mExitStartTime = System.currentTimeMillis();
                showToast("再按一次离开" + getResources().getString(R.string.app_name));
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }

    private void dealAction(){
        String actionUrl = getIntent().getStringExtra(IntroActivity.PUSH_KEY);
        if (!TextUtils.isEmpty(actionUrl)) {
            CommonRouter.router2PagerByUrl(this, actionUrl);
        }
    }
}
