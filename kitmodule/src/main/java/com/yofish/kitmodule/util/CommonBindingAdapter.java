package com.yofish.kitmodule.util;

import android.databinding.BindingAdapter;
import android.graphics.Bitmap;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;

import com.yofish.imagemodule.ImgLoader;
import com.yofish.imagemodule.ImgLoaderListener;
import com.yofish.imagemodule.ImgOptions;
import com.yofish.kitmodule.R;
import com.yofish.kitmodule.baseAdapter.recyclerview.CommonAdapter;
import com.yofish.kitmodule.baseAdapter.recyclerview.DBRvAdapter;

/**
 * 常用的bindingAdapter
 * <p>
 * Created by hch on 2018/12/13.
 */
public class CommonBindingAdapter {

    public CommonBindingAdapter(){}

    @BindingAdapter({"android:src"})
    public static void setSrc(ImageView var0, Bitmap var1) {
        var0.setImageBitmap(var1);
    }

    @BindingAdapter({"android:src"})
    public static void setSrc(ImageView var0, int var1) {
        var0.setImageResource(var1);
    }


    @BindingAdapter({"bindBitmapImg"})
    public static void loadBitmapImg(ImageView imageView, Bitmap bitmap) {
        if (null == bitmap) {
            return;
        }
        new ImgLoader.Builder().loadType(ImgOptions.LoadType.BITMAP).bitmap(bitmap).into(imageView);
    }

    /**
     * imgShape=0原图 imgShape=1圆形图 imgShape=2圆角图
     * @param imageView
     * @param url
     * @param shape
     * @param imgLoaderListener
     */
    @BindingAdapter(value = {"bindNetImg", "imgShape", "bindNetImgListener"}, requireAll = false)
    public static void loadNetImg(ImageView imageView, String url, int shape, ImgLoaderListener imgLoaderListener) {
        if (TextUtils.isEmpty(url)) {
            return;
        } else {
            int holder;
            ImgLoader.Builder builder = new ImgLoader.Builder();
            if (0 == shape) {
                holder = R.drawable.netimg_default_rect_shape;
                builder.url(url).placeHolder(holder).error(holder);
            } else if (1 == shape) {
                holder = R.drawable.netimg_default_round_shape;
                builder.imgType(ImgOptions.ImgType.CIRCLE).url(url).placeHolder(holder).error(holder);
            } else if (2 == shape) {
                holder = R.drawable.netimg_default_fillet_shape;
                builder.imgType(ImgOptions.ImgType.ROUND).url(url).placeHolder(holder).error(holder);
            }

            if (imgLoaderListener == null) {
                builder.into(imageView);
            }else {
                builder.listener(imgLoaderListener).intoWithListener(imageView);
            }

        }
    }

//    @BindingAdapter({"anbuiAdapter"})
//    public static void setListAdapter(ListView var0, DBBaseAdapter var1) {
//        var0.setAdapter(var1);
//    }
//
//    @BindingAdapter({"anbuiAdapter"})
//    public static void setGridAdapter(GridView var0, DBBaseAdapter var1) {
//        var0.setAdapter(var1);
//    }

    @BindingAdapter({"bindAdapter"})
    public static void setRecyclerViewAdapter(RecyclerView recyclerView, DBRvAdapter adapter) {
        recyclerView.setAdapter(adapter);
    }

    @BindingAdapter({"bindAdapter"})
    public static void setRecyclerViewAdapter(RecyclerView recyclerView, CommonAdapter adapter) {
        recyclerView.setAdapter(adapter);
    }

//    @BindingAdapter({"anbuiAdapter"})
//    public static void setRecyclerViewAdapter(RecyclerView var0, HeaderAndFooterWrapper var1) {
//        var0.setAdapter(var1);
//    }
//
//    @BindingAdapter({"anbuiStopRefresh"})
//    public static void stopRefresh(PtrClassicFrameLayout var0, boolean var1) {
//        if (var1) {
//            var0.refreshComplete();
//        }
//
//    }
//
//    @BindingAdapter({"anbuiStopRefresh"})
//    public static void stopRefresh(PtrFrameLayout var0, boolean var1) {
//        if (var1) {
//            var0.refreshComplete();
//        }
//
//    }
//
//    @BindingAdapter({"anbuiStopRefresh"})
//    public static void stopRefresh(PtrMetialFrameLayout var0, boolean var1) {
//        if (var1) {
//            var0.refreshComplete();
//        }
//
//    }

    @BindingAdapter({"bindOnLongClick"})
    public static void onLongClick(View view, View.OnLongClickListener longClickListener) {
        view.setOnLongClickListener(longClickListener);
    }

    @BindingAdapter({"errhint"})
    public static void onErrHint(TextInputLayout textInputLayout, String str) {
        textInputLayout.setError(str);
    }

    @BindingAdapter({"onMenuClickListener"})
    public static void onMenuClickListener(Toolbar toolbar, Toolbar.OnMenuItemClickListener listener) {
        toolbar.setOnMenuItemClickListener(listener);
    }
}
