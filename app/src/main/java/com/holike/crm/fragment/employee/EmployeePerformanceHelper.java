package com.holike.crm.fragment.employee;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gallopmark.recycler.adapterhelper.CommonAdapter;
import com.holike.crm.R;
import com.holike.crm.adapter.AbsFormAdapter;
import com.holike.crm.base.BaseFragment;
import com.holike.crm.base.FragmentHelper;
import com.holike.crm.bean.MonthDataBossBean;
import com.holike.crm.dialog.CalendarPickerDialog;
import com.holike.crm.dialog.LevelAreaDialog;
import com.holike.crm.dialog.MonthDataTipsDialog;
import com.holike.crm.helper.TextSpanHelper;
import com.holike.crm.util.RecyclerUtils;

import java.util.Date;
import java.util.List;

/**
 * Created by pony on 2019/10/30.
 * Version v3.0 app报表
 */
class EmployeePerformanceHelper extends FragmentHelper {

    private Callback mCallback;
    private FrameLayout mContainerLayout;
    private LevelAreaDialog mDialog;
    private String mType, mCityCode;
    private boolean mSelectDealer;  //是否选择了经销商
    private Date mStartDate, mEndDate;
    private List<Date> mSelectedDates;
    private Handler mHandler;

    EmployeePerformanceHelper(BaseFragment<?, ?> fragment, Callback callback) {
        super(fragment);
        this.mCallback = callback;
        mContainerLayout = obtainView(R.id.container);
        Bundle bundle = fragment.getArguments();
        if (bundle == null) {
            onFirstEntry();
        } else {
            mType = bundle.getString("type");
            mCityCode = bundle.getString("cityCode");
            mHandler = new Handler();
            mHandler.postDelayed(this::doRequest, 300);
        }
    }

    /*首次进入*/
    private void onFirstEntry() {
        LinearLayout contentLayout = obtainView(R.id.ll_container);
        View view = LayoutInflater.from(mContext).inflate(R.layout.include_employee_performance_main_top, contentLayout, false);
        final TextView tvSelectDealer = view.findViewById(R.id.tv_select_dealer);
        tvSelectDealer.setOnClickListener(v -> onSelectDealer(tvSelectDealer));
        contentLayout.addView(view, 1);
    }

    private void onSelectDealer(final TextView tv) {
        if (mDialog == null) {
            mDialog = new LevelAreaDialog(mContext);
        }
        mDialog.setRequired(true).setOnAreaSelectListener(new LevelAreaDialog.OnAreaSelectListener() {
            @Override
            public void onAreaSelected(String name, String type, String cityCode, boolean isSelectDealer) {
                mType = type;
                mCityCode = cityCode;
                tv.setText(name);
                mSelectDealer = true;
                doRequest();
            }

            @Override
            public void onDismissDealer() {
                mFragment.showShortToast(R.string.please_select_dealer);
            }
        });
        mDialog.show();
    }

    void onCalendarPicker() {
        new CalendarPickerDialog.Builder(mContext).maxDate(new Date())
                .withSelectedDates(mSelectedDates)
                .clickToClear(true)
                .calendarRangeSelectedListener(new CalendarPickerDialog.OnCalendarRangeSelectedListener() {
                    @Override
                    public void onLeftClicked(CalendarPickerDialog dialog) {
                    }

                    @Override
                    public void onRightClick(CalendarPickerDialog dialog, List<Date> selectedDates, Date start, Date end) {
                        mSelectedDates = selectedDates;
                        if (selectedDates.size() >= 1) {
                            mStartDate = start;
                            mEndDate = end;
                        } else {
                            mStartDate = null;
                            mEndDate = null;
                        }
                        dialog.dismiss();
                        if (mSelectDealer) {
                            doRequest();
                        }
                    }
                }).show();
    }

    void doRequest() {
        mCallback.doRequest(mType, mCityCode, mStartDate, mEndDate);
    }

    void onSuccess(MonthDataBossBean bean) {
        String isShop = bean.isShop;
        mContainerLayout.removeAllViews();
        if (!TextUtils.equals(isShop, "1") && !TextUtils.equals(isShop, "2") && !TextUtils.equals(isShop, "3")) {
            LayoutInflater.from(mContext).inflate(R.layout.include_empty_textview, mContainerLayout, true);
        } else {
            requestUpdate(isShop, bean);
        }
    }

