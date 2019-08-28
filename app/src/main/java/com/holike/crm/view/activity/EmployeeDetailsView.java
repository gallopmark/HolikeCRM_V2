package com.holike.crm.view.activity;

import com.holike.crm.base.BaseView;
import com.holike.crm.bean.EmployeeDetailBean;

public interface EmployeeDetailsView extends BaseView {
    void onShowLoading();

    void onSuccess(EmployeeDetailBean bean);

    void onFailure(String message);

    void onHideLoading();

    void onDataChanged(boolean isChanged);

    void onSaveSuccess();
    void onSaveFailure(String errorMessage);
}
