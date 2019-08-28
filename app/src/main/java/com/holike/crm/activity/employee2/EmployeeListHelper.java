package com.holike.crm.activity.employee2;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gallopmark.recycler.adapterhelper.CommonAdapter;
import com.holike.crm.R;
import com.holike.crm.base.BaseActivity;
import com.holike.crm.base.IntentValue;
import com.holike.crm.bean.DistributionStoreBean;
import com.holike.crm.bean.EmployeeBeanV2;
import com.holike.crm.bean.MultiItem;
import com.holike.crm.bean.NoMoreBean;
import com.holike.crm.bean.RoleDataBean;
import com.holike.crm.popupwindown.StringItemPopupWindow;
import com.holike.crm.util.KeyBoardUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gallop on 2019/8/6.
 * Copyright holike possess 2019.
 */
class EmployeeListHelper {

    private BaseActivity<?, ?> mActivity;
    private String mShopId;
    //    private String mStatus;
    private String mRoleCode;

    private int mSelectShop, mSelectRole;  //已选的门店、角色index

    private boolean mIsRefresh;
    private List<MultiItem> mItems;
    private EmployeeListAdapter mAdapter;
    private Callback mCallback;

    EmployeeListHelper(BaseActivity<?, ?> activity, Callback callback) {
        this.mActivity = activity;
        this.mCallback = callback;
        mItems = new ArrayList<>();
        mAdapter = new EmployeeListAdapter(mActivity, mItems);
        attach();
        onQuery();
    }

    private void attach() {
        RecyclerView recyclerView = mActivity.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        recyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener((adapter, holder, view, position) -> {
            EmployeeBeanV2 bean = getItem(position);
            if (bean != null) {
                Bundle bundle = new Bundle();
                bundle.putString("userId", bean.userId);
//                bundle.putString("shopName", bean.shopName);
//                bundle.putString("roleName", bean.roleName);
                mActivity.startActivity(EmployeeDetailsActivity.class, bundle);
            }
        });
    }

    private EmployeeBeanV2 getItem(int position) {
        if (position < 0 || position > mItems.size()) return null;
        if (mItems.get(position).getItemType() == 2) {
            return null;
        }
        return (EmployeeBeanV2) mItems.get(position);
    }

    void onViewClicked(View view) {
        long delayMillis = highKeyboard();
        switch (view.getId()) {
            case R.id.tv_select_shop:
                view.postDelayed(() -> onSelectShop((TextView) view), delayMillis);
                break;
            case R.id.tv_select_role:
                view.postDelayed(() -> onSelectRole((TextView) view), delayMillis);
                break;
        }
    }

    private long highKeyboard() {
        if (KeyBoardUtil.isSoftShowing(mActivity)) {
            KeyBoardUtil.hideSoftInput(mActivity);
            return 500;
        }
        return 0;
    }

    /*选择门店*/
    private void onSelectShop(final TextView tv) {
        List<DistributionStoreBean> list = new ArrayList<>(IntentValue.getInstance().getShopData());
        list.add(0, new DistributionStoreBean("", mActivity.getString(R.string.all)));
        List<String> items = new ArrayList<>();
        for (DistributionStoreBean bean : list) {
            items.add(bean.shopName);
        }
        if (items.size() == 1) return;
        onArrowUp(tv);
        StringItemPopupWindow popupWindow = new StringItemPopupWindow(mActivity, items, mSelectShop);
        popupWindow.setOnMenuItemClickListener((pw, position, content) -> {
            mSelectShop = position;
            mShopId = list.get(position).shopId;
            tv.setText(list.get(position).shopName);
            if (position == 0) {
                setUnselected(tv, mActivity.getString(R.string.employee_list_select_shop));
            } else {
                setSelected(tv);
            }
            pw.dismiss();
            onQuery();
        });
        popupWindow.showAsDown(tv);
        popupWindow.setOnDismissListener(() -> onArrowDown(tv));
    }

    /*选择角色*/
    private void onSelectRole(final TextView tv) {
        List<RoleDataBean> list = new ArrayList<>(IntentValue.getInstance().getRoleData());
        list.add(0, new RoleDataBean("", mActivity.getString(R.string.all)));
        List<String> items = new ArrayList<>();
        for (RoleDataBean bean : list) {
            items.add(bean.roleName);
        }
        if (items.size() == 1) return;
        onArrowUp(tv);
        StringItemPopupWindow popupWindow = new StringItemPopupWindow(mActivity, items, mSelectRole);
        popupWindow.setOnMenuItemClickListener((pw, position, content) -> {
            mSelectRole = position;
            mRoleCode = list.get(position).roleCode;
            tv.setText(list.get(position).roleName);
            if (position == 0) {
                setUnselected(tv, mActivity.getString(R.string.employee_list_select_role));
            } else {
                setSelected(tv);
            }
            pw.dismiss();
            onQuery();
        });
        popupWindow.showAsDown(tv);
        popupWindow.setOnDismissListener(() -> onArrowDown(tv));
    }

