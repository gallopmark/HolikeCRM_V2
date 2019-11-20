package com.holike.crm.activity.customer;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.holike.crm.R;
import com.holike.crm.activity.customer.helper.CustomerStatusListHelper;
import com.holike.crm.activity.homepage.MessageV2Activity;
import com.holike.crm.base.MyFragmentActivity;
import com.holike.crm.bean.CustomerStatusBean;
import com.holike.crm.enumeration.CustomerValue;
import com.holike.crm.presenter.activity.CustomerStatusListPresenter;
import com.holike.crm.presenter.fragment.HomePagePresenter2;
import com.holike.crm.view.activity.CustomerStatusListView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import butterknife.BindView;

/**
 * Created by pony 2019/7/8
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

    private CustomerStatusListHelper mHelper;

    @Override
    protected CustomerStatusListPresenter attachPresenter() {
        return new CustomerStatusListPresenter();
    }

    @Override
    protected int setContentViewId() {
        return R.layout.activity_customer_statuslist;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        String statusName = getIntent().getStringExtra("statusName");
        setTitle(statusName);
        mHelper = new CustomerStatusListHelper(this, statusName, this);
        mHelper.setAdapter(mRecyclerView);
        mRefreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                mHelper.onLoadMore();
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                mHelper.onRefresh();
            }
        });
        mHelper.startFirstLoad();
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
            if (mHelper.isFirstLoading()) {  //首次加载完成（没有加载到数据）
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
        mHelper.onHttpResponse(bean, bean.getList());
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
            mHelper.clearData();
            onLoadComplete(false);
            noAuthority();
        } else {
            if (mHelper.isFirstLoading()) {
                onLoadComplete(false);
                noNetwork(failed);
            } else {
                showShortToast(failed);
                onLoadComplete(true);
            }
        }
    }

    @Override
    public void reload() {
        mHelper.startFirstLoad();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setRightMsg(HomePagePresenter2.isNewMsg());
    }

    @Override
    protected void clickRightMenu(CharSequence menuText, View actionView) {
        startActivity(MessageV2Activity.class);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == REQUEST_CODE && (resultCode == CustomerValue.RESULT_CODE_RECEIVE_HOUSE
//                || resultCode == CustomerValue.RESULT_CODE_LOST_HOUSE)) {
//            if (requestCode == CustomerValue.RESULT_CODE_RECEIVE_HOUSE) {
//                mHelper.onReceiveHouseOk();
//            }
//            mHelper.onRefresh();
//        } else
        if ((resultCode == CustomerValue.RESULT_CODE_ACTIVATION ||
                resultCode == CustomerValue.RESULT_CODE_HIGH_SEAS) && data != null) {
            String personalId = data.getStringExtra(CustomerValue.PERSONAL_ID);
            String houseId = data.getStringExtra(CustomerValue.HOUSE_ID);
            CustomerDetailV2Activity.open(this, personalId, houseId, false);
        }
    }

    @Override
    protected void onDestroy() {
        mHelper.destroy();
        super.onDestroy();
    }
}
