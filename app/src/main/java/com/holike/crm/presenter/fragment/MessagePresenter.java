package com.holike.crm.presenter.fragment;

import android.content.Context;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gallopmark.recycler.adapterhelper.CommonAdapter;
import com.holike.crm.R;
import com.holike.crm.base.BasePresenter;
import com.holike.crm.bean.MessageBean;
import com.holike.crm.model.fragment.MessageModel;
import com.holike.crm.view.fragment.MessageView;

import java.util.ArrayList;
import java.util.List;

public class MessagePresenter extends BasePresenter<MessageView, MessageModel> {

    private List<MessageBean.MessageListBean> messageList = new ArrayList<>();
    private MessageAdapter messageAdapter;

    public class MessageAdapter extends CommonAdapter<MessageBean.MessageListBean> {
        private int mLayoutId;
        private boolean isNotify;

        MessageAdapter(Context context, int layoutId, List<MessageBean.MessageListBean> datas, boolean isNotify) {
            super(context, datas);
            this.mLayoutId = layoutId;
            this.isNotify = isNotify;
        }

        @Override
        protected int bindView(int viewType) {
            return mLayoutId;
        }

        @Override
        public void onBindHolder(RecyclerHolder holder, MessageBean.MessageListBean bean, int position) {
            ImageView ivUnread = holder.obtainView(R.id.iv_item_rv_message_unread);
            TextView tvTitle = holder.obtainView(R.id.tv_item_rv_message_title);
            TextView tvDate = holder.obtainView(R.id.tv_item_rv_message_date);
            TextView tvTime = holder.obtainView(R.id.tv_item_rv_message_time);
            TextView tvPublisher = holder.obtainView(R.id.tv_item_rv_message_publisher);
            tvTitle.setText(bean.getTitle());
            tvDate.setText(bean.getDate());
            tvTime.setText(bean.getTime());
            if (isNotify) {
                tvPublisher.setVisibility(View.GONE);
            } else {
                String publisher = mContext.getString(R.string.message_publisher);
                if (!TextUtils.isEmpty(bean.getDescribe())) publisher += bean.getDescribe();
                tvPublisher.setText(publisher);
                tvPublisher.setVisibility(View.VISIBLE);
            }
            if (TextUtils.equals(bean.getIsRead(), "0")) {
                ivUnread.setVisibility(View.VISIBLE);
                tvTitle.setTextColor(ContextCompat.getColor(mContext, R.color.textColor4));
            } else {
                ivUnread.setVisibility(View.INVISIBLE);
                tvTitle.setTextColor(ContextCompat.getColor(mContext, R.color.textColor11));
            }
            holder.itemView.setOnClickListener(v -> {
                bean.setIsRead("1");
                notifyDataSetChanged();
                if (getView() != null)
                    getView().openMessage(bean);
            });
        }

    }

    public void setAdapter(RecyclerView recyclerView, boolean isNotify) {
        messageAdapter = new MessageAdapter(recyclerView.getContext(), R.layout.item_rv_message, messageList, isNotify);
        recyclerView.setAdapter(messageAdapter);
    }

    public void onRefreshCompleted(List<MessageBean.MessageListBean> beans) {
        this.messageList.clear();
        onLoadMoreCompleted(beans);
    }

    public void onLoadMoreCompleted(List<MessageBean.MessageListBean> beans) {
        this.messageList.addAll(beans);
        messageAdapter.notifyDataSetChanged();
    }

    public void clearData() {
        this.messageList.clear();
        messageAdapter.notifyDataSetChanged();
    }

    /**
     * 获取通知
     */
    public void getGetNotity(String pageNo) {
        model.getMessage(pageNo, "10", "1", new MessageModel.GetMessageListener() {
            @Override
            public void success(MessageBean messageBean) {
                if (getView() != null)
                    getView().getNotifySuccess(messageBean);
            }

            @Override
            public void failed(String failed) {
                if (getView() != null)
                    getView().getNotifyFailed(failed);
            }
        });
    }

    /**
     * 获取公告
     */
    public void getAnnouncement(String pageNo) {
        model.getMessage(pageNo, "10", "2", new MessageModel.GetMessageListener() {
            @Override
            public void success(MessageBean messageBean) {
                if (getView() != null)
                    getView().getAnnouncementSuccess(messageBean);
            }

            @Override
            public void failed(String failed) {
                if (getView() != null)
                    getView().getAnnouncementFailed(failed);
            }
        });
    }
}
