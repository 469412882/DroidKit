package com.yofish.droidkit.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.yofish.kitmodule.baseAdapter.abslistview.CommonAdapter;
import com.yofish.kitmodule.baseAdapter.abslistview.ViewHolder;
import com.yofish.kitmodule.base_component.BaseActivity;
import com.yofish.kitmodule.permission.AndPermission;
import com.yofish.kitmodule.permission.Permission;
import com.yofish.kitmodule.permission.PermissionListener;
import com.yofish.kitmodule.permission.Rationale;
import com.yofish.kitmodule.permission.RationaleListener;
import com.yofish.kitmodule.util.LogUtils;
import com.yofish.kitmodule.util.Utility;
import com.yofish.droidkit.R;
import com.yofish.droidkit.repository.bean.BankData;
import com.yofish.droidkit.repository.bean.BankInfoBean;
import com.yofish.netmodule.NetClient;
import com.yofish.netmodule.callback.BaseCallBack;
import com.yofish.netmodule.callback.ProgressCallBack;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 网络测试页
 *
 * Created by hch on 2017/6/28.
 */

public class NetWorkActivity extends BaseActivity implements View.OnClickListener {

    CommonAdapter adapter;

    ProgressBar uploadPbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initToolbar("网络请求");
        AndPermission.with(this).requestCode(100).permission(Permission.STORAGE).callback(permissionListener)
                .rationale(rationaleListener).start();
    }

    @Override
    protected int setLayoutId(Bundle savedInstanceState) {
        return R.layout.activity_network;
    }

    @Override
    protected void initViews() {
        findViewById(R.id.request).setOnClickListener(this);
        findViewById(R.id.upload_file).setOnClickListener(this);
        findViewById(R.id.upload_files).setOnClickListener(this);
        findViewById(R.id.download_files).setOnClickListener(this);
        uploadPbar = (ProgressBar) findViewById(R.id.upload_progress);

        ListView listView = (ListView) findViewById(R.id.listview);
        listView.setAdapter(adapter = new CommonAdapter<BankInfoBean>(this, R.layout.banklist_item, null) {
            @Override
            protected void convert(ViewHolder viewHolder, BankInfoBean item, int position) {
                viewHolder.setImageResource(R.id.bank_logo, R.mipmap.ic_launcher_round);
                viewHolder.setText(R.id.bank_name, item.getBankName());
            }
        });
    }

    @Override
    protected void loadData() {

    }

    private void requestData() {
        NetClient.newBuilder(this).baseUrl("http://credit.youyuwo.com/notcontrol/manage/")
                .method("queryCardImportBankList.go").callBack(new ProgressCallBack<BankData>() {
                    @Override
                    public void onSuccess(BankData bankData) {
                        adapter.resetData(bankData.getBankList());
                        LogUtils.d("请求银行列表成功");
                    }

                    @Override
                    public void onFailed(String code, String errors) {
                        Toast.makeText(NetWorkActivity.this, errors, Toast.LENGTH_SHORT).show();
                    }
                }).sendPost();
    }

    private void uploadFile() {
        try {
            InputStream inputStream = getResources().getAssets().open("bigPic.jpg");
            File file = Utility.saveFile(inputStream, "bigPic.jpg");
            NetClient.newBuilder(this).baseUrl("http://credit.youyuwo.com/control/user/").method("uploadIconNew.go")
                    .addFile(file).callBack(new BaseCallBack() {
                        @Override
                        public void onSuccess(Object o) {
                            Toast.makeText(NetWorkActivity.this, "success", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailed(String code, String errors) {
                            Toast.makeText(NetWorkActivity.this, errors, Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onProgress(int progress, long total) {
                            super.onProgress(progress, total);
                            uploadPbar.setProgress(progress);
                        }
                    }).uploadFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void uploadFiles() {
        try {
            InputStream inputStream = getResources().getAssets().open("pic.png");
            InputStream inputStream2 = getResources().getAssets().open("pic2.png");
            File file = Utility.saveFile(inputStream, "pic.png");
            File file2 = Utility.saveFile(inputStream2, "pic2.png");
            Map<String, Object> params = new HashMap<>();
            params.put("username", "huach");
            new NetClient.Builder(this).baseUrl("http://credit.youyuwo.com/control/user/").method("uploadIconNew.go")
                    .addFile(file).addFile(file2).params(params).callBack(new ProgressCallBack() {
                        @Override
                        public void onSuccess(Object o) {
                            Toast.makeText(NetWorkActivity.this, "success", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailed(String code, String errors) {
                            Toast.makeText(NetWorkActivity.this, errors, Toast.LENGTH_SHORT).show();
                        }
                    }).uploadFiles();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.request:
                requestData();
                break;
            case R.id.upload_file:
                uploadFile();
                break;
            case R.id.upload_files:
                uploadFiles();
                break;
            case R.id.download_files:
                Intent intent = new Intent(this, DownloadActivity.class);
                startActivity(intent);
                break;
        }
    }

    /**
     * 回调监听。
     */
    private PermissionListener permissionListener = new PermissionListener() {
        @Override
        public void onSucceed(int requestCode, @NonNull List<String> grantPermissions) {

        }

        @Override
        public void onFailed(int requestCode, @NonNull List<String> deniedPermissions) {
            // 用户否勾选了不再提示并且拒绝了权限，那么提示用户到设置中授权。
            if (AndPermission.hasAlwaysDeniedPermission(NetWorkActivity.this, deniedPermissions)) {
                AndPermission.defaultSettingDialog(NetWorkActivity.this, 100).setTitle("权限申请")
                        .setMessage("应用程序需要访问SD卡的权限来存储或读取图片，请您到设置页面手动授权，否则功能无法正常使用！").setPositiveButton("好，去设置").show();
            }
        }
    };

    /**
     * Rationale支持，这里自定义对话框。
     */
    private RationaleListener rationaleListener = new RationaleListener() {
        @Override
        public void showRequestPermissionRationale(int requestCode, final Rationale rationale) {
            // 这里使用自定义对话框，如果不想自定义，用AndPermission默认对话框：
            // AndPermission.rationaleDialog(Context, Rationale).show();

            // 自定义对话框。
            new AlertDialog.Builder(NetWorkActivity.this).setTitle(R.string.title_dialog)
                    .setMessage("应用程序需要访问SD卡的权限来存储或读取图片")
                    .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            rationale.resume();
                        }
                    }).setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            rationale.cancel();
                        }
                    }).show();
        }
    };
}
