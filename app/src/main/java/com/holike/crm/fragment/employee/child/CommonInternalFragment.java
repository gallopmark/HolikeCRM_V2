package com.holike.crm.fragment.employee.child;

import com.holike.crm.R;
import com.holike.crm.bean.EmployeeRankingBean;
import com.holike.crm.bean.EvaluateTypeBean;
import com.holike.crm.fragment.report.GeneralReportFragment;
import com.holike.crm.presenter.GeneralReportPresenter;

import java.util.List;

abstract class CommonInternalFragment<H extends CommonInternalHelper> extends GeneralReportFragment<H>
        implements CommonInternalHelper.Callback, GeneralReportPresenter.OnGetEvaluateTypeCallback {

    @Override
    protected int setContentViewId() {
        return R.layout.fragment_employee_ranking_internal;
    }

    @Override
    public void onQueryShop(String type, String cityCode) {
        showLoading();
        mPresenter.getEvaluateType(type, cityCode, this);
    }

    @Override
    public void onGetEvaluateTypeSuccess(List<EvaluateTypeBean> dataList) {
        dismissLoading();
        mHelper.showShopPicker(dataList);
    }

    @Override
    public void onGetEvaluateTypeFailure(String failReason) {
        dismissLoading();
        showShortToast(failReason);
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

    @Override
    public void onDestroyView() {
        mHelper.onDestroy();
        super.onDestroyView();
    }
}
