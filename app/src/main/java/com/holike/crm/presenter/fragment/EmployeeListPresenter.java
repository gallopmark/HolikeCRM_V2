package com.holike.crm.presenter.fragment;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.gallopmark.recycler.adapterhelper.CommonAdapter;
import com.holike.crm.R;
import com.holike.crm.base.BasePresenter;
import com.holike.crm.bean.DistributionStoreBean;
import com.holike.crm.bean.EmployeeBean;
import com.holike.crm.model.fragment.EmployeeModel;
import com.holike.crm.popupwindown.StringItemPopupWindow;
import com.holike.crm.view.fragment.EmployeeListView;

import java.util.ArrayList;
import java.util.List;

public class EmployeeListPresenter extends BasePresenter<EmployeeListView, EmployeeModel> {
    private List<EmployeeBean> beans = new ArrayList<>();
    private EmployeeListAdapter adapter;
    private int mStoreSelected = 0;
    private int mBillSelected = 0;

    private class EmployeeListAdapter extends CommonAdapter<EmployeeBean> {
        private static final int TYPE_ITEM = 1;
        private static final int TYPE_FOOTER = 2;

        private EmployeeListAdapter(Context context, List<EmployeeBean> datas) {
            super(context, datas);
        }

        @Override
        public int getItemViewType(int position) {
            if (position == getItemCount() - 1) {
                return TYPE_FOOTER;
            }
            return TYPE_ITEM;
        }

        @Override
        protected int bindView(int viewType) {
            if (viewType == TYPE_FOOTER) {
                return R.layout.item_employeelist_footer;
            }
            return R.layout.item_employeelist;
        }

        @Override
        public void onBindHolder(RecyclerHolder holder, EmployeeBean bean, int position) {
            if (holder.getItemViewType() == 2) {
                holder.setText(R.id.mFooterTextView, mContext.getString(R.string.complete_loading));
            } else {
                holder.setText(R.id.mShortNameTv, bean.getShortName());
                holder.setText(R.id.mNameTv, bean.getName());
                holder.setText(R.id.mPhoneTv, bean.getPhone());
                holder.setText(R.id.mNumberTv, bean.getUserId());
                if (TextUtils.equals(bean.getStatus(), "1")) {
                    holder.setTextColor(R.id.mStatusTv, ContextCompat.getColor(mContext, R.color.textColor14));
                    holder.setBackgroundResource(R.id.mStatusTv, R.drawable.bg_textcolor14_corners2);
                    holder.setText(R.id.mStatusTv, mContext.getString(R.string.valid));
                    holder.setVisibility(R.id.mStatusTv, View.VISIBLE);
                    holder.setBackgroundResource(R.id.mShortNameTv, R.drawable.bg_blue_round);
                } else if (TextUtils.equals(bean.getStatus(), "0")) {
                    holder.setTextColor(R.id.mStatusTv, ContextCompat.getColor(mContext, R.color.textColor21));
                    holder.setBackgroundResource(R.id.mStatusTv, R.drawable.bg_textcolor21_corners2);
                    holder.setText(R.id.mStatusTv, mContext.getString(R.string.invalid));
                    holder.setVisibility(R.id.mStatusTv, View.VISIBLE);
                    holder.setBackgroundResource(R.id.mShortNameTv, R.drawable.bg_textcolor21_oval);
                } else {
                    holder.setVisibility(R.id.mStatusTv, View.GONE);
                    holder.setBackgroundResource(R.id.mShortNameTv, R.drawable.bg_blue_round);
                }
                if (position < getItemCount()) {
                    holder.setVisibility(R.id.mDividerV, true);
                } else {
                    holder.setVisibility(R.id.mDividerV, false);
                }
            }
        }
    }

    public void setAdapter(Context context, RecyclerView recyclerView, OnItemClickListener itemClickListener) {
        adapter = new EmployeeListAdapter(context, this.beans);
        adapter.setOnItemClickListener((adapter, holder, view, position) -> {
            if (position >= 0 && position < beans.size()) {
                if (itemClickListener != null) {
                    itemClickListener.onItemClick(beans.get(position), position);
                }
            }
        });
        recyclerView.setAdapter(adapter);
    }

    public void addAndNotifyDataSetChanged(List<EmployeeBean> beans) {
        this.beans.clear();
        this.beans.addAll(beans);
        this.beans.add(new EmployeeBean()); //footView
//        if (countBean != null) {
//            this.countBean = countBean;
//            this.beans.add(this.countBean);
//        }
        adapter.notifyDataSetChanged();
    }

    public void clearData() {
        this.beans.clear();
        adapter.notifyDataSetChanged();
    }

    public void getEmployeeList(@NonNull String content, @NonNull String shopId, @NonNull String status) {
        model.getEmployeeList(content, shopId, status, new EmployeeModel.GetEmployeeListListener() {
            @Override
            public void onStart() {
                if (getView() != null)
                    getView().onShowLoading();
            }

            @Override
            public void onSuccess(List<EmployeeBean> mData) {
                if (getView() != null)
                    getView().getEmployeeList(mData);
            }

            @Override
            public void onFailure(String errorMessage) {
                if (getView() != null)
                    getView().getEmployeeListError(errorMessage);
            }

            @Override
            public void onFinished() {
                if (getView() != null)
                    getView().onHideLoading();
            }
        });
    }

    public void getStoreList(EmployeeModel.OnGetStoreCallback getDictionaryListener) {
        model.getStoreList(getDictionaryListener);
    }

    public void showPopupWindow(Context context, View parent, List<DistributionStoreBean> dictionaryBeans,
                                OnMenuSelectedListener onMenuSelectedListener, TextView arrowView, final int type) {
        if (dictionaryBeans == null || dictionaryBeans.size() == 0) return;
        final List<DistributionStoreBean> list = new ArrayList<>(dictionaryBeans);
        list.add(0, new DistributionStoreBean("", context.getString(R.string.all)));
        arrowView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.choice_up, 0);
        List<String> items = new ArrayList<>();
        for (DistributionStoreBean bean : list) {
            items.add(bean.getShopName());
        }
        int selectPosition;
        if (type == 1) {
            selectPosition = mStoreSelected;
        } else {
            selectPosition = mBillSelected;
        }
        StringItemPopupWindow popupWindow = new StringItemPopupWindow(context, items, selectPosition).setOnMenuItemClickListener((pw, position, content) -> {
            if (type == 1) {
                mStoreSelected = position;
            } else {
                mBillSelected = position;
            }
            if (onMenuSelectedListener != null)
                onMenuSelectedListener.onSelected(list.get(position), position == 0);
            pw.dismiss();
        });
        popupWindow.setOnDismissListener(() -> arrowView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.choice_down, 0));
        popupWindow.showAsDown(parent);
    }

    public interface OnMenuSelectedListener {
        void onSelected(DistributionStoreBean bean, boolean isFirst);
    }

    public interface OnItemClickListener {
        void onItemClick(EmployeeBean bean, int position);
    }
}
