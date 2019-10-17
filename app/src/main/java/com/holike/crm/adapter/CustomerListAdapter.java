package com.holike.crm.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.gallopmark.recycler.adapterhelper.CommonAdapter;
import com.holike.crm.R;
import com.holike.crm.bean.CustomerListBeanV2;
import com.holike.crm.bean.MultiItem;

import java.util.List;

/*客户列表适配器*/
public class CustomerListAdapter extends CommonAdapter<MultiItem> {

    private String mTipsTime, mTipsFollowup, mTipsName,
            mTipsPhone, mWxTips, mTipsAddress, mTipsIntention,
            mTipsDeposit, mTipsSource, mTipsStatus,
            mTipsAssign, mTipsEmpty;

    private long mTotalRows;
    private boolean mDesc = true;

    public CustomerListAdapter(Context context, List<MultiItem> mDatas) {
        super(context, mDatas);
        mTipsTime = context.getString(R.string.create_time_tips);
        mTipsFollowup = context.getString(R.string.latest_followup);
        mTipsName = context.getString(R.string.customer_name_tips);
        mTipsPhone = context.getString(R.string.customer_phone_tips);
        mWxTips = context.getString(R.string.customer_wechat_tips);
        mTipsAddress = context.getString(R.string.customer_address_tips);
        mTipsIntention = context.getString(R.string.customer_intention_tips);
        mTipsDeposit = context.getString(R.string.customer_deposit_tips);
        mTipsSource = context.getString(R.string.customer_source_tips);
        mTipsStatus = context.getString(R.string.customer_status_tips);
        mTipsAssign = context.getString(R.string.receive_deposit_click_assign_shop);
        mTipsEmpty = context.getString(R.string.not_filled_in);
    }

    public void setDesc(boolean isDesc) {
        this.mDesc = isDesc;
    }

    public void setTotalRows(long totalRows) {
        this.mTotalRows = totalRows;
    }

    @Override
    public void onBindHolder(RecyclerHolder holder, MultiItem multiItem, int position) {
        if (holder.getItemViewType() == 1) {
            CustomerListBeanV2.CustomerBean bean = (CustomerListBeanV2.CustomerBean) multiItem;
            if (mDesc) { //升序
                holder.setText(R.id.tv_count, String.valueOf(mTotalRows - position));
            } else { //降序
                holder.setText(R.id.tv_count, String.valueOf(position + 1));
            }
            holder.setText(R.id.tv_createTime, obtain(mTipsTime, bean.createDate, false));
            holder.setText(R.id.tv_followup, obtain(mTipsFollowup, getDefaultText(bean.lastFollowTime), false));
            holder.setText(R.id.tv_name, obtain(mTipsName, getDefaultText(bean.userName), false));
            TextView tvPhone = holder.obtainView(R.id.tv_phone);
            if (!TextUtils.isEmpty(bean.phoneNumber)) { //显示手机号
                tvPhone.setText(obtain(mTipsPhone, bean.phoneNumber, true));
            } else if (!TextUtils.isEmpty(bean.wxNumber)) {//显示微信
                tvPhone.setText(obtain(mWxTips, bean.wxNumber, false));
            } else {
                tvPhone.setText(obtain(mWxTips, mTipsEmpty, false));
            }
            holder.setText(R.id.tv_address, obtain(mTipsAddress, getDefaultText(bean.address), false));
            holder.setText(R.id.tv_intention, obtain(mTipsIntention, getDefaultText(bean.intentionLevel), false));
            holder.setText(R.id.tv_deposit, obtain(mTipsDeposit, getDefaultText(bean.deposit), false));
            holder.setText(R.id.tv_source, obtain(mTipsSource, bean.source, false));
            if (TextUtils.isEmpty(bean.shopId)) {
                holder.setText(R.id.tv_status, obtain(mTipsStatus, mTipsAssign, true));
            } else {
                holder.setText(R.id.tv_status, obtain(mTipsStatus, bean.status, false));
            }
            holder.bindChildClick(R.id.tv_phone);
            holder.bindChildLongClick(R.id.tv_phone);
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
        int flag = SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE;
        if (!isPressed) {
            ss.setSpan(new ForegroundColorSpan(ContextCompat.getColor(mContext, R.color.textColor4)), start, end, flag);
        } else {
            ss.setSpan(new ForegroundColorSpan(ContextCompat.getColor(mContext, R.color.textColor14)), start, end, flag);
        }
        ss.setSpan(new StyleSpan(Typeface.BOLD), start, end, flag);
        return ss;
    }

    @Override
    public int getItemViewType(int position) {
        return mDatas.get(position).getItemType();
    }

    @Override
    protected int bindView(int viewType) {
        if (viewType == 1) {
            return R.layout.item_rv_customer_list;
        }
        return R.layout.item_nomore_data;
    }
}
