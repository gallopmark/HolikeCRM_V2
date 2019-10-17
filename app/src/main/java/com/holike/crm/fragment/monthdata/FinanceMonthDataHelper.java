package com.holike.crm.fragment.monthdata;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewStub;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gallopmark.recycler.adapterhelper.CommonAdapter;
import com.holike.crm.R;
import com.holike.crm.base.BaseActivity;
import com.holike.crm.base.BaseFragment;
import com.holike.crm.bean.MonthDataFinanceBean;

import java.util.Date;
import java.util.List;

/**
 * Created by gallop on 2019/8/9.
 * Copyright holike possess 2019.
 * 财务本月数据
 */
class FinanceMonthDataHelper extends MonthDataHelper {

    private ViewStub mContentViewStub;
    private View mFragmentView;
    private View mContentView;

    FinanceMonthDataHelper(BaseFragment<?, ?> fragment, Callback callback) {
        super((BaseActivity<?, ?>) fragment.getActivity(), callback);
        mFragmentView = fragment.getContentView();
        mContentViewStub = mFragmentView.findViewById(R.id.form_data_vs);
        Bundle bundle = fragment.getArguments();
        boolean isAnimation = false;
        if (bundle != null) {
            mType = bundle.getString("type");
            mCityCode = bundle.getString("cityCode");
            mStartDate = (Date) bundle.getSerializable("startDate");
            mEndDate = (Date) bundle.getSerializable("endDate");
            isAnimation = bundle.getBoolean("isAnimation");
        }
        long delayMillis = 0;
        if (isAnimation) {
            delayMillis = 300;
        }
        mFragmentView.postDelayed(mAction, delayMillis);
    }

    private final Runnable mAction = this::onQuery;

    void onDestroy() {
        mFragmentView.removeCallbacks(mAction);
    }

    void onSuccess(MonthDataFinanceBean bean) {
        if (!TextUtils.equals(bean.isShop, "1") && !TextUtils.equals(bean.isShop, "2")) {
            return;
        }
        setTopData(bean);
        if (mContentView == null) {
            mContentView = mContentViewStub.inflate();
        }
        FrameLayout contentLayout = mContentView.findViewById(R.id.fl_form_data_container);
        if (contentLayout.getChildCount() >= 2) {
            contentLayout.removeViewAt(1);
        }
        String isShop = bean.isShop;
        View firstLayout;
        if (TextUtils.equals(isShop, "1")) {
            firstLayout = LayoutInflater.from(mActivity).inflate(R.layout.include_form_data_firstrow_60dp, contentLayout, false);
        } else {
            firstLayout = LayoutInflater.from(mActivity).inflate(R.layout.include_form_data_firstrow_80dp, contentLayout, false);
        }
        TextView tvFirst = firstLayout.findViewById(R.id.tv_first_name);
        contentLayout.addView(firstLayout, 1);
        RecyclerView sideRecyclerView = mContentView.findViewById(R.id.rv_side);
        RecyclerView contentRecyclerView = mFragmentView.findViewById(R.id.rv_content);
        sideRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        contentRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        contentRecyclerView.setNestedScrollingEnabled(false);
        setScrollSynchronous(sideRecyclerView, contentRecyclerView);
        setScrollSynchronous(contentRecyclerView, sideRecyclerView);
        LinearLayout scrollableLayout = mContentView.findViewById(R.id.ll_scrollable_content);
        if (scrollableLayout.getChildCount() >= 2) {
            scrollableLayout.removeViewAt(0);
        }
        //isShop 1门店数据 2门店人员收款数据
        if (TextUtils.equals(isShop, "1")) {
            tvFirst.setText(mActivity.getString(R.string.report_table_divide));
            View view = LayoutInflater.from(mActivity).inflate(R.layout.include_monthdata_finance_tablist, scrollableLayout, false);
            scrollableLayout.addView(view, 0);
            sideRecyclerView.setAdapter(new ClickableSideAdapter(mActivity, bean.getArr()));
            contentRecyclerView.setAdapter(new ScrollableContentAdapter(mActivity, bean.getArr()));
        } else {
            TextView tvUnit = mFragmentView.findViewById(R.id.tv_unit);
            tvUnit.setVisibility(View.GONE);
            tvFirst.setText(mActivity.getString(R.string.tips_customer_name));
            View view = LayoutInflater.from(mActivity).inflate(R.layout.include_monthdata_finance_tablist2, scrollableLayout, false);
            scrollableLayout.addView(view, 0);
            sideRecyclerView.setAdapter(new ArrBean2Adapter(mActivity, bean.getArr2()));
            contentRecyclerView.setAdapter(new ContentArrBean2Adapter(mActivity, bean.getArr2()));
        }
    }

