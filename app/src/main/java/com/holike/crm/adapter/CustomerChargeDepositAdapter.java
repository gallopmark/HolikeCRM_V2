package com.holike.crm.adapter;

import android.content.Context;

import com.gallopmark.recycler.adapterhelper.CommonAdapter;
import com.holike.crm.R;
import com.holike.crm.bean.MultiItem;
import com.holike.crm.helper.TextSpanHelper;

import java.util.List;

/**
 * Created by pony on 2019/9/4.
 * Copyright holike possess 2019.
 * 收取订金 客户列表
 */
public class CustomerChargeDepositAdapter extends CommonAdapter<MultiItem> {
    private String mNameTips, mPhoneTips, mWxTips, mAddressTips;
    private TextSpanHelper mTextHelper;

    public CustomerChargeDepositAdapter(Context context, List<MultiItem> mDatas) {
        super(context, mDatas);
        mTextHelper = TextSpanHelper.from(mContext);
        mNameTips = mContext.getString(R.string.customer_name_tips);
        mPhoneTips = mContext.getString(R.string.customer_phone_tips);
        mWxTips = mContext.getString(R.string.customer_wechat_tips);
        mAddressTips = mContext.getString(R.string.customer_address_tips);
    }

    @Override
    public int getItemViewType(int position) {
        return mDatas.get(position).getItemType();
    }

    @Override
    protected int bindView(int viewType) {
        if (viewType == 1)
            return R.layout.item_customer_charge_deposit;
        return R.layout.item_nomore_data;
    }

    @Override
    public void onBindHolder(RecyclerHolder holder, MultiItem item, int position) {
        if (holder.getItemViewType() == 1) {

        } else {
            holder.setText(R.id.mNoMoreTextView, mContext.getString(R.string.list_no_more));
        }
    }
}
