package com.holike.crm.presenter.fragment;

import android.content.Context;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.TextView;

import com.gallopmark.recycler.adapterhelper.CommonAdapter;
import com.holike.crm.R;
import com.holike.crm.base.BasePresenter;
import com.holike.crm.bean.HomepageBean;
import com.holike.crm.bean.MultiItem;
import com.holike.crm.bean.NoMoreBean;
import com.holike.crm.bean.PayListBean;
import com.holike.crm.model.fragment.PayListModel;
import com.holike.crm.popupwindown.StringItemPopupWindow;
import com.holike.crm.util.KeyBoardUtil;
import com.holike.crm.view.fragment.PayListView;

import java.util.ArrayList;
import java.util.List;

public class PayListPresenter extends BasePresenter<PayListView, PayListModel> {

    private List<HomepageBean.TypeListBean.BrankDataBean> mBankDataBeans;
    private int mSelectPosition;
    private List<MultiItem> mBeans = new ArrayList<>();
    private PayListAdapter mAdapter;

    static class PayListAdapter extends CommonAdapter<MultiItem> {

        PayListAdapter(Context context, List<MultiItem> mDatas) {
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
        mAdapter = new PayListAdapter(recyclerView.getContext(), mBeans);
        recyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener((adapter, holder, view, position) -> {
            if (position >= 0 && position < mBeans.size()) {
                if (mBeans.get(position) instanceof PayListBean) {
                    PayListBean bean = (PayListBean) mBeans.get(position);
                    if (getView() != null) getView().onItemClick(bean);
                }
            }
        });
    }

    public void clearData() {
        this.mBeans.clear();
    }

    public void onRefreshCompleted(List<PayListBean> beans) {
        clearData();
        onLoadMoreCompleted(beans);
    }

    public void onLoadMoreCompleted(List<PayListBean> beans) {
        if (beans != null && !beans.isEmpty()) {
            this.mBeans.addAll(beans);
        }
        mAdapter.notifyDataSetChanged();
    }

    public void noMoreData() {
        mBeans.add(new NoMoreBean());
        mAdapter.notifyDataSetChanged();
    }

    public void getData(int pageNo, String startTime, String endTime, String searchContent, String status) {
        if (getModel() != null) {
            getModel().getData(String.valueOf(pageNo), startTime, endTime, searchContent, status, new PayListModel.PayListListener() {
                @Override
                public void success(List<PayListBean> bean) {
                    if (getView() != null)
                        getView().onSuccess(bean);
                }

                @Override
                public void fail(String errorMsg) {
                    if (getView() != null)
                        getView().onFail(errorMsg);
                }
            });
        }
    }

    public void addBankDataBeans(List<HomepageBean.TypeListBean.BrankDataBean> beans) {
        if (beans != null && !beans.isEmpty()) {
            mBankDataBeans = new ArrayList<>(beans);
        }
    }

    public void showStatePopupWindow(Context context, View mContentView, View parent) {
        if (mBankDataBeans == null || mBankDataBeans.isEmpty()) return;
        long delayMills = 0;
        if (KeyBoardUtil.isKeyboardShown(mContentView)) {
            KeyBoardUtil.hideKeyboard(mContentView);
            delayMills = 200;
        }
        parent.postDelayed(() -> showPopupWindow(context, parent), delayMills);
    }

    private void showPopupWindow(Context context, View parent) {
        final List<HomepageBean.TypeListBean.BrankDataBean> list = new ArrayList<>(mBankDataBeans);
        list.add(0, new HomepageBean.TypeListBean.BrankDataBean("", context.getString(R.string.all)));
        List<String> items = new ArrayList<>();
        for (HomepageBean.TypeListBean.BrankDataBean bean : list) {
            items.add(bean.getBarkName());
        }
        StringItemPopupWindow popupWindow = new StringItemPopupWindow(context, items, mSelectPosition);
        popupWindow.setOnMenuItemClickListener((pw, position, content) -> {
            mSelectPosition = position;
            if (getView() != null)
                getView().onFilterItemSelect(list.get(position));
            popupWindow.dismiss();
        });
        popupWindow.showAsDown(parent);
        if (getView() != null) getView().onPopupWindowShowing();
        popupWindow.setOnDismissListener(() -> {
            if (getView() != null) getView().onPopupWindowDismiss();
        });
    }
}
