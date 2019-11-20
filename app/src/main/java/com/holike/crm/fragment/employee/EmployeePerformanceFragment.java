package com.holike.crm.fragment.employee;

import androidx.annotation.NonNull;

import com.holike.crm.R;
import com.holike.crm.bean.MonthDataBossBean;
import com.holike.crm.fragment.report.GeneralReportFragment;

import java.util.Date;

/**
 * Created by pony on 2019/10/30.
 * Version v3.0 app报表
 * 员工绩效
 */
public class EmployeePerformanceFragment extends GeneralReportFragment<EmployeePerformanceHelper> implements EmployeePerformanceHelper.Callback {

    @NonNull
    @Override
    protected EmployeePerformanceHelper newHelper() {
        return new EmployeePerformanceHelper(this, this);
    }

    @Override
    protected int setContentViewId() {
        return R.layout.fragment_employee_performance;
    }

    @Override
    protected void setup() {
        setTitle(R.string.title_staff_performance);
        setRightMenu(R.string.query_date, view -> mHelper.onCalendarPicker());
    }

    @Override
    public void doRequest(String type, String cityCode, Date startDate, Date endDate) {
        showLoading();
        mPresenter.getEmployeePerformance(type, cityCode, startDate, endDate);
    }

    @Override
    public void onSuccess(Object object) {
        super.onSuccess(object);
        mHelper.onSuccess((MonthDataBossBean) object);
    }

    @Override
    public void onFailure(String failReason) {
        super.onFailure(failReason);
        mHelper.onFailure(failReason);
    }

    @Override
    protected void reload() {
        mHelper.doRequest();
    }

    @Override
    public void onDestroyView() {
        mHelper.onDestroy();
        super.onDestroyView();
    }
}
