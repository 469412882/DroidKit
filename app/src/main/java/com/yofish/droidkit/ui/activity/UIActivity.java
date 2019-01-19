package com.yofish.droidkit.ui.activity;

import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckedTextView;

import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureMimeType;
import com.yofish.imagemodule.ImgLoader;
import com.yofish.kitmodule.baseAdapter.abslistview.CommonAdapter;
import com.yofish.kitmodule.baseAdapter.abslistview.ViewHolder;
import com.yofish.kitmodule.base_component.BaseActivity;
import com.yofish.kitmodule.util.Utility;
import com.yofish.kitmodule.wedget.BottomSheetDialog;
import com.yofish.droidkit.R;
import com.yofish.kitmodule.wedget.ExpandableTextView;
import com.yofish.kitmodule.wedget.ImageViewWithBorder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UIActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initToolbar("常用UI");
    }

    @Override
    protected int setLayoutId(Bundle savedInstanceState) {
        return R.layout.activity_ui;
    }

    @Override
    protected void initViews() {
        findViewById(R.id.dialog).setOnClickListener(this);
        findViewById(R.id.dialog2).setOnClickListener(this);
        findViewById(R.id.dialog3).setOnClickListener(this);
        findViewById(R.id.bottom_dialog).setOnClickListener(this);
        findViewById(R.id.listview).setOnClickListener(this);
        findViewById(R.id.recyclerview).setOnClickListener(this);
        findViewById(R.id.pagersliding).setOnClickListener(this);
        findViewById(R.id.login_btn).setOnClickListener(this);
        findViewById(R.id.elevation_view).setOnTouchListener(new View.OnTouchListener() {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        ObjectAnimator.ofFloat(v, "translationZ", v.getElevation(), Utility.dpToPx(7, getResources())).setDuration(100).start();
                        break;
                    case MotionEvent.ACTION_UP:
                        ObjectAnimator.ofFloat(v, "translationZ", v.getElevation(), Utility.dpToPx(1, getResources())).setDuration(100).start();
                        break;
                }
                return false;
            }
        });
        final CheckedTextView checkedTextView = (CheckedTextView) findViewById(R.id.checkedTextView);
        checkedTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkedTextView.setChecked(!checkedTextView.isChecked());
            }
        });

        ImageViewWithBorder img1 = findViewById(R.id.img1);
        ImageViewWithBorder img2 = findViewById(R.id.img2);
        ImageViewWithBorder img3 = findViewById(R.id.img3);
        ImgLoader.getInstance().load("http://cdn.duitang.com/uploads/item/201408/28/20140828142218_PS4fi.thumb.700_0.png",img1);
        ImgLoader.getInstance().load("http://cdn.duitang.com/uploads/item/201408/28/20140828142218_PS4fi.thumb.700_0.png",img2);
        ImgLoader.getInstance().load("http://cdn.duitang.com/uploads/item/201408/28/20140828142218_PS4fi.thumb.700_0.png",img3);

        ExpandableTextView expandableTextView = findViewById(R.id.expand_text_view);
        expandableTextView.setText("限感恩节活动页面商品可用；限时购、特价等特惠商品，处于新品期的商品及详情页标注不可用券的商品除外限感恩节活动页面商品可用；限时购、特价等特惠商品，处于新品期的商品及详情页标注不可用券的商品除外");
    }

    @Override
    protected void loadData() {

    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.dialog:
                new AlertDialog.Builder(this).setTitle("title").setMessage("material design 风格的dialog")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                showToast("确定");
                            }
                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                showToast("取消");
                            }
                        }).create().show();
                break;
            case R.id.dialog2:
                new AlertDialog.Builder(this)
                        .setTitle("title")
                        .setMultiChoiceItems(new String[] { "选项1", "选项2" }, new boolean[] { true, false },
                                new DialogInterface.OnMultiChoiceClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                                    }

                                }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                showToast("确定");
                            }
                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                showToast("取消");
                            }
                        }).create().show();
                break;
            case R.id.dialog3:
                new AlertDialog.Builder(this)
                        .setTitle("title")
                        .setAdapter(
                                new CommonAdapter<String>(UIActivity.this, R.layout.list_dialog_item,
                                        Arrays.asList(new String[] { "one", "two", "three", "four", "five", })) {
                                    @Override
                                    protected void convert(ViewHolder viewHolder, String item, int position) {
                                        viewHolder.setText(R.id.title, item);
                                    }
                                }, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                showToast("确定");
                            }
                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                showToast("取消");
                            }
                        }).create().show();
                break;
            case R.id.bottom_dialog:
                final String[] datas = new String[] { "one", "two", "three", "four", "five" };
                final List<BottomSheetDialog.BottomSheetBean> list = new ArrayList<>();
                list.add(new BottomSheetDialog.BottomSheetBean(R.mipmap.ic_launcher_round, "one"));
                list.add(new BottomSheetDialog.BottomSheetBean(R.mipmap.ic_launcher_round, "two"));
                list.add(new BottomSheetDialog.BottomSheetBean(R.mipmap.ic_launcher_round, "three"));
                list.add(new BottomSheetDialog.BottomSheetBean(R.mipmap.ic_launcher_round, "four"));
                list.add(new BottomSheetDialog.BottomSheetBean(R.mipmap.ic_launcher_round, "five"));
                BottomSheetDialog.newBuilder(this).dataList(list)
                        .onItemClickListener(new BottomSheetDialog.OnItemClickListener() {
                            @Override
                            public void onItemClick(int pos) {
                                showToast(list.get(pos).str);
                            }
                        }).create().show();
                break;
            case R.id.listview:
                startActivity(new Intent(this, ListViewActivity.class));
                break;
            case R.id.recyclerview:
                startActivity(new Intent(this, RecyclerViewActivity.class));
                break;
            case R.id.pagersliding:
                startActivity(new Intent(this, PagerSlidingActivity.class));
                /*PictureSelector.create(this)
                        .openGallery(PictureMimeType.ofImage())
                        .theme(R.style.picture_default_style)
                        .maxSelectNum(1)
                        .previewImage(true)
                        .hideBottomControls(true)
                        .isCamera(false)
                        .enableCrop(true)
                        .withAspectRatio(1,1)
                        .compress(true)
                        .compressQuality(30)
                        .forResult(100);*/
                break;

        }
    }
}
