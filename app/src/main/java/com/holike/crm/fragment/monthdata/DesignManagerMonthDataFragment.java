package com.holike.crm.fragment.monthdata;

import androidx.annotation.NonNull;

import com.holike.crm.R;
import com.holike.crm.bean.MonthDataDesignBean;
import com.holike.crm.fragment.report.GeneralReportFragment;

import java.util.Date;

/**
 * Created by pony on 2019/10/24.
 * Copyright holike possess 2019.
 * 设计经理-本月数据 v3.0
 */
public class DesignManagerMonthDataFragment extends GeneralReportFragment<DesignManagerMonthDataHelper> implements MonthDataHelper.Callback {

    @NonNull
    @Override
    protected DesignManagerMonthDataHelper newHelper() {
        return new DesignManagerMonthDataHelper(this, this);
    }

    @Override
    protected int setContentViewId() {
        return R.layout.fragment_month_data_common;
    }

    @Override
    protected void setup() {
        setTitle(R.string.title_staff_performance);
        setRightMenu(R.string.report_select_date, view -> mHelper.onSelectCalendar());
    }

    @Override
    public void onQuery(String type, String cityCode, Date startDate, Date endDate) {
        showLoading();
        mPresenter.getDesignManagerData(type, cityCode, startDate, endDate);
    }

    @Override
    public void onSuccess(Object object) {
        super.onSuccess(object);
        mHelper.onSuccess((MonthDataDesignBean) object);
    }

    @Override
    public void onFailure(String failReason) {
        super.onFailure(failReason);
        mHelper.onFailure(failReason);
    }

    @Override
    protected void reload() {
        mHelper.onQuery();
    }

    @Override
    public void onDestroyView() {
        mHelper.onDestroy();
        super.onDestroyView();
    }
}
