package com.holike.crm.dialog;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.holike.crm.R;
import com.holike.crm.bean.CustomerListBeanV2;

/**
 * Created by gallop on 2019/8/21.
 * Copyright holike possess 2019.
 * 线上引流客户分配门店dialog
 */
public class DistributionShopDialog extends DistributionDialog {

    public DistributionShopDialog(Context context, CustomerListBeanV2.CustomerBean bean) {
        super(context);
        setText(bean);
    }

    @Override
    protected int bindContentView() {
        return R.layout.dialog_distribution_shop;
    }

    private void setText(CustomerListBeanV2.CustomerBean bean) {
        TextView tvUserName = mContentView.findViewById(R.id.tv_userName);
        tvUserName.setText(bean.userName);
        TextView tvArea = mContentView.findViewById(R.id.tv_area);
        tvArea.setText(bean.detailsAddress);
        TextView tvAddress = mContentView.findViewById(R.id.tv_address);
        tvAddress.setText(bean.address);
        TextView tvEmptyRemark = mContentView.findViewById(R.id.tv_empty_remark);
        TextView tvRemark = mContentView.findViewById(R.id.tv_remark);
        if (TextUtils.isEmpty(bean.remark)) {
            tvEmptyRemark.setVisibility(View.VISIBLE);
            tvRemark.setVisibility(View.GONE);
        } else {
            tvEmptyRemark.setVisibility(View.GONE);
            tvRemark.setVisibility(View.VISIBLE);
            tvRemark.setText(bean.remark);
        }
    }
}
