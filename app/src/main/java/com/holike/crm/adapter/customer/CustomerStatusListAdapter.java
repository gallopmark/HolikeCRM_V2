package com.holike.crm.adapter.customer;

import android.content.Context;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;

import androidx.annotation.StringRes;
import androidx.core.content.ContextCompat;

import com.gallopmark.recycler.adapterhelper.CommonAdapter;
import com.holike.crm.R;
import com.holike.crm.bean.CustomerStatusBean;
import com.holike.crm.bean.MultiItem;

import java.util.List;

/**
 * Created by pony 2019/7/8
 * Copyright (c) 2019 holike
 */
public abstract class CustomerStatusListAdapter extends CommonAdapter<MultiItem> {
    protected String mTipsTime, mTipsFollowup;
    private String mTipsName, mTipsPhone, mTipsWx, mTipsAddress, mTipsEmpty;
    private long mTotalRows;
    private int mPadding2dp;
    private boolean mDesc = true; //是否是升序

    public CustomerStatusListAdapter(Context context, List<MultiItem> mDatas) {
        super(context, mDatas);
        mTipsTime = context.getString(R.string.create_time_tips);
        mTipsFollowup = context.getString(R.string.latest_followup);
        mTipsName = context.getString(R.string.customer_name_tips);
        mTipsPhone = context.getString(R.string.customer_phone_tips);
        mTipsWx = context.getString(R.string.customer_wechat_tips);
        mTipsAddress = context.getString(R.string.customer_address_tips);
        mTipsEmpty = context.getString(R.string.not_filled_in);
        mPadding2dp = mContext.getResources().getDimensionPixelSize(R.dimen.dp_2);
    }

    public void setTotalRows(long totalRows) {
        this.mTotalRows = totalRows;
    }

    public void setDesc(boolean isDesc) {
        this.mDesc = isDesc;
    }

    @Override
    public void onBindHolder(RecyclerHolder holder, MultiItem multiItem, int position) {
        int itemType = holder.getItemViewType();
        if (itemType == 1) {
            CustomerStatusBean.InnerBean bean = (CustomerStatusBean.InnerBean) multiItem;
            if (mDesc) { //升序
                holder.setText(R.id.tv_count, String.valueOf(mTotalRows - position));
            } else { //降序
                holder.setText(R.id.tv_count, String.valueOf(position + 1));
            }
            holder.setText(R.id.tv_name, obtain(mTipsName, wrap(bean.userName), false));
            if (!TextUtils.isEmpty(bean.phoneNumber)) {  //如果手机号已填
                holder.setText(R.id.tv_phoneWx, obtain(mTipsPhone, bean.phoneNumber, true));
            } else {
                if (!TextUtils.isEmpty(bean.wxNumber)) { //如果微信号已填
                    holder.setText(R.id.tv_phoneWx, obtain(mTipsWx, wrap(bean.wxNumber), false));
                } else {  //手机号和微信号都未填 则展示“手机号未填写”
                    holder.setText(R.id.tv_phoneWx, obtain(mTipsPhone, mTipsEmpty, false));
                }
            }
            holder.setText(R.id.tv_address, obtain(mTipsAddress, wrap(bean.address), false));
            holder.bindChildClick(R.id.tv_phoneWx);
            holder.bindChildLongClick(R.id.tv_phoneWx);
            if (bean.isRed()) { //是否标记红色
                holder.itemView.setBackgroundResource(R.drawable.bg_corners_white_strokered2dp_5dp);
                holder.itemView.setPadding(mPadding2dp, holder.itemView.getPaddingTop(), mPadding2dp, holder.itemView.getPaddingBottom());
            } else {
                holder.itemView.setPadding(0, holder.itemView.getPaddingTop(), 0, holder.itemView.getPaddingBottom());
                holder.itemView.setBackgroundResource(R.drawable.bg_corners_white_5dp);
            }
            setup(holder, bean, position);
        } else {
            holder.setText(R.id.mNoMoreTextView, mContext.getString(R.string.list_no_more));
        }
    }

    public abstract void setup(RecyclerHolder holder, CustomerStatusBean.InnerBean bean, int position);

    public String wrap(String content) {
        return TextUtils.isEmpty(content) ? mTipsEmpty : content;
    }

    public SpannableString obtain(@StringRes int id, String content, boolean isBlue) {
        return obtain(mContext.getString(id), content, isBlue);
    }

    public SpannableString obtain(String origin, String content, boolean isBlue) {
        int start = origin.length();
        String source = origin + (TextUtils.isEmpty(content) ? "" : content);
        int end = source.length();
        SpannableString ss = new SpannableString(source);
        int flag = SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE;
        if (!isBlue) {
            ss.setSpan(new ForegroundColorSpan(ContextCompat.getColor(mContext, R.color.textColor4)), start, end, flag);
        } else {
            ss.setSpan(new ForegroundColorSpan(ContextCompat.getColor(mContext, R.color.textColor14)), start, end, flag);
        }
        ss.setSpan(new StyleSpan(Typeface.BOLD), start, end, flag);
        return ss;
    }

    protected SpannableString obtain2(String origin, String content) {
        int start = 0;
        String source = origin + (TextUtils.isEmpty(content) ? "" : content);
        int end = source.length();
        return obtainRedBold(source, start, end);
    }

    private SpannableString obtainRedBold(String source, int start, int end) {
        SpannableString ss = new SpannableString(source);
        int flag = SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE;
        ss.setSpan(new ForegroundColorSpan(ContextCompat.getColor(mContext, R.color.textColor15)), start, end, flag);
        ss.setSpan(new StyleSpan(Typeface.BOLD), start, end, flag);
        return ss;
    }

    protected SpannableString obtain3(String origin, String content) {
        int start = origin.length();
        String source = origin + (TextUtils.isEmpty(content) ? "" : content);
        int end = source.length();
        return obtainRedBold(source, start, end);
    }

    @Override
    public int getItemViewType(int position) {
        return mDatas.get(position).getItemType();
    }

    @Override
    protected int bindView(int viewType) {
        if (viewType == 1) {
            return bindLayoutResId();
        }
        return R.layout.item_nomore_data;
    }

    public abstract int bindLayoutResId();
}
