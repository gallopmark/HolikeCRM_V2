package com.holike.crm.fragment.message;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;

import com.holike.crm.R;
import com.holike.crm.activity.customer.CustomerDetailV2Activity;
import com.holike.crm.activity.homepage.OrderDetailsActivity;
import com.holike.crm.base.BaseActivity;
import com.holike.crm.bean.MessageBean;
import com.holike.crm.util.Constants;
import com.scwang.smartrefresh.header.WaterDropHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.BallPulseFooter;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import butterknife.BindView;

/**
 * Created by wqj on 2018/3/8.
 * 通知列表
 */
@Deprecated
public class NotifyFragment extends MessageFragment {
    @BindView(R.id.mRefreshLayout)
    SmartRefreshLayout mRefreshLayout;
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;

    @Override
    protected int setContentViewId() {
        return R.layout.fragment_message_notify;
    }

    @Override
    protected void init() {
        mPresenter.setAdapter(mRecyclerView, true);
        mRefreshLayout.setRefreshHeader(new WaterDropHeader(mContext));
        mRefreshLayout.setRefreshFooter(new BallPulseFooter(mContext));
        mRefreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshlayout) {
                loadmore();
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshlayout) {
                refresh(false);
            }
        });
        loadNotify(true);
    }

    /**
     * 加载更多
     */
    protected void loadmore() {
        loadType = 2;
        mPresenter.getGetNotity(String.valueOf(pageNo));
    }

    /**
     * 刷新
     */
    protected void refresh(boolean showLoading) {
        pageNo = 1;
        loadType = 1;
        loadNotify(showLoading);
    }

    private void loadNotify(boolean showLoading) {
        if (showLoading) {
            showLoading();
        }
        mPresenter.getGetNotity(String.valueOf(pageNo));
    }

    /**
     * 重新加载
     */
    @Override
    protected void reload() {
        super.reload();
        loadNotify(true);
    }

    /**
     * 获取通知列表成功
     */
    @Override
    public void getNotifySuccess(MessageBean messageBean) {
        dismissLoading();
        finishLoad();
        switch (loadType) {
            case 0:
            case 1:
                refreshSuccess(messageBean);
                break;
            case 2:
                loadmoreSuccess(messageBean);
                break;
        }
        pageNo++;
    }

    /**
     * 加载更多成功
     */
    protected void loadmoreSuccess(MessageBean messageBean) {
        if (isContainList(messageBean)) {
            mRefreshLayout.setEnableLoadMore(true);
            mPresenter.onLoadMoreCompleted(messageBean.getMessageList());
        } else {
            mRefreshLayout.setEnableLoadMore(false);
        }
    }

    /**
     * 刷新成功
     */
    protected void refreshSuccess(MessageBean messageBean) {
//        ((MessageV2Activity) mContext).showUnreadMsg(messageBean.getNoticeRead(), messageBean.getAnnouncementRead());
        mRefreshLayout.setEnableLoadMore(true);
        this.messageBean = messageBean;
        if (isContainList(messageBean)) {
            if (mRefreshLayout.getVisibility() != View.VISIBLE) {
                mRefreshLayout.setVisibility(View.VISIBLE);
            }
            hasData();
            mPresenter.onRefreshCompleted(messageBean.getMessageList());
        } else {
            noData(R.drawable.no_notice, R.string.tips_nonotice, false);
        }
    }

    private void finishLoad() {
        mRefreshLayout.finishRefresh();
        mRefreshLayout.finishLoadMore();
    }

    /**
     * 获取通知失败
     */
    @Override
    public void getNotifyFailed(String failed) {
        dismissLoading();
        finishLoad();
        onLoadFailed(failed);
    }

    /**
     * 打开通知
     */
    @Override
    public void openMessage(MessageBean.MessageListBean messageListBean) {
        if (messageListBean == null) return;
//        ((MessageV2Activity) mContext).showUnreadMsg(messageBean.getNoticeRead(), messageBean.getAnnouncementRead());
//        if (TextUtils.isEmpty(messageListBean.getOrderId())) {
        CustomerDetailV2Activity.open((BaseActivity) mContext, messageListBean.getPersonalId(), messageListBean.getMessageId());
//        } else {
//            startOrderDetails(this, messageListBean.getOrderId(), messageListBean.getMessageId(), REQUEST_CODE);
//
//        }
    }

    /**
     * 打开订单详情
     */
    @Deprecated
    public static void startOrderDetails(Fragment fragment, String orderId, String messageId, int requestCode) {
        if (fragment.getActivity() == null) return;
        Intent intent = new Intent(fragment.getActivity(), OrderDetailsActivity.class);
        intent.putExtra(Constants.ORDER_ID, orderId);
        intent.putExtra(Constants.MESSAGE_ID, messageId);
        fragment.startActivityForResult(intent, requestCode);
    }
}
