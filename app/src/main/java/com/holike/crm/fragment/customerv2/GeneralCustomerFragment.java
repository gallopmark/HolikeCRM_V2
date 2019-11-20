package com.holike.crm.fragment.customerv2;

import android.app.Activity;

import com.holike.crm.base.BaseActivity;
import com.holike.crm.base.MyFragment;
import com.holike.crm.presenter.fragment.GeneralCustomerPresenter;
import com.holike.crm.view.fragment.GeneralCustomerView;

/**
 * Created by pony on 2019/8/1.
 * Copyright holike possess 2019.
 */
abstract class GeneralCustomerFragment extends MyFragment<GeneralCustomerPresenter, GeneralCustomerView> implements GeneralCustomerView {
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
        showShortToast(failReason);
    }

    void setResultOk(Object object) {
        if (object instanceof String) {
            showShortToast((String) object);
            BaseActivity<?, ?> activity = (BaseActivity<?, ?>) mContext;
            activity.setResult(Activity.RESULT_OK);
            activity.finish();
        }
    }
}
