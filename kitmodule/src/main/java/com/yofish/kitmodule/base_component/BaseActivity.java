package com.yofish.kitmodule.base_component;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.yofish.kitmodule.R;
import com.yofish.kitmodule.wedget.CommonToolbar;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import static com.yofish.kitmodule.util.Utility.isDarkColor;

/**
 * baseActivity， 继承自RxAppCompatActivity， 可以使用RXJava捕获activity的生命周期，
 * 比如网络请求在activity销毁的时候cancel掉请求
 *
 * Created by hch on 2017/8/2.
 */

public abstract class BaseActivity extends RxAppCompatActivity {
    private Toolbar mToolbar;
    private boolean fullScreenFlag = true;
    private boolean mIsFullScreen = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initConfig();
        fullScreen();
        setContentView(setLayoutId());
        receiveData(getIntent());
        initViews();
    }

    public void setFullScreenFlag(boolean fullScreenFlag) {
        this.fullScreenFlag = fullScreenFlag;
    }

    /**
     * 设置页面layoutId
     * 
     * @return int
     */
    protected abstract int setLayoutId();

    /**
     * 初始化view
     */
    protected abstract void initViews();

    /**
     * 初始化view
     */
    protected void receiveData(Intent intent) {

    }

    /**
     * 设置
     */
    protected void initConfig(){

    }

    /**
     * 设置全屏主题
     */
    public void fullScreen() {
        if (!fullScreenFlag) {
            return;
        }
        try {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                View decorView = this.getWindow().getDecorView();
                int flag = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
                if (isDarkColor(getResources().getColor(R.color.colorToolbarBg))) {
                    flag |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
                }
                decorView.setSystemUiVisibility(flag);
                this.getWindow().setStatusBarColor(Color.TRANSPARENT);
                mIsFullScreen = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 设置状态栏的文字颜色
     *
     * @param darkcolor true : 设置为白色文字， false ： 设置为灰色文字
     */
    public void setStatusBarHintDarkGEM(boolean darkcolor) {
        if (!fullScreenFlag) {
            return;
        }
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            View decorView = this.getWindow().getDecorView();
            int flag = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            if(darkcolor) {
                flag |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            }

            decorView.setSystemUiVisibility(flag);
            mIsFullScreen = true;
        }

    }

    /**
     * 点击菜单
     * 
     * @param item
     *            item
     * @return boolean
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 设置toolbar，顶部默认有返回键
     * 
     * @param title
     *            标题
     */
    public void initToolbar(String title) {
        this.initToolbar(title, true);
    }

    /**
     * 设置toolbar，可设置顶部返回键
     * 
     * @param title
     *            标题
     * @param showIcon
     *            是否显示返回键
     */
    public void initToolbar(String title, boolean showIcon) {
        this.setToolbar(getWindow().getDecorView());
        if (mToolbar != null) {
            this.mToolbar.setTitle(title);
            this.setSupportActionBar(this.mToolbar);
            this.getSupportActionBar().setDisplayHomeAsUpEnabled(showIcon);
            if (mToolbar instanceof CommonToolbar) {
                if (!mIsFullScreen) {
                    ((CommonToolbar)mToolbar).notFullScreen();
                }
            }
        }
    }

    /**
     * mToolbar的赋值，遍历所有子view，寻找toolbar
     * 
     * @param view
     */
    private void setToolbar(View view) {
        try {
            if (view instanceof ViewGroup) {
                if (view instanceof Toolbar) {
                    this.mToolbar = (Toolbar) view;
                } else if (null == this.mToolbar) {
                    for (int i = 0; i < ((ViewGroup) view).getChildCount(); ++i) {
                        this.setToolbar(((ViewGroup) view).getChildAt(i));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public Toolbar getToolbar() {
        return mToolbar;
    }

    /**
     * toast提示
     * 
     * @param msg
     *            msg
     */
    public void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

}
