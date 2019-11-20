package com.holike.crm.fragment.monthdata;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewStub;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.holike.crm.R;
import com.holike.crm.adapter.AbsFormAdapter;
import com.holike.crm.base.BaseActivity;
import com.holike.crm.base.BaseFragment;
import com.holike.crm.bean.MonthDataDesignBean;
import com.holike.crm.util.RecyclerUtils;

import java.util.List;


class DesignManagerMonthDataHelper extends MonthDataHelper {

    private FrameLayout mContainerLayout;

    DesignManagerMonthDataHelper(BaseFragment<?, ?> fragment, Callback callback) {
        super(fragment, callback);
    }

    @Override
    void initView() {
        mContainerLayout = mFragmentView.findViewById(R.id.fl_container);
    }

    void onSuccess(MonthDataDesignBean bean) {
        mContainerLayout.removeAllViews();
        View view = LayoutInflater.from(mContext).inflate(R.layout.include_month_data_designmanager_content, mContainerLayout, true);
        TextView tvDatetime = view.findViewById(R.id.tv_datetime);
        tvDatetime.setText(bean.timeDetail);
        TextView tvShop = view.findViewById(R.id.tv_shop_detail);
        if (!TextUtils.isEmpty(bean.shopDetail)) {
            tvShop.setText(bean.shopDetail);
            tvShop.setVisibility(View.VISIBLE);
        } else {
            tvShop.setVisibility(View.GONE);
        }
        ViewStub vs = view.findViewById(R.id.vs_content);
        if (bean.getArr().isEmpty()) {   //没有数据
            vs.setLayoutResource(R.layout.include_empty_textview);
            vs.inflate();
        } else {
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) vs.getLayoutParams();
            lp.leftMargin = mContext.getResources().getDimensionPixelSize(R.dimen.dp_10);
            vs.setLayoutResource(R.layout.include_form_data_content);
            View contentView = vs.inflate();
            requestUpdate(bean, contentView);
        }
    }

    private void requestUpdate(MonthDataDesignBean bean, View contentView) {
        FrameLayout contentLayout = contentView.findViewById(R.id.fl_form_data_container);
        final RecyclerView contentRecyclerView = contentView.findViewById(R.id.rv_content);
        contentRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        contentRecyclerView.setNestedScrollingEnabled(false);
        View firstLayout = LayoutInflater.from(mContext).inflate(R.layout.include_form_data_column_60dp, contentLayout, false);
        TextView tvFirst = firstLayout.findViewById(R.id.tv_first_name);
        tvFirst.setText(mContext.getString(R.string.report_table_divide));
        contentLayout.addView(firstLayout, 1);  //动态添加layout 第一列数据展示布局
        RecyclerView sideRecyclerView = contentView.findViewById(R.id.rv_side);
        sideRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        LinearLayout scrollableLayout = contentView.findViewById(R.id.ll_scrollable_content);
        View view = LayoutInflater.from(mContext).inflate(R.layout.include_monthdata_designmanager_tablist, scrollableLayout, false);
        scrollableLayout.addView(view, 0);
        sideRecyclerView.setAdapter(new SideAdapter(mContext, bean.getArr()));
        contentRecyclerView.setAdapter(new ContentAdapter(mContext, bean.getArr()));
        RecyclerUtils.setScrollSynchronous(sideRecyclerView, contentRecyclerView);
    }

    void onFailure(String failReason) {
        mContainerLayout.removeAllViews();
        LayoutInflater.from(mContext).inflate(R.layout.include_empty_page, mContainerLayout, true);
        mFragment.noNetwork(failReason);
    }

    private final class SideAdapter extends AbsFormAdapter<MonthDataDesignBean.ArrBean> {

        SideAdapter(Context context, List<MonthDataDesignBean.ArrBean> dataList) {
            super(context, dataList);
        }

        @Override
        protected int bindView(int viewType) {
            return R.layout.item_month_data_designmanager_side;
        }

        @Override
        protected void bindViewHolder(RecyclerHolder holder, MonthDataDesignBean.ArrBean bean, int position) {
            if (bean.isClickable()) {
                holder.setTextColorRes(R.id.tv_division, R.color.colorAccent);
                holder.itemView.setEnabled(true);
            } else {
                holder.setTextColorRes(R.id.tv_division, R.color.textColor8);
                holder.itemView.setEnabled(false);
            }
            holder.setText(R.id.tv_division, bean.name); //划分
            holder.setText(R.id.tv_number_measure, bean.scaleCount); //量尺数
            holder.setText(R.id.tv_number_drawing, bean.picCount); //出图数
            holder.setText(R.id.tv_number_signatures, bean.contractCount); //签约数
            holder.setText(R.id.tv_number_place_orders, bean.orders); //下单数
            holder.setText(R.id.tv_deposit_customers, bean.receivables); //已收款
            holder.setText(R.id.tv_contract_amount, bean.contractTotal); //成交总金额
            holder.itemView.setOnClickListener(view -> startFragment(bean.type, bean.cityCode));
        }
    }

    private void startFragment(String type, String cityCode) {
        ((BaseActivity<?, ?>) mContext).startFragment(new DesignManagerMonthDataFragment(), obtainBundle(type, cityCode));
    }

    private Bundle obtainBundle(String type, String cityCode) {
        android.os.Bundle bundle = new Bundle();
        bundle.putString("type", type);
        bundle.putString("cityCode", cityCode);
        bundle.putSerializable("startDate", mStartDate);
        bundle.putSerializable("endDate", mEndDate);
        bundle.putBoolean("isAnimation", true);
        return bundle;
    }

    private final class ContentAdapter extends AbsFormAdapter<MonthDataDesignBean.ArrBean> {

        ContentAdapter(Context context, List<MonthDataDesignBean.ArrBean> dataList) {
            super(context, dataList);
        }

        @Override
        protected int bindView(int viewType) {
            return R.layout.item_month_data_designmanager_content;
        }

        @Override
        protected void bindViewHolder(RecyclerHolder holder, MonthDataDesignBean.ArrBean bean, int position) {
            if (bean.isClickable()) {
                holder.setTextColorRes(R.id.tv_division, R.color.colorAccent);
                holder.itemView.setEnabled(true);
            } else {
                holder.setTextColorRes(R.id.tv_division, R.color.textColor8);
                holder.itemView.setEnabled(false);
            }
            holder.setText(R.id.tv_division, bean.name); //划分
            holder.setText(R.id.tv_number_measure, bean.scaleCount); //量尺数
            holder.setText(R.id.tv_number_drawing, bean.picCount); //出图数
            holder.setText(R.id.tv_number_signatures, bean.contractCount); //签约数
            holder.setText(R.id.tv_number_place_orders, bean.orders); //下单数
            holder.setText(R.id.tv_deposit_customers, bean.receivables); //已收款
            holder.setText(R.id.tv_contract_amount, bean.contractTotal); //成交总金额
        }
    }

}
