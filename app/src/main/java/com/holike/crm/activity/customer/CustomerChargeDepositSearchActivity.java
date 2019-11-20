package com.holike.crm.activity.customer;

import android.os.Bundle;

import com.holike.crm.R;
import com.holike.crm.activity.customer.helper.CustomerChargeDepositSearchHelper;

/**
 * Created by pony on 2019/9/3.
 * Copyright holike possess 2019.
 * 收取订金客户（搜索页面）
 */
public class CustomerChargeDepositSearchActivity extends GeneralCustomerActivity {
    private CustomerChargeDepositSearchHelper mHelper;

    @Override
    protected int setContentViewId() {
        return R.layout.activity_customer_charge_deposi_search;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        mHelper = new CustomerChargeDepositSearchHelper(this);
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

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, 0);
    }
}
