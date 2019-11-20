package com.holike.crm.activity.customer;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.holike.crm.R;
import com.holike.crm.activity.customer.helper.CustomerChargeDepositHelper;

/**
 * Created by pony on 2019/9/2.
 * Copyright holike possess 2019.
 * 收取订金（最近联系客户列表页）
 */
public class CustomerChargeDepositActivity extends GeneralCustomerActivity {

    private CustomerChargeDepositHelper mHelper;

    @Override
    protected int setContentViewId() {
        return R.layout.activity_customer_charge_deposit;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        setTitle(getString(R.string.receive_deposit_title));
        setRightMenu(R.string.customer_manager_newInstance);
        mHelper = new CustomerChargeDepositHelper(this);
    }

    @Override
    protected void clickRightMenu(CharSequence menuText, View actionView) {
        Intent intent = new Intent(this, CustomerEditActivity.class);
        intent.putExtra("isDeposit", true);
        openActivity(intent);
    }

    @Override
    public void onSuccess(Object object) {
        super.onSuccess(object);
    }

    @Override
    public void onFailure(String failReason) {
        dismissLoading();
        mHelper.onFailure(failReason);
    }
}
