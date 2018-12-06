/*
 * Copyright © Yan Zhenjie
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.yofish.droidkit.activity.perission;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.yofish.kitmodule.base_component.BaseActivity;
import com.yofish.droidkit.R;

/**
 * Created by Yan Zhenjie on 2017/5/1.
 */
public class PermissionAnyWhereActivity extends BaseActivity implements View.OnClickListener {

    private PermissionRequest permissionRequest;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initToolbar("anywhere");

        //noinspection ConstantConditions
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        findViewById(R.id.btn_req).setOnClickListener(this);
        permissionRequest = new PermissionRequest(this, new PermissionRequest.PermissionCallback() {
            @Override
            public void onSuccessful() {
                Toast.makeText(PermissionAnyWhereActivity.this, R.string.successfully, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure() {
                Toast.makeText(PermissionAnyWhereActivity.this, R.string.failure, Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_permission_anywhere;
    }

    @Override
    protected void initViews() {

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_req: {
                permissionRequest.request();
                break;
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home: {
                finish();
                break;
            }
        }
        return true;
    }
}
