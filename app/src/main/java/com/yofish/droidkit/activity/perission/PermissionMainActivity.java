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

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.yofish.kitmodule.base_component.BaseActivity;
import com.yofish.droidkit.R;

public class PermissionMainActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initToolbar("权限管理");
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_permission_main;
    }

    @Override
    protected void initViews() {
        findViewById(R.id.btn_annotation).setOnClickListener(this);
        findViewById(R.id.btn_rationale).setOnClickListener(this);
        findViewById(R.id.btn_listener).setOnClickListener(this);
        findViewById(R.id.btn_other).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_annotation: {
                startActivity(new Intent(this, PermissionAnnotationActivity.class));
                break;
            }
            case R.id.btn_listener: {
                startActivity(new Intent(this, PermissionListenerActivity.class));
                break;
            }
            case R.id.btn_other: {
                startActivity(new Intent(this, PermissionAnyWhereActivity.class));
                break;
            }
            case R.id.btn_rationale: {
                startActivity(new Intent(this, PermissionRationaleActivity.class));
                break;
            }
        }
    }
}
