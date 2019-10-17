package com.holike.crm.fragment.message;

import android.text.TextUtils;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gallopmark.recycler.adapterhelper.BaseRecyclerAdapter;
import com.gallopmark.recycler.adapterhelper.CommonAdapter;
import com.holike.crm.R;
import com.holike.crm.base.MyFragment;
import com.holike.crm.bean.MessageResultBean;
import com.holike.crm.presenter.fragment.MessageV2Presenter;
import com.holike.crm.view.fragment.MessageV2View;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by gallop on 2019/8/21.
 * Copyright holike possess 2019.
 */
public abstract class IMessageFragment extends MyFragment<MessageV2Presenter, MessageV2View>
        implements OnRefreshLoadMoreListener, MessageV2View, BaseRecyclerAdapter.OnItemClickListener {

    @BindView(R.id.smartRefreshLayout)
    SmartRefreshLayout mRefreshLayout;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;

    private MessageResultBean mResultBean;
    List<MessageResultBean.MessageBean> mMessageList;
    private CommonAdapter<?> mAdapter;

    private String mType = "1"; //类型 1待处理事项 2公告
    private int mPageNo = 1; //页码
    private final int mPageSize = 20; //页码大小
    private boolean isLoadCompleted; //加载完成
    private boolean isRefresh; //是否是刷新

    private IMessageRequestCallback mCallback;

    public void setCallback(IMessageRequestCallback callback) {
        this.mCallback = callback;
    }

    @Override
    protected MessageV2Presenter attachPresenter() {
        return new MessageV2Presenter();
    }

    @Override
    protected int setContentViewId() {
        return R.layout.fragment_imessage;
    }

    @Override
    protected void init() {
        mType = getType();
        mMessageList = new ArrayList<>();
        mAdapter = newAdapter();
        mRefreshLayout.setOnRefreshLoadMoreListener(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(this);
        initData(true);
    }

    abstract String getType();

    @NonNull
    abstract CommonAdapter<?> newAdapter();

    private void initData(boolean isShowLoading) {
        if (isShowLoading) {
            showLoading();
        }
        mPresenter.getMessageList(mType, mPageNo, mPageSize);
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        refreshMessage();
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        isRefresh = false;
        initData(false);
    }

    private void refreshMessage() {
        mPageNo = 1;
        isRefresh = true;
        initData(false);
    }

    @Override
    public void onSuccess(MessageResultBean resultBean) {
        dismissLoading();
        finishLoad();
        if (mResultBean == null) {
            mResultBean = resultBean;
        }
        if (mCallback != null) {
            mCallback.onResponse(resultBean);
        }
        List<MessageResultBean.MessageBean> messageList = resultBean.getMessageList();
        if (!isLoadCompleted) {  //是否是第一次加载成功
            if (messageList.isEmpty()) {
                if (TextUtils.equals(mType, "1")) {
                    noData(R.drawable.no_result, R.string.tips_nopendingevents, false);
                } else {
                    noData(R.drawable.no_notice, R.string.tips_noannounce, false);
                }
            } else {
                displayContent();
                updateMessage(messageList);
            }
            isLoadCompleted = true;
        } else {
            if (isRefresh) {  //刷新
                this.mMessageList.clear();
                updateMessage(messageList);
            } else { //加载更多
                updateMessage(messageList);
            }
            mRefreshLayout.setEnableLoadMore(isLoadEnabled(messageList));
        }
        mPageNo++;
    }

    private void displayContent() {
        hasData();
        if (mRefreshLayout.getVisibility() != View.VISIBLE) {
            mRefreshLayout.setVisibility(View.VISIBLE);
        }
    }

    /*是否可以加载更多，如果返回的列表为空或size小于指定的pageSize 则不允许下拉加载更多*/
    private boolean isLoadEnabled(List<MessageResultBean.MessageBean> messageList) {
        return !messageList.isEmpty() && messageList.size() >= mPageSize;
    }

    private void updateMessage(List<MessageResultBean.MessageBean> list) {
        this.mMessageList.addAll(list);
        this.mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onFailure(String failReason) {
        dismissLoading();
        finishLoad();
        if (!isLoadCompleted) {
            if (isNoAuth(failReason)) {
                noAuthority();
            } else {
                noNetwork();
            }
        } else {
            showShortToast(failReason);
        }
    }

    private void finishLoad() {
        mRefreshLayout.finishRefresh();
        mRefreshLayout.finishLoadMore();
    }

    @Override
    public void onItemClick(BaseRecyclerAdapter adapter, BaseRecyclerAdapter.RecyclerHolder holder, View view, int position) {
        if (position >= 0 && position < mMessageList.size()) {
            MessageResultBean.MessageBean bean = mMessageList.get(position);
//            if (!bean.isRead() && mCallback != null && mResultBean != null) {  //设置消息已读
//                if (TextUtils.equals(mType, "1")) {  //待处理事件
//                    mResultBean.noticeRead = mResultBean.noticeRead - 1 > -1 ? mResultBean.noticeRead - 1 : 0;
//                } else {  //公告
//                    mResultBean.announcementRead = mResultBean.announcementRead - 1 > -1 ? mResultBean.announcementRead - 1 : 0;
//                }
//                mCallback.onResponse(mResultBean);
//            }
//            mMessageList.get(position).setRead(); //设置消息已读
//            mAdapter.notifyDataSetChanged();
            onItemClick(bean, position);
        }
    }

    abstract void onItemClick(MessageResultBean.MessageBean messageBean, int position);

    @Override
    public void onRedistributeSuccess(String message) {
        dismissLoading();
        showShortToast(message);
        refreshMessage();
    }

    @Override
    public void onRedistributeFailure(String failReason) {
        dismissLoading();
        showShortToast(failReason);
    }

    public interface IMessageRequestCallback {
        void onResponse(MessageResultBean bean);
    }
}
