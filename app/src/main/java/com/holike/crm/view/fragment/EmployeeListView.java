package com.holike.crm.view.fragment;

import com.holike.crm.base.BaseView;
import com.holike.crm.bean.EmployeeBean;

import java.util.List;

public interface EmployeeListView extends BaseView {
    void onShowLoading();
    void getEmployeeList(List<EmployeeBean> mData);
    void getEmployeeListError(String errorMessage);
    void onHideLoading();
}
