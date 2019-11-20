package com.holike.crm.fragment.employee.child;


import com.holike.crm.R;
import com.holike.crm.bean.EmployeeRankingBean;
import com.holike.crm.fragment.report.GeneralReportFragment;

import java.util.Date;

abstract class CommonDealerFragment<H extends CommonDealerHelper> extends GeneralReportFragment<H> {

    @Override
    protected int setContentViewId() {
        return R.layout.fragment_employee_ranking_dealer;
    }

    protected void getData(Date startDate, Date endDate, String typeData) {
        showLoading();
        mPresenter.getEmployeeRanking(startDate, endDate, null, null, typeData);
    }

    @Override
    public void onSuccess(Object obj) {
        dismissLoading();
        mHelper.onSuccess((EmployeeRankingBean) obj);
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
}
