package com.holike.crm.fragment.message;

import android.content.Context;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.NonNull;

import com.gallopmark.recycler.adapterhelper.CommonAdapter;
import com.holike.crm.R;
import com.holike.crm.activity.message.MessageDetailsActivity;
import com.holike.crm.bean.MessageResultBean;

import java.util.List;

/**
 * Created by pony on 2019/8/21.
 * Copyright holike possess 2019.
 * 公告列表
 */
public class AnnouncementFragment extends IMessageFragment {
    @Override
    String getType() {
        return "2";
    }

    @NonNull
    @Override
    CommonAdapter<?> newAdapter() {
        return new AnnouncementAdapter(mContext, mMessageList);
    }

    @Override
    void onItemClick(MessageResultBean.MessageBean bean, int position) {
        MessageDetailsActivity.open(this, bean.messageId, REQUEST_CODE);
    }

    static class AnnouncementAdapter extends CommonAdapter<MessageResultBean.MessageBean> {

        String mPublisherTips;

        AnnouncementAdapter(Context context, List<MessageResultBean.MessageBean> mDatas) {
            super(context, mDatas);
            mPublisherTips = context.getString(R.string.message_publisher);
        }

        @Override
        protected int bindView(int viewType) {
            return R.layout.item_message_announcement;
        }

        @Override
        public void onBindHolder(RecyclerHolder holder, MessageResultBean.MessageBean bean, int position) {
            if (bean.isRead()) {  //已读
                holder.setVisibility(R.id.iv_isRead, View.INVISIBLE);
                holder.setTextColorRes(R.id.tv_title, R.color.textColor8);
                holder.setTypeface(R.id.tv_title, Typeface.defaultFromStyle(Typeface.NORMAL));
                holder.setTextColorRes(R.id.tv_date, R.color.textColor8);
                holder.setTextColorRes(R.id.tv_time, R.color.textColor8);
                holder.setTextColorRes(R.id.tv_publisher, R.color.textColor8);
            } else { //未读
                holder.setVisibility(R.id.iv_isRead, View.VISIBLE);
                holder.setTextColorRes(R.id.tv_title, R.color.textColor4);
                holder.setTypeface(R.id.tv_title, Typeface.defaultFromStyle(Typeface.BOLD));
                holder.setTextColorRes(R.id.tv_date, R.color.textColor4);
                holder.setTextColorRes(R.id.tv_time, R.color.textColor4);
                holder.setTextColorRes(R.id.tv_publisher, R.color.textColor4);
            }
            holder.setText(R.id.tv_title, bean.title);
            holder.setText(R.id.tv_date, bean.date);
            holder.setText(R.id.tv_time, bean.time);
            String publisher = mPublisherTips;
            if (!TextUtils.isEmpty(bean.describe)) {
                publisher += bean.describe;
            }
            holder.setText(R.id.tv_publisher, publisher);
        }
    }
}
