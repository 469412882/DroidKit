package com.yofish.kitmodule.wedget;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.yofish.kitmodule.R;
import com.yofish.kitmodule.baseAdapter.abslistview.CommonAdapter;
import com.yofish.kitmodule.baseAdapter.abslistview.ViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * 底部弹出列表dialog
 * 
 * Created by hch on 2017/8/14.
 */

public class BottomSheetDialog {

    private Dialog mDialog;

    public static Builder newBuilder(Context context) {
        return new Builder(context);
    }

    public BottomSheetDialog(Dialog dialog){
        this.mDialog = dialog;
    }

    public void show(){
        mDialog.show();
    }

    public static class Builder {
        private Context context;
        private int theme = -1;
        private boolean cancelable = true;
        private boolean hasIcon;
        private OnItemClickListener listener;
        private List<BottomSheetBean> dataList;

        public Builder(Context context) {
            this.context = context;
            dataList = new ArrayList<>();
        }

        public Builder theme(int theme) {
            this.theme = theme;
            return this;
        }

        public Builder cancelable(boolean cancelable) {
            this.cancelable = cancelable;
            return this;
        }

        public Builder stringArrays(String[] strs) {
            if (strs == null) {
                return this;
            }
            dataList.clear();
            for (String str : strs) {
                dataList.add(new BottomSheetBean(0, str));
            }
            return this;
        }

        public Builder dataList(List<BottomSheetBean> dataList) {
            if (dataList == null) {
                return this;
            }
            this.dataList.clear();
            this.dataList.addAll(dataList);
            hasIcon = true;
            return this;
        }

        public Builder onItemClickListener(OnItemClickListener listener) {
            this.listener = listener;
            return this;
        }

        public BottomSheetDialog create() {
            final Dialog mBottomSheetDialog = new Dialog(context, theme == -1 ? R.style.MaterialDialogSheet : theme);
            View content = LayoutInflater.from(context).inflate(R.layout.bottom_sheet_dialog, null);
            final ListView listView = (ListView) content.findViewById(R.id.sheet_list);
            listView.setAdapter(new CommonAdapter<BottomSheetBean>(context, R.layout.bottom_sheet_dialog_item, dataList) {
                @Override
                protected void convert(ViewHolder viewHolder, BottomSheetBean item, final int position) {
                    ImageView icon = viewHolder.getView(R.id.icon);
                    TextView textView = viewHolder.getView(R.id.content);
                    if (hasIcon) {
                        icon.setVisibility(View.VISIBLE);
                        icon.setImageResource(item.icon);
                    } else {
                        icon.setVisibility(View.GONE);
                        ((LinearLayout)viewHolder.getConvertView()).setGravity(Gravity.CENTER);
                    }
                    textView.setText(item.str);
                    viewHolder.getConvertView().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (listener != null) {
                                listener.onItemClick(position);
                            }
                            mBottomSheetDialog.dismiss();
                        }
                    });
                }
            });
            mBottomSheetDialog.setContentView(content);
            mBottomSheetDialog.setCancelable(cancelable);
            mBottomSheetDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            mBottomSheetDialog.getWindow().setGravity(Gravity.BOTTOM);
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(mBottomSheetDialog);
            return bottomSheetDialog;
        }

    }

    public interface OnItemClickListener {
        void onItemClick(int pos);
    }

    public static class BottomSheetBean {
        public int icon;
        public String str;

        public BottomSheetBean(int icon, String str) {
            this.icon = icon;
            this.str = str;
        }
    }
}
