package com.holike.crm.adapter.customer;

import android.content.Context;

import com.holike.crm.R;
import com.holike.crm.bean.CustomerStatusBean;
import com.holike.crm.bean.MultiItem;

import java.util.List;

/**
 * Created by pony 2019/7/8
 * Copyright (c) 2019 holike
 * 已下单客户
 */
public class CustomerOrderPlacedAdapter extends CustomerStatusListAdapter {
    private String mTipContractDelivery, mTipsStatus, mTipsTailStatus,
            mTipsGuide, mTipsDesigner;

    public CustomerOrderPlacedAdapter(Context context, List<MultiItem> mDatas) {
        super(context, mDatas);
        mTipContractDelivery = context.getString(R.string.customer_contract_delivery_tips);
        mTipsStatus = context.getString(R.string.customer_status_tips);
        mTipsTailStatus = context.getString(R.string.customer_tail_status_tips);
        mTipsGuide = context.getString(R.string.customer_guide_tips);
        mTipsDesigner = context.getString(R.string.followup_designer);
    }

    @Override
    public void setup(RecyclerHolder holder, CustomerStatusBean.InnerBean bean, int position) {
        holder.setText(R.id.tv_delivery, obtain(mTipContractDelivery, wrap(bean.appDeliveryDate), false)); //约定交货
        holder.setText(R.id.tv_status, obtain(mTipsStatus, wrap(bean.statusCode), false)); //状态
        holder.setText(R.id.tv_tailStatus, obtain3(mTipsTailStatus, wrap(bean.lastRemaining))); //尾款情况
        holder.setText(R.id.tv_guide, obtain(mTipsGuide, wrap(bean.salesName), false)); //导购
        holder.setText(R.id.tv_designer, obtain(mTipsDesigner, wrap(bean.designer), false)); //设计师
    }

    @Override
    public int bindLayoutResId() {
        return R.layout.item_rv_customer_status_orderplaced_v2;
    }
}
