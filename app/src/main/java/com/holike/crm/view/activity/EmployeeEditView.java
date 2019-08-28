package com.holike.crm.view.activity;

import com.holike.crm.base.BaseView;

public interface EmployeeEditView extends BaseView {
    void onContentFill(boolean isFill);
    void onTabChanged(int currentTab);
    void onShowLoading();
    void onSaveSuccess();
    void onSaveFailure(String message);
    void onHideLoading();
}
