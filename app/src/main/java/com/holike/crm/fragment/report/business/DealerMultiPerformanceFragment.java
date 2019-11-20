package com.holike.crm.fragment.report.business;

import com.holike.crm.R;
import com.holike.crm.bean.BusinessReferenceTypeBean;
import com.holike.crm.fragment.report.GeneralReportFragment;

/*经销商-系列、花色、宅配业绩*/
abstract class DealerMultiPerformanceFragment<H extends DealerMultiPerformanceHelper>
        extends GeneralReportFragment<H> implements DealerMultiPerformanceHelper.Callback {

    @Override
    protected void setup() {
        setTitle();
    }

    abstract void setTitle();

    @Override
    protected int setContentViewId() {
        return R.layout.fragment_business_reference;
    }

    @Override
    public void doRequest(String month, String dimensionOf, String dimensionOfCli, String shopCode) {
        showLoading();
        mPresenter.getDealerMultiPerformance(month, dimensionOf, dimensionOfCli, shopCode);
    }

    @Override
    public void onSuccess(Object object) {
        super.onSuccess(object);
        mHelper.onSuccess((BusinessReferenceTypeBean) object);
    }

    @Override
    public void onFailure(String failReason) {
        super.onFailure(failReason);
        mHelper.onFailure(failReason);
    }

    @Override
    protected void reload() {
        mHelper.reload();
    }
}
