package com.holike.crm.activity.customer.helper;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.holike.crm.R;
import com.holike.crm.activity.customer.CustomerChargeDepositSearchActivity;
import com.holike.crm.base.BaseActivity;
import com.holike.crm.helper.MultiItemListHelper;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;


/**
 * Created by gallop on 2019/9/2.
 * Copyright holike possess 2019.
 */
public class CustomerChargeDepositHelper extends MultiItemListHelper {
    private BaseActivity<?, ?> mActivity;
    private SmartRefreshLayout mRefreshLayout;
    private RecyclerView mRecyclerView;

    public CustomerChargeDepositHelper(BaseActivity<?, ?> activity) {
        super(activity, 1);
        this.mActivity = activity;
        initView();
    }

    private void initView() {
        mActivity.findViewById(R.id.tv_search).setOnClickListener(view -> {
            mActivity.startActivity(CustomerChargeDepositSearchActivity.class);
            mActivity.overridePendingTransition(0, 0);
        });
        mRefreshLayout = mActivity.findViewById(R.id.mRefreshLayout);
        mRecyclerView = mActivity.findViewById(R.id.recyclerView);
    }

    @Override
    public void onStartLoad(boolean isFirstLoading) {

    }

    @Override
    public void onRefreshSuccess(boolean isFirstLoad, boolean isEmpty, boolean isLoadAll) {
        setLoadMoreEnabled(true);
        setContentEnabled(true);
        if (!isEmpty) {
            mActivity.hasData();
            setLoadMoreEnabled(!isLoadAll);
        } else {
            if (isFirstLoading()) {  //首次加载完成（没有加载到数据）
                showEmptyView(false);
            } else {
                setLoadMoreEnabled(false);
            }
        }
    }

    @Override
    public void onLoadMoreSuccess(boolean isLoadMoreEnabled) {
        setLoadMoreEnabled(isLoadMoreEnabled);
    }

    /*加载失败*/
    public void onFailure(String failReason) {
        onLoadComplete();
        if (mActivity.isNoAuth(failReason)) {
            clearData();
            setContentEnabled(false);
            mActivity.noAuthority();
        } else {
            if (isFirstLoading()) {
                clearData();
                showEmptyView(true);
            } else {
                mActivity.showShortToast(failReason);
            }
        }
    }

    private void onLoadComplete() {
        mRefreshLayout.finishLoadMore();
        mRefreshLayout.finishRefresh();
    }

    private void setLoadMoreEnabled(boolean isLoadMoreEnabled) {
        mRefreshLayout.setEnableLoadMore(isLoadMoreEnabled);
    }

    /*显示缺省页*/
    private void showEmptyView(boolean isNetworkError) {
        setContentEnabled(false);
        if (isNetworkError) {
            mActivity.noNetwork();
        } else {
            mActivity.noResult();
        }
    }

    /*列表页面显示或隐藏*/
    private void setContentEnabled(boolean isEnabled) {
        if (isEnabled) {
            if (mRefreshLayout.getVisibility() != View.VISIBLE) {
                mRefreshLayout.setVisibility(View.VISIBLE);
            }
        } else {
            mRefreshLayout.setVisibility(View.GONE);
        }
    }
}