    private void setSelected(TextView tv) {
        tv.setTextColor(ContextCompat.getColor(mActivity, R.color.textColor4));
    }

    private void setUnselected(TextView tv, String unselectedText) {
        tv.setTextColor(ContextCompat.getColor(mActivity, R.color.textColor8));
        tv.setText(unselectedText);
    }

    private void onArrowUp(TextView tv) {
        tv.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.choice_up, 0);
    }

    private void onArrowDown(TextView tv) {
        tv.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.choice_down, 0);
    }

    /*点击键盘搜索 重置状态*/
    void doSearch() {
        mShopId = null;
        mRoleCode = null;
        setUnselected(mActivity.findViewById(R.id.tv_select_shop), mActivity.getString(R.string.employee_list_select_shop));
        setUnselected(mActivity.findViewById(R.id.tv_select_role), mActivity.getString(R.string.employee_list_select_role));
        onQuery();
    }

    void onQuery() {
        mCallback.onQuery(mShopId, mRoleCode, mIsRefresh);
    }

    void onRefresh() {
        mIsRefresh = true;
        onQuery();
    }

    void onSuccess(List<EmployeeBeanV2> list, SmartRefreshLayout refreshLayout) {
        refreshLayout.finishRefresh();
        if (list.isEmpty()) {
            mActivity.noResult();
        } else {
            mActivity.hasData();
            if (refreshLayout.getVisibility() != View.VISIBLE) {
                refreshLayout.setVisibility(View.VISIBLE);
            }
            this.mItems.clear();
            this.mItems.addAll(list);
            this.mItems.add(new NoMoreBean());
            mAdapter.notifyDataSetChanged();
        }
    }

    void onFailure(String failReason, SmartRefreshLayout refreshLayout) {
        refreshLayout.finishRefresh();
        if (mActivity.isNoAuth(failReason)) {
            mActivity.noAuthority();
        } else {
            if (mIsRefresh) {
                mActivity.showShortToast(failReason);
            } else {
                refreshLayout.setVisibility(View.GONE);
                mActivity.noNetwork();
            }
        }
        mIsRefresh = false;
    }

    static class EmployeeListAdapter extends CommonAdapter<MultiItem> {

        EmployeeListAdapter(Context context, List<MultiItem> mDatas) {
            super(context, mDatas);
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
            return R.layout.item_employeelistv2;
        }

        @Override
        public void onBindHolder(RecyclerHolder holder, MultiItem multiItem, int position) {
            if (holder.getItemViewType() == 2) {
                holder.setText(R.id.mNoMoreTextView, mContext.getString(R.string.complete_loading));
            } else {
                EmployeeBeanV2 bean = (EmployeeBeanV2) multiItem;
                holder.setText(R.id.tv_short_name, bean.getShortName());
                holder.setText(R.id.tv_name, bean.name);
                holder.setText(R.id.tv_phone, bean.phone);
//                holder.setText(R.id.mNumberTv, bean.userId);
                holder.setText(R.id.tv_role_name, bean.roleName);
                holder.setText(R.id.tv_shop_name, bean.shopName);
                if (bean.isValid()) {
                    holder.setTextColor(R.id.tv_status, ContextCompat.getColor(mContext, R.color.textColor14));
                    holder.setBackgroundResource(R.id.tv_status, R.drawable.bg_textcolor14_corners2);
                    holder.setText(R.id.tv_status, mContext.getString(R.string.valid));
                    holder.setBackgroundResource(R.id.tv_short_name, R.drawable.bg_blue_round);
                } else {
                    holder.setTextColor(R.id.tv_status, ContextCompat.getColor(mContext, R.color.textColor21));
                    holder.setBackgroundResource(R.id.tv_status, R.drawable.bg_textcolor21_corners2);
                    holder.setText(R.id.tv_status, mContext.getString(R.string.invalid));
                    holder.setBackgroundResource(R.id.tv_short_name, R.drawable.bg_textcolor21_oval);
                }
                if (position < getItemCount()) {
                    holder.setVisibility(R.id.v_divider, true);
                } else {
                    holder.setVisibility(R.id.v_divider, false);
                }
            }
        }
    }

    interface Callback {
        void onQuery(String shopId, String roleCode, boolean isRefresh);
    }
}
