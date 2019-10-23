package com.holike.crm.fragment.reportv3.business;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewStub;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.grallopmark.tablayout.CommonTabLayout;
import com.grallopmark.tablayout.TabEntity;
import com.grallopmark.tablayout.listener.CustomTabEntity;
import com.grallopmark.tablayout.listener.OnTabSelectListener;
import com.holike.crm.R;
import com.holike.crm.base.BaseFragment;
import com.holike.crm.base.FragmentHelper;
import com.holike.crm.dialog.CalendarPickerDialog;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

abstract class BusinessReferenceHelper extends FragmentHelper implements OnTabSelectListener {

    protected CommonTabLayout mTabLayout;
    protected FrameLayout mContainerLayout;
    protected String mTime;
    protected String mShopId;
    protected Date mStartDate, mEndDate;
    private List<Date> mSelectDates;

    BusinessReferenceHelper(BaseFragment<?, ?> fragment) {
        super(fragment);
        mTabLayout = obtainView(R.id.tabLayout);
        mContainerLayout = obtainView(R.id.fl_container);
        initTabLayout();
        obtainBundleValue(fragment.getArguments());
    }

    private void initTabLayout() {
        List<CustomTabEntity> tabEntities = new ArrayList<>();
        tabEntities.add(new TabEntity(mContext.getString(R.string.tab_annual)));
        tabEntities.add(new TabEntity(mContext.getString(R.string.tab_this_month)));
        tabEntities.add(new TabEntity(mContext.getString(R.string.tab_query_date)));
        mTabLayout.setTabData(tabEntities);
        mTabLayout.setOnTabSelectListener(this);
    }

    @Override
    public void onTabSelect(int position) {
        if (position == 0) {  //全年
            mTime = "";
            onHttpRequest();
        } else if (position == 1) { //当月
            mTime = "1";
            onHttpRequest();
        } else {
            onCalendarPicker();
        }
    }

    @Override
    public void onTabReselect(int position) {
        if (position > 1) {
            onCalendarPicker();
        }
    }

    private void onCalendarPicker() {
        new CalendarPickerDialog.Builder(mContext).maxDate(new Date())
                .withSelectedDates(mSelectDates)
                .clickToClear(true)
                .calendarRangeSelectedListener(new CalendarPickerDialog.OnCalendarRangeSelectedListener() {
                    @Override
                    public void onLeftClicked(CalendarPickerDialog dialog) {

                    }

                    @Override
                    public void onRightClick(CalendarPickerDialog dialog, List<Date> selectedDates, Date start, Date end) {
                        dialog.dismiss();
                        if (selectedDates.size() >= 1) {
                            mSelectDates = selectedDates;
                            mStartDate = start;
                            mEndDate = end;
                        } else {
                            mStartDate = null;
                            mEndDate = null;
                        }
                        onHttpRequest();
                    }
                }).show();
    }

    private void obtainBundleValue(Bundle bundle) {
        if (bundle != null) {
            mTime = bundle.getString("time");
            mShopId = bundle.getString("shopId");
        }
    }

    abstract void onHttpRequest();

    void onSuccess(@Nullable Object obj) {
        mContainerLayout.removeAllViews();
        if (obj == null) {
            noResult();
        } else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.include_business_reference, mContainerLayout, true);
            ViewStub vs = view.findViewById(R.id.vs_first_row);
            vs.setLayoutResource(getFirstRowLayoutResource());
            vs.inflate();
            requestUpdate(obj, view.findViewById(R.id.tv_time_detail),
                    view.findViewById(R.id.tv_shop),
                    view.findViewById(R.id.rv_tab),
                    view.findViewById(R.id.rv_content));
        }
    }

    private void noResult() {
        LayoutInflater.from(mContext).inflate(R.layout.include_empty_page, mContainerLayout, true);
        mFragment.noResult();
    }

    abstract int getFirstRowLayoutResource();

    abstract void requestUpdate(@NonNull Object obj, TextView tvDatetime, TextView tvShop, RecyclerView rvTab, RecyclerView rvContent);

    void onFailure(String failReason) {
        mContainerLayout.removeAllViews();
        LayoutInflater.from(mContext).inflate(R.layout.include_empty_page, mContainerLayout, true);
        mFragment.noNetwork(failReason);
    }

    void reload(){
        onHttpRequest();
    }
}
