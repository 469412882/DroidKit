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

import com.yofish.kitmodule.baseAdapter.abslistview.CommonAdapter;
import com.yofish.kitmodule.baseAdapter.abslistview.ViewHolder;
import com.yofish.kitmodule.base_component.BaseActivity;
import com.yofish.kitmodule.util.Utility;
import com.yofish.kitmodule.wedget.BottomSheetDialog;
import com.yofish.droidkit.R;

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
                break;
        }
    }
}
