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
import com.holike.crm.bean.MonthDataBossBean;
import com.holike.crm.helper.TextSpanHelper;

import java.util.Date;
import java.util.List;

/**
 * Created by gallop on 2019/8/9.
 * Copyright holike possess 2019.
 * 老板、店长等本月数据
 */
class BossMonthDataHelper extends MonthDataHelper {

    private ViewStub mContentViewStub;
    private View mFragmentView;
    private View mContentView;

    BossMonthDataHelper(BaseFragment<?, ?> fragment, Callback callback) {
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
            boolean isDescription = bundle.getBoolean("isDescription", true);
            mFragmentView.findViewById(R.id.tv_description).setVisibility(isDescription ? View.VISIBLE : View.GONE);
            isAnimation = bundle.getBoolean("isAnimation", false);
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

    void onSuccess(MonthDataBossBean bean) {
        if (!TextUtils.equals(bean.isShop, "1") && !TextUtils.equals(bean.isShop, "2")
                && !TextUtils.equals(bean.isShop, "3")) {
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
        String shop = bean.isShop;
        final RecyclerView contentRecyclerView = mContentView.findViewById(R.id.rv_content);
        contentRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        contentRecyclerView.setNestedScrollingEnabled(false);
        if (TextUtils.equals(shop, "1")) { //1小组数据 2员工数据 3员工收款数据
            View firstLayout = LayoutInflater.from(mActivity).inflate(R.layout.include_form_data_firstrow_60dp, contentLayout, false);
            TextView tvFirst = firstLayout.findViewById(R.id.tv_first_name);
            tvFirst.setText(mActivity.getString(R.string.report_table_divide));
            contentLayout.addView(firstLayout, 1);  //动态添加layout 第一列数据展示布局
            RecyclerView sideRecyclerView = mContentView.findViewById(R.id.rv_side);
            sideRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
            LinearLayout scrollableLayout = getScrollableLayout();
            View view = LayoutInflater.from(mActivity).inflate(R.layout.include_monthdata_boss_tablist, scrollableLayout, false);
            TextView tvInstallArea = view.findViewById(R.id.tv_install_area);
            setSquareMeter(tvInstallArea);
            scrollableLayout.addView(view, 0);
            sideRecyclerView.setAdapter(new ClickableSideAdapter(mActivity, bean.getArr()));
            ScrollableContentAdapter adapter = new ScrollableContentAdapter(mActivity, bean.getArr());
            contentRecyclerView.setAdapter(adapter);
            setScrollSynchronous(sideRecyclerView, contentRecyclerView);
            setScrollSynchronous(contentRecyclerView, sideRecyclerView);
        } else if (TextUtils.equals(shop, "2")) { //员工数据
            List<MonthDataBossBean.ArrBean2> list = bean.getArr2();
            final RecyclerView rvRole = mFragmentView.findViewById(R.id.rv_role);
            rvRole.setVisibility(View.VISIBLE);
            rvRole.setLayoutManager(new LinearLayoutManager(mActivity, RecyclerView.HORIZONTAL, false));
            final RoleListAdapter adapter = new RoleListAdapter(mActivity, list);
            rvRole.setAdapter(adapter);
            adapter.setOnItemClickListener((a, holder, view, position) -> {
                adapter.setSelectIndex(position);
                rvRole.smoothScrollToPosition(position);
                switchRole(contentLayout, position, list, contentRecyclerView);
            });
            switchRole(contentLayout, 0, list, contentRecyclerView);
        } else { //员工收款数据
            View firstLayout = LayoutInflater.from(mActivity).inflate(R.layout.include_form_data_firstrow_80dp, contentLayout, false);
            TextView tvFirst = firstLayout.findViewById(R.id.tv_first_name);
            tvFirst.setText(mActivity.getString(R.string.tips_customer_name));
            contentLayout.addView(firstLayout, 1);  //动态添加layout 第一列数据展示布局
            RecyclerView sideRecyclerView = mContentView.findViewById(R.id.rv_side);
            sideRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
            LinearLayout scrollableLayout = getScrollableLayout();
            View view = LayoutInflater.from(mActivity).inflate(R.layout.include_monthdata_boss_tablowest, scrollableLayout, false);
            scrollableLayout.addView(view, 0);
            sideRecyclerView.setAdapter(new SingleSideAdapter(mActivity, bean.getArr3()));
            contentRecyclerView.setAdapter(new SingleDetailDataAdapter(mActivity, bean.getArr3()));
            setScrollSynchronous(sideRecyclerView, contentRecyclerView);
            setScrollSynchronous(contentRecyclerView, sideRecyclerView);
        }
    }

    private void setTopData(MonthDataBossBean bean) {
        LinearLayout layout = mFragmentView.findViewById(R.id.ll_shop_name);
        if (!TextUtils.isEmpty(bean.shopDetail) || !TextUtils.isEmpty(bean.userName)) {
            layout.setVisibility(View.VISIBLE);
            TextView tvShop = layout.findViewById(R.id.tv_shop);
            tvShop.setText(bean.shopDetail);
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

    private void setSquareMeter(TextView tv) {
        tv.setText(TextSpanHelper.getSquareMeter(tv.getText().toString()));
    }

    private LinearLayout getScrollableLayout() {
        LinearLayout scrollableLayout = mContentView.findViewById(R.id.ll_scrollable_content);
        if (scrollableLayout.getChildCount() >= 2) {
            scrollableLayout.removeViewAt(0);
        }
        return scrollableLayout;
    }

    private void switchRole(FrameLayout contentLayout, int position, List<MonthDataBossBean.ArrBean2> list, RecyclerView contentRecyclerView) {
        if (list.isEmpty() || position < 0 || position > list.size()) {
            return;
        }
        MonthDataBossBean.ArrBean2 bean = list.get(position);
        //1导购 2业务员 3设计师 4 安装工 5管理员
        if (!TextUtils.equals(bean.type, "1") && !TextUtils.equals(bean.type, "2")
                && !TextUtils.equals(bean.type, "3") && !TextUtils.equals(bean.type, "4")
                && !TextUtils.equals(bean.type, "5")) {
            return;
        }
        if (contentLayout.getChildCount() >= 2) {
            contentLayout.removeViewAt(1);
        }
        View firstLayout;
        if (TextUtils.equals(bean.type, "4")) { //安装工第一列数据
            firstLayout = LayoutInflater.from(mActivity).inflate(R.layout.include_form_data_firstrow_80dp, contentLayout, false);
        } else {
            firstLayout = LayoutInflater.from(mActivity).inflate(R.layout.include_form_data_firstrow_60dp, contentLayout, false);
        }
        TextView tvFirst = firstLayout.findViewById(R.id.tv_first_name);
        tvFirst.setText(mActivity.getString(R.string.report_table_divide));
        contentLayout.addView(firstLayout, 1);  //动态添加layout 第一列数据展示布局
//        sideRecyclerView.setAdapter(new GeneralSideAdapter(mActivity, bean.getList()));
        RecyclerView sideRecyclerView = mContentView.findViewById(R.id.rv_side);
        sideRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        if (TextUtils.equals(bean.type, "1") || TextUtils.equals(bean.type, "2")) { //导购、业务员
            LinearLayout scrollableLayout = getScrollableLayout();
            View view = LayoutInflater.from(mActivity).inflate(R.layout.include_monthdata_boss_tablist2, scrollableLayout, false);
            scrollableLayout.addView(view, 0);
            sideRecyclerView = mContentView.findViewById(R.id.rv_side);
            sideRecyclerView.setAdapter(new GuideSalesmanSideAdapter(mActivity, bean.getList()));
            contentRecyclerView.setAdapter(new GuideSalesmanDetailDataAdapter(mActivity, bean.getList()));
        } else if (TextUtils.equals(bean.type, "3")) { //设计师
            LinearLayout scrollableLayout = getScrollableLayout();
            View view = LayoutInflater.from(mActivity).inflate(R.layout.include_monthdata_boss_tablist3, scrollableLayout, false);
            scrollableLayout.addView(view, 0);
            sideRecyclerView.setAdapter(new DesignerDetailSideAdapter(mActivity, bean.getList()));
            contentRecyclerView.setAdapter(new DesignerDetailDataAdapter(mActivity, bean.getList()));
        } else if (TextUtils.equals(bean.type, "4")) { //安装工
            LinearLayout scrollableLayout = getScrollableLayout();
            View view = LayoutInflater.from(mActivity).inflate(R.layout.include_monthdata_boss_tablist4, scrollableLayout, false);
            TextView tvInstallArea = view.findViewById(R.id.tv_install_area);
            setSquareMeter(tvInstallArea);
            scrollableLayout.addView(view, 0);
            sideRecyclerView.setAdapter(new InstallerDetailSideAdapter(mActivity, bean.getList()));
            contentRecyclerView.setAdapter(new InstallerDetailDataAdapter(mActivity, bean.getList()));
        } else {  //管理者
            LinearLayout scrollableLayout = getScrollableLayout();
            View view = LayoutInflater.from(mActivity).inflate(R.layout.include_monthdata_boss_tablist5, scrollableLayout, false);
            scrollableLayout.addView(view, 0);
            sideRecyclerView = mContentView.findViewById(R.id.rv_side);
            sideRecyclerView.setAdapter(new ManagerSideDetailSideAdapter(mActivity, bean.getList()));
            contentRecyclerView.setAdapter(new ManagerSideDetailDataAdapter(mActivity, bean.getList()));
        }
        setScrollSynchronous(sideRecyclerView, contentRecyclerView);
        setScrollSynchronous(contentRecyclerView, sideRecyclerView);
    }

    /*第一列门店数据*/
    class ClickableSideAdapter extends CommonAdapter<MonthDataBossBean.ArrBean> {

        ClickableSideAdapter(Context context, List<MonthDataBossBean.ArrBean> mDatas) {
            super(context, mDatas);
        }

        @Override
        protected int bindView(int viewType) {
            return R.layout.item_month_data_boss_side;
        }

        @Override
        public void onBindHolder(RecyclerHolder holder, MonthDataBossBean.ArrBean bean, int position) {
            if (position % 2 == 0) {
                holder.itemView.setBackgroundResource(R.color.textColor24);
            } else {
                holder.itemView.setBackgroundResource(R.color.color_while);
            }
            if (bean.isClickable()) {
                holder.setTextColorRes(R.id.tv_division, R.color.colorAccent);
                holder.itemView.setEnabled(true);
            } else {
                holder.setTextColorRes(R.id.tv_division, R.color.textColor8);
                holder.itemView.setEnabled(false);
            }
//            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) tvDivision.getLayoutParams();
            holder.setText(R.id.tv_division, bean.area); //划分
            holder.setText(R.id.tv_principal, bean.name); //名字
//            holder.setVisibility(R.id.tv_principal,View.INVISIBLE);
            holder.setText(R.id.tv_new_customers, bean.newCount); //新建客户数
            holder.setText(R.id.tv_number_calls, bean.conversation); //通话次数
            holder.setText(R.id.tv_number_scale, bean.prescaleCount); //预约量尺数
            holder.setText(R.id.tv_number_measure, bean.scaleCount); //量尺数
            holder.setText(R.id.tv_number_drawing, bean.picCount); //出图数
            holder.setText(R.id.tv_deposit_customers, bean.ordersCustomer); //订单客户数
            holder.setText(R.id.tv_signatures, bean.contractCount); //签约数
            holder.setText(R.id.tv_order_number, bean.orders); //下单数
            holder.setText(R.id.tv_number_install, bean.installed); //安装数
            holder.setText(R.id.tv_install_area, bean.are); //安装平方数
            holder.setText(R.id.tv_complete_rate, bean.firstSuccess); //一次安装完成率
//            holder.setText(R.id.tv_satisfaction, bean.Satisfied); //客户满意度
//            holder.setText(R.id.tv_store_turnover_rate, bean.enterPercent); //进店成交率
//            holder.setText(R.id.tv_scale_rate, bean.scalePercent); //量尺成交率
            holder.setText(R.id.tv_number_highSeas, bean.seaCustomer); //公海客户数
            holder.setText(R.id.tv_received, bean.receivables); //已收款
            holder.setText(R.id.tv_contract_amount, bean.contractTotal); //成交总金额
            holder.itemView.setOnClickListener(view -> startFragment(bean.type, bean.cityCode, true));
//            holder.itemView.setOnClickListener(view -> BossMonthDataActivity.open(mActivity, bean.type, bean.cityCode, bean.area));
        }
    }

    /*首层明细数据适配器*/
    class ScrollableContentAdapter extends CommonAdapter<MonthDataBossBean.ArrBean> {

        ScrollableContentAdapter(Context context, List<MonthDataBossBean.ArrBean> mDatas) {
            super(context, mDatas);
        }

        @Override
        public void onBindHolder(RecyclerHolder holder, MonthDataBossBean.ArrBean bean, int position) {
            if (position % 2 == 0) {
                holder.itemView.setBackgroundResource(R.color.textColor24);
            } else {
                holder.itemView.setBackgroundResource(R.color.color_while);
            }
            holder.setText(R.id.tv_division, bean.area); //划分
            holder.setText(R.id.tv_principal, bean.name); //名字
            holder.setText(R.id.tv_new_customers, bean.newCount); //新建客户数
            holder.setText(R.id.tv_number_calls, bean.conversation); //通话次数
            holder.setText(R.id.tv_number_scale, bean.prescaleCount); //预约量尺数
            holder.setText(R.id.tv_number_measure, bean.scaleCount); //量尺数
            holder.setText(R.id.tv_number_drawing, bean.picCount); //出图数
            holder.setText(R.id.tv_deposit_customers, bean.ordersCustomer); //订单客户数
            holder.setText(R.id.tv_signatures, bean.contractCount); //签约数
            holder.setText(R.id.tv_order_number, bean.orders); //下单数
            holder.setText(R.id.tv_number_install, bean.installed); //安装数
            holder.setText(R.id.tv_install_area, bean.are); //安装平方数
            holder.setText(R.id.tv_complete_rate, bean.firstSuccess); //一次安装完成率
//            holder.setText(R.id.tv_satisfaction, bean.Satisfied); //客户满意度
//            holder.setText(R.id.tv_store_turnover_rate, bean.enterPercent); //进店成交率
//            holder.setText(R.id.tv_scale_rate, bean.scalePercent); //量尺成交率
            holder.setText(R.id.tv_number_highSeas, bean.seaCustomer); //公海客户数
            if (bean.isClickable2()) {
                holder.setTextColorRes(R.id.tv_received, R.color.colorAccent);
                holder.setEnabled(R.id.tv_received, true);
            } else {
                holder.setTextColorRes(R.id.tv_received, R.color.textColor8);
                holder.setEnabled(R.id.tv_received, false);
            }
            holder.setOnClickListener(R.id.tv_received, view -> startFragment(bean.type, bean.cityCode, false));
            holder.setText(R.id.tv_received, bean.receivables); //已收款
            holder.setText(R.id.tv_contract_amount, bean.contractTotal); //成交总金额
        }

        @Override
        protected int bindView(int viewType) {
            return R.layout.item_month_data_boss_content;
        }
    }

    /*角色列表适配器*/
    class RoleListAdapter extends CommonAdapter<MonthDataBossBean.ArrBean2> {

        int mSelectIndex;

        RoleListAdapter(Context context, List<MonthDataBossBean.ArrBean2> mDatas) {
            super(context, mDatas);
        }

        void setSelectIndex(int selectIndex) {
            this.mSelectIndex = selectIndex;
            notifyDataSetChanged();
        }

        @Override
        protected int bindView(int viewType) {
            return R.layout.item_month_data_boss_role;
        }

        @Override
        public void onBindHolder(RecyclerHolder holder, MonthDataBossBean.ArrBean2 bean, int position) {
            if (mSelectIndex == position) {
                holder.setTextColorRes(R.id.tv_role, R.color.color_while);
                holder.itemView.setBackgroundResource(R.drawable.bg_corners5dp_top_coloraccent);
            } else {
                holder.setTextColorRes(R.id.tv_role, R.color.textColor5);
                holder.itemView.setBackgroundResource(R.drawable.bg_corners5dp_top_bg);
            }
            holder.setText(R.id.tv_role, bean.tag);
        }
    }

    /*导购、业务员第一列数据*/
    class GuideSalesmanSideAdapter extends CommonAdapter<MonthDataBossBean.ArrBean> {

        GuideSalesmanSideAdapter(Context context, List<MonthDataBossBean.ArrBean> mDatas) {
            super(context, mDatas);
        }

        @Override
        protected int bindView(int viewType) {
            return R.layout.item_month_data_guidesalesman_side;
        }

        @Override
        public void onBindHolder(RecyclerHolder holder, MonthDataBossBean.ArrBean bean, int position) {
            if (position % 2 == 0) {
                holder.itemView.setBackgroundResource(R.color.textColor24);
            } else {
                holder.itemView.setBackgroundResource(R.color.color_while);
            }
            holder.setText(R.id.tv_principal, bean.name);
            holder.setText(R.id.tv_new_customers, bean.newCount); //新建客户数
            holder.setText(R.id.tv_number_calls, bean.conversation); //通话次数
            holder.setText(R.id.tv_number_scale, bean.prescaleCount); //预约量尺数
//            holder.setText(R.id.tv_number_measure, bean.scaleCount); //量尺数
//            holder.setText(R.id.tv_number_drawing, bean.picCount); //出图数
            holder.setText(R.id.tv_deposit_customers, bean.ordersCustomer); //订单客户数
            holder.setText(R.id.tv_signatures, bean.contractCount); //签约数
//            holder.setText(R.id.tv_order_number, bean.orders); //下单数
//            holder.setText(R.id.tv_number_install, bean.installed); //安装数
//            holder.setText(R.id.tv_store_turnover_rate, bean.enterPercent); //进店成交率
//            holder.setText(R.id.tv_scale_rate, bean.scalePercent); //量尺成交率
            holder.setText(R.id.tv_number_highSeas, bean.seaCustomer); //公海客户数
            holder.setText(R.id.tv_received, bean.receivables); //已收款
            holder.setText(R.id.tv_contract_amount, bean.contractTotal); //成交总金额
        }
    }

    /*导购业务员数据明细*/
    class GuideSalesmanDetailDataAdapter extends CommonAdapter<MonthDataBossBean.ArrBean> {

        GuideSalesmanDetailDataAdapter(Context context, List<MonthDataBossBean.ArrBean> mDatas) {
            super(context, mDatas);
        }

        @Override
        protected int bindView(int viewType) {
            return R.layout.item_month_data_guidesalesman_content;
        }

        @Override
        public void onBindHolder(RecyclerHolder holder, MonthDataBossBean.ArrBean bean, int position) {
            if (position % 2 == 0) {
                holder.itemView.setBackgroundResource(R.color.textColor24);
            } else {
                holder.itemView.setBackgroundResource(R.color.color_while);
            }
            holder.setText(R.id.tv_principal, bean.name);
            holder.setText(R.id.tv_new_customers, bean.newCount); //新建客户数
            holder.setText(R.id.tv_number_calls, bean.conversation); //通话次数
            holder.setText(R.id.tv_number_scale, bean.prescaleCount); //预约量尺数
//            holder.setText(R.id.tv_number_measure, bean.scaleCount); //量尺数
//            holder.setText(R.id.tv_number_drawing, bean.picCount); //出图数
            holder.setText(R.id.tv_deposit_customers, bean.ordersCustomer); //订单客户数
            holder.setText(R.id.tv_signatures, bean.contractCount); //签约数
//            holder.setText(R.id.tv_order_number, bean.orders); //下单数
//            holder.setText(R.id.tv_number_install, bean.installed); //安装数
//            holder.setText(R.id.tv_store_turnover_rate, bean.enterPercent); //进店成交率
//            holder.setText(R.id.tv_scale_rate, bean.scalePercent); //量尺成交率
            holder.setText(R.id.tv_number_highSeas, bean.seaCustomer); //公海客户数
            holder.setText(R.id.tv_received, bean.receivables); //已收款
            holder.setText(R.id.tv_contract_amount, bean.contractTotal); //成交总金额
            if (bean.isClickable2()) {
                holder.setEnabled(R.id.tv_received, true);
                holder.setTextColorRes(R.id.tv_received, R.color.colorAccent);
            } else {
                holder.setEnabled(R.id.tv_received, false);
                holder.setTextColorRes(R.id.tv_received, R.color.textColor8);
            }
            holder.setOnClickListener(R.id.tv_received, view -> startFragment(bean.type, bean.cityCode, false));
        }
    }

    /*设计师第一列数据*/
    class DesignerDetailSideAdapter extends CommonAdapter<MonthDataBossBean.ArrBean> {

        DesignerDetailSideAdapter(Context context, List<MonthDataBossBean.ArrBean> mDatas) {
            super(context, mDatas);
        }

        @Override
        protected int bindView(int viewType) {
            return R.layout.item_month_data_designer_side;
        }

        @Override
        public void onBindHolder(RecyclerHolder holder, MonthDataBossBean.ArrBean bean, int position) {
            if (position % 2 == 0) {
                holder.itemView.setBackgroundResource(R.color.textColor24);
            } else {
                holder.itemView.setBackgroundResource(R.color.color_while);
            }
            holder.setText(R.id.tv_principal, bean.name);
            holder.setText(R.id.tv_number_calls, bean.conversation); //通话次数
            holder.setText(R.id.tv_number_measure, bean.scaleCount); //量尺数
            holder.setText(R.id.tv_number_drawing, bean.picCount); //出图数
            holder.setText(R.id.tv_signatures, bean.contractCount); //签约数
            holder.setText(R.id.tv_order_number, bean.orders); //下单数
//            holder.setText(R.id.tv_number_install, bean.installed); //安装数
//            holder.setText(R.id.tv_scale_rate, bean.scalePercent); //量尺成交率
            holder.setText(R.id.tv_number_highSeas, bean.seaCustomer); //公海客户数
            holder.setText(R.id.tv_received, bean.receivables); //已收款
            holder.setText(R.id.tv_contract_amount, bean.contractTotal); //成交总金额
        }
    }

    /*设计师数据明细*/
    class DesignerDetailDataAdapter extends CommonAdapter<MonthDataBossBean.ArrBean> {

        DesignerDetailDataAdapter(Context context, List<MonthDataBossBean.ArrBean> mDatas) {
            super(context, mDatas);
        }

        @Override
        protected int bindView(int viewType) {
            return R.layout.item_month_data_designer_content;
        }

        @Override
        public void onBindHolder(RecyclerHolder holder, MonthDataBossBean.ArrBean bean, int position) {
            if (position % 2 == 0) {
                holder.itemView.setBackgroundResource(R.color.textColor24);
            } else {
                holder.itemView.setBackgroundResource(R.color.color_while);
            }
            holder.setText(R.id.tv_principal, bean.name);
            holder.setText(R.id.tv_number_calls, bean.conversation); //通话次数
            holder.setText(R.id.tv_number_measure, bean.scaleCount); //量尺数
            holder.setText(R.id.tv_number_drawing, bean.picCount); //出图数
            holder.setText(R.id.tv_signatures, bean.contractCount); //签约数
            holder.setText(R.id.tv_order_number, bean.orders); //下单数
//            holder.setText(R.id.tv_number_install, bean.installed); //安装数
//            holder.setText(R.id.tv_scale_rate, bean.scalePercent); //量尺成交率
            holder.setText(R.id.tv_number_highSeas, bean.seaCustomer); //公海客户数
            holder.setText(R.id.tv_received, bean.receivables); //已收款
            holder.setEnabled(R.id.tv_received, bean.isClickable());
            if (bean.isClickable2()) {
                holder.setTextColorRes(R.id.tv_received, R.color.colorAccent);
            } else {
                holder.setTextColorRes(R.id.tv_received, R.color.textColor8);
            }
            holder.setOnClickListener(R.id.tv_received, view -> startFragment(bean.type, bean.cityCode, false));
            holder.setText(R.id.tv_contract_amount, bean.contractTotal); //成交总金额
        }
    }

    class InstallerDetailSideAdapter extends CommonAdapter<MonthDataBossBean.ArrBean> {

        InstallerDetailSideAdapter(Context context, List<MonthDataBossBean.ArrBean> mDatas) {
            super(context, mDatas);
        }

        @Override
        protected int bindView(int viewType) {
            return R.layout.item_month_data_installer_side;
        }

        @Override
        public void onBindHolder(RecyclerHolder holder, MonthDataBossBean.ArrBean bean, int position) {
            if (position % 2 == 0) {
                holder.itemView.setBackgroundResource(R.color.textColor24);
            } else {
                holder.itemView.setBackgroundResource(R.color.color_while);
            }
            holder.setText(R.id.tv_principal, bean.name);
            holder.setText(R.id.tv_install_count, bean.installed); //安装数
            holder.setText(R.id.tv_install_area, bean.are); //安装平方数
            holder.setText(R.id.tv_complete_rate, bean.firstSuccess); //一次安装完成率
//            holder.setText(R.id.tv_satisfaction, bean.Satisfied); //客户满意度
        }
    }

    /*安装工数据明细*/
    class InstallerDetailDataAdapter extends CommonAdapter<MonthDataBossBean.ArrBean> {

        InstallerDetailDataAdapter(Context context, List<MonthDataBossBean.ArrBean> mDatas) {
            super(context, mDatas);
        }

        @Override
        protected int bindView(int viewType) {
            return R.layout.item_month_data_installer_content;
        }

        @Override
        public void onBindHolder(RecyclerHolder holder, MonthDataBossBean.ArrBean bean, int position) {
            if (position % 2 == 0) {
                holder.itemView.setBackgroundResource(R.color.textColor24);
            } else {
                holder.itemView.setBackgroundResource(R.color.color_while);
            }
            holder.setText(R.id.tv_principal, bean.name);
            holder.setText(R.id.tv_install_count, bean.installed); //安装数
            holder.setText(R.id.tv_install_area, bean.are); //安装平方数
            holder.setText(R.id.tv_complete_rate, bean.firstSuccess); //一次安装完成率
//            holder.setText(R.id.tv_satisfaction, bean.Satisfied); //客户满意度
        }
    }

    /*管理者第一列数据*/
    class ManagerSideDetailSideAdapter extends CommonAdapter<MonthDataBossBean.ArrBean> {

        ManagerSideDetailSideAdapter(Context context, List<MonthDataBossBean.ArrBean> mDatas) {
            super(context, mDatas);
        }

        @Override
        protected int bindView(int viewType) {
            return R.layout.item_month_data_manager_side;
        }

        @Override
        public void onBindHolder(RecyclerHolder holder, MonthDataBossBean.ArrBean bean, int position) {
            if (position % 2 == 0) {
                holder.itemView.setBackgroundResource(R.color.textColor24);
            } else {
                holder.itemView.setBackgroundResource(R.color.color_while);
            }
            holder.setText(R.id.tv_principal, bean.name);
            holder.setText(R.id.tv_new_customers, bean.newCount); //新建客户数
            holder.setText(R.id.tv_number_calls, bean.conversation); //通话次数
            holder.setText(R.id.tv_number_scale, bean.prescaleCount); //预约量尺数
            holder.setText(R.id.tv_deposit_customers, bean.ordersCustomer); //订单客户数
            holder.setText(R.id.tv_signatures, bean.contractCount); //签约数
            holder.setText(R.id.tv_received, bean.receivables); //已收款
            holder.setText(R.id.tv_contract_amount, bean.contractTotal); //成交总金额
        }
    }

    class ManagerSideDetailDataAdapter extends CommonAdapter<MonthDataBossBean.ArrBean> {

        ManagerSideDetailDataAdapter(Context context, List<MonthDataBossBean.ArrBean> mDatas) {
            super(context, mDatas);
        }

        @Override
        protected int bindView(int viewType) {
            return R.layout.item_month_data_manager_content;
        }

        @Override
        public void onBindHolder(RecyclerHolder holder, MonthDataBossBean.ArrBean bean, int position) {
            if (position % 2 == 0) {
                holder.itemView.setBackgroundResource(R.color.textColor24);
            } else {
                holder.itemView.setBackgroundResource(R.color.color_while);
            }
            holder.setText(R.id.tv_principal, bean.name);
            holder.setText(R.id.tv_new_customers, bean.newCount); //新建客户数
            holder.setText(R.id.tv_number_calls, bean.conversation); //通话次数
            holder.setText(R.id.tv_number_scale, bean.prescaleCount); //预约量尺数
            holder.setText(R.id.tv_deposit_customers, bean.ordersCustomer); //订单客户数
            holder.setText(R.id.tv_signatures, bean.contractCount); //签约数
            holder.setText(R.id.tv_received, bean.receivables); //已收款
            holder.setText(R.id.tv_contract_amount, bean.contractTotal); //成交总金额
            if (bean.isClickable2()) {
                holder.setEnabled(R.id.tv_received, true);
                holder.setTextColorRes(R.id.tv_received, R.color.colorAccent);
            } else {
                holder.setEnabled(R.id.tv_received, false);
                holder.setTextColorRes(R.id.tv_received, R.color.textColor8);
            }
            holder.setOnClickListener(R.id.tv_received, view -> startFragment(bean.type, bean.cityCode, false));
        }
    }

    /*第一列个人数据（客户姓名）*/
    class SingleSideAdapter extends CommonAdapter<MonthDataBossBean.ArrBean3> {

        SingleSideAdapter(Context context, List<MonthDataBossBean.ArrBean3> mDatas) {
            super(context, mDatas);
        }

        @Override
        protected int bindView(int viewType) {
            return R.layout.item_month_data_single_side;
        }

        @Override
        public void onBindHolder(RecyclerHolder holder, MonthDataBossBean.ArrBean3 bean, int position) {
            if (position % 2 == 0) {
                holder.itemView.setBackgroundResource(R.color.textColor24);
            } else {
                holder.itemView.setBackgroundResource(R.color.color_while);
            }
            holder.setText(R.id.tv_customer_name, bean.name);
            holder.setText(R.id.tv_receipt_category, bean.type);//收款类型
            holder.setText(R.id.tv_receipt_amount, bean.money); //收款金额
            holder.setText(R.id.tv_receipt_time, bean.payTime);//收款时间
        }
    }

    /*个人数据明细*/
    class SingleDetailDataAdapter extends CommonAdapter<MonthDataBossBean.ArrBean3> {

        SingleDetailDataAdapter(Context context, List<MonthDataBossBean.ArrBean3> mDatas) {
            super(context, mDatas);
        }

        @Override
        protected int bindView(int viewType) {
            return R.layout.item_month_data_single_content;
        }

        @Override
        public void onBindHolder(RecyclerHolder holder, MonthDataBossBean.ArrBean3 bean, int position) {
            if (position % 2 == 0) {
                holder.itemView.setBackgroundResource(R.color.textColor24);
            } else {
                holder.itemView.setBackgroundResource(R.color.color_while);
            }
            holder.setText(R.id.tv_customer_name, bean.name);
            holder.setText(R.id.tv_receipt_category, bean.type);//收款类型
            holder.setText(R.id.tv_receipt_amount, bean.money); //收款金额
            holder.setText(R.id.tv_receipt_time, bean.payTime);//收款时间
        }
    }

    private void startFragment(String type, String cityCode, boolean isDescription) {
        mActivity.startFragment(new BossMonthDataFragment(), obtainBundle(type, cityCode, isDescription));
    }

    private Bundle obtainBundle(String type, String cityCode, boolean isDescription) {
        Bundle bundle = new Bundle();
        bundle.putString("type", type);
        bundle.putString("cityCode", cityCode);
        bundle.putSerializable("startDate", mStartDate);
        bundle.putSerializable("endDate", mEndDate);
        bundle.putBoolean("isDescription", isDescription);
        bundle.putBoolean("isAnimation", true);
        return bundle;
    }
}
