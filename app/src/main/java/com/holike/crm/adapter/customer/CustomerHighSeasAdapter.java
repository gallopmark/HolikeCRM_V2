package com.holike.crm.adapter.customer;

import android.content.Context;

import com.holike.crm.R;
import com.holike.crm.bean.CustomerStatusBean;
import com.holike.crm.bean.MultiItem;

import java.util.List;

/**
 * Created by pony on 2019/7/11.
 * Copyright holike possess 2019.
 * 公海客户列表
 */
public class CustomerHighSeasAdapter extends CustomerStatusListAdapter {

    private String mTipsHighSeasType;

    public CustomerHighSeasAdapter(Context context, List<MultiItem> mDatas) {
        super(context, mDatas);
        mTipsHighSeasType = context.getString(R.string.customer_high_seas_type_tips);
    }

    @Override
    public void setup(RecyclerHolder holder, CustomerStatusBean.InnerBean bean, int position) {
        holder.setText(R.id.tv_createTime, obtain(mTipsTime, wrap(bean.createDate), false)); //创建日期
        holder.setText(R.id.tv_highSeasType, obtain(mTipsHighSeasType, wrap(bean.highSeasType), false)); //公海分类
        holder.setText(R.id.tv_followup, obtain(mTipsFollowup, wrap(bean.lastFollowTime), false)); //最新跟进
    }

    @Override
    public int bindLayoutResId() {
        return R.layout.item_rv_customer_status_highseas_v2;
    }
}
