package com.holike.crm.view.activity;

import com.holike.crm.base.BaseView;
import com.holike.crm.bean.EmployeeDetailV2Bean;

/**
 * Created by pony on 2019/8/7.
 * Copyright holike possess 2019.
 */
public interface EmployeeDetailV2View extends BaseView {
    void onSuccess(EmployeeDetailV2Bean bean);
    void onFailure(String failReason);
}
