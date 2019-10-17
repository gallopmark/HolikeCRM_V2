package com.holike.crm.activity.customer;

import android.content.Intent;
import android.os.Bundle;

import com.holike.crm.R;
import com.holike.crm.activity.customer.helper.CustomerOnlineLogHelper;
import com.holike.crm.base.BaseActivity;
import com.holike.crm.bean.CustomerOnlineLogBean;
import com.holike.crm.enumeration.CustomerValue;

/**
 * Created by gallop on 2019/10/15.
 * Copyright holike possess 2019.
 * 线上引流客户（线上记录）
 */
public class CustomerOnlineLogActivity extends GeneralCustomerActivity implements CustomerOnlineLogHelper.OnlineLogCallback {

    public static void open(BaseActivity<?, ?> activity, String personalId) {
        Intent intent = new Intent(activity, CustomerOnlineLogActivity.class);
        intent.putExtra(CustomerValue.PERSONAL_ID, personalId);
        activity.openActivity(intent);
    }

    private CustomerOnlineLogHelper mHelper;

    @Override
    protected int setContentViewId() {
        return R.layout.activity_customer_onlinelogs;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        setTitle(getString(R.string.tips_customer_online_title));
        mHelper = new CustomerOnlineLogHelper(this, this);
    }

    @Override
    public void onRequestOnlineLog(String customerId) {
        showLoading();
        mPresenter.getCustomerOnlineLog(customerId);
    }

    @Override
    public void onSuccess(Object object) {
        dismissLoading();
        mHelper.onSuccess((CustomerOnlineLogBean) object);
    }

    @Override
    public void onFailure(String failReason) {
        dismissLoading();
        mHelper.onFailure(failReason);
    }

    @Override
    public void reload() {
        mHelper.reload();
    }
}
