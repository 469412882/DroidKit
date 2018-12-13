package com.yofish.droidkit.repository;

import android.content.Context;

import com.yofish.droidkit.repository.bean.BankData;
import com.yofish.kitmodule.base_component.repository.BaseRepository;
import com.yofish.netmodule.NetClient;
import com.yofish.netmodule.callback.BaseCallBack;

/**
 * file description
 * <p>
 * Created by hch on 2018/12/13.
 */
public class BankInfoRepository extends BaseRepository<com.yofish.droidkit.repository.bean.BankData> {

    /**
     * 是否要加载假数据
     *
     * @return boolean
     */
    @Override
    protected boolean isFake() {
        return super.isFake();
    }

    public BankInfoRepository(Context context) {
        super(context);
    }

    /**
     * 加载网络数据
     */
    @Override
    public void requestNetData() {
        NetClient.newBuilder(getContext())
                .baseUrl("http://credit.youyuwo.com/notcontrol/manage/")
                .method("queryCardImportBankList.go")
                .callBack(new BaseCallBack<BankData>() {
                    @Override
                    public void onSuccess(BankData bankData) {
                        if (callBack != null) {
                            callBack.onSuccess(bankData);
                        }
                    }

                    @Override
                    public void onFailed(String code, String errors) {
                        if (callBack != null) {
                            callBack.onFailed(code, errors);
                        }
                    }

                    @Override
                    public void onComplete() {
                        super.onComplete();
                        if (callBack != null) {
                            callBack.onComplete();
                        }
                    }
                }).sendPost();
    }

    /**
     * 加载缓存数据
     */
    @Override
    public void loadCacheData() {

    }

    /**
     * 加载假数据
     */
    @Override
    public void loadFakeData() {

    }
}
