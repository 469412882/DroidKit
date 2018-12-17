package com.yofish.droidkit.viewmodule;

import android.app.Application;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;
import android.view.View;

import com.yofish.droidkit.BR;
import com.yofish.droidkit.R;
import com.yofish.droidkit.repository.bean.BannerRouterBean;
import com.yofish.droidkit.viewmodule.item.BankItemViewModel;
import com.yofish.droidkit.viewmodule.item.ImgItemViewModel;
import com.yofish.imagemodule.ImgLoaderListener;

import com.yofish.kitmodule.baseAdapter.abslistview.DBLvAdapter;
import com.yofish.kitmodule.base_component.viewmodel.BaseViewModel;
import com.yofish.kitmodule.binding.command.BindingAction;
import com.yofish.kitmodule.binding.command.BindingCommand;
import com.yofish.kitmodule.binding.command.BindingConsumer;
import com.yofish.kitmodule.util.CommonBindingAdapter;
import com.yofish.kitmodule.util.LogUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class BindingImageViewModel extends BaseViewModel {

    List<String> list = Arrays.asList(new String[]{"http://pic26.nipic.com/20121227/10193203_131357536000_2.jpg"
            ,"http://shop.img.huishuaka.com/imgs/windows/2017/6/28/4785f349-5aec-4893-bcd8-f7a17929.png"
            ,"http://shop.img.huishuaka.com/imgs/windows/2017/6/28/54e2cc22-55bc-4a41-8dff-93f793cb.png"
            ,"http://shop.img.huishuaka.com/imgs/windows/2017/6/28/ad8de935-0616-498e-879d-d01bd34d.png"});

    public ObservableField<String> imgUrl = new ObservableField<>();
    public ObservableField<DBLvAdapter<ImgItemViewModel>> listAdapter = new ObservableField<>();

    int index = 0;

    public BindingCommand imgStartCommend =  new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            LogUtils.i("start ...");
        }
    });

    public BindingCommand imgFinishCommend =  new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            LogUtils.i("finish ...");
        }
    });

    public BindingCommand imgFailedCommend =  new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            LogUtils.i("failed ...");
        }
    });

    public BindingCommand<CommonBindingAdapter.ImgLoaderListenerWrapper> imgProgressCommend =  new BindingCommand<>(new BindingConsumer<CommonBindingAdapter.ImgLoaderListenerWrapper>() {
        @Override
        public void call(CommonBindingAdapter.ImgLoaderListenerWrapper imgLoaderListenerWrapper) {
            LogUtils.i(imgLoaderListenerWrapper.percentage + "% ..." );
        }
    });

    public BindingCommand clickBtn2 = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            showSnacBar("click ...");
        }
    });

    public BindingCommand longClickBtn2 = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            showSnacBar("long click ...");
        }
    });

    /**
     * 下拉刷新
     */
    public BindingCommand onRefresh = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            ArrayList<ImgItemViewModel> data = new ArrayList<>();
            for (int i = 0; i < 10; i++) {
                data.add(new ImgItemViewModel(BindingImageViewModel.this));
            }
            listAdapter.get().resetData(data);
            loadingComplete();
        }
    });

    /**
     * 上拉加载
     */
    public BindingCommand onLoadMore = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            Observable.just("")
                    .delay(2,TimeUnit.SECONDS)
                    .subscribeOn(Schedulers.computation())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<String>() {
                        @Override
                        public void call(String s) {
                            ArrayList<ImgItemViewModel> data = new ArrayList<>();
                            for (int i = 0; i < 5; i++) {
                                data.add(new ImgItemViewModel(BindingImageViewModel.this));
                            }

                            listAdapter.get().addData(data);
                            loadingComplete();
                        }
                    });
        }
    });

    public BindingImageViewModel(@NonNull Application application) {
        super(application);
    }


    @Override
    public void onCreate() {
        super.onCreate();
        listAdapter.set(new DBLvAdapter<ImgItemViewModel>(R.layout.img_item_bding,BR.imgItemVM));
    }

    public void imgClick(View view) {
        String url = list.get(index ++);
        if (index == list.size()) {
            index = 0;
        }
        imgUrl.set(url);
    }

}
