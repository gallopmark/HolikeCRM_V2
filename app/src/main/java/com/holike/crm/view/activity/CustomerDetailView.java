package com.holike.crm.view.activity;

import com.holike.crm.base.BaseView;
import com.holike.crm.bean.CustomerDetailBean;

/**
 * Created by wqj on 2018/6/11.
 * 客户管理
 */

public interface CustomerDetailView extends BaseView {

    void getCustomerSuccess(CustomerDetailBean bean);

    void getCustomerFailed(String failed);

}
