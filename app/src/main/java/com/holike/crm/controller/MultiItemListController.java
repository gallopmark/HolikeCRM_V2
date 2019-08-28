package com.holike.crm.controller;


import android.content.Context;

import com.gallopmark.recycler.adapterhelper.BaseRecyclerAdapter;
import com.holike.crm.bean.MultiItem;
import com.holike.crm.bean.NoMoreBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gallop on 2019/7/9.
 * Copyright holike possess 2019.
 */
public abstract class MultiItemListController {
    protected Context mContext;
    protected List<MultiItem> mListBeans;
    protected BaseRecyclerAdapter mAdapter;
    protected NoMoreBean mNoMoreBean;

    protected int mPageNo, mPageSize;
    protected final int mStartPage;
    protected boolean isFirstLoading = true, isRefresh, isLoadMore;

    public int getPageNo() {
        return mPageNo;
    }

    public int getPageSize() {
        return mPageSize;
    }

    protected MultiItemListController(Context context, int startPage) {
        this(context, startPage, 10);  //pageSize默认10
    }

    protected MultiItemListController(Context context, int startPage, int pageSize) {
        this.mContext = context;
        this.mStartPage = startPage;
        this.mPageNo = this.mStartPage;
        this.mPageSize = pageSize;
        this.mListBeans = new ArrayList<>();
        this.mNoMoreBean = new NoMoreBean();
    }

    /*是否是首次加载*/
    public boolean isFirstLoading() {
        return isFirstLoading;
    }

    public boolean isRefresh() {
        return isRefresh;
    }

    /*重置起始状态值*/
    public void onReset() {
        this.mPageNo = this.mStartPage;
        this.isFirstLoading = true;
        this.isRefresh = false;
        this.isLoadMore = false;
    }

    public void startFirstLoad() {
        onReset();
        onStartLoad(isFirstLoading);
    }

    public void onRefresh() {
        this.mPageNo = this.mStartPage;
        this.isFirstLoading = false;
        this.isRefresh = true;
        this.isLoadMore = false;
        onStartLoad(isFirstLoading);
    }

    public void onLoadMore() {
        this.isFirstLoading = false;
        this.isRefresh = false;
        this.isLoadMore = true;
        onStartLoad(isFirstLoading);
    }

    public abstract void onStartLoad(boolean isFirstLoading);

    public void onHttpResultOk(List<? extends MultiItem> listBeans) {
        boolean isEmpty = (listBeans == null || listBeans.isEmpty());
        boolean isLoadAll = (isEmpty || listBeans.size() < mPageSize);
        if (isFirstLoading) {
            refreshSuccess(isEmpty, isLoadAll, listBeans);
            isFirstLoading = false;
        } else {
            if (isRefresh) {
                refreshSuccess(isEmpty, isLoadAll, listBeans);
            } else if (isLoadMore) {
                loadMoreSuccess(isEmpty, isLoadAll, listBeans);
            }
        }
        mPageNo++;
    }

    private void refreshSuccess(boolean isEmpty, boolean isLoadAll, List<? extends MultiItem> listBeans) {
        if (!isEmpty) {
            onRefreshCompleted(listBeans);
        }
        if (isLoadAll) {
            noMoreData();
        }
        onRefreshSuccess(isEmpty, isLoadAll);
    }

    public abstract void onRefreshSuccess(boolean isEmpty, boolean isLoadAll);

    public void onRefreshCompleted(List<? extends MultiItem> mBeans) {
        this.mListBeans.clear();
        onLoadMoreCompleted(mBeans);
    }

    private void loadMoreSuccess(boolean isEmpty, boolean isLoadAll, List<? extends MultiItem> listBeans) {
        if (!isEmpty) {
            onLoadMoreCompleted(listBeans);
        }
        if (isLoadAll) {
            noMoreData();
        }
        onLoadMoreSuccess(!isLoadAll);
    }

    public abstract void onLoadMoreSuccess(boolean isLoadMoreEnabled);

    public void onLoadMoreCompleted(List<? extends MultiItem> mBeans) {
        if (mBeans != null && !mBeans.isEmpty()) {
            this.mListBeans.addAll(mBeans);
        }
        notifyDataSetChanged();
    }

    public void noMoreData() {
        this.mListBeans.remove(mNoMoreBean);
        this.mListBeans.add(mNoMoreBean);
        notifyDataSetChanged();
    }

    public void clearData() {
        this.mListBeans.clear();
        notifyDataSetChanged();
    }

    public void notifyDataSetChanged() {
        if (this.mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }
    }

    public void notifyItemRemoved(int position) {
        if (this.mAdapter != null) {
            this.mAdapter.notifyItemRemoved(position);
        }
    }
}
