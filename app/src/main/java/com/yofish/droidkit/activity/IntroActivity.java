package com.yofish.droidkit.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.gifdecoder.GifDecoder;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.yofish.kitmodule.base_component.BaseActivity;
import com.yofish.kitmodule.util.AppSharedPrefrences;
import com.yofish.kitmodule.util.LogUtils;
import com.yofish.kitmodule.util.Utility;
import com.yofish.kitmodule.wedget.CustomProgressBar;
import com.yofish.droidkit.R;
import com.yofish.droidkit.bean.BannerRouterBean;
import com.yofish.droidkit.bean.StartInfoBean;
import com.yofish.droidkit.fragment.IntroFragment;
import com.yofish.droidkit.utils.AppSwitch;
import com.nineoldandroids.animation.ValueAnimator;
import com.yofish.netmodule.NetClient;
import com.yofish.netmodule.callback.BaseCallBack;

import java.util.HashMap;

/**
 * 启动页
 * 1、首先加载启动静态图
 * 2、检查是否需要显示引导页 （优先级：GIF > 静态引导， 注意：静态引导需要修改INTRO_HASVIEWED_KEY 以改变引导页版本，GIF引导需要命名为def_intro.gif）
 * 3、检查是否需要显示广告页 （优先级：GIF > 静态广告）
 *
 * <p>
 * Created by hch on 2018/9/28.
 */

public class IntroActivity extends BaseActivity implements View.OnClickListener {

    /**
     * PUSH_KEY
     */
    public static final String PUSH_KEY = "push_key";
    /**
     * 第一个启动页的启动时间
     */
    private final int DEF_INTRO_TIME = 3 * 1000;
    /**
     * 第二个启动页的默认持续时间
     */
    private final int SECOND_INTRO_DEF_TIME = 3 * 1000;

    /**
     * 第二个启动页动图的默认持续时间
     */
    private final int SECOND_INTRO_GIF_TIME = 3 * 1000;
    /**
     * 引导页动画
     */
    private GifDrawable introGifDrawable;
    /**
     * 动图drawable
     */
    private GifDrawable bannderGifDrawable;
    /**
     * 动图drawable
     */
    private Drawable glideDrawable;
    /**
     * 开屏广告信息
     */
    private BannerRouterBean mBannerRouterBean;
    /**
     * 是否有引导页
     */
    private boolean hasIntroPage = false;
    /**
     * 用来判断是否已经查看过引导页 引导页版本每次发布版本都需要变动
     */
    private final String INTRO_HASVIEWED_KEY = "INTRO_HASVIEWED_100";
    /**
     * 启动页view
     */
    private ImageView introPageView;
    /**
     * 跳过按钮
     */
    private View skipView;
    /**
     * 跳过按钮
     */
    private TextView mSkipBtn;
    /**
     * 广告下边的view
     */
    private View bannerBottom;
    /**
     * 广告下边的view
     */
    private CustomProgressBar mProgressBar;
    /**
     * 立即体验按钮
     */
    private View experienceNowView;
    /**
     * 底部按钮
     */
    private View bottomBtn;

    @Override
    protected int setLayoutId() {
        return R.layout.activity_intro;
    }

