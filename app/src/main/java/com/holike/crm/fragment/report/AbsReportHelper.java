package com.holike.crm.fragment.report;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;

import com.grallopmark.tablayout.CommonTabLayout;
import com.grallopmark.tablayout.TabEntity;
import com.grallopmark.tablayout.listener.CustomTabEntity;
import com.grallopmark.tablayout.listener.OnTabSelectListener;
import com.holike.crm.R;
import com.holike.crm.base.BaseFragment;
import com.holike.crm.dialog.CalendarPickerDialog;
import com.holike.crm.helper.CalendarPickerHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public abstract class AbsReportHelper extends GeneralReportHelper {
    String mSpliceTit;
    public String mTime = "2"; //默认查询本月
    public String mType, mCityCode;
    public Date mStartDate, mEndDate;
    private List<Date> mSelectDate;
    private Handler mHandler;

    public AbsReportHelper(BaseFragment<?, ?> fragment) {
        super(fragment);
        String title = mContext.getString(getTitleId());
        String subTitle = initParams(fragment.getArguments());
        if (!TextUtils.isEmpty(subTitle) && withSubTitle()) {
            title += ("-" + subTitle);
        }
        fragment.setTitle(title);
        setTabLayout();
    }

    protected abstract int getTitleId();

    protected boolean withSubTitle() {
        return true;
    }

    private String initParams(Bundle bundle) {
        String title = null;
        if (bundle != null) {
            mSpliceTit = bundle.getString("spliceTit");
            title = bundle.getString("title");
            mTime = bundle.getString("time");
            mType = bundle.getString("type");
            mCityCode = bundle.getString("cityCode");
            mStartDate = (Date) bundle.getSerializable("startDate");
            mEndDate = (Date) bundle.getSerializable("endDate");
        }
        return title;
    }

    private void setTabLayout() {
        CommonTabLayout tabLayout = obtainView(R.id.tab_layout);
        List<CustomTabEntity> tabEntities = new ArrayList<>();
        tabEntities.add(new TabEntity(mContext.getString(R.string.tab_this_month)));
        tabEntities.add(new TabEntity(mContext.getString(R.string.tab_annual)));
        tabEntities.add(new TabEntity(mContext.getString(R.string.tab_query_date)));
        tabLayout.setTabData(tabEntities);
        if (TextUtils.equals(mTime, "1")) {  //全年
            tabLayout.setCurrentTab(1);
        } else if (TextUtils.equals(mTime, "2")) { //本月
            tabLayout.setCurrentTab(0);
        } else if (TextUtils.equals(mTime, "3")) { //选择日期
            tabLayout.setCurrentTab(2);
        }
        tabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                if (position == 0) {  //本月
                    mTime = "2";
                    mStartDate = null;
                    mEndDate = null;
                    doRequest();
                } else if (position == 1) { //全年
                    mTime = "1";
                    mStartDate = null;
                    mEndDate = null;
                    doRequest();
                } else { //查询日期
                    mTime = "3";
                    onSelectDates();
                }
            }

            @Override
            public void onTabReselect(int position) {
                if (position == 2) {
                    onSelectDates();
                }
            }
        });
    }

    private void onSelectDates() {
        CalendarPickerHelper.showCalendarDialog(mContext, mSelectDate, new CalendarPickerHelper.SimpleCalendarPickerListener() {

            @Override
            public void onCalendarPicker(CalendarPickerDialog dialog, List<Date> selectedDates, Date start, Date end) {
                dialog.dismiss();
                mSelectDate = selectedDates;
                if (!selectedDates.isEmpty()) {
                    mStartDate = start;
                    mEndDate = end;
                    doRequest();
                } else {
                    mStartDate = null;
                    mEndDate = null;
                }
            }
        });
    }

    protected void requestByDelay() {
        mHandler = new Handler();
        mHandler.postDelayed(this::doRequest, 300);
    }

    public abstract void doRequest();

    public void detach() {
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
    }
}
