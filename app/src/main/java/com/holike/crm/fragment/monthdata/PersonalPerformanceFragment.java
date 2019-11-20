package com.holike.crm.fragment.monthdata;

import androidx.annotation.NonNull;

import com.holike.crm.R;
import com.holike.crm.bean.PersonalPerformanceBean;
import com.holike.crm.fragment.report.GeneralReportFragment;

import java.util.Date;

/**
 * Created by pony on 2019/10/25.
 * Copyright holike possess 2019.
 * 个人绩效-适用 售后，仓库，安装工，安装经理，业务员导购，设计师
 */
public class PersonalPerformanceFragment extends GeneralReportFragment<PersonalPerformanceHelper> implements PersonalPerformanceHelper.Callback {

    @NonNull
    @Override
    protected PersonalPerformanceHelper newHelper() {
        return new PersonalPerformanceHelper(this, this);
    }

    @Override
    protected int setContentViewId() {
        return R.layout.fragment_personal_performance;
    }

    @Override
    protected void setup() {
        setTitle(R.string.title_personal_performance);
    }

    @Override
    public void onQuery(String type, String time, Date startDate, Date endDate) {
        showLoading();
        mPresenter.getPersonalPerformance(type, time, startDate, endDate);
    }

    @Override
    public void onSuccess(Object object) {
        super.onSuccess(object);
        mHelper.onSuccess((PersonalPerformanceBean) object);
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
}
