package com.holike.crm.adapter.customer;

import android.content.Context;

import com.holike.crm.R;
import com.holike.crm.bean.CustomerStatusBean;
import com.holike.crm.bean.MultiItem;

import java.util.List;

/**
 * Created by gallop 2019/7/8
 * Copyright (c) 2019 holike
 * 待量房客户(客户列表)
 */
public class CustomerMeasureAdapter extends CustomerStatusListAdapter {
    private String mRulerDateTips, mSourceTips, mRulerTips;

    public CustomerMeasureAdapter(Context context, List<MultiItem> mDatas) {
        super(context, mDatas);
        mRulerDateTips = context.getString(R.string.customer_appointment_ruler_tips);
        mSourceTips = context.getString(R.string.customer_source_tips);
        mRulerTips = context.getString(R.string.customer_ruler_tips);
    }

    @Override
    public void setup(RecyclerHolder holder, CustomerStatusBean.InnerBean bean, int position) {
        holder.setText(R.id.tv_rulerDate, obtain2(mRulerDateTips, wrap(bean.appointmentTime)));
        holder.setText(R.id.tv_source, obtain(mSourceTips, wrap(bean.source), false));
        holder.setText(R.id.tv_ruler, obtain(mRulerTips, wrap(bean.appointMeasureBy), false));
    }

    @Override
    public int bindLayoutResId() {
        return R.layout.item_rv_customer_status_measure_v2;
    }
}
