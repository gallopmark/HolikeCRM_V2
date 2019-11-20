package com.holike.crm.fragment.report.multiple;


import com.holike.crm.R;
import com.holike.crm.bean.PerformanceAnalysisBean;
import com.holike.crm.fragment.report.GeneralReportFragment;

import java.util.Date;

/**
 * Created by pony on 2019/11/7.
 * Version v3.0 app报表
 * 出厂业绩分析-完成率同比通用
 */
abstract class AbsAnalysisFragment<H extends AbsAnalysisHelper> extends GeneralReportFragment<H> implements AbsAnalysisHelper.Callback {

    @Override
    protected int setContentViewId() {
        return R.layout.fragment_report_general;
    }

    @Override
    public void onRequest(String time, String dimensionOf, String dimensionOfCli, String type, String cityCode, Date startDate, Date endDate) {
        showLoading();
        mPresenter.getMarketPerformanceAnalysis(time, dimensionOf, dimensionOfCli, type, cityCode, startDate, endDate);
    }

    @Override
    public void onSuccess(Object object) {
        super.onSuccess(object);
        mHelper.onSuccess((PerformanceAnalysisBean) object);
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
        mHelper.detach();
        super.onDestroyView();
    }
}
