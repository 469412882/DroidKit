package com.yofish.droidkit.fragment;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.yofish.kitmodule.base_component.BaseFragment;
import com.yofish.kitmodule.util.Utility;
import com.yofish.droidkit.R;

/**
 * file description
 * <p>
 * Created by hch on 2018/9/28.
 */

public class FragmentFind extends BaseFragment implements View.OnClickListener {
    @Override
    protected int setLayoutId() {
        return R.layout.fragment_fx;
    }

    @Override
    protected void initViews(ViewGroup rootView) {
        rootView.findViewById(R.id.get_sp).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.get_sp:
                String sp = Utility.testSpData();
                Toast.makeText(getContext(), sp, Toast.LENGTH_LONG).show();
                break;
        }
    }
}