    private void requestUpdate(String isShop, MonthDataBossBean bean) {
        if (TextUtils.equals(isShop, "1")) {
            View container = LayoutInflater.from(mContext).inflate(R.layout.include_employee_performance_main, mContainerLayout, true);
            setDatetime(container.findViewById(R.id.tv_time), bean);
            setDescription(container.findViewById(R.id.tv_description));
            FrameLayout contentLayout = container.findViewById(R.id.fl_content_layout);
            if (bean.getArr().isEmpty()) {
                LayoutInflater.from(mContext).inflate(R.layout.include_empty_textview, contentLayout, true);
            } else {
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) contentLayout.getLayoutParams();
                params.leftMargin = mContext.getResources().getDimensionPixelSize(R.dimen.dp_10);
                View root = LayoutInflater.from(mContext).inflate(R.layout.include_form_data_content, contentLayout, true);
                FrameLayout flContainer = root.findViewById(R.id.fl_form_data_container);
                View firstLayout = LayoutInflater.from(mContext).inflate(R.layout.include_form_data_column_60dp, flContainer, false);
                TextView tvFirst = firstLayout.findViewById(R.id.tv_first_name);
                tvFirst.setText(mContext.getString(R.string.report_table_divide));
                flContainer.addView(firstLayout, 1);  //动态添加layout 第一列数据展示布局
                RecyclerView sideRecyclerView = firstLayout.findViewById(R.id.rv_side);
                sideRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
                LinearLayout scrollableLayout = flContainer.findViewById(R.id.ll_scrollable_content);
                View view = LayoutInflater.from(mContext).inflate(R.layout.include_monthdata_boss_tablist, scrollableLayout, false);
                TextView tvInstallArea = view.findViewById(R.id.tv_install_area);
                tvInstallArea.setText(TextSpanHelper.getSquareMeter(tvInstallArea.getText().toString()));
                scrollableLayout.addView(view, 0);
                sideRecyclerView.setAdapter(new ClickableSideAdapter(mContext, bean.getArr()));
                ScrollableContentAdapter adapter = new ScrollableContentAdapter(mContext, bean.getArr());
                RecyclerView contentRecyclerView = flContainer.findViewById(R.id.rv_content);
                contentRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
                contentRecyclerView.setAdapter(adapter);
                RecyclerUtils.setScrollSynchronous(sideRecyclerView, contentRecyclerView);
            }
        } else if (TextUtils.equals(isShop, "2")) { //员工数据
            View container = LayoutInflater.from(mContext).inflate(R.layout.include_employee_performance_child, mContainerLayout, true);
            setDatetime(container.findViewById(R.id.tv_time), bean);
            setDescription(container.findViewById(R.id.tv_description));
            setShopDetail(container.findViewById(R.id.tv_shop_detail), bean);
            FrameLayout contentLayout = container.findViewById(R.id.fl_content_layout);
            List<MonthDataBossBean.ArrBean2> list = bean.getArr2();
            if (list.isEmpty()) {
                LayoutInflater.from(mContext).inflate(R.layout.include_empty_textview, contentLayout, true);
            } else {
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) contentLayout.getLayoutParams();
                params.leftMargin = mContext.getResources().getDimensionPixelSize(R.dimen.dp_10);
                View root = LayoutInflater.from(mContext).inflate(R.layout.include_employee_performance_child_content, contentLayout, true);
                final FrameLayout flContainer = root.findViewById(R.id.fl_form_data_container);
                final RecyclerView rvRole = root.findViewById(R.id.rv_role);
                rvRole.setLayoutManager(new LinearLayoutManager(mContext, RecyclerView.HORIZONTAL, false));
                final RoleListAdapter adapter = new RoleListAdapter(mContext, list);
                rvRole.setAdapter(adapter);
                RecyclerView contentRecyclerView = flContainer.findViewById(R.id.rv_content);
                contentRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
                adapter.setOnItemClickListener((a, holder, view, position) -> {
                    adapter.setSelectIndex(position);
                    rvRole.smoothScrollToPosition(position);
                    switchRole(flContainer, position, list, contentRecyclerView);
                });
                switchRole(flContainer, 0, list, contentRecyclerView);
            }
        } else { //员工收款数据
            View container = LayoutInflater.from(mContext).inflate(R.layout.include_employee_performance_receipt, mContainerLayout, true);
            setDatetime(container.findViewById(R.id.tv_time), bean);
            setShopDetail(container.findViewById(R.id.tv_shop_detail), bean);
            String name = mContext.getString(R.string.customer_name_tips);
            if (!TextUtils.isEmpty(bean.userName)) {
                name += bean.userName;
            }
            ((TextView) container.findViewById(R.id.tv_name)).setText(name);
            FrameLayout contentLayout = container.findViewById(R.id.fl_content_layout);
            if (bean.getArr3().isEmpty()) {
                LayoutInflater.from(mContext).inflate(R.layout.include_empty_textview, contentLayout, true);
            } else {
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) contentLayout.getLayoutParams();
                params.leftMargin = mContext.getResources().getDimensionPixelSize(R.dimen.dp_10);
                View root = LayoutInflater.from(mContext).inflate(R.layout.include_form_data_content, contentLayout, true);
                FrameLayout flContainer = root.findViewById(R.id.fl_form_data_container);
                View firstLayout = LayoutInflater.from(mContext).inflate(R.layout.include_form_data_column_80dp, flContainer, false);
                TextView tvFirst = firstLayout.findViewById(R.id.tv_first_name);
                tvFirst.setText(mContext.getString(R.string.tips_customer_name));
                flContainer.addView(firstLayout, 1);  //动态添加layout 第一列数据展示布局
                RecyclerView sideRecyclerView = firstLayout.findViewById(R.id.rv_side);
                sideRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
                LinearLayout scrollableLayout = flContainer.findViewById(R.id.ll_scrollable_content);
                View view = LayoutInflater.from(mContext).inflate(R.layout.include_monthdata_boss_tablowest, scrollableLayout, false);
                scrollableLayout.addView(view, 0);
                sideRecyclerView.setAdapter(new SingleSideAdapter(mContext, bean.getArr3()));
                RecyclerView contentRecyclerView = flContainer.findViewById(R.id.rv_content);
                contentRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
                contentRecyclerView.setAdapter(new SingleDetailDataAdapter(mContext, bean.getArr3()));
                RecyclerUtils.setScrollSynchronous(sideRecyclerView, contentRecyclerView);
            }
        }
    }

    private void setDatetime(TextView tv, MonthDataBossBean bean) {
        tv.setText(bean.timeDetail);
    }

    private void setDescription(TextView tv) {
        tv.setOnClickListener(view -> new MonthDataTipsDialog(mContext).show());
    }

    private void setShopDetail(TextView tv, MonthDataBossBean bean) {
        tv.setText(bean.shopDetail);
    }

    private void switchRole(FrameLayout contentLayout, int position, List<MonthDataBossBean.ArrBean2> list, RecyclerView contentRecyclerView) {
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
            firstLayout = LayoutInflater.from(mContext).inflate(R.layout.include_form_data_column_80dp, contentLayout, false);
        } else {
            firstLayout = LayoutInflater.from(mContext).inflate(R.layout.include_form_data_column_60dp, contentLayout, false);
        }
        TextView tvFirst = firstLayout.findViewById(R.id.tv_first_name);
        tvFirst.setText(mContext.getString(R.string.report_table_divide));
        contentLayout.addView(firstLayout, 1);  //动态添加layout 第一列数据展示布局
