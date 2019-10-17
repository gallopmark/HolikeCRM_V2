package com.holike.crm.adapter.customer;

import android.content.Context;

import com.holike.crm.R;
import com.holike.crm.bean.CustomerStatusBean;
import com.holike.crm.bean.MultiItem;

import java.util.List;

/**
 * Created by gallop on 2019/9/3.
 * Copyright holike possess 2019.
 * 已收订金客户
 */
public class CustomerDepositAdapter extends CustomerStatusListAdapter {
    private String mTipsPayTime, mTipsSource, mTipsDepositReceived, mTipsStatus, mTipsGuide;

    public CustomerDepositAdapter(Context context, List<MultiItem> mDatas) {
        super(context, mDatas);
        mTipsPayTime = mContext.getString(R.string.latest_payment_tips);
        mTipsSource = mContext.getString(R.string.customer_source_tips);
        mTipsDepositReceived = mContext.getString(R.string.followup_deposit_received_tips);
        mTipsStatus = mContext.getString(R.string.customer_status_tips);
        mTipsGuide = mContext.getString(R.string.customer_guide_tips);
    }

    @Override
    public void setup(RecyclerHolder holder, CustomerStatusBean.InnerBean bean, int position) {
        holder.setText(R.id.tv_latest_payment, obtain(mTipsPayTime, wrap(bean.payTime), false)); //最新收款时间
        holder.setText(R.id.tv_source, obtain(mTipsSource, wrap(bean.source), false)); //来源
        holder.setText(R.id.tv_deposit_received, obtain3(mTipsDepositReceived, bean.sum)); //已收订金
        holder.setText(R.id.tv_status, obtain(mTipsStatus, wrap(bean.statusCode), false)); //状态
        holder.setText(R.id.tv_guide, obtain(mTipsGuide, wrap(bean.salesName), false)); //导购
    }

    @Override
    public int bindLayoutResId() {
        return R.layout.item_rv_customer_status_deposit_v2;
    }
}
