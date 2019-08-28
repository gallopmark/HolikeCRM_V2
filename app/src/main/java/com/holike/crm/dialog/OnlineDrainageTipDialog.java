package com.holike.crm.dialog;

import android.content.Context;

import androidx.annotation.NonNull;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.holike.crm.R;

/**
 * 线上引流提示dialog
 */
public class OnlineDrainageTipDialog extends CommonDialog {


    public OnlineDrainageTipDialog(@NonNull Context context, boolean isDealer) {
        super(context);
        init(context, isDealer);
    }

    @Override
    protected int bindContentView() {
        return R.layout.dialog_online_drainage;
    }

    private void init(Context context, boolean isDealer) {
        TextView confirm = mContentView.findViewById(R.id.tv_confirm);
        LinearLayout mRemarkLl1 = mContentView.findViewById(R.id.mRemarkLl1);
        TextView tvRemark2 = mContentView.findViewById(R.id.tvRemark2);
        TextView tvRemark3 = mContentView.findViewById(R.id.tvRemark3);
        if (isDealer) {  //经销商
            mRemarkLl1.setVisibility(View.GONE);
            tvRemark2.setText(context.getString(R.string.online_drainage_report_tip4));
            tvRemark3.setText(context.getString(R.string.online_drainage_report_tip5));
        } else {
            mRemarkLl1.setVisibility(View.VISIBLE);
            tvRemark2.setText(context.getString(R.string.online_drainage_report_tip2));
            tvRemark3.setText(context.getString(R.string.online_drainage_report_tip3));
        }
        confirm.setOnClickListener(v -> dismiss());
    }


}
