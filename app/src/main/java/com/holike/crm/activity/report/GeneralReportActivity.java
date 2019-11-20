package com.holike.crm.activity.report;


import com.holike.crm.base.BaseHelper;
import com.holike.crm.base.CommonActivity;
import com.holike.crm.presenter.GeneralReportPresenter;
import com.holike.crm.view.GeneralReportView;

/**
 * Created by pony on 2019/10/28.
 * Version v3.0 app报表
 */
abstract class GeneralReportActivity<H extends BaseHelper> extends CommonActivity<GeneralReportPresenter, GeneralReportView, H>
        implements GeneralReportView {

    @Override
    protected GeneralReportPresenter attachPresenter() {
        return new GeneralReportPresenter();
    }

    @Override
    public void onSuccess(Object obj) {
        dismissLoading();
    }

    @Override
    public void onFailure(String failReason) {
        dismissLoading();
    }
}
