package com.holike.crm.view.activity;


import com.holike.crm.base.BaseView;
import com.holike.crm.bean.CustomerListBeanV2;
import com.holike.crm.bean.SysCodeItemBean;

/**
 * Created by pony 2019/7/5
 * Copyright (c) 2019 holike
 */
public interface CustomerV2View extends BaseView {
    void onGetSystemCodeSuccess(SysCodeItemBean systemCode);

    void onGetSystemCodeFailure(String failed);

    void getCustomerListSuccess(CustomerListBeanV2 bean);

    void getCustomerListFailed(String failed);

    void deleteCustomerSuccess(String result, int position);

    void deleteCustomerFailure(String message);

    void onSavePhoneRecordSuccess(String message);

    void onSavePhoneRecordFailure(String body, String failReason);

    void distributionShopSuccess(String message);

    void distributionShopFailure(String failReason);
}
