package com.holike.crm.view.activity;

import com.holike.crm.base.BaseView;

public interface EmployeeResetPasswordView extends BaseView {
    void onShowLoading();
    void onResetSuccess();
    void onResetFailure(String message);
    void onHideLoading();
}
