package com.holike.crm.fragment.message;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import com.holike.crm.R;
import com.holike.crm.activity.homepage.MessageV2Activity;
import com.holike.crm.activity.message.MessageDetailsActivity;
import com.holike.crm.bean.MessageBean;
import com.holike.crm.util.Constants;
import com.scwang.smartrefresh.header.WaterDropHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.BallPulseFooter;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.umeng.analytics.MobclickAgent;

import butterknife.BindView;

/**
 * Created by wqj on 2018/3/8.
 * 公告列表
 */
@Deprecated
public class AnnounceFragment extends MessageFragment {
    @BindView(R.id.srl_system)
    SmartRefreshLayout srlSystem;
    @BindView(R.id.rv_announcement)
    RecyclerView rvAnnouncement;

    private MessageBean messageBean;

    @Override
    protected int setContentViewId() {
        return R.layout.fragment_message_system;
    }

    @Override
    protected void init() {
        mPresenter.setAdapter(rvAnnouncement, false);
        srlSystem.setRefreshHeader(new WaterDropHeader(mContext));
        srlSystem.setRefreshFooter(new BallPulseFooter(mContext));
        srlSystem.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshlayout) {
                LoadMore();
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshlayout) {
                refresh(false);
            }
        });
        loadAnnouncement(true);
    }

    /**
     * 加载更多
     */
    private void LoadMore() {
        loadType = 2;
        loadAnnouncement(false);
    }

    /**
     * 刷新
     */
    protected void refresh(boolean showLoading) {
        pageNo = 1;
        loadType = 1;
        loadAnnouncement(showLoading);
    }

    private void loadAnnouncement(boolean showLoading) {
        if (showLoading) {
            showLoading();
        }
        mPresenter.getAnnouncement(String.valueOf(pageNo));
    }

    /**
     * 重新加载
     */
    @Override
    protected void reload() {
        super.reload();
        loadAnnouncement(true);
    }

    /**
     * 获取公告成功
     */
    @Override
    public void getAnnouncementSuccess(MessageBean messageBean) {
        dismissLoading();
        finishLoad();
        switch (loadType) {
            case 0:
            case 1:
                refreshSuccess(messageBean);
                break;
            case 2:
                LoadMoreSuccess(messageBean);
                break;
        }
        pageNo++;
    }

    /**
     * 加载更多成功
     */
    private void LoadMoreSuccess(MessageBean messageBean) {
        if (isContainList(messageBean)) {
            srlSystem.setEnableLoadMore(true);
            mPresenter.onLoadMoreCompleted(messageBean.getMessageList());
        } else {
            srlSystem.setEnableLoadMore(false);
        }
    }

    /**
     * 刷新成功
     */
    protected void refreshSuccess(MessageBean messageBean) {
//        ((MessageV2Activity) mContext).showUnreadMsg(messageBean.getNoticeRead(), messageBean.getAnnouncementRead());
        srlSystem.setEnableLoadMore(true);
        this.messageBean = messageBean;
        if (isContainList(messageBean)) {
            if (srlSystem.getVisibility() != View.VISIBLE) {
                srlSystem.setVisibility(View.VISIBLE);
            }
            hasData();
            mPresenter.onRefreshCompleted(messageBean.getMessageList());
        } else {
            noData(R.drawable.no_notice, R.string.tips_nonotice, false);
        }
    }

    @Override
    protected void hasData() {
        super.hasData();
        rvAnnouncement.setVisibility(View.VISIBLE);
    }

    /**
     * 获取公告失败
     */
    @Override
    public void getAnnouncementFailed(String failed) {
        dismissLoading();
        finishLoad();
        onLoadFailed(failed);
    }

    private void finishLoad() {
        srlSystem.finishRefresh();
        srlSystem.finishLoadMore();
    }

    /**
     * 打开公告
     */
    @Override
    public void openMessage(MessageBean.MessageListBean messageListBean) {
        if (messageListBean == null) return;
        int msgRedCount = messageBean.getAnnouncementRead() - 1 > -1 ? messageBean.getAnnouncementRead() - 1 : 0;
        messageBean.setAnnouncementRead(msgRedCount);
//        ((MessageV2Activity) mContext).showUnreadMsg(messageBean.getNoticeRead(), msgRedCount);
        startMessageDetailsActivity(this, messageListBean.getMessageId(), REQUEST_CODE);
    }

    /**
     * 打开公告详情
     */
    public static void startMessageDetailsActivity(Fragment fragment, String messageId, int requestCode) {
        if (fragment.getActivity() == null) return;
        MobclickAgent.onEvent(fragment.getActivity(), "message_announce_details");
        Intent intent = new Intent(fragment.getActivity(), MessageDetailsActivity.class);
        intent.putExtra(Constants.MESSAGE_ID, messageId);
        fragment.startActivityForResult(intent, requestCode);
    }
}
