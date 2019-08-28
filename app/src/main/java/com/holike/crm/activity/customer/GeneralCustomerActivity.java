package com.holike.crm.activity.customer;

import com.holike.crm.base.MyFragmentActivity;
import com.holike.crm.presenter.fragment.GeneralCustomerPresenter;
import com.holike.crm.view.fragment.GeneralCustomerView;

/**
 * Created by gallop on 2019/8/14.
 * Copyright holike possess 2019.
 * 客户管理模块通用activity
 */
abstract class GeneralCustomerActivity extends MyFragmentActivity<GeneralCustomerPresenter, GeneralCustomerView> implements GeneralCustomerView {
    @Override
    protected GeneralCustomerPresenter attachPresenter() {
        return new GeneralCustomerPresenter();
    }

    @Override
    public void onSuccess(Object object) {
        dismissLoading();
    }

    @Override
    public void onFailure(String failReason) {
        dismissLoading();
        showLongToast(failReason);
    }

    public void setResultOk(Object object){
        if(object instanceof String){
            showShortToast((String) object);
            setResult(RESULT_OK);
            finish();
        }
    }
}
