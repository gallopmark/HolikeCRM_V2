package com.holike.crm.presenter.fragment;

import android.content.Context;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.widget.TextView;

import com.gallopmark.recycler.adapterhelper.CommonAdapter;
import com.holike.crm.R;
import com.holike.crm.base.BasePresenter;
import com.holike.crm.base.MyFragment;
import com.holike.crm.bean.MultiItem;
import com.holike.crm.bean.NoMoreBean;
import com.holike.crm.bean.PayListBean;
import com.holike.crm.fragment.bank.DetailsFragment;
import com.holike.crm.model.fragment.OnlineDeclarationModel;
import com.holike.crm.util.Constants;
import com.holike.crm.view.fragment.OnlineDeclarationView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 在线申报
 */
public class OnlineDeclarationPresenter extends BasePresenter<OnlineDeclarationView, OnlineDeclarationModel> {
    private List<MultiItem> mBeans = new ArrayList<>();
    private DeclarationListAdapter adapter;

    private static class DeclarationListAdapter extends CommonAdapter<MultiItem> {

        DeclarationListAdapter(Context context, List<MultiItem> mDatas) {
            super(context, mDatas);
        }

        @Override
        public int getItemViewType(int position) {
            return mDatas.get(position).getItemType();
        }

        @Override
        protected int bindView(int viewType) {
            if (viewType == 1) return R.layout.item_online_declare_list;
            return R.layout.item_nomore_data;
        }

        @Override
        public void onBindHolder(RecyclerHolder holder, MultiItem multiItem, int position) {
            int itemType = holder.getItemViewType();
            if (itemType == 1) {
                TextView tvState = holder.obtainView(R.id.tv_state);
                holder.setDrawableLeft(R.id.tv_order_reference, R.drawable.icon_bank);
                holder.setDrawableLeft(R.id.tv_store_name, R.drawable.icon_car_number);
                holder.setDrawableLeft(R.id.tv_order_money, R.drawable.icon_money);
                PayListBean bean = (PayListBean) multiItem;
                holder.setText(R.id.tv_place_order_time, String.format(mContext.getString(R.string.bill_list_place_order_of_time), bean.getCreate_date()));
                holder.setText(R.id.tv_order_status, String.format(mContext.getString(R.string.online_declaration_trading_time), bean.getTransaction_date()));
                holder.setText(R.id.tv_order_reference, bean.getRecip_bk_name());
                holder.setText(R.id.tv_store_name, bean.getRecip_acc_no());
                holder.setText(R.id.tv_order_money, bean.getCredit_amount());
                holder.setText(R.id.tv_state, bean.getStatus_name());
                holder.setVisibility(R.id.tv_state, true);
                switch (bean.getStatus_code()) {
                    case "01":
                        //蓝色 color5
                        tvState.setTextColor(ContextCompat.getColor(mContext, R.color.textColor5));
                        tvState.setBackgroundResource(R.drawable.bg_states_blue);
                        break;
                    case "02":
                    case "04":
                        //黄色
                        tvState.setTextColor(ContextCompat.getColor(mContext, R.color.textColor13));
                        tvState.setBackgroundResource(R.drawable.bg_states_orange);
                        break;
                    case "03":
                    case "05":
                    case "07":
                        //红色
                        tvState.setTextColor(ContextCompat.getColor(mContext, R.color.textColor12));
                        tvState.setBackgroundResource(R.drawable.bg_states_red);
                        break;
                    case "06":
                        //绿色
                        tvState.setTextColor(ContextCompat.getColor(mContext, R.color.color_green));
                        tvState.setBackgroundResource(R.drawable.bg_states_green);
                        break;

                }
            } else {
                holder.setText(R.id.mNoMoreTextView, mContext.getString(R.string.list_no_more));
            }
        }
    }

    public void setAdapter(RecyclerView recyclerView) {
        adapter = new DeclarationListAdapter(recyclerView.getContext(), mBeans);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener((adapter, holder, view, position) -> {
            if (position >= 0 && position < mBeans.size()) {
                if (mBeans.get(position) instanceof PayListBean) {
                    PayListBean bean = (PayListBean) mBeans.get(position);
                    if (getView() != null) getView().onItemClick(bean);
                }
            }
        });
    }

