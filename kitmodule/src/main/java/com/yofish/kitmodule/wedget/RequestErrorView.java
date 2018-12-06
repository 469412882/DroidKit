package com.yofish.kitmodule.wedget;

import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yofish.kitmodule.R;
import com.yofish.kitmodule.util.Utility;


/**
 * Nodata和neterror
 * 
 * Created by hch on 2017/7/13.
 */

public class RequestErrorView extends LinearLayout {

    private TextView noDataView;

    private TextView netErrorView;

    private LinearLayout rootView;

    private View contentView;

    private OnRequestAgainListener mListener;

    public RequestErrorView(Context context) {
        this(context, null);
    }

    public RequestErrorView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    public RequestErrorView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.request_error, this, true);
        noDataView = (TextView) findViewById(R.id.nodata);
        netErrorView = (TextView) findViewById(R.id.neterror);
        rootView = (LinearLayout) findViewById(R.id.root);
        noDataView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onRequestAgain();
                    reset();
                }
            }
        });
        netErrorView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    reset();
                    mListener.onRequestAgain();
                }
            }
        });
    }

    public void setNoDataClickable(boolean clickable){
        noDataView.setClickable(clickable);
    }

    private void setNoData() {
        noDataView.setVisibility(VISIBLE);
        netErrorView.setVisibility(GONE);
    }

    private void setNetError() {
        noDataView.setVisibility(GONE);
        netErrorView.setVisibility(VISIBLE);
    }

    public void errorResponse() {
        showError();
        if (Utility.isNetworkConnected(getContext())) {
            setNoData();
        } else {
            setNetError();
        }
    }

    public void errorResponse(String errorMsg) {
        showError();
        if (Utility.isNetworkConnected(getContext())) {
            setNoData();
            noDataView.setText(errorMsg);
        } else {
            setNetError();
        }
    }

    private void reset() {
        this.setVisibility(GONE);
        if (contentView != null) {
            contentView.setVisibility(VISIBLE);
        }
    }

    private void showError() {
        this.setVisibility(VISIBLE);
        if (contentView != null) {
            contentView.setVisibility(GONE);
        }
    }

    /**
     * 支持在错误页面下方增加自定义的view
     * 
     * @param layourtId
     *            布局Id
     */
    public void addBottomView(int layourtId, OnClickListener listener) {
        View view = LayoutInflater.from(getContext()).inflate(layourtId, rootView, false);
        addBottomView(view, listener);
    }

    /**
     * 支持在错误页面下方增加自定义的view
     * 
     * @param view
     *            view
     */
    public void addBottomView(View view, OnClickListener listener) {
        rootView.addView(view);
        view.setOnClickListener(listener);
    }

    public void setOnRequestAgainListener(View contentView, OnRequestAgainListener listener) {
        this.contentView = contentView;
        mListener = listener;
    }

    public interface OnRequestAgainListener {
        void onRequestAgain();
    }

}
