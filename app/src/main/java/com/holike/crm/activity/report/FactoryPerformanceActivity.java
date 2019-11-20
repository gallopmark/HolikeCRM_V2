package com.holike.crm.activity.report;

import androidx.annotation.NonNull;

import com.holike.crm.R;
import com.holike.crm.bean.FactoryPerformanceBean;

/**
 * Created by pony on 2019/11/6.
 * Version v3.0 app报表
 * 出产业绩报表-经销商
 */
public class FactoryPerformanceActivity extends GeneralReportActivity<FactoryPerformanceHelper> implements FactoryPerformanceHelper.Callback {
    @NonNull
    @Override
    protected FactoryPerformanceHelper newHelper() {
        return new FactoryPerformanceHelper(this, this);
    }

    @Override
    protected int setContentViewId() {
        return R.layout.activity_factory_performance;
    }

    @Override
    public void onRequest(String dimensionOf, String month) {
        showLoading();
        mPresenter.getFactoryPerformance(dimensionOf, month);
    }

    @Override
    public void onSuccess(Object obj) {
        super.onSuccess(obj);
        mActivityHelper.onSuccess((FactoryPerformanceBean) obj);
    }

    @Override
    public void onFailure(String failReason) {
        super.onFailure(failReason);
        mActivityHelper.onFailure(failReason);
    }

    @Override
    public void reload() {
        mActivityHelper.doRequest();
    }

    @Override
    protected void onDestroy() {
        mActivityHelper.destroy();
        super.onDestroy();
    }
}
