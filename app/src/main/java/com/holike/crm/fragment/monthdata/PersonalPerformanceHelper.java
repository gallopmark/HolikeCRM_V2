package com.holike.crm.fragment.monthdata;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.grallopmark.tablayout.CommonTabLayout;
import com.grallopmark.tablayout.TabEntity;
import com.grallopmark.tablayout.listener.CustomTabEntity;
import com.grallopmark.tablayout.listener.OnTabSelectListener;
import com.holike.crm.R;
import com.holike.crm.adapter.AbsFormAdapter;
import com.holike.crm.base.BaseFragment;
import com.holike.crm.base.BaseHelper;
import com.holike.crm.bean.PersonalPerformanceBean;
import com.holike.crm.dialog.CalendarPickerDialog;
import com.holike.crm.enumeration.UserTypeValue;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

class PersonalPerformanceHelper implements BaseHelper, OnTabSelectListener {

    private BaseFragment<?, ?> mFragment;
    private Context mContext;
    private View mFragmentView;
    private FrameLayout mContentLayout;
    private String mType,  //类型 5仓库 6安装工 61安装经理 8设计师/ 9导购/业务员 11售后
            mTime = "1";  //1 本月 2全年 3自定义时间
    private Callback mCallback;
    private Date mStartDate, mEndDate;
    private List<Date> mSelectedDates;

    PersonalPerformanceHelper(BaseFragment<?, ?> fragment, Callback callback) {
        this.mFragment = fragment;
        this.mContext = fragment.getContext();
        this.mCallback = callback;
        mFragmentView = fragment.getContentView();
        Bundle bundle = fragment.getArguments();
        if (bundle != null) {
            mType = bundle.getString("userType");
        }
        setup();
        onQuery();
    }

    private void setup() {
        CommonTabLayout tabLayout = mFragmentView.findViewById(R.id.tab_layout);
        List<CustomTabEntity> tabEntities = new ArrayList<>();
        tabEntities.add(new TabEntity(mContext.getString(R.string.tab_this_month)));
        tabEntities.add(new TabEntity(mContext.getString(R.string.tab_annual)));
        tabEntities.add(new TabEntity(mContext.getString(R.string.tab_query_date)));
        tabLayout.setTabData(tabEntities);
        tabLayout.setOnTabSelectListener(this);
        mContentLayout = mFragmentView.findViewById(R.id.fl_container);
    }

    @Override
    public void onTabSelect(int position) {
        if (position == 0 || position == 1) {
            mStartDate = null;
            mEndDate = null;
            if (position == 0) {
                mTime = "1";
            } else {
                mTime = "2";
            }
            onQuery();
        } else {
            mTime = "3";
            onSelectCalendar();
        }
    }

    @Override
    public void onTabReselect(int position) {
        if (position == 2) {
            onSelectCalendar();
        }
    }

    private void onSelectCalendar() {
        new CalendarPickerDialog.Builder(mContext).maxDate(new Date())
                .withSelectedDates(mSelectedDates)
                .clickToClear(true)
                .calendarRangeSelectedListener(new CalendarPickerDialog.OnCalendarRangeSelectedListener() {
                    @Override
                    public void onLeftClicked(CalendarPickerDialog dialog) {

                    }

                    @Override
                    public void onRightClick(CalendarPickerDialog dialog, List<Date> selectedDates, Date start, Date end) {
                        dialog.dismiss();
                        mSelectedDates = new ArrayList<>(selectedDates);
                        if (selectedDates.size() >= 1) {
                            mStartDate = start;
                            mEndDate = end;
                            onQuery();
                        } else {  //没有选择日期 不查询
                            mStartDate = null;
                            mEndDate = null;
                        }
                    }
                }).show();
    }

    void onQuery() {
        mCallback.onQuery(mType, mTime, mStartDate, mEndDate);
    }

