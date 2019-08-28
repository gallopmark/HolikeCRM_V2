package com.holike.crm.fragment.message;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.NonNull;

import com.gallopmark.recycler.adapterhelper.CommonAdapter;
import com.holike.crm.R;
import com.holike.crm.activity.customer.CustomerDetailV2Activity;
import com.holike.crm.base.BaseActivity;
import com.holike.crm.bean.MessageResultBean;
import com.holike.crm.dialog.MaterialDialog;

import java.util.List;

/**
 * Created by gallop on 2019/8/21.
 * Copyright holike possess 2019.
 * 待处理时间
 */
public class PendingMsgFragment extends IMessageFragment {

    private int mCurrentIndex = -1;

    @Override
    String getType() {
        return "1";
    }

    @NonNull
    @Override
    CommonAdapter<?> newAdapter() {
        return new PendingMsgAdapter(mContext, mMessageList);
    }

    @Override
    void onItemClick(MessageResultBean.MessageBean bean, int position) {
        CustomerDetailV2Activity.open((BaseActivity) mContext, bean.personalId, bean.messageId);
    }

    class PendingMsgAdapter extends CommonAdapter<MessageResultBean.MessageBean> {

        PendingMsgAdapter(Context context, List<MessageResultBean.MessageBean> mDatas) {
            super(context, mDatas);
        }

        @Override
        protected int bindView(int viewType) {
            return R.layout.item_message_pending;
        }

        @Override
        public void onBindHolder(RecyclerHolder holder, MessageResultBean.MessageBean bean, int position) {
            holder.setVisibility(R.id.iv_isRead, !bean.isRead());
            if (bean.isRead()) {
                holder.setVisibility(R.id.iv_isRead, View.INVISIBLE);
            } else {
                holder.setVisibility(R.id.iv_isRead, View.VISIBLE);
            }
            holder.setVisibility(R.id.ll_show_layout, bean.isShow());
            holder.itemView.setSelected(bean.isClick());
            if (bean.isClick()) {
                holder.setTextColorRes(R.id.tv_refuse, R.color.colorAccent);
                holder.setTextColorRes(R.id.tv_agree, R.color.color_while);
                holder.setBackgroundResource(R.id.tv_refuse, R.drawable.bg_clickable2_selector);
                holder.setBackgroundResource(R.id.tv_agree, R.drawable.bg_clickable_selector);
            } else {
                holder.setTextColorRes(R.id.tv_refuse, R.color.textColor21);
                holder.setTextColorRes(R.id.tv_agree, R.color.textColor21);
                holder.setBackgroundResource(R.id.tv_refuse, R.drawable.bg_corners4dp_f3);
                holder.setBackgroundResource(R.id.tv_agree, R.drawable.bg_corners4dp_f3);
            }
            if (TextUtils.isEmpty(bean.getType())) {
                holder.setVisibility(R.id.tv_type, View.GONE);
            } else {
                holder.setText(R.id.tv_type, bean.getType());
                holder.setVisibility(R.id.tv_type, View.VISIBLE);
            }
            holder.setText(R.id.tv_title, bean.wrapTitle());
            holder.setText(R.id.tv_date, bean.date);
            holder.setText(R.id.tv_time, bean.time);

            holder.setOnClickListener(R.id.tv_refuse, view -> {
                if (bean.isClick()) {
                    redistribute(position, bean, "disAgree");//拒绝分配
                }
            });
            holder.setOnClickListener(R.id.tv_agree, view -> {
                if (bean.isClick()) {
                    redistribute(position, bean, "agree");  //同意分配
                }
            });
        }
    }

    private void redistribute(int position, MessageResultBean.MessageBean bean, String type) {
        mCurrentIndex = position;
        MaterialDialog.Builder builder = new MaterialDialog.Builder(mContext);
        builder.title(R.string.dialog_title_assign_customer);
        String name = TextUtils.isEmpty(bean.name) ? "" : bean.name;
        if (TextUtils.equals(type, "disAgree")) {
            builder.message(mContext.getString(R.string.dialog_message2_assign_customer) + name);
        } else {
            builder.message(mContext.getString(R.string.dialog_message1_assign_customer) + name);
        }
        builder.negativeButton(R.string.cancel, null);
        builder.positiveButton(R.string.confirm, (dialogInterface, i) -> {
            dialogInterface.dismiss();
            mPresenter.redistribute(bean.messageId, type);
        });
        builder.show();
    }

    @Override
    public void onRedistributeSuccess(String message) {
        super.onRedistributeSuccess(message);
        if (mCurrentIndex >= 0 && mCurrentIndex < mMessageList.size()) {
            mMessageList.get(mCurrentIndex).setUnClickable();
            mAdapter.notifyDataSetChanged();
        }
    }
}
