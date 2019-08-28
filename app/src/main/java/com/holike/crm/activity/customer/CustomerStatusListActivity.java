package com.holike.crm.activity.customer;


import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.holike.crm.R;
import com.holike.crm.activity.customer.helper.CustomerStatusListHelper;
import com.holike.crm.activity.homepage.MessageV2Activity;
import com.holike.crm.base.MyFragmentActivity;
import com.holike.crm.bean.CustomerStatusBean;
import com.holike.crm.presenter.activity.CustomerStatusListPresenter;
import com.holike.crm.presenter.fragment.HomePagePresenter;
import com.holike.crm.presenter.fragment.HomePagePresenter2;
import com.holike.crm.view.activity.CustomerStatusListView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import butterknife.BindView;

/**
 * Created by gallop 2019/7/8
 * Copyright (c) 2019 holike
 * 客户状态列表
 */
public class CustomerStatusListActivity extends MyFragmentActivity<CustomerStatusListPresenter, CustomerStatusListView>
        implements CustomerStatusListView, CustomerStatusListHelper.CustomerStatusListCallback {
    @BindView(R.id.ll_content_layout)
    View mContentLayout;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout mRefreshLayout;
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;

    private CustomerStatusListHelper mStatusListController;

    @Override
    protected CustomerStatusListPresenter attachPresenter() {
        return new CustomerStatusListPresenter();
    }

    @Override
    protected int setContentViewId() {
        return R.layout.activity_customer_statuslist;
    }

    @Override
    protected void init() {
        super.init();
        String statusName = getIntent().getStringExtra("statusName");
        setTitle(statusName);
        mStatusListController = new CustomerStatusListHelper(this, statusName, this);
        mStatusListController.setAdapter(mRecyclerView);
        mRefreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                mStatusListController.onLoadMore();
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                mStatusListController.onRefresh();
            }
        });
        mStatusListController.startFirstLoad();
    }

    @Override
    public void onGetCustomerStatusList(boolean isShowLoading, String statusName, String earnestStatus,
                                        String intentionStatus, String customerStatus, String tailStatus,
                                        String seaStatus, String orderBy, int pageNo, int pageSize) {
        if (isShowLoading) {
            showLoading();
        }
        mPresenter.getCustomerStatusList(statusName, earnestStatus, intentionStatus, customerStatus, tailStatus, seaStatus, orderBy, pageNo, pageSize);
    }

    @Override
    public void refreshSuccess(boolean isEmpty, boolean isLoadAll) {
        if (!isEmpty) {
            hasData();
            setLoadMoreEnabled(!isLoadAll);
        } else {
            if (mStatusListController.isFirstLoading()) {  //首次加载完成（没有加载到数据）
                noResult();
            }
        }
    }

    @Override
    public void hasData() {
        super.hasData();
        onLoadComplete(true);
    }

    @Override
    public void loadMoreSuccess(boolean isLoadMoreEnabled) {
        setLoadMoreEnabled(isLoadMoreEnabled);
    }

    private void setLoadMoreEnabled(boolean isLoadMoreEnabled) {
        mRefreshLayout.setEnableLoadMore(isLoadMoreEnabled);
    }

    @Override
    public void onSuccess(CustomerStatusBean bean) {
        dismissLoading();
        onLoadComplete(true);
        mStatusListController.onHttpResponse(bean, bean.getList());
    }

    private void onLoadComplete(boolean isVisible) {
        mRefreshLayout.finishLoadMore();
        mRefreshLayout.finishRefresh();
        if (isVisible) {
            if (mContentLayout.getVisibility() != View.VISIBLE) {
                mContentLayout.setVisibility(View.VISIBLE);
            }
        } else {
            mContentLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void onFailed(String failed) {
        dismissLoading();
        if (isNoAuth(failed)) {
            mStatusListController.clearData();
            onLoadComplete(false);
            noAuthority();
        } else {
            if (mStatusListController.isFirstLoading()) {
                onLoadComplete(false);
                noData(R.drawable.no_network, failed, true);
            } else {
                showShortToast(failed);
                onLoadComplete(true);
            }
        }
    }

    @Override
    public void reload() {
        mStatusListController.startFirstLoad();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setRightMsg(HomePagePresenter2.isNewMsg());
    }

    @Override
    protected void clickRightMenu() {
        startActivity(MessageV2Activity.class);
    }
}
