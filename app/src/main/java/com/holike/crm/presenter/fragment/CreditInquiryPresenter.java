package com.holike.crm.presenter.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gallopmark.recycler.adapterhelper.CommonAdapter;
import com.holike.crm.R;
import com.holike.crm.activity.homepage.OrderDetailsActivity;
import com.holike.crm.base.BasePresenter;
import com.holike.crm.base.MyFragment;
import com.holike.crm.bean.CreditInquiryBean;
import com.holike.crm.bean.MultiItem;
import com.holike.crm.bean.NoMoreBean;
import com.holike.crm.model.fragment.CreditInquiryModel;
import com.holike.crm.util.Constants;
import com.holike.crm.view.fragment.CreditInquiryView;

import java.util.ArrayList;
import java.util.List;

public class CreditInquiryPresenter extends BasePresenter<CreditInquiryView, CreditInquiryModel> {
    private List<MultiItem> mBeans = new ArrayList<>();
    private NoMoreBean noMoreBean = new NoMoreBean();
    private CreditAdapter mAdapter;

    private class CreditAdapter extends CommonAdapter<MultiItem> {

        CreditAdapter(Context context, List<MultiItem> mDatas) {
            super(context, mDatas);
        }

        @Override
        public void onBindHolder(RecyclerHolder holder, MultiItem item, int position) {
            int itemType = holder.getItemViewType();
            if (itemType == 2) {
                holder.setText(R.id.mNoMoreTextView, mContext.getString(R.string.list_no_more));
            } else {
                CreditInquiryBean.PageDataBean pageDataBean = (CreditInquiryBean.PageDataBean) item;
                holder.itemView.setBackgroundColor(ContextCompat.getColor(mContext, position % 2 == 0 ? R.color.bg_transparent : R.color.light_text5));
                holder.setText(R.id.tv_order_number, pageDataBean.getOrderId());
                holder.setText(R.id.tv_factory_price_before_discount, MyFragment.textEmptyNumber(pageDataBean.getPlantPriceBe()));
                holder.setText(R.id.tv_factory_price_after_discount, MyFragment.textEmptyNumber(pageDataBean.getPlantPriceAf()));
                holder.itemView.setOnLongClickListener(v -> {
                    if (getView() != null)
                        getView().onItemLongClick(pageDataBean.getOrderId());
                    return true;
                });
                holder.itemView.setOnClickListener(v -> {
                    Intent intent = new Intent(mContext, OrderDetailsActivity.class);
                    intent.putExtra(Constants.ORDER_ID, pageDataBean.getOrderId());
                    ((Activity) mContext).startActivityForResult(intent, REQUEST_CODE);
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
            } else {
                return R.layout.item_credit_inquiry_detail;
            }
        }
    }

    public void setAdapter(RecyclerView recyclerView) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(recyclerView.getContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new CreditAdapter(recyclerView.getContext(), mBeans);
        recyclerView.setAdapter(mAdapter);
    }

    public void onRefreshCompleted(List<CreditInquiryBean.PageDataBean> beans) {
        this.mBeans.clear();
        onLoadMoreCompleted(beans);
    }

    public void onLoadMoreCompleted(List<CreditInquiryBean.PageDataBean> beans) {
        if (beans != null && !beans.isEmpty()) {
            this.mBeans.addAll(beans);
        }
        mAdapter.notifyDataSetChanged();
    }

    public void noMoreData() {
        mBeans.remove(noMoreBean);
        mBeans.add(noMoreBean);
        mAdapter.notifyDataSetChanged();
    }

    public void getData(int pageNo) {
        model.getData(String.valueOf(pageNo), new CreditInquiryModel.CreditInquiryListener() {
            @Override
            public void onSuccess(CreditInquiryBean bean) {
                if (getView() != null)
                    getView().onSuccess(bean);
            }

            @Override
            public void onFail(String errorMsg) {
                if (getView() != null)
                    getView().onFail(errorMsg);
            }
        });

    }
}
