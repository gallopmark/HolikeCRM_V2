package com.holike.crm.adapter.customer;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;

import com.holike.crm.R;
import com.holike.crm.adapter.CustomerStatusListAdapter;
import com.holike.crm.bean.CustomerStatusBean;
import com.holike.crm.bean.MultiItem;

import java.util.List;

/**
 * Created by gallop 2019/7/8
 * Copyright (c) 2019 holike
 * 待签约客户（客户列表）
 */
public class CustomerUnsignAdapter extends CustomerStatusListAdapter {
    private String mTipsUploadDate, mTipsSource, mTipsDesigner,
            mTipsReservation, mTipsCountdown;

    public CustomerUnsignAdapter(Context context, List<MultiItem> mDatas) {
        super(context, mDatas);
        mTipsUploadDate = context.getString(R.string.followup_upload_scheme);
        mTipsSource = context.getString(R.string.customer_source_tips);
        mTipsDesigner = context.getString(R.string.followup_designer);
        mTipsReservation = context.getString(R.string.followup_reservation);
        mTipsCountdown = context.getString(R.string.customer_countdown_tips);
    }

    @Override
    public void setup(RecyclerHolder holder, CustomerStatusBean.InnerBean bean, int position) {
        holder.setText(R.id.tv_upload_date, obtain2(mTipsUploadDate, wrap(bean.nextFollowupDate))); //上传方案时间
        holder.setText(R.id.tv_source, obtain(mTipsSource, bean.source, false)); //来源
        holder.setText(R.id.tv_designer, obtain(mTipsDesigner, wrap(bean.designer), false)); //设计师
        holder.setText(R.id.tv_reservation, obtain2(mTipsReservation, wrap(bean.measureAppConfirmTime))); //确图
        if (TextUtils.isEmpty(bean.customerProtectTime)) {
            holder.setVisibility(R.id.tv_countdown, View.GONE);
        } else {
            holder.setText(R.id.tv_countdown, obtain2(mTipsCountdown, bean.customerProtectTime));
            holder.setVisibility(R.id.tv_countdown, View.VISIBLE);
        }
    }

    @Override
    public int bindLayoutResId() {
        return R.layout.item_rv_customer_status_sign_v2;
    }
}