    private void setTopData(MonthDataFinanceBean bean) {
        LinearLayout layout = mFragmentView.findViewById(R.id.ll_shop_name);
        if (!TextUtils.isEmpty(bean.shopName) || !TextUtils.isEmpty(bean.userName)) {
            layout.setVisibility(View.VISIBLE);
            TextView tvShop = layout.findViewById(R.id.tv_shop);
            tvShop.setText(bean.shopName);
            TextView tvName = layout.findViewById(R.id.tv_name);
            if (!TextUtils.isEmpty(bean.userName)) {
                String text = mActivity.getString(R.string.customer_name_tips);
                int start = text.length();
                text += bean.userName;
                int end = text.length();
                SpannableString ss = new SpannableString(text);
                ss.setSpan(new StyleSpan(Typeface.BOLD), start, end, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
                tvName.setText(ss);
            }
        } else {
            layout.setVisibility(View.GONE);
        }
    }

    /*财务首层第一列数据*/
    class ClickableSideAdapter extends CommonAdapter<MonthDataFinanceBean.ArrBean> {

        ClickableSideAdapter(Context context, List<MonthDataFinanceBean.ArrBean> mDatas) {
            super(context, mDatas);
        }

        @Override
        protected int bindView(int viewType) {
            return R.layout.item_month_data_finance_side1;
        }

        @Override
        public void onBindHolder(RecyclerHolder holder, MonthDataFinanceBean.ArrBean bean, int position) {
            if (position % 2 == 0) {
                holder.itemView.setBackgroundResource(R.color.textColor24);
            } else {
                holder.itemView.setBackgroundResource(R.color.color_while);
            }
            holder.setText(R.id.tv_division, bean.area);
            holder.setText(R.id.tv_principal, bean.name); //负责人
            holder.setText(R.id.tv_receipted, bean.receivables); //已收款
            holder.setText(R.id.tv_deposit, bean.deposit); //定金
            holder.setText(R.id.tv_contract_amount, bean.contract); //合同款
            holder.setText(R.id.tv_tail, bean.tail); //尾款
            holder.setText(R.id.tv_total, bean.contractTotal); //成交总金额
            if (bean.isClickable()) {
                holder.setTextColorRes(R.id.tv_division, R.color.colorAccent);
                holder.itemView.setEnabled(true);
            } else {
                holder.setTextColorRes(R.id.tv_division, R.color.textColor8);
                holder.itemView.setEnabled(false);
            }
            holder.itemView.setOnClickListener(view -> startFragment(bean.type, bean.cityCode));
        }
    }

    /*财务首层数据*/
    class ScrollableContentAdapter extends CommonAdapter<MonthDataFinanceBean.ArrBean> {

        ScrollableContentAdapter(Context context, List<MonthDataFinanceBean.ArrBean> mDatas) {
            super(context, mDatas);
        }

        @Override
        public void onBindHolder(RecyclerHolder holder, MonthDataFinanceBean.ArrBean bean, int position) {
            if (position % 2 == 0) {
                holder.itemView.setBackgroundResource(R.color.textColor24);
            } else {
                holder.itemView.setBackgroundResource(R.color.color_while);
            }
            holder.setText(R.id.tv_division, bean.area);
            holder.setText(R.id.tv_principal, bean.name); //负责人
            holder.setText(R.id.tv_receipted, bean.receivables); //已收款
            if (bean.isClickable2()) {
                holder.setEnabled(R.id.tv_receipted, true);
                holder.setTextColorRes(R.id.tv_receipted, R.color.colorAccent);
            } else {
                holder.setEnabled(R.id.tv_receipted, false);
                holder.setTextColorRes(R.id.tv_receipted, R.color.textColor8);
            }
            holder.setOnClickListener(R.id.tv_receipted, view -> startFragment(bean.type, bean.cityCode));
            holder.setText(R.id.tv_deposit, bean.deposit); //定金
            holder.setText(R.id.tv_contract_amount, bean.contract); //合同款
            holder.setText(R.id.tv_tail, bean.tail); //尾款
            holder.setText(R.id.tv_total, bean.contractTotal); //成交总金额
        }

        @Override
        protected int bindView(int viewType) {
            return R.layout.item_month_data_finance_content1;
        }
    }

    class ArrBean2Adapter extends CommonAdapter<MonthDataFinanceBean.ArrBean2> {

        ArrBean2Adapter(Context context, List<MonthDataFinanceBean.ArrBean2> mDatas) {
            super(context, mDatas);
        }

        @Override
        protected int bindView(int viewType) {
            return R.layout.item_month_data_finance_side2;
        }

        @Override
        public void onBindHolder(RecyclerHolder holder, MonthDataFinanceBean.ArrBean2 bean, int position) {
            if (position % 2 == 0) {
                holder.itemView.setBackgroundResource(R.color.textColor24);
            } else {
                holder.itemView.setBackgroundResource(R.color.color_while);
            }
            holder.setText(R.id.tv_customer_name, bean.name);
            holder.setText(R.id.tv_receipt_category, bean.type); //收款类别
            holder.setText(R.id.tv_receipt_amount, bean.money); //收款金额
            holder.setText(R.id.tv_receipt_time, bean.payTime); //收款时间
        }
    }

    class ContentArrBean2Adapter extends CommonAdapter<MonthDataFinanceBean.ArrBean2> {

        ContentArrBean2Adapter(Context context, List<MonthDataFinanceBean.ArrBean2> mDatas) {
            super(context, mDatas);
        }

        @Override
        public void onBindHolder(RecyclerHolder holder, MonthDataFinanceBean.ArrBean2 bean, int position) {
            if (position % 2 == 0) {
                holder.itemView.setBackgroundResource(R.color.textColor24);
            } else {
                holder.itemView.setBackgroundResource(R.color.color_while);
            }
            holder.setText(R.id.tv_customer_name, bean.name);
            holder.setText(R.id.tv_receipt_category, bean.type); //收款类别
            holder.setText(R.id.tv_receipt_amount, bean.money); //收款金额
            holder.setText(R.id.tv_receipt_time, bean.payTime); //收款时间
        }

        @Override
        protected int bindView(int viewType) {
            return R.layout.item_month_data_finance_content2;
        }
    }

    private void startFragment(String type, String cityCode) {
        mActivity.startFragment(new FinanceMonthDataFragment(), obtainBundle(type, cityCode), true);
    }

    private Bundle obtainBundle(String type, String cityCode) {
        Bundle bundle = new Bundle();
        bundle.putString("type", type);
        bundle.putString("cityCode", cityCode);
        bundle.putSerializable("startDate", mStartDate);
        bundle.putSerializable("endDate", mEndDate);
        bundle.putBoolean("isAnimation", true);
        return bundle;
    }
}
