package com.yofish.droidkit.fragment;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;

import com.yofish.droidkit.activity.AlipayActivity;
import com.yofish.kitmodule.base_component.BaseFragment;
import com.yofish.kitmodule.router.CommonRouter;
import com.yofish.droidkit.R;
import com.yofish.droidkit.activity.ImgLoaderActivity;
import com.yofish.droidkit.activity.NetWorkActivity;
import com.yofish.droidkit.activity.UIActivity;
import com.yofish.droidkit.activity.VideoActivity;
import com.yofish.droidkit.activity.perission.PermissionMainActivity;

/**
 * file description
 * <p>
 * Created by hch on 2018/9/28.
 */

public class FragmentHome extends BaseFragment implements View.OnClickListener {
    @Override
    protected int setLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initViews(ViewGroup rootView) {
        rootView.findViewById(R.id.network).setOnClickListener(this);
        rootView.findViewById(R.id.imgloader).setOnClickListener(this);
        rootView.findViewById(R.id.webview).setOnClickListener(this);
        rootView.findViewById(R.id.permission).setOnClickListener(this);
        rootView.findViewById(R.id.ui).setOnClickListener(this);
        rootView.findViewById(R.id.router).setOnClickListener(this);
        rootView.findViewById(R.id.video).setOnClickListener(this);
        rootView.findViewById(R.id.alipay).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.network:
                intent.setClass(getContext(), NetWorkActivity.class);
                startActivity(intent);
                break;
            case R.id.imgloader:
                intent.setClass(getContext(), ImgLoaderActivity.class);
                startActivity(intent);
                break;
            case R.id.webview:
//                new WebKit.Builder(this).title("百度一下 你就知道").url("https://www.baidu.com").showclose(true ).openWebPage();
                CommonRouter.router2PagerByUrl(getContext(), "/droidkit/webview?webTitle="+ Uri.encode("百度一下 你就知道")+"&webUrl="+Uri.encode("https://www.baidu.com"));
                break;
            case R.id.permission:
                intent.setClass(getContext(), PermissionMainActivity.class);
                startActivity(intent);
                break;
            case R.id.ui:
                intent.setClass(getContext(), UIActivity.class);
                startActivity(intent);
                break;
            case R.id.router:
                CommonRouter.router2PagerByUrl(getContext(), "/app/recycler?a=1&b=2");
//                ARouter.getInstance().build("/app/recycler").navigation();
                break;
            case R.id.video:
                intent.setClass(getContext(), VideoActivity.class);
                startActivity(intent);
                break;
            case R.id.alipay:
                intent.setClass(getContext(), AlipayActivity.class);
                startActivity(intent);
                break;
        }
    }
}
