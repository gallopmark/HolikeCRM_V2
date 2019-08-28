package com.holike.crm.adapter.customer;

import android.content.Context;

import com.holike.crm.R;
import com.holike.crm.adapter.CustomerStatusListAdapter;
import com.holike.crm.bean.CustomerStatusBean;
import com.holike.crm.bean.MultiItem;

import java.util.List;

/**
 * Created by gallop 2019/7/8
 * Copyright (c) 2019 holike
 * 待收全款客户（客户列表）
 */
public class CustomerCollectMoneyAdapter extends CustomerStatusListAdapter {
    private String mTipsSignDate, mTipsSource, mTipsStatus,
            mTipsSignMoney, mTipsTail, mTipsGuide, mTipsDesigner;

    public CustomerCollectMoneyAdapter(Context context, List<MultiItem> mDatas) {
        super(context, mDatas);
        mTipsSignDate = context.getString(R.string.customer_date_of_signing_tips);
        mTipsSource = context.getString(R.string.customer_source_tips);
        mTipsStatus = context.getString(R.string.customer_status_tips);
        mTipsSignMoney = context.getString(R.string.customer_sign_money_tips);
        mTipsTail = context.getString(R.string.customer_unfinished_payment_tips);
        mTipsGuide = context.getString(R.string.customer_guide_tips);
        mTipsDesigner = context.getString(R.string.followup_designer);
    }

    @Override
    public void setup(RecyclerHolder holder, CustomerStatusBean.InnerBean bean, int position) {
        holder.setText(R.id.tv_signDate, obtain2(mTipsSignDate, wrap(bean.contractDate))); //签约日期
        holder.setText(R.id.tv_source, obtain(mTipsSource, wrap(bean.source), false)); //来源
        holder.setText(R.id.tv_status, obtain(mTipsStatus, wrap(bean.statusCode), false)); //状态
        holder.setText(R.id.tv_signMoney, obtain(mTipsSignMoney, wrap(bean.salesAmount), false)); //签约金额
        holder.setText(R.id.tv_tail, obtain2(mTipsTail, wrap(bean.lastRemaining))); //未收尾款
        holder.setText(R.id.tv_guide, obtain(mTipsGuide, wrap(bean.salesName), false)); //导购
        holder.setText(R.id.tv_designer, obtain(mTipsDesigner, wrap(bean.designer), false)); //设计师
    }

    @Override
    public int bindLayoutResId() {
        return R.layout.item_rv_customer_status_collect_money_v2;
    }
}