    @Override
    protected void initViews() {
        introPageView = (ImageView) findViewById(R.id.intro_image);
        bannerBottom = findViewById(R.id.bannerbottom);
        mProgressBar = (CustomProgressBar) findViewById(R.id.progress_bar);
        skipView = findViewById(R.id.intro_skip);
        mSkipBtn = (TextView) findViewById(R.id.skip_btn);

        experienceNowView = findViewById(R.id.intro_experience_now);
        experienceNowView.setOnClickListener(this);
        bottomBtn = findViewById(R.id.intro_btn);
        findViewById(R.id.login_btn).setOnClickListener(this);
        findViewById(R.id.register_btn).setOnClickListener(this);
        findViewById(R.id.skip_btn).setOnClickListener(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doNextIntro();
            }
        }, DEF_INTRO_TIME);
        loadStartInfo();
        loadDefIntro();
        loadBannerInfo();
    }

    /**
     * 默认启动页启动后执行动作
     */
    private void doNextIntro() {
        LogUtils.i("doNextIntro");
        /**先看是否有引导页需要显示*/
        if (hasIntroPage) {
            if (null != introGifDrawable) {
                int duration = getGifDuration(introGifDrawable);
                introPageView.setImageDrawable(introGifDrawable);
                introGifDrawable.setLoopCount(1);
                introGifDrawable.start();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        showBtn();
                    }
                }, duration);
            } else {
                if (isFinishing()) {
                    return;
                } else {
                    try {
                        introPageView.setVisibility(View.GONE);
                        IntroFragment introFragment = new IntroFragment();
                        introFragment.setShowBtnCallBack(new IntroFragment.ShowBtnCallBack() {
                            @Override
                            public void show() {
                                showBtn();
                            }

                            @Override
                            public void hide() {
                                hideBtn();
                            }
                        });
                        getSupportFragmentManager().beginTransaction().add(R.id.intro_main, introFragment)
                                .commitAllowingStateLoss();
                    } catch (Exception e) {

                    }
                }
            }
            return;
        }
        /**没有引导页再显示广告图片*/
        if (null != glideDrawable || null != bannderGifDrawable) {
            /**广告可以点击跳转*/
            introPageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String target = "";
                    if (mBannerRouterBean != null) {
                        target = mBannerRouterBean.getActionUrl();
                    }
                    gotoMain(target);
                }
            });
            /**广告可以跳过*/
            skipView.setVisibility(View.VISIBLE);
            bannerBottom.setVisibility(View.VISIBLE);
            LogUtils.i("set glid");
            int delayTime;
            if (null != bannderGifDrawable) {
                int duration = getGifDuration(bannderGifDrawable);
                delayTime = Math.max(duration, SECOND_INTRO_GIF_TIME);
                introPageView.setImageDrawable(bannderGifDrawable);
                LogUtils.i("invalidateDrawable");
                bannderGifDrawable.start();
            } else {
                delayTime = SECOND_INTRO_DEF_TIME;
                introPageView.setImageDrawable(glideDrawable);
            }
            ValueAnimator animator = ValueAnimator.ofInt(100);
            animator.setDuration(delayTime);
            animator.setInterpolator(new LinearInterpolator());
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    mProgressBar.setProgress((Integer) animation.getAnimatedValue());
                }
            });
            animator.start();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    gotoMain("");
                }
            }, delayTime);
        } else {
            gotoMain("");
        }
    }

    private void showBtn() {
        //显示立即体验按钮
        experienceNowView.setAnimation(AnimationUtils.loadAnimation(
                IntroActivity.this, R.anim.out_from_bottom_anim));
        experienceNowView.setVisibility(View.VISIBLE);
    }

    private void hideBtn() {
        experienceNowView.setVisibility(View.GONE);
    }

    /**
     * 跳转到首页
     */
    private void gotoMain(String target) {
        if (isFinishing()) {
            return;
        }
        Intent intent = new Intent(IntroActivity.this, MainActivity.class);
        intent.putExtra(PUSH_KEY, target);
        startActivity(intent);
        finish();
    }

    /**
     * 加载默认引导页
     */
    private void loadDefIntro() {
        if (TextUtils.isEmpty(AppSharedPrefrences.getInstance()
                .get(INTRO_HASVIEWED_KEY, ""))) {
            hasIntroPage = true;
            LogUtils.i("has intro");
            /**只要这个版本没有看过引导页 显示引导页*/
            int introGifId = 0;
            try {
                introGifId = Utility.getDrawableId(this, "def_intro");
            } catch (Exception e) {

            }
            if (introGifId > 0) {
                Glide.with(this)
                        .asGif()
                        .load(introGifId)
                        .into(new SimpleTarget<GifDrawable>() {
                            @Override
                            public void onResourceReady(GifDrawable resource, Transition<? super GifDrawable> transition) {
                                LogUtils.i("onResourceReady");
                                introGifDrawable = resource;
                            }
                        });
            } else {
                introGifDrawable = null;
            }
        } else {
            hasIntroPage = false;
        }
    }

    /**
     * 取启动信息
     */
    private void loadStartInfo() {
        NetClient.newBuilder(this).baseUrl("http://creditv2.youyuwo.com/notcontrol/enjoy/v2/")
                .method("start.go").callBack(new BaseCallBack<StartInfoBean>() {
            @Override
            public void onSuccess(StartInfoBean startInfoBean) {
                if (null != startInfoBean) {
                    AppSwitch.setAppMajiaSwitch(startInfoBean.getMajiaSwitch());
                }
            }

            @Override
            public void onFailed(String errors) {

            }
        }).sendPost();
    }

    /**
     * 取开屏广告信息
     */
    private void loadBannerInfo() {
        HashMap<String, String> params = new HashMap<>();
        params.put("iterativeVersionCode", "20");
        NetClient.newBuilder(this).baseUrl("http://creditv2.youyuwo.com/notcontrol/enjoy/v2/")
                .method("start.go").params(params).callBack(new BaseCallBack<BannerRouterBean>() {
            @Override
            public void onSuccess(BannerRouterBean bannerRouterBean) {
                JsonObject jsonObject = Utility.readAssetsJsonFile(IntroActivity.this, "openScreen.json");
                bannerRouterBean = new Gson().fromJson(jsonObject.get("data"), BannerRouterBean.class);
                if (!isFinishing()) {
                    loadRemoteIntropage(bannerRouterBean);
                }
                mBannerRouterBean = bannerRouterBean;
            }

            @Override
            public void onFailed(String errors) {

            }
        }).sendPost();
    }

    /**
     * 显示下发启动页
     */
    private void loadRemoteIntropage(BannerRouterBean bannerRouterBean) {
        if (bannerRouterBean == null) {
            return;
        }
        String url = bannerRouterBean.getPicUrl();
        /**测试begin*/
//        url = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1538796744&di=56c6dbe391359582cb2d43ae2961546c&imgtype=jpg&er=1&src=http%3A%2F%2Fimg.zcool.cn%2Fcommunity%2F01e9b459c884e3a8012053f8d1eaaa.png%401280w_1l_2o_100sh.png";
//        url = "http://t.huishuaka.com/imgs/youyuloan/testintro.gif";
        /**测试begin*/
        RequestOptions options = new RequestOptions();
        options.diskCacheStrategy(DiskCacheStrategy.RESOURCE);
        if (!TextUtils.isEmpty(url)) {
            if (url.endsWith(".gif")) {
                LogUtils.i("load gif");
                Glide.with(this)
                        .asGif()
                        .apply(options)
                        .load(url)
                        .into(new SimpleTarget<GifDrawable>() {
                            @Override
                            public void onResourceReady(GifDrawable resource, Transition<? super GifDrawable> transition) {
                                LogUtils.i("onResourceReady");
                                bannderGifDrawable = resource;
                            }
                        });
            } else {
                Glide.with(this)
                        .asDrawable()
                        .load(url)
                        .apply(options)
                        .into(new SimpleTarget<Drawable>() {
                            @Override
                            public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
                                glideDrawable = resource;
                            }
                        });
            }

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.intro_experience_now:
                /**进入首页*/
                gotoMain("");
                break;
            case R.id.login_btn:
                //login
                break;
            case R.id.register_btn:
                //register
                break;
            case R.id.skip_btn:
                gotoMain("");
                break;
        }
        /**标记为已看*/
        AppSharedPrefrences.getInstance().put(INTRO_HASVIEWED_KEY, "1");
    }

    private int getGifDuration(GifDrawable gifDrawable){
        int duration = 0;
        try {
            // 计算动画时长
            //GifDecoder decoder = gifDrawable.getDecoder();//4.0开始没有这个方法了
            Drawable.ConstantState state = gifDrawable.getConstantState();
            if(state!=null){
                //不能混淆GifFrameLoader和GifState类
                Object gifFrameLoader = Utility.getValue(state,"frameLoader");
                if(gifFrameLoader!=null){
                    Object decoder = Utility.getValue(gifFrameLoader,"gifDecoder");
                    if(decoder!=null && decoder instanceof GifDecoder){
                        for (int i = 0; i < gifDrawable.getFrameCount(); i++) {
                            duration += ((GifDecoder) decoder).getDelay(i);
                        }
                    }
                }
                LogUtils.e("gif播放动画时长duration:"+duration);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return duration;
    }


}
