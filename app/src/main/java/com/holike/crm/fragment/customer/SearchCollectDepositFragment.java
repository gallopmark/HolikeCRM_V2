package com.holike.crm.fragment.customer;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.gallopmark.recycler.adapterhelper.CommonAdapter;
import com.holike.crm.R;
import com.holike.crm.base.MyFragment;
import com.holike.crm.bean.CollectDepositListBean;
import com.holike.crm.bean.CustomerDetailBean;
import com.holike.crm.bean.MultiItem;
import com.holike.crm.bean.NoMoreBean;
import com.holike.crm.presenter.activity.CollectDepositPresenter;
import com.holike.crm.util.Constants;
import com.holike.crm.view.activity.CollectDepositView;
import com.scwang.smartrefresh.header.WaterDropHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.BallPulseFooter;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by wqj on 2018/8/6.
 * 收取定金-搜索
 */

public class SearchCollectDepositFragment extends MyFragment<CollectDepositPresenter, CollectDepositView> implements CollectDepositView {
    private final int TYPE_REFRESH = 1;
    private final int TYPE_LOADMORE = 2;
    @BindView(R.id.et_search_collect_deposit)
    EditText etSearch;
    @BindView(R.id.rv_search_collect)
    RecyclerView rv;
    @BindView(R.id.srl_search_collect)
    SmartRefreshLayout slf;
    private int pageNo = Constants.DEFAULT_PAGE;
    private List<MultiItem> mBeans = new ArrayList<>();
    private NoMoreBean mNoMoreBean = new NoMoreBean();
    private CollectDepositAdapter mAdapter;
    private int type = TYPE_REFRESH;

    private class CollectDepositAdapter extends CommonAdapter<MultiItem> {

        CollectDepositAdapter(Context context, List<MultiItem> mDatas) {
            super(context, mDatas);
        }

        @Override
        public void onBindHolder(RecyclerHolder holder, MultiItem multiItem, int position) {
            if (holder.getItemViewType() == 2) {
                holder.setText(R.id.mNoMoreTextView, mContext.getString(R.string.list_no_more));
            } else {
                CollectDepositListBean bean = (CollectDepositListBean) multiItem;
                TextView tvName = holder.obtainView(R.id.tv_item_rv_collect_deposit_name);
                TextView tvPhone = holder.obtainView(R.id.tv_item_rv_collect_deposit_phone);
                TextView tvAddress = holder.obtainView(R.id.tv_item_rv_collect_deposit_address);
                tvName.setText(bean.getUserName());
                tvPhone.setText(bean.getPhoneNumber());
                tvAddress.setText(bean.getAddress());
                holder.itemView.setOnClickListener(v -> {
                    Map<String, Serializable> params = new HashMap<>();
                    params.put(Constants.COLLECT_DEPOSIT_LIST_BEAN, bean);
                    startFragment(params, new com.holike.crm.fragment.customer.workflow.CollectDepositFragment());
                });
            }
        }

        @Override
        public int getItemViewType(int position) {
            return mDatas.get(position).getItemType();
        }

        @Override
        protected int bindView(int viewType) {
            if (viewType == 2) {
                return R.layout.item_nomore_data;
            }
            return R.layout.item_rv_collect_deposit_list;
        }
    }

    @Override
    protected int setContentViewId() {
        return R.layout.fragment_search_collect_deposit;
    }

    @Override
    protected CollectDepositPresenter attachPresenter() {
        return new CollectDepositPresenter();
    }

    @Override
    protected void init() {
        super.init();
        rv.setVisibility(View.GONE);
        rv.setLayoutManager(new LinearLayoutManager(mContext));
        mAdapter = new CollectDepositAdapter(mContext, mBeans);
        rv.setAdapter(mAdapter);
        etSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                hideSoftInput(etSearch);
                refresh();
                showLoading();
            }
            return false;
        });
        slf.setRefreshHeader(new WaterDropHeader(mContext));
        slf.setRefreshFooter(new BallPulseFooter(mContext));
        slf.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshlayout) {
                pageNo++;
                type = TYPE_LOADMORE;
                mPresenter.getData(pageNo, etSearch.getText().toString());
//                showLoading();
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshlayout) {
                refresh();
            }
        });
    }

    private void refresh() {
        type = TYPE_REFRESH;
        pageNo = Constants.DEFAULT_PAGE;
        mPresenter.getData(pageNo, etSearch.getText().toString());
    }


    @OnClick(R.id.tv_search_collect_cancel)
    public void onViewClicked() {
        finishFragment();
    }

    /**
     * 获取数据成功
     * 嗯
     */
    @Override
    public void success(List<CollectDepositListBean> beans) {
        dismissLoading();
        slf.finishRefresh();
        slf.finishLoadMore();
        if (beans.size() == 0) {
            if (type == TYPE_REFRESH) {
                noData(R.drawable.no_result, R.string.tips_noresult, false);
            } else {
                slf.setEnableLoadMore(false);
                this.mBeans.remove(mNoMoreBean);
                this.mBeans.add(mNoMoreBean);
                mAdapter.notifyDataSetChanged();
            }
        } else {
            rv.setVisibility(View.VISIBLE);
            hasData();
            if (type == TYPE_REFRESH) {
                slf.setEnableLoadMore(true);
                this.mBeans.clear();
                this.mBeans.addAll(beans);
                mAdapter.notifyDataSetChanged();
            } else {
                this.mBeans.addAll(beans);
                mAdapter.notifyDataSetChanged();
            }
        }
    }

    /**
     * 获取数据失败
     */
    @Override
    public void failed(String failed) {
        dismissLoading();
        slf.finishRefresh();
        slf.finishLoadMore();
        showShortToast(failed);
    }

    @Override
    public void getCustomerFailed(String failed) {
        dismissLoading();
        showShortToast(failed);
    }

    @Override
    public void getCustomerSuccess(CustomerDetailBean customerDetailBean) {
        dismissLoading();
    }
}
