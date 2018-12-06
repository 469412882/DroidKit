package com.yofish.droidkit.fragment;

import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.yofish.kitmodule.base_component.BaseFragment;
import com.yofish.kitmodule.wedget.viewpager.CommonViewPagerAdapter;
import com.yofish.kitmodule.wedget.viewpager.DotsView;
import com.yofish.droidkit.R;

import java.util.Arrays;
import java.util.List;

/**
 * 引导页
 * <p>
 * Created by hch on 2018/9/29.
 */

public class IntroFragment extends BaseFragment {

    private ViewPager viewPager;
    private CommonViewPagerAdapter mAdapter;
    private DotsView dotsView;
    Integer[] mImgRes = { R.drawable.intro_p1, R.drawable.intro_p2, R.drawable.intro_p3};
    private List<Integer> introImgs = Arrays.asList(mImgRes);
    /**
     * 立即体验 按键点击事件*
     */
    private ShowBtnCallBack mListener;

    /**
     * 设置立即体验按键回调
     *
     * @param listener
     *            listener
     */
    public void setShowBtnCallBack(ShowBtnCallBack listener) {
        this.mListener = listener;
    }

    @Override
    protected int setLayoutId() {
        return R.layout.fragment_intro;
    }

    @Override
    protected void initViews(ViewGroup rootView) {
        viewPager = (ViewPager) rootView.findViewById(R.id.intro_vp);
        dotsView = (DotsView) rootView.findViewById(R.id.dots_view);
        viewPager.setAdapter(mAdapter = new CommonViewPagerAdapter<Integer>(getContext(), R.layout.intro_vp_item, introImgs) {
            @Override
            protected void convert(View view, Integer itemData) {
                ((ImageView) view.findViewById(R.id.intro_img)).setImageResource(itemData);
            }
        });
        dotsView.setViewPager(viewPager);
        dotsView.setSize(introImgs.size());
        dotsView.setPosition(0);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if ((introImgs.size() - 1) == position) {
                    if (mListener != null) {
                        mListener.show();
                    }
                    dotsView.setVisibility(View.GONE);
                } else {
                    if (mListener != null) {
                        mListener.hide();
                    }
                    dotsView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public interface ShowBtnCallBack {
        void show();

        void hide();
    }
}
