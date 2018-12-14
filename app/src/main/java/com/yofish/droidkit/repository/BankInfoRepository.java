package com.yofish.droidkit.repository;

import android.content.Context;

import com.yofish.kitmodule.base_component.repository.BaseRepository;
import com.yofish.kitmodule.util.AppSharedPrefrences;

/**
 * file description
 * <p>
 * Created by hch on 2018/12/13.
 */
public class BankInfoRepository extends BaseRepository<com.yofish.droidkit.repository.bean.BankData> {

    @Override
    protected String getBaseUrl() {
        return "http://credit.youyuwo.com/notcontrol/manage/";
    }

    @Override
    protected String getMethod() {
        return "queryCardImportBankList.go";
    }

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
     * 加载缓存数据
     */
    @Override
    public void loadCacheData() {
        AppSharedPrefrences.getInstance().get("key", "defaultValue");
        if (callBack != null) {
            //do something
        }
    }

    /**
     * 加载假数据
     */
    @Override
    public void loadFakeData() {
//        JSONObject jsonObject = Utility.readJsonFromAssets()
//        BankData data = JSON.parseObject(jsonObject, BankData.class);
//        if (callBack!=null) {
//            callBack.onSuccess();
//        }
    }
}
