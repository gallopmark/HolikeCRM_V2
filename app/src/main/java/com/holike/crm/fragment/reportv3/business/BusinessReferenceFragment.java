package com.holike.crm.fragment.reportv3.business;

import com.holike.crm.R;
import com.holike.crm.fragment.reportv3.GeneralReportFragment;

abstract class BusinessReferenceFragment<H extends BusinessReferenceHelper> extends GeneralReportFragment<H> {

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
    public void onSuccess(Object object) {
        super.onSuccess(object);
        mHelper.onSuccess(object);
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
