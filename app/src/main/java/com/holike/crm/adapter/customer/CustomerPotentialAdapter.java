package com.holike.crm.adapter.customer;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;

import com.holike.crm.R;
import com.holike.crm.bean.CustomerStatusBean;
import com.holike.crm.bean.MultiItem;

import java.util.List;

/**
 * Created by gallop 2019/7/9
 * Copyright (c) 2019 holike
 * 潜在客户列表适配器
 */
public class CustomerPotentialAdapter extends CustomerStatusListAdapter {

    private String mIntentTips, mGuideTips, mNextFollowupTips, mCountdownTips;

    public CustomerPotentialAdapter(Context context, List<MultiItem> mDatas) {
        super(context, mDatas);
        mIntentTips = context.getString(R.string.customer_intention_tips);
        mGuideTips = context.getString(R.string.customer_guide_tips);
        mNextFollowupTips = context.getString(R.string.customer_followup_tips);
        mCountdownTips = context.getString(R.string.customer_countdown_tips);
    }

    @Override
    public void setup(RecyclerHolder holder, CustomerStatusBean.InnerBean bean, int position) {
        holder.setText(R.id.tv_createTime, obtain(mTipsTime, wrap(bean.createDate), false));
        holder.setText(R.id.tv_intention, obtain(mIntentTips, wrap(bean.intentionLevel), false)); //意向
        holder.setText(R.id.tv_guide, obtain(mGuideTips, wrap(bean.salesName), false)); //导购
        holder.setText(R.id.tv_newest_followup, obtain(mTipsFollowup, wrap(bean.lastFollowTime), false)); //最新跟进
        holder.setText(R.id.tv_followup, obtain(mNextFollowupTips, wrap(bean.nextFollowupDate), false)); //下次跟进
        if (TextUtils.isEmpty(bean.customerProtectTime)) {
            holder.setVisibility(R.id.tv_countdown, View.GONE);
        } else {
            holder.setText(R.id.tv_countdown, obtain2(mCountdownTips, bean.customerProtectTime));
            holder.setVisibility(R.id.tv_countdown, View.VISIBLE);
        }
    }

    @Override
    public int bindLayoutResId() {
        return R.layout.item_rv_customer_status_potential_v2;
    }
}
