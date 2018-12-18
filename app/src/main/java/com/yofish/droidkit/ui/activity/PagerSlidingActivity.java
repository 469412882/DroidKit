package com.yofish.droidkit.ui.activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.yofish.droidkit.R;
import com.yofish.kitmodule.base_component.BaseActivity;
import com.yofish.kitmodule.wedget.slidingtab.PagerSlidingIndicator;
import com.yofish.kitmodule.wedget.slidingtab.ViewPagerHelper;
import com.yofish.kitmodule.wedget.slidingtab.indicators.LinePagerIndicator;
import com.yofish.kitmodule.wedget.slidingtab.navigator.ColorTransitionPagerTitleView;
import com.yofish.kitmodule.wedget.slidingtab.navigator.CommonNavigator;
import com.yofish.kitmodule.wedget.slidingtab.navigator.CommonNavigatorAdapter;
import com.yofish.kitmodule.wedget.slidingtab.navigator.IPagerIndicator;
import com.yofish.kitmodule.wedget.slidingtab.navigator.IPagerTitleView;
import com.yofish.kitmodule.wedget.slidingtab.navigator.SimplePagerTitleView;
import com.yofish.kitmodule.wedget.slidingtab.viewpager.ExamplePagerAdapter;

import java.util.Arrays;
import java.util.List;

/**
 * Created by wyn on 2018/12/17.
 */
public class PagerSlidingActivity extends BaseActivity {

    private static final String[] CHANNELS = new String[]{ "DUT",  "LOLLIPOP", "M", "NOUGAT"};
    private List<String> mDataList = Arrays.asList(CHANNELS);
    private ExamplePagerAdapter mExamplePagerAdapter = new ExamplePagerAdapter(mDataList);
    private ViewPager mViewPager;


    @Override
    protected int setLayoutId(Bundle savedInstanceState) {
        return R.layout.bannerview_activity;
    }

    @Override
    protected void initViews() {
        initToolbar("PagerSlidingTab");

        initIndicator();

    }

    private void initIndicator() {

        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        mViewPager.setAdapter(mExamplePagerAdapter);

        PagerSlidingIndicator magicIndicator =  findViewById(R.id.indicator);
        magicIndicator.setBackgroundColor(Color.WHITE);
        CommonNavigator commonNavigator = new CommonNavigator(this);
        commonNavigator.setAdjustMode(true);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {

            @Override
            public int getCount() {
                return mDataList.size();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                SimplePagerTitleView simplePagerTitleView = new ColorTransitionPagerTitleView(context);
                simplePagerTitleView.setNormalColor(Color.GRAY);
                simplePagerTitleView.setSelectedColor(Color.parseColor("#00afff"));
                simplePagerTitleView.setText(mDataList.get(index));
                simplePagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mViewPager.setCurrentItem(index);
                    }
                });
                return simplePagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator linePagerIndicator = new LinePagerIndicator(context);
                linePagerIndicator.setMode(LinePagerIndicator.MODE_WRAP_CONTENT);
                linePagerIndicator.setColors(Color.parseColor("#00afff"));
                return linePagerIndicator;
            }
        });
        magicIndicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(magicIndicator, mViewPager);
    }
}
