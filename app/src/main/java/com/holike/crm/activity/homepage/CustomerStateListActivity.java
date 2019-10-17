package com.holike.crm.activity.homepage;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.holike.crm.R;
import com.holike.crm.base.MyFragmentActivity;
import com.holike.crm.bean.CustomerStateListBean;
import com.holike.crm.presenter.activity.CustomerStateListPresenter;
import com.holike.crm.presenter.fragment.HomePagePresenter;
import com.holike.crm.view.activity.CustomerStateListView;
import com.scwang.smartrefresh.header.WaterDropHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.BallPulseFooter;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.List;

import butterknife.BindView;

/**
 * 客户状态列表
 */
@Deprecated
public class CustomerStateListActivity extends MyFragmentActivity<CustomerStateListPresenter, CustomerStateListView> implements CustomerStateListView {

    @BindView(R.id.ll_title)
    LinearLayout llTitle;
    @BindView(R.id.tv_customer_state_list_customer)
    TextView tvCustomer;
    @BindView(R.id.tv_customer_state_list_customer_num)
    TextView tvCustomerNum;
    @BindView(R.id.tv_customer_state_list_add)
    TextView tvAdd;
    @BindView(R.id.tv_customer_state_list_add_num)
    TextView tvAddNum;
    @BindView(R.id.rv_customer_state_list)
    RecyclerView mRecyclerView;
    @BindView(R.id.srl_search_collect)
    SmartRefreshLayout srlListCollect;

    private String stateName;
    //    private boolean isBoss;

    private int pageNo = 1;
    private int pageSize = 10;
    private int loadType = 0;

    @Override
    protected CustomerStateListPresenter attachPresenter() {
        return new CustomerStateListPresenter();
    }

    @Override
    protected int setContentViewId() {
        return R.layout.activity_customer_state_list;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        setLeft(getString(R.string.homepage));
//        isBoss = getIntent().getBooleanExtra(Constants.IS_BOSS, false);
        stateName = getIntent().getStringExtra("stateName");
        setTitle(stateName);
        mPresenter.setAdapter(mRecyclerView, stateName);
        srlListCollect.setRefreshHeader(new WaterDropHeader(this));
        srlListCollect.setRefreshFooter(new BallPulseFooter(this));
        srlListCollect.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                loadmore();
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                refresh(false);
            }
        });
        loadData(true);
    }

    private void loadData(boolean showLoading) {
        if (showLoading) {
            showLoading();
        }
        mPresenter.getDate(stateName, pageNo, pageSize);
    }

    @Override
    public void refresh(boolean showLoading) {
        pageNo = 1;
        loadType = 1;
        loadData(showLoading);
    }

    @Override
    public void loadmore() {
        loadType = 2;
        loadData(false);
    }

    @Override
    public void refreshSuccess(List<CustomerStateListBean.DateBean> listBeans) {
        dismissLoading();
        if (listBeans.size() > 0) {
            hasData();
            mPresenter.onRefreshCompleted(listBeans);
            if (listBeans.size() < pageSize) {
                loadAllSuccess();
            } else {
                srlListCollect.setEnableLoadMore(true);
            }
        } else {
            noResult();
        }
    }

    /**
     * 加载更多成功
     */
    @Override
    public void loadmoreSuccess(List<CustomerStateListBean.DateBean> listBeans) {
        srlListCollect.setEnableLoadMore(true);
        mPresenter.onLoadMoreCompleted(listBeans);
//        mRecyclerView.setupAdapter().notifyDataSetChanged();
    }

    @Override
    public void loadAllSuccess() {
        srlListCollect.setEnableLoadMore(false);
        mPresenter.noMoreData();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        setRightMenu(HomePagePresenter.getMsgNum());
        setRightMsg(HomePagePresenter.isNewMsg());
    }

    @Override
    protected void clickRightMenu(String menuText, View actionView) {
        startActivity(MessageV2Activity.class);
    }

    @Override
    public void success(CustomerStateListBean bean) {
        srlListCollect.finishLoadMore();
        srlListCollect.finishRefresh();
        llTitle.setBackgroundColor(ContextCompat.getColor(this, R.color.color_while));
//        NewItemHelper.getInstance().getNewItem(bean, stateName, this);
        tvCustomer.setText(TextUtils.isEmpty(stateName) ? "" : stateName + "：");
        tvCustomerNum.setText(String.valueOf(bean.getSize()));
        if (TextUtils.equals(stateName, "意向客户跟进")) {
            tvCustomer.setText("意向客户：");
            tvAdd.setVisibility(View.VISIBLE);
            tvAddNum.setVisibility(View.VISIBLE);
            tvAddNum.setText(String.valueOf(bean.getYesterdayAdd()));
        } else if (TextUtils.equals(stateName, "待量房客户")) {
            tvAdd.setVisibility(View.VISIBLE);
            tvAddNum.setVisibility(View.VISIBLE);
            tvAdd.setText("今日量房客户：");
            tvAddNum.setText(String.valueOf(bean.getYesterdayAdd()));
        } else if (TextUtils.equals(stateName, "待安装客户")) {
            tvAdd.setVisibility(View.VISIBLE);
            tvAddNum.setVisibility(View.VISIBLE);
            tvAdd.setText("未派单：");
            tvAddNum.setText(String.valueOf(bean.getYesterdayAdd()));
        } else if (TextUtils.equals(stateName, "已流失客户")) {
            tvCustomer.setText(getString(R.string.customer_have_lost_30days));
        }
        if (loadType == 2) {
            if (bean.getDate().size() < pageSize) {
                loadmoreSuccess(bean.getDate());
                loadAllSuccess();
            } else {
                loadmoreSuccess(bean.getDate());
            }
        } else {
            refreshSuccess(bean.getDate());
        }
        pageNo++;
    }

    @Override
    public void failed(String failed) {
        dismissLoading();
        if (loadType == 0) {
            dealWithFailed(failed, true);
        } else {
            showShortToast(failed);
        }
    }

    @Override
    public void reload() {
        loadType = 0;
        loadData(true);
    }
}
