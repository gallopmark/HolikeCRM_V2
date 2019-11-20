package com.holike.crm.view.activity;

import com.holike.crm.base.BaseView;

/**
 * Created by pony on 2019/9/19.
 * Copyright holike possess 2019.
 */
public interface EmployeeResetPwView extends BaseView {
    void onResetSuccess(String message);

    void onResetFailure(String failReason);
}
