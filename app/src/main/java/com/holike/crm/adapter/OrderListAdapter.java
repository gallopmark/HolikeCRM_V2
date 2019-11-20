package com.holike.crm.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.gallopmark.recycler.adapterhelper.CommonAdapter;
import com.holike.crm.R;
import com.holike.crm.bean.MultiItem;
import com.holike.crm.bean.OrderListBean;
import com.holike.crm.util.DensityUtil;
import com.holike.crm.util.LogCat;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by pony 2019/7/4
 * Copyright (c) 2019 holike
 * 订单列表适配器
 */
public class OrderListAdapter extends CommonAdapter<MultiItem> {

    private String mTipsType, mTipsStatus, mTipsNumber,
            mTipsName, mTipsPhone, mTipsAddress,
            mTipsSpace, mTipsTime, mTipsEmpty;
    private int mMinWidth;  //下单日期textView最小宽度（为了显示电话的TextView能与其左对齐并且文本能显示完全）

    public OrderListAdapter(Context context, List<MultiItem> mDatas) {
        super(context, mDatas);
        mTipsType = context.getString(R.string.order_center_type);
        mTipsStatus = context.getString(R.string.order_center_state);
        mTipsNumber = context.getString(R.string.order_number2_tips);
        mTipsName = context.getString(R.string.order_name_tips);
        mTipsPhone = context.getString(R.string.order_phone_tips);
        mTipsAddress = context.getString(R.string.order_address_tips);
        mTipsSpace = context.getString(R.string.order_space_tips);
        mTipsTime = context.getString(R.string.order_date_tips);

        mTipsEmpty = context.getString(R.string.not_filled_in);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        mMinWidth = getTextWidth(mTipsTime + format.format(new Date()));
    }

    private int getTextWidth(String text) {
        TextPaint paint = new TextPaint();
        paint.setTextSize(mContext.getResources().getDimensionPixelSize(R.dimen.textSize_13));
        return (int) (paint.measureText(text) + mContext.getResources().getDimensionPixelSize(R.dimen.dp_10));
    }

    @Override
    public void onBindHolder(RecyclerHolder holder, MultiItem multiItem, int position) {
        if (holder.getItemViewType() == 1) {
            OrderListBean bean = (OrderListBean) multiItem;
            holder.setText(R.id.tv_orderType, obtain(mTipsType, bean.getOrderTypeName(), false));
            holder.setText(R.id.tv_orderStatus, obtain(mTipsStatus, bean.getOrderStatusName(), false));
            holder.setText(R.id.tv_orderNumber, obtain(mTipsNumber, bean.getOrderId(), false));
            holder.setText(R.id.tv_name, obtain(mTipsName, getDefaultText(bean.getName()), false));
            holder.setText(R.id.tv_phone, obtain(mTipsPhone, bean.getPhone(), true));
            holder.setText(R.id.tv_address, obtain(mTipsAddress, getDefaultText(bean.getVillage()), false));
            holder.setText(R.id.tv_space, obtain(mTipsSpace, getDefaultText(bean.getSpaceRoom()), false));
            TextView tvDate = holder.obtainView(R.id.tv_orderTime);
            tvDate.setMinWidth(mMinWidth);
            tvDate.setText(obtain(mTipsTime, getDefaultText(bean.getCreateDate()), false));
            holder.bindChildClick(R.id.tv_phone);
            holder.bindChildLongClick(R.id.tv_phone);
            holder.bindChildLongClick(R.id.tv_orderNumber);
        } else {
            holder.setText(R.id.mNoMoreTextView, mContext.getString(R.string.list_no_more));
        }
    }

    private String getDefaultText(String content) {
        return TextUtils.isEmpty(content) ? mTipsEmpty : content;
    }

    private SpannableString obtain(String origin, String content, boolean isPressed) {
        int start = origin.length();
        String source = origin + (TextUtils.isEmpty(content) ? "" : content);
        int end = source.length();
        SpannableString ss = new SpannableString(source);
        if (!isPressed) {
            ss.setSpan(new ForegroundColorSpan(ContextCompat.getColor(mContext, R.color.textColor4)), start, end, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
        } else {
            ss.setSpan(new ForegroundColorSpan(ContextCompat.getColor(mContext, R.color.textColor14)), start, end, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        ss.setSpan(new StyleSpan(Typeface.BOLD), start, end, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
        return ss;
    }

    @Override
    public int getItemViewType(int position) {
        return mDatas.get(position).getItemType();
    }

    @Override
    protected int bindView(int viewType) {
        if (viewType == 1) return R.layout.item_rv_order_centerv2;
        return R.layout.item_nomore_data;
    }
}
