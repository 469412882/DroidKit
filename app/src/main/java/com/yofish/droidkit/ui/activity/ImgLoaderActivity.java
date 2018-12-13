package com.yofish.droidkit.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.yofish.kitmodule.baseAdapter.abslistview.CommonAdapter;
import com.yofish.kitmodule.baseAdapter.abslistview.ViewHolder;
import com.yofish.kitmodule.base_component.BaseActivity;
import com.yofish.droidkit.R;
import com.yofish.imagemodule.ImgLoader;
import com.yofish.imagemodule.ImgOptions;

import java.util.Arrays;
import java.util.List;

public class ImgLoaderActivity extends BaseActivity implements View.OnClickListener {

    private ImageView img;
    private ImageView gif;
    private TextView cacheView;
    private CommonAdapter<String> mAdapter;
    private GridView gridView;
    private List<String> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initToolbar("图片加载");
    }

    @Override
    protected int setLayoutId(Bundle savedInstanceState) {
        return R.layout.activity_img_loader;
    }

    @Override
    protected void initViews() {
        findViewById(R.id.load_img).setOnClickListener(this);
        findViewById(R.id.load_gif).setOnClickListener(this);
        findViewById(R.id.load_list).setOnClickListener(this);
        findViewById(R.id.clear_cache).setOnClickListener(this);
        img = (ImageView) findViewById(R.id.img);
        gif = (ImageView) findViewById(R.id.gif);
        cacheView = (TextView) findViewById(R.id.cache_size);
        cacheView.setText("缓存大小：" + ImgLoader.getInstance().getCacheSize(this));
        gridView = (GridView) findViewById(R.id.gallery);
        gridView.setAdapter(mAdapter = new CommonAdapter<String>(this, R.layout.gallery_item) {
            @Override
            protected void convert(ViewHolder viewHolder, String item, int position) {
                ImgLoader.getInstance().load(item, (ImageView) viewHolder.getView(R.id.pic));
            }
        });
        list = Arrays.asList(new String[]{"http://shop.img.huishuaka.com/imgs/windows/2017/6/28/94e61bc2-8f66-454e-b8a9-0809dd79.png"
                ,"http://shop.img.huishuaka.com/imgs/windows/2017/6/28/4785f349-5aec-4893-bcd8-f7a17929.png"
                ,"http://shop.img.huishuaka.com/imgs/windows/2017/6/28/54e2cc22-55bc-4a41-8dff-93f793cb.png"
                ,"http://shop.img.huishuaka.com/imgs/windows/2017/6/28/ad8de935-0616-498e-879d-d01bd34d.png"});
    }

    @Override
    protected void loadData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.load_img:
                // ImgLoader.getInstance().load(
                // "http://shop.img.huishuaka.com/imgs/windows/2017/6/21/4fd4f67a-7564-4524-a05f-4d0f6600.png",
                // img);
                new ImgLoader.Builder()
                        .url("http://shop.img.huishuaka.com/imgs/windows/2017/6/21/4fd4f67a-7564-4524-a05f-4d0f6600.png")
                        .placeHolder(R.mipmap.loading_defaul_rect_big).imgType(ImgOptions.ImgType.NORMAL).into(img);
                break;
            case R.id.load_gif:
                ImgLoader
                        .getInstance()
                        .load("http://b.hiphotos.baidu.com/zhidao/wh%3D600%2C800/sign=a7c40bb6087b02080c9c37e752e9deeb/0824ab18972bd407fc38f0ee79899e510fb30921.jpg",
                                gif);
                break;
            case R.id.load_list:
                mAdapter.addData(list);
                break;
            case R.id.clear_cache:
                ImgLoader.getInstance().clearImgDiskCache(this);
                cacheView.setText("缓存大小：" + ImgLoader.getInstance().getCacheSize(this));
                break;
        }
    }
}
