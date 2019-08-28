package com.holike.crm.activity.homepage;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.holike.crm.R;
import com.holike.crm.base.MyFragmentActivity;
import com.holike.crm.bean.FeedbackRecordBean;
import com.holike.crm.presenter.activity.FeedbackRecordPresenter;
import com.holike.crm.view.activity.FeedbackRecordView;
import com.scwang.smartrefresh.header.WaterDropHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.BallPulseFooter;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.List;

import butterknife.BindView;

/**
 * Created by wqj on 2018/7/19.
 * 反馈记录
 */

public class FeedbackRecordActivity extends MyFragmentActivity<FeedbackRecordPresenter, FeedbackRecordView> implements FeedbackRecordView {
    @BindView(R.id.rv_feedback_record)
    RecyclerView mRecyclerView;
    @BindView(R.id.srl_feedback_record)
    SmartRefreshLayout srl;

    private int pageNo = 1;
    private int loadType = 0;

    @Override
    protected int setContentViewId() {
        return R.layout.fragment_feedback_record;
    }

    @Override
    protected FeedbackRecordPresenter attachPresenter() {
        return new FeedbackRecordPresenter();
    }

    @Override
    protected void init() {
        super.init();
        setTitle(getString(R.string.feedback_record));
        mPresenter.setAdapter(mRecyclerView);
        srl.setRefreshHeader(new WaterDropHeader(this));
        srl.setRefreshFooter(new BallPulseFooter(this));
        srl.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshlayout) {
                refresh(false);
            }

            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshlayout) {
                loadmore();
            }
        });
        refresh(true);
    }

    /**
     * 刷新
     */
    @Override
    public void refresh(boolean showLoading) {
        if (showLoading) {
            showLoading();
        }
        pageNo = 1;
        loadType = 1;
        srl.setEnableLoadMore(true);
        mPresenter.getRecord(pageNo);
    }

    /**
     * 加载更多
     */
    @Override
    public void loadmore() {
        loadType = 2;
        mPresenter.getRecord(pageNo);
    }

    /**
     * 获取数据成功
     */
    @Override
    public void getRecordSuccess(List<FeedbackRecordBean> list) {
        dismissLoading();
        pageNo++;
        loadComplete();
        if (list.isEmpty()) {
            if (loadType == 0 || loadType == 1) {
                noRecord();
            } else {
                loadAll();
            }
        } else {
            if (loadType == 2) {
                mPresenter.onLoadMoreCompleted(list);
            } else {
                mPresenter.onRefreshCompleted(list);
            }
        }
    }

    /**
     * 获取数据失败
     */
    @Override
    public void getRecordFailed(String failed) {
        dismissLoading();
        loadComplete();
        showShortToast(failed);
    }

    /**
     * 没有反馈记录
     */
    @Override
    public void noRecord() {
        noData(R.drawable.no_result, R.string.tips_norecord, false);
    }

    /**
     * 加载全部完成
     */
    @Override
    public void loadAll() {
        dismissLoading();
        loadComplete();
        srl.setEnableLoadMore(false);
    }

    /**
     * 加载成功
     */
    @Override
    public void loadComplete() {
        dismissLoading();
        srl.finishRefresh();
        srl.finishLoadMore();
    }

    @Override
    public void reload() {
        refresh(true);
    }
}
