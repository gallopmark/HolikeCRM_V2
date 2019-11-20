package com.holike.crm.fragment.satisfaction;

import android.os.Bundle;

import androidx.annotation.NonNull;

import com.holike.crm.R;
import com.holike.crm.base.CommonFragment;
import com.holike.crm.bean.CustomerSatisfactionBean;
import com.holike.crm.presenter.fragment.CustomerSatisfactionPresenter;
import com.holike.crm.view.fragment.CustomerSatisfactionView;

/**
 * Created by pony on 2019/10/8.
 * Copyright holike possess 2019.
 * 客户满意度报表
 */
public class CustomerSatisfactionFragment extends CommonFragment<CustomerSatisfactionPresenter,
        CustomerSatisfactionView, SatisfactionHelper> implements CustomerSatisfactionView, SatisfactionHelper.Callback {

    @NonNull
    @Override
    protected SatisfactionHelper newHelper() {
        return new SatisfactionHelper(this, this);
    }

    @Override
    protected CustomerSatisfactionPresenter attachPresenter() {
        return new CustomerSatisfactionPresenter();
    }

    @Override
    protected int setContentViewId() {
        return R.layout.fragment_customer_satisfaction;
    }

    @Override
    protected void setup() {
        setTitle(mContext.getString(R.string.customer_satisfaction_tips));
    }

    @Override
    public void onHttpRequest(String type, String cityCode, String datetime) {
        showLoading();
        mPresenter.doRequest(type, cityCode, datetime);
    }

    @Override
    public void toNextLevel(Bundle bundle) {
        startFragment(new CustomerSatisfactionFragment(), bundle, true);
    }

    @Override
    public void onSuccess(CustomerSatisfactionBean bean) {
        dismissLoading();
        mHelper.onHttpSuccess(bean);
    }

    @Override
    public void onFailure(String failReason) {
        dismissLoading();
        mHelper.onHttpFailure(failReason);
    }

    @Override
    protected void reload() {
        mHelper.doRequest();
    }

    @Override
    public void onDestroyView() {
        mHelper.deDetach();
        super.onDestroyView();
    }
}
