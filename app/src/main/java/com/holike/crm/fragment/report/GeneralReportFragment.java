package com.holike.crm.fragment.report;

import com.holike.crm.base.BaseHelper;
import com.holike.crm.base.CommonFragment;
import com.holike.crm.presenter.GeneralReportPresenter;
import com.holike.crm.view.GeneralReportView;

/**
 * Created by pony on 2019/10/22.
 * Copyright holike possess 2019.
 * 报表通用fragment
 */
public abstract class GeneralReportFragment<H extends BaseHelper>
        extends CommonFragment<GeneralReportPresenter, GeneralReportView, H>
        implements GeneralReportView {

    @Override
    protected GeneralReportPresenter attachPresenter() {
        return new GeneralReportPresenter();
    }

    @Override
    public void onSuccess(Object object) {
        dismissLoading();
    }

    @Override
    public void onFailure(String failReason) {
        dismissLoading();
    }
}
