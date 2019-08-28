package com.holike.crm.presenter.fragment;

import android.content.Context;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;

import com.gallopmark.recycler.adapterhelper.CommonAdapter;
import com.holike.crm.R;
import com.holike.crm.base.BasePresenter;
import com.holike.crm.base.MyFragment;
import com.holike.crm.bean.BillListBean;
import com.holike.crm.bean.MultiItem;
import com.holike.crm.bean.NoMoreBean;
import com.holike.crm.fragment.bank.BillDetialFragment;
import com.holike.crm.model.fragment.BillListModel;
import com.holike.crm.util.Constants;
import com.holike.crm.view.fragment.BillListView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BillListPresenter extends BasePresenter<BillListView, BillListModel> {

    private List<MultiItem> mBeans = new ArrayList<>();
    private NoMoreBean mNoMoreBean = new NoMoreBean();
    private BillListAdapter mAdapter;

    class BillListAdapter extends CommonAdapter<MultiItem> {

        BillListAdapter(Context context, List<MultiItem> mDatas) {
            super(context, mDatas);
        }

        @Override
        public void onBindHolder(RecyclerHolder holder, MultiItem multiItem, int position) {
            if (holder.getItemViewType() == 2) {
                holder.setText(R.id.mNoMoreTextView, mContext.getString(R.string.list_no_more));
            } else {
                final BillListBean.PageDataBean pageDataBean = (BillListBean.PageDataBean) multiItem;
                holder.setText(R.id.tv_cztxt, MyFragment.textEmpty(pageDataBean.getCztxt()));
                holder.setText(R.id.tv_order_bstkd, MyFragment.textEmpty(pageDataBean.getBstkd()));
                holder.setText(R.id.tv_order_date, MyFragment.textEmpty(pageDataBean.getZdate()));
                holder.setText(R.id.tv_money, Double.parseDouble(pageDataBean.getMoney()) > 0 ? "+" + MyFragment.textEmptyNumber(pageDataBean.getMoney()) : MyFragment.textEmptyNumber(pageDataBean.getMoney()));
                holder.setTextColor(R.id.tv_money, Double.parseDouble(pageDataBean.getMoney()) > 0 ? mContext.getResources().getColor(R.color.bg_homepage_new) : mContext.getResources().getColor(R.color.textColor4));
                holder.setVisibility(R.id.v_line, getItemCount() != position + 1);
                holder.itemView.setOnClickListener(v -> {
                    if (getView() != null) {
                        getView().onItemClick(pageDataBean);
                    }
                });

                holder.itemView.setOnLongClickListener(v -> {
                    if (TextUtils.isEmpty(pageDataBean.getOrderId())) return true;
                    if (getView() != null)
                        getView().onItemLongClick(pageDataBean.getOrderId());
                    return true;
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
            return R.layout.item_bill_list_detail;
        }
    }

    public void setAdapter(RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        mAdapter = new BillListAdapter(recyclerView.getContext(), mBeans);
        recyclerView.setAdapter(mAdapter);
    }

    public void onRefreshCompleted(List<BillListBean.PageDataBean> beans) {
        this.mBeans.clear();
        onLoadMoreCompleted(beans);
    }

    public void onLoadMoreCompleted(List<BillListBean.PageDataBean> beans) {
        if (beans != null && !beans.isEmpty()) {
            this.mBeans.addAll(beans);
        }
        mAdapter.notifyDataSetChanged();
    }

    public void noMoreData() {
        mBeans.remove(mNoMoreBean);
        mBeans.add(mNoMoreBean);
        mAdapter.notifyDataSetChanged();
    }

    public void getHeadData(int pageNo, String startTime, String endTime) {
        model.getData(String.valueOf(pageNo), startTime == null ? "" : startTime, endTime == null ? "" : endTime, new BillListModel.BillListListener() {
            @Override
            public void onSuccess(BillListBean bean) {
                if (bean.getHeadData() == null) {
                    if (getView() != null)
                        getView().fail("没有数据！");
                } else {

                    if (getView() != null)
                        getView().success(bean);
                }
            }

            @Override
            public void onFail(String errorMsg) {
                if (getView() != null)
                    getView().fail(errorMsg);
            }
        });
    }

    public void setList(Context context, MyFragment myFragment, RecyclerView rv, final int layoutId, List<BillListBean.PageDataBean> beans) {
        rv.setAdapter(new CommonAdapter<BillListBean.PageDataBean>(context, beans) {
            @Override
            protected int bindView(int viewType) {
                return layoutId;
            }

            @Override
            public void onBindHolder(RecyclerHolder holder, BillListBean.PageDataBean pageDataBean, int position) {
                //                h.setText(R.id.tv_place_order_time, String.format(context.getString(R.string.bill_list_place_order_of_time), TimeUtil.stampToString(pageDataBean.getCreateDate(), "yyyy.MM.dd")));
//                h.setText(R.id.tv_order_status, String.format(context.getString(R.string.bill_list_order_statue), pageDataBean.getStatusCode()));
                holder.setText(R.id.tv_cztxt, MyFragment.textEmpty(pageDataBean.getCztxt()));
                holder.setText(R.id.tv_order_bstkd, MyFragment.textEmpty(pageDataBean.getBstkd()));
                holder.setText(R.id.tv_order_date, MyFragment.textEmpty(pageDataBean.getZdate()));
                holder.setText(R.id.tv_money, Double.parseDouble(pageDataBean.getMoney()) > 0 ? "+" + MyFragment.textEmptyNumber(pageDataBean.getMoney()) : MyFragment.textEmptyNumber(pageDataBean.getMoney()));
                holder.setTextColor(R.id.tv_money, Double.parseDouble(pageDataBean.getMoney()) > 0 ? mContext.getResources().getColor(R.color.bg_homepage_new) : mContext.getResources().getColor(R.color.textColor4));
                holder.setVisibility(R.id.v_line, beans.size() != position + 1);
                holder.itemView.setOnClickListener(v -> {
                    Map<String, Serializable> params = new HashMap<>();
                    params.put(Constants.BILL_DETAIL, pageDataBean);
                    myFragment.startFragment(params, new BillDetialFragment());
                });

                holder.itemView.setOnLongClickListener(v -> {
                    if (TextUtils.isEmpty(pageDataBean.getOrderId())) return true;
                    if (getView() != null)
                        getView().onItemLongClick(pageDataBean.getOrderId());
                    return true;
                });
            }
        });
    }
}