    public void onRefreshCompleted(List<PayListBean> beans) {
        this.mBeans.clear();
        onLoadMoreCompleted(beans);
    }

    public void onLoadMoreCompleted(List<PayListBean> beans) {
        if (beans != null && !beans.isEmpty()) {
            this.mBeans.addAll(beans);
        }
        adapter.notifyDataSetChanged();
    }

    public void noMoreData() {
        mBeans.add(new NoMoreBean());
        adapter.notifyDataSetChanged();
    }

    public void getData(int pageNo, String startTime, String endTime, String searchContent) {
        model.getData(String.valueOf(pageNo), startTime, endTime, searchContent, new OnlineDeclarationModel.OnlineDeclarationListener() {
            @Override
            public void success(List<PayListBean> bean) {
                if (getView() != null)
                    getView().success(bean);
            }

            @Override
            public void fail(String errorMsg) {
                if (getView() != null)
                    getView().fail(errorMsg);
            }
        });
    }


    public void setList(Context context, MyFragment myFragment, RecyclerView rv, final int layoutId, List<PayListBean> beans) {
        rv.setAdapter(new CommonAdapter<PayListBean>(context, beans) {
            @Override
            protected int bindView(int viewType) {
                return layoutId;
            }

            @Override
            public void onBindHolder(RecyclerHolder holder, PayListBean bean, int position) {
                TextView t1 = holder.obtainView(R.id.tv_order_reference);
                TextView t2 = holder.obtainView(R.id.tv_store_name);
                TextView t3 = holder.obtainView(R.id.tv_order_money);
                TextView tvState = holder.obtainView(R.id.tv_state);

                t1.setCompoundDrawablesWithIntrinsicBounds(mContext.getResources().getDrawable(R.drawable.icon_bank),
                        null, null, null);
                t2.setCompoundDrawablesWithIntrinsicBounds(mContext.getResources().getDrawable(R.drawable.icon_car_number),
                        null, null, null);
                t3.setCompoundDrawablesWithIntrinsicBounds(mContext.getResources().getDrawable(R.drawable.icon_money),
                        null, null, null);

                holder.setText(R.id.tv_place_order_time, String.format(context.getString(R.string.online_declaration_creation_time), bean.getCreate_date()));
                holder.setText(R.id.tv_order_status, String.format(context.getString(R.string.online_declaration_trading_time), bean.getTransaction_date()));
                holder.setText(R.id.tv_order_reference, bean.getRecip_bk_name());
                holder.setText(R.id.tv_store_name, bean.getRecip_acc_no());
                holder.setText(R.id.tv_order_money, bean.getCredit_amount());
                holder.setText(R.id.tv_state, bean.getStatus_name());
                holder.setVisibility(R.id.tv_state, true);

                switch (bean.getStatus_code()) {
                    case "01":
                        //蓝色 color5
                        tvState.setTextColor(mContext.getResources().getColor(R.color.textColor5));
                        tvState.setBackgroundResource(R.drawable.bg_states_blue);
                        break;
                    case "02":
                    case "04":
                        //黄色
                        tvState.setTextColor(mContext.getResources().getColor(R.color.textColor13));
                        tvState.setBackgroundResource(R.drawable.bg_states_orange);
                        break;
                    case "03":
                    case "05":
                    case "07":
                        //红色
                        tvState.setTextColor(mContext.getResources().getColor(R.color.textColor12));
                        tvState.setBackgroundResource(R.drawable.bg_states_red);
                        break;
                    case "06":
                        //绿色
                        tvState.setTextColor(mContext.getResources().getColor(R.color.color_green));
                        tvState.setBackgroundResource(R.drawable.bg_states_green);
                        break;

                }

                holder.itemView.setOnClickListener(v -> {
                    bean.setCategory(Constants.ONLINE_DECLARATION);
                    Map<String, Serializable> params = new HashMap<>();
                    params.put(Constants.PAY_LIST, bean);
                    myFragment.startFragment(params, new DetailsFragment());
                });
            }
        });
    }
}
