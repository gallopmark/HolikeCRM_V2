package com.holike.crm.helper;


import android.content.Context;

import com.gallopmark.recycler.adapterhelper.BaseRecyclerAdapter;
import com.holike.crm.bean.MultiItem;
import com.holike.crm.bean.NoMoreBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gallop on 2019/7/9.
 * Copyright holike possess 2019.
 * 上拉刷新、下拉加载更多辅助类
 */
public abstract class MultiItemListHelper {
    protected Context mContext;
    protected List<MultiItem> mListBeans;
    protected BaseRecyclerAdapter mAdapter;
    protected NoMoreBean mNoMoreBean;

    protected int mPageNo, mPageSize;
    protected final int mStartPage;

    private int mRequestType = 0;
//    private boolean mFirstLoading = true, isRefresh, isLoadMore;

    public int getPageNo() {
        return mPageNo;
    }

    public int getPageSize() {
        return mPageSize;
    }

    protected MultiItemListHelper(Context context, int startPage) {
        this(context, startPage, 10);  //pageSize默认10
    }

    protected MultiItemListHelper(Context context, int startPage, int pageSize) {
        this.mContext = context;
        this.mStartPage = startPage;
        this.mPageNo = this.mStartPage;
        this.mPageSize = pageSize;
        this.mListBeans = new ArrayList<>();
        this.mNoMoreBean = new NoMoreBean();
    }

    /*是否是首次加载*/
    public boolean isFirstLoading() {
        return mRequestType == 0;
    }

    public boolean isRefresh() {
        return mRequestType == 1;
    }

    /*重置起始状态值*/
    public void onReset() {
        this.mPageNo = this.mStartPage;
        this.mRequestType = 0;
    }

    public void startFirstLoad() {
        onReset();
        onStartLoad(isFirstLoading());
    }

    public void onRefresh() {
        this.mPageNo = this.mStartPage;
        mRequestType = 1;
        onStartLoad(false);
    }

    public void onLoadMore() {
        mRequestType = 2;
        onStartLoad(false);
    }

    public abstract void onStartLoad(boolean isFirstLoading);

    protected void onHttpResultOk(List<? extends MultiItem> listBeans) {
        boolean isEmpty = (listBeans == null || listBeans.isEmpty());
        boolean isLoadAll = (isEmpty || listBeans.size() < mPageSize);
        if (mRequestType == 0) { //首次加载
            refreshSuccess(isEmpty, isLoadAll, listBeans);
        } else if (mRequestType == 1) {  //下拉刷新
            refreshSuccess(isEmpty, isLoadAll, listBeans);
        } else { //上拉加载更多
            loadMoreSuccess(isLoadAll, listBeans);
        }
        mPageNo++;
    }

    private void refreshSuccess(boolean isEmpty, boolean isLoadAll, List<? extends MultiItem> listBeans) {
        onRefreshCompleted(listBeans);
        if (isLoadAll) {
            noMoreData();
        }
        onRefreshSuccess(isFirstLoading(), isEmpty, isLoadAll);
    }

    public abstract void onRefreshSuccess(boolean isFirstLoad, boolean isEmpty, boolean isLoadAll);

    private void onRefreshCompleted(List<? extends MultiItem> mBeans) {
        this.mListBeans.clear();
        onLoadMoreCompleted(mBeans);
    }

    private void loadMoreSuccess(boolean isLoadAll, List<? extends MultiItem> listBeans) {
        onLoadMoreCompleted(listBeans);
        if (isLoadAll) {
            noMoreData();
        }
        onLoadMoreSuccess(!isLoadAll);
    }

    public abstract void onLoadMoreSuccess(boolean isLoadMoreEnabled);

    private void onLoadMoreCompleted(List<? extends MultiItem> mBeans) {
        if (mBeans != null && !mBeans.isEmpty()) {
            this.mListBeans.addAll(mBeans);
        }
        notifyDataSetChanged();
    }

    /*没有更多数据，添加底部“全部加载完成”*/
    private void noMoreData() {
        this.mListBeans.remove(mNoMoreBean);
        this.mListBeans.add(mNoMoreBean);
        notifyDataSetChanged();
    }

    /*清空数据*/
    public void clearData() {
        this.mListBeans.clear();
        notifyDataSetChanged();
    }

    protected void notifyDataSetChanged() {
        if (this.mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }
    }

    protected void notifyItemRemoved(int position) {
        if (this.mAdapter != null) {
            this.mAdapter.notifyItemRemoved(position);
        }
    }
}
