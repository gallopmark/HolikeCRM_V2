package com.holike.crm.presenter.fragment;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.gallopmark.recycler.adapterhelper.CommonAdapter;
import com.holike.crm.R;
import com.holike.crm.base.BasePresenter;
import com.holike.crm.bean.HomepageBean;
import com.holike.crm.bean.MultiItem;
import com.holike.crm.bean.NoMoreBean;
import com.holike.crm.bean.PayListBean;
import com.holike.crm.model.fragment.PayListModel;
import com.holike.crm.popupwindown.PopupWindowUtils;
import com.holike.crm.util.DensityUtil;
import com.holike.crm.util.KeyBoardUtil;
import com.holike.crm.view.fragment.PayListView;

import java.util.ArrayList;
import java.util.List;

public class PayListPresenter extends BasePresenter<PayListView, PayListModel> {

    private List<HomepageBean.TypeListBean.BrankDataBean> bankDataBeans = new ArrayList<>();
    private int selectPosition;
    private List<MultiItem> mBeans = new ArrayList<>();
    private PayListAdapter adapter;

    private static class PayListAdapter extends CommonAdapter<MultiItem> {

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
        adapter = new PayListAdapter(recyclerView.getContext(), mBeans);
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
        adapter.notifyDataSetChanged();
    }

    public void noMoreData() {
        mBeans.add(new NoMoreBean());
        adapter.notifyDataSetChanged();
    }

    public void getData(int pageNo, String startTime, String endTime, String searchContent, String status) {
        model.getData(String.valueOf(pageNo), startTime, endTime, searchContent, status, new PayListModel.PayListListener() {
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

    public void addBankDataBeans(List<HomepageBean.TypeListBean.BrankDataBean> beans) {
        if (beans != null && !beans.isEmpty()) {
            this.bankDataBeans.addAll(beans);
        }
    }

    public void showStatePopupWindow(Context context, View mContentView, View parent) {
        if (bankDataBeans.isEmpty()) return;
        long delayMills = 0;
        if (KeyBoardUtil.isKeyboardShown(mContentView)) {
            KeyBoardUtil.hideKeyboard(mContentView);
            delayMills = 200;
        }
        parent.postDelayed(() -> showPopupWindow(context, parent), delayMills);
    }

    private void showPopupWindow(Context context, View parent) {
        final List<HomepageBean.TypeListBean.BrankDataBean> list = new ArrayList<>(bankDataBeans);
        list.add(0, new HomepageBean.TypeListBean.BrankDataBean("", context.getString(R.string.all)));
        View contentView = LayoutInflater.from(context).inflate(R.layout.popupwindow_string_menulist, new LinearLayout(context), false);
        RecyclerView recyclerView = contentView.findViewById(R.id.mRecyclerView);
        if (list.size() > 6) {
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) recyclerView.getLayoutParams();
            params.height = (int) (DensityUtil.dp2px(40.5f) * 6.5f);
            recyclerView.setLayoutParams(params);
        }
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setBackgroundColor(ContextCompat.getColor(context, R.color.bg_dialog));
        PopupWindow popupWindow = new PopupWindow(contentView, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable());
        popupWindow.setOutsideTouchable(true);
        MenuFilterAdapter filterAdapter = new MenuFilterAdapter(context, list, selectPosition);
        filterAdapter.setOnItemClickListener((adapter, holder, view, position) -> {
            selectPosition = position;
            filterAdapter.setSelectedIndex(position);
            if (getView() != null)
                getView().onFilterItemSelect(list.get(position));
            popupWindow.dismiss();
        });
        recyclerView.setAdapter(filterAdapter);
        contentView.setOnClickListener(v -> popupWindow.dismiss());
        PopupWindowUtils.showPopupWindow(popupWindow, parent, 0, 0, Gravity.TOP);
        if (getView() != null) getView().onPopupWindowShowing();
        popupWindow.setOnDismissListener(() -> {
            if (getView() != null) getView().onPopupWindowDismiss();
        });
    }

    private class MenuFilterAdapter extends CommonAdapter<HomepageBean.TypeListBean.BrankDataBean> {
        private int selectedIndex;

        private MenuFilterAdapter(Context context, List<HomepageBean.TypeListBean.BrankDataBean> datas, int selectedIndex) {
            super(context, datas);
            this.selectedIndex = selectedIndex;
        }

        private void setSelectedIndex(int selectedIndex) {
            this.selectedIndex = selectedIndex;
            notifyDataSetChanged();
        }

        @Override
        public int getItemViewType(int position) {
            return super.getItemViewType(position);
        }

        @Override
        protected int bindView(int viewType) {
            return R.layout.item_rv_popup_filter;
        }

        @Override
        public void onBindHolder(RecyclerHolder holder, HomepageBean.TypeListBean.BrankDataBean bean, int position) {
            if (position < getItemCount()) {
                holder.setVisibility(R.id.dv_item_rv_popup_filter, true);
            } else {
                holder.setVisibility(R.id.dv_item_rv_popup_filter, false);
            }
            if (position == selectedIndex) {
                holder.itemView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.textColor5));
                holder.setTextColor(R.id.tv_item_rv_popup_filter, ContextCompat.getColor(mContext, R.color.color_while));
            } else {
                holder.itemView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.color_while));
                holder.setTextColor(R.id.tv_item_rv_popup_filter, ContextCompat.getColor(mContext, R.color.textColor8));
            }
            holder.setText(R.id.tv_item_rv_popup_filter, bean.getBarkName());
        }
    }
}