    void onSuccess(PersonalPerformanceBean bean) {
        mContentLayout.removeAllViews();
        if (bean == null) {
            LayoutInflater.from(mContext).inflate(R.layout.include_empty_textview, mContentLayout, true);
        } else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.include_personal_performance, mContentLayout, true);
            TextView tvTime = view.findViewById(R.id.tv_time_detail);
            tvTime.setText(bean.time);
            TextView tvUnit = view.findViewById(R.id.tv_unit);
            tvUnit.setVisibility(isUnitVisible() ? View.VISIBLE : View.GONE);
            RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
            setAdapter(recyclerView, bean);
        }
    }


    //导购业务员或设计师---显示右侧单位文本
    private boolean isUnitVisible() {
        return TextUtils.equals(mType, UserTypeValue.GUIDE_SALESMAN_V2)
                || TextUtils.equals(mType, UserTypeValue.DESIGN_MANAGER);
    }

    private void setAdapter(RecyclerView recyclerView, PersonalPerformanceBean bean) {
        List<Item> items = new ArrayList<>();
        if (TextUtils.equals(mType, UserTypeValue.GUIDE_SALESMAN_V2)) {  //导购业务员
            items.add(new Item(mContext.getString(R.string.number_of_new_customers), bean.newCount));
            items.add(new Item(mContext.getString(R.string.number_of_reservation_scale), bean.prescaleCount));
            items.add(new Item(mContext.getString(R.string.number_of_deposit_customers), bean.ordersCustomer));
            items.add(new Item(mContext.getString(R.string.number_of_signatures), bean.contractCount));
            items.add(new Item(mContext.getString(R.string.payments_received2), bean.receivables));
            items.add(new Item(mContext.getString(R.string.total_contract_amount2), bean.contractTotal));
        } else if (TextUtils.equals(mType, UserTypeValue.DESIGNER)) {  //设计师
            items.add(new Item(mContext.getString(R.string.number_of_measuring), bean.scaleCount));
            items.add(new Item(mContext.getString(R.string.number_of_output_graphs), bean.picCount));
            items.add(new Item(mContext.getString(R.string.number_of_signatures), bean.contractCount));
            items.add(new Item(mContext.getString(R.string.number_of_place_orders), bean.orderCount));
            items.add(new Item(mContext.getString(R.string.payments_received2), bean.receivables));
            items.add(new Item(mContext.getString(R.string.total_contract_amount2), bean.contractTotal));
        } else if (TextUtils.equals(mType, UserTypeValue.INSTALLER_V2)) { //安装工
            items.add(new Item(mContext.getString(R.string.number_of_install_customers), bean.installed));
            items.add(new Item(mContext.getString(R.string.number_of_install_squares), bean.area));
            items.add(new Item(mContext.getString(R.string.once_install_completion_rate), bean.firstSuccess));
            items.add(new Item(mContext.getString(R.string.customer_satisfaction_tips), bean.Satisfied));
        } else if (TextUtils.equals(mType, UserTypeValue.WAREHOUSE)) { //仓管
            items.add(new Item(mContext.getString(R.string.number_of_scanned_input_parts), bean.scan));
        } else if (TextUtils.equals(mType, UserTypeValue.AFTER_SALE)) { //售后
            items.add(new Item(mContext.getString(R.string.once_install_completion_rate), bean.firstSuccess));
            items.add(new Item(mContext.getString(R.string.customer_satisfaction_tips), bean.Satisfied));
        }
        recyclerView.setAdapter(new ItemAdapter(mContext, items));
    }

    private final class Item {
        String name;
        String data;

        Item(String name, String data) {
            this.name = name;
            this.data = data;
        }
    }

    private final class ItemAdapter extends AbsFormAdapter<Item> {

        ItemAdapter(Context context, List<Item> mDatas) {
            super(context, mDatas);
        }

        @Override
        protected int bindView(int viewType) {
            return R.layout.item_personal_performance;
        }

        @Override
        protected void bindViewHolder(RecyclerHolder holder, Item item, int position) {
            holder.setText(R.id.tv_name, item.name);
            holder.setText(R.id.tv_data, item.data);
            if (position == getItemCount() - 1) {
                holder.setVisibility(R.id.v_bottom_line, View.VISIBLE);
            } else {
                holder.setVisibility(R.id.v_bottom_line, View.GONE);
            }
        }
    }

    void onFailure(String failReason) {
        mContentLayout.removeAllViews();
        View view = LayoutInflater.from(mContext).inflate(R.layout.include_empty_page, mContentLayout, true);
        view.findViewById(R.id.ll_empty_page).setBackgroundResource(R.color.color_while);
        mFragment.noNetwork(failReason);
    }

    interface Callback {
        void onQuery(String type, String time, Date startDate, Date endDate);
    }
}
