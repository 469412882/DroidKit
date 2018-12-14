package com.yofish.droidkit.viewmodule;

import android.app.Application;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;
import android.view.View;

import com.yofish.imagemodule.ImgLoaderListener;
import com.yofish.kitmodule.base_component.viewmodel.BaseViewModel;
import com.yofish.kitmodule.util.LogUtils;

import java.util.Arrays;
import java.util.List;

public class BindingImageViewModel extends BaseViewModel {

    List<String> list = Arrays.asList(new String[]{"http://shop.img.huishuaka.com/imgs/windows/2017/6/28/94e61bc2-8f66-454e-b8a9-0809dd79.png"
            ,"http://shop.img.huishuaka.com/imgs/windows/2017/6/28/4785f349-5aec-4893-bcd8-f7a17929.png"
            ,"http://shop.img.huishuaka.com/imgs/windows/2017/6/28/54e2cc22-55bc-4a41-8dff-93f793cb.png"
            ,"http://shop.img.huishuaka.com/imgs/windows/2017/6/28/ad8de935-0616-498e-879d-d01bd34d.png"});

    public ObservableField<String> imgUrl = new ObservableField<>();
    public ObservableField<ImgLoaderListener> imgloaderListener = new ObservableField<>();
    int index = 0;

    public BindingImageViewModel(@NonNull Application application) {
        super(application);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        imgloaderListener.set(new ImgLoaderListener() {
            @Override
            public void start() {
                showSnacBar("start");
            }

            @Override
            public void finish() {
                showSnacBar("finish");
            }

            @Override
            public void progress(boolean isComplete, int percentage, long bytesRead, long totalBytes) {
                LogUtils.i(percentage + "%");
            }

            @Override
            public void failed() {

            }
        });

    }

    public void imgClick(View view) {
        String url = list.get(index ++);
        if (index == list.size()) {
            index = 0;
        }
        imgUrl.set(url);
    }

}
