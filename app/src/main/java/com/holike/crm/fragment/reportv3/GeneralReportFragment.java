package com.holike.crm.fragment.reportv3;

import com.holike.crm.base.BaseHelper;
import com.holike.crm.base.CommonFragment;
import com.holike.crm.presenter.fragment.GeneralReportPresenter;
import com.holike.crm.view.fragment.GeneralCustomerView;

/**
 * Created by gallop on 2019/10/22.
 * Copyright holike possess 2019.
 * 报表通用fragment
 */
public abstract class GeneralReportFragment<H extends BaseHelper> extends CommonFragment<GeneralReportPresenter, GeneralCustomerView, H> implements GeneralCustomerView {

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
