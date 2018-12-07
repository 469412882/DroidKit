package com.yofish.droidkit.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.alipay.sdk.app.EnvUtils;
import com.alipay.sdk.app.PayTask;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.yofish.droidkit.R;
import com.yofish.droidkit.bean.PayResult;
import com.yofish.kitmodule.base_component.BaseActivity;
import com.yofish.kitmodule.util.LogUtils;
import com.yofish.netmodule.NetClient;
import com.yofish.netmodule.callback.ProgressCallBack;
import com.yofish.netmodule.datatype.AllJsonObject;

import java.util.HashMap;
import java.util.Map;

/**
 * file description
 * <p>
 * Created by hch on 2018/12/7.
 */
public class AlipayActivity extends BaseActivity implements View.OnClickListener {

    private static final int SDK_PAY_FLAG = 1;

    private TextView mPayResult;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    @SuppressWarnings("unchecked")
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     * 对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        mPayResult.setText("支付成功" + payResult);
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        mPayResult.setText("支付失败" + payResult);
                    }
                    break;
                }
                default:
                    break;
            }
        }

        ;
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        EnvUtils.setEnv(EnvUtils.EnvEnum.SANDBOX);
        super.onCreate(savedInstanceState);
        initToolbar("支付测试");
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_alipay;
    }

    @Override
    protected void initViews() {
        findViewById(R.id.do_pay).setOnClickListener(this);
        mPayResult = findViewById(R.id.pay_result);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.do_pay:
                Map<String, String> params = new HashMap<>();
                NetClient.newBuilder(this)
                        .baseUrl("http://pqq.free.idcfengye.com/payment/")
                        .method("pay")
                        .params(params)
                        .callBack(new ProgressCallBack<AllJsonObject>() {
                            @Override
                            public void onSuccess(AllJsonObject result) {
                                String orderInfo = result.getResponseBodyJson().get("data").getAsString();
                                if (TextUtils.isEmpty(orderInfo)) {
                                    mPayResult.setText("调用后台订单返回data为空");
                                    return;
                                }
                                doPay(orderInfo);
                            }

                            @Override
                            public void onFailed(String errors) {
                                super.onFailed(errors);
                                mPayResult.setText(errors);
                            }
                        }).sendPost();
                break;
        }
    }

    private void doPay(final String orderInfo) {
        LogUtils.i("orderInfo:" + orderInfo + "--------end");
        final Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                PayTask alipay = new PayTask(AlipayActivity.this);
                Map<String, String> result = alipay.payV2(orderInfo, true);
                LogUtils.i( result.toString());

                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }
}