//        sideRecyclerView.setAdapter(new GeneralSideAdapter(mContext, bean.getList()));
        RecyclerView sideRecyclerView = mContainerLayout.findViewById(R.id.rv_side);
        sideRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        if (TextUtils.equals(bean.type, "1") || TextUtils.equals(bean.type, "2")) { //导购、业务员
            LinearLayout scrollableLayout = contentLayout.findViewById(R.id.ll_scrollable_content);
            View view = LayoutInflater.from(mContext).inflate(R.layout.include_monthdata_boss_tablist2, scrollableLayout, false);
            scrollableLayout.addView(view, 0);
            sideRecyclerView = mContainerLayout.findViewById(R.id.rv_side);
            sideRecyclerView.setAdapter(new GuideSalesmanSideAdapter(mContext, bean.getList()));
            contentRecyclerView.setAdapter(new GuideSalesmanDetailDataAdapter(mContext, bean.getList()));
        } else if (TextUtils.equals(bean.type, "3")) { //设计师
            LinearLayout scrollableLayout = contentLayout.findViewById(R.id.ll_scrollable_content);
            View view = LayoutInflater.from(mContext).inflate(R.layout.include_monthdata_boss_tablist3, scrollableLayout, false);
            scrollableLayout.addView(view, 0);
            sideRecyclerView.setAdapter(new DesignerDetailSideAdapter(mContext, bean.getList()));
            contentRecyclerView.setAdapter(new DesignerDetailDataAdapter(mContext, bean.getList()));
        } else if (TextUtils.equals(bean.type, "4")) { //安装工
            LinearLayout scrollableLayout = contentLayout.findViewById(R.id.ll_scrollable_content);
            View view = LayoutInflater.from(mContext).inflate(R.layout.include_monthdata_boss_tablist4, scrollableLayout, false);
            TextView tvInstallArea = view.findViewById(R.id.tv_install_area);
            tvInstallArea.setText(TextSpanHelper.getSquareMeter(tvInstallArea.getText().toString()));
            scrollableLayout.addView(view, 0);
            sideRecyclerView.setAdapter(new InstallerDetailSideAdapter(mContext, bean.getList()));
            contentRecyclerView.setAdapter(new InstallerDetailDataAdapter(mContext, bean.getList()));
        } else {  //管理者
            LinearLayout scrollableLayout = contentLayout.findViewById(R.id.ll_scrollable_content);
            View view = LayoutInflater.from(mContext).inflate(R.layout.include_monthdata_boss_tablist5, scrollableLayout, false);
            scrollableLayout.addView(view, 0);
            sideRecyclerView = mContainerLayout.findViewById(R.id.rv_side);
            sideRecyclerView.setAdapter(new ManagerSideDetailSideAdapter(mContext, bean.getList()));
            contentRecyclerView.setAdapter(new ManagerSideDetailDataAdapter(mContext, bean.getList()));
        }
        RecyclerUtils.setScrollSynchronous(sideRecyclerView, contentRecyclerView);
    }

    /*第一列门店数据*/
    class ClickableSideAdapter extends AbsFormAdapter<MonthDataBossBean.ArrBean> {

        ClickableSideAdapter(Context context, List<MonthDataBossBean.ArrBean> mDatas) {
            super(context, mDatas);
        }

        @Override
        protected int bindView(int viewType) {
            return R.layout.item_month_data_boss_side;
        }

        @Override
        protected void bindViewHolder(RecyclerHolder holder, MonthDataBossBean.ArrBean bean, int position) {
            if (bean.isClickable()) {
                holder.setTextColorRes(R.id.tv_division, R.color.colorAccent);
                holder.itemView.setEnabled(true);
            } else {
                holder.setTextColorRes(R.id.tv_division, R.color.textColor8);
                holder.itemView.setEnabled(false);
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
            holder.setText(R.id.tv_number_highSeas, bean.seaCustomer); //公海客户数
            holder.setText(R.id.tv_received, bean.receivables); //已收款
            holder.setText(R.id.tv_contract_amount, bean.contractTotal); //成交总金额
            holder.itemView.setOnClickListener(view -> startFragment(bean.type, bean.cityCode));
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
            holder.setText(R.id.tv_number_highSeas, bean.seaCustomer); //公海客户数
            if (bean.isClickable2()) {
                holder.setTextColorRes(R.id.tv_received, R.color.colorAccent);
                holder.setEnabled(R.id.tv_received, true);
            } else {
                holder.setTextColorRes(R.id.tv_received, R.color.textColor8);
                holder.setEnabled(R.id.tv_received, false);
            }
            holder.setOnClickListener(R.id.tv_received, view -> startFragment(bean.type, bean.cityCode));
            holder.setText(R.id.tv_received, bean.receivables); //已收款
            holder.setText(R.id.tv_contract_amount, bean.contractTotal); //成交总金额
        }

        @Override
        protected int bindView(int viewType) {
            return R.layout.item_month_data_boss_content;
        }
    }

    private void startFragment(String type, String cityCode) {
        Bundle bundle = new Bundle();
        bundle.putString("type", type);
        bundle.putString("cityCode", cityCode);
        mFragment.startFragment(new EmployeePerformanceFragment(), bundle, true);
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
            return R.layout.item_form_data_tabhost;
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
            holder.setText(R.id.tv_deposit_customers, bean.ordersCustomer); //订单客户数
            holder.setText(R.id.tv_signatures, bean.contractCount); //签约数
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
            holder.setText(R.id.tv_deposit_customers, bean.ordersCustomer); //订单客户数
            holder.setText(R.id.tv_signatures, bean.contractCount); //签约数
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
            holder.setOnClickListener(R.id.tv_received, view -> startFragment(bean.type, bean.cityCode));
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
            holder.setText(R.id.tv_number_highSeas, bean.seaCustomer); //公海客户数
            holder.setText(R.id.tv_received, bean.receivables); //已收款
            holder.setEnabled(R.id.tv_received, bean.isClickable());
            if (bean.isClickable2()) {
                holder.setTextColorRes(R.id.tv_received, R.color.colorAccent);
            } else {
                holder.setTextColorRes(R.id.tv_received, R.color.textColor8);
            }
            holder.setOnClickListener(R.id.tv_received, view -> startFragment(bean.type, bean.cityCode));
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
            holder.setOnClickListener(R.id.tv_received, view -> startFragment(bean.type, bean.cityCode));
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

    void onFailure(String failReason) {
        mContainerLayout.removeAllViews();
        View view = LayoutInflater.from(mContext).inflate(R.layout.include_empty_page, mContainerLayout, true);
        view.findViewById(R.id.ll_empty_page).setBackgroundResource(R.color.color_while);
        mFragment.noNetwork(failReason);
    }

    interface Callback {
        void doRequest(String type, String cityCode, Date startDate, Date endDate);
    }

    void onDestroy() {
        if (mDialog != null) {
            mDialog.dismiss();
        }
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
    }
}
