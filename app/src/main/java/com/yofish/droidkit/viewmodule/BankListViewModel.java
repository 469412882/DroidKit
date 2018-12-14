package com.yofish.droidkit.viewmodule;

import android.app.Application;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;
import android.view.View;

import com.yofish.droidkit.BR;
import com.yofish.droidkit.R;
import com.yofish.droidkit.repository.BankInfoRepository;
import com.yofish.droidkit.repository.bean.BankData;
import com.yofish.droidkit.repository.bean.BankInfoBean;
import com.yofish.droidkit.viewmodule.item.BankItemViewModel;
import com.yofish.kitmodule.baseAdapter.recyclerview.DBRvAdapter;
import com.yofish.kitmodule.base_component.repository.IRepositoryCallBack;
import com.yofish.kitmodule.base_component.viewmodel.BaseViewModel;
import com.yofish.kitmodule.util.PagerInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * file description
 * <p>
 * Created by hch on 2018/12/13.
 */
public class BankListViewModel extends BaseViewModel {
    /** 非ItemViewModel类型的adapter */
//    public ObservableField<CommonAdapter<BankInfoBean>> mAdapter = new ObservableField<>();
    /**
     * ItemViewModel类型的adapter
     */
    public ObservableField<DBRvAdapter<BankItemViewModel>> mAdapter = new ObservableField<>();

    public BankListViewModel(@NonNull Application application) {
        super(application);
       /* mAdapter.set(new CommonAdapter<BankInfoBean>(getApplication(), R.layout.banklist_item) {

            @Override
            protected void convert(ViewHolder holder, BankInfoBean bankInfoBean, int position) {
                holder.setImageResource(R.id.bank_logo, R.mipmap.ic_launcher_round);
                holder.setText(R.id.bank_name, bankInfoBean.getBankName());
            }
        });*/
        mAdapter.set(new DBRvAdapter<BankItemViewModel>(R.layout.banklist_item_bding, BR.viewModel));
    }

    public void requestRepository(final boolean isLoadMore) {
        BankInfoRepository repository = new BankInfoRepository(getApplication());
        repository.setCallBack(new IRepositoryCallBack<BankData>() {
            @Override
            public void onSuccess(BankData bankData) {
                List<BankItemViewModel> itemViewModels = new ArrayList<>();
                for (BankInfoBean bankInfoBean : bankData.getBankList()) {
                    BankItemViewModel model = new BankItemViewModel(BankListViewModel.this);
                    model.bankName.set(bankInfoBean.getBankName());
                    model.bankIconURL.set(bankInfoBean.getBankIconURL());
                    itemViewModels.add(model);
                }
                if (isLoadMore) {
                    mAdapter.get().addData(itemViewModels);
                    updatePage(new PagerInfo(1, 2));
                } else {
                    mAdapter.get().resetData(itemViewModels);
                }
            }

            @Override
            public void onFailed(String code, String desc) {
                showSnacBar(desc);
                setNetErr();
            }

            @Override
            public void onComplete() {
                loadingComplete();
            }
        });
        repository.loadData();
    }

    @Override
    public void clickRetry(View view) {
        super.clickRetry(view);
        requestRepository(false);
    }
}
