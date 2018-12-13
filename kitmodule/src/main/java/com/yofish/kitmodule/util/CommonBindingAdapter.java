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

    @BindingAdapter({"bindNetImg"})
    public static void loadNetImg(ImageView imageView, String url) {
        ImgLoader.getInstance().load(url, imageView);
//        if (TextUtils.isEmpty(url)) {
//            Glide.clear(var0);
//        } else {
//            Glide.with(var0.getContext()).load(var1).crossFade().placeholder(drawable.anbui_netimg_default_rect_shape).into(var0);
//        }
    }

    @BindingAdapter({"bindBitmapImg"})
    public static void loadBitmapImg(ImageView imageView, Bitmap bitmap) {
//        if (null == var1) {
//            Glide.clear(var0);
//        } else {
//            Glide.with(var0.getContext()).load(var1).crossFade().placeholder(drawable.anbui_netimg_default_rect_shape).into(var0);
//        }
    }

    @BindingAdapter({"bindNetImg", "imgShape"})
    public static void loadNetImg(ImageView imageView, String url, int shape) {
//        if (TextUtils.isEmpty(var1)) {
//            Glide.clear(var0);
//        } else {
//            Object var3 = null;
//            int var4 = drawable.anbui_netimg_default_rect_shape;
//            if (0 == var2) {
//                var3 = new GlideCircleTransform(var0.getContext());
//                var4 = drawable.anbui_netimg_default_round_shape;
//            } else if (1 == var2) {
//                var3 = new GlideRoundTransform(var0.getContext());
//                var4 = drawable.anbui_netimg_default_fillet_shape;
//            }
//
//            if (null != var3) {
//                Glide.with(var0.getContext()).load(var1).crossFade().placeholder(var4).error(var4).transform(new BitmapTransformation[]{(BitmapTransformation)var3}).into(var0);
//            } else {
//                Glide.with(var0.getContext()).load(var1).crossFade().placeholder(var4).error(var4).into(var0);
//            }
//
//        }
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
