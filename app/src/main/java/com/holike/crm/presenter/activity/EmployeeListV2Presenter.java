package com.holike.crm.presenter.activity;

import com.holike.crm.base.BasePresenter;
import com.holike.crm.bean.EmployeeBeanV2;
import com.holike.crm.http.RequestCallBack;
import com.holike.crm.model.employee.GeneralEmployeeModel;
import com.holike.crm.view.activity.EmployeeListV2View;

import java.util.List;

/**
 * Created by gallop on 2019/8/6.
 * Copyright holike possess 2019.
 * 员工列表
 */
public class EmployeeListV2Presenter extends BasePresenter<EmployeeListV2View, GeneralEmployeeModel> {

    public void getEmployeeList(String content, String shopId, String status, String roleCode) {
        if (getModel() != null) {
            getModel().getEmployeeList(content, shopId, status, roleCode, new RequestCallBack<List<EmployeeBeanV2>>() {
                @Override
                public void onFailed(String failReason) {
                    if (getView() != null) {
                        getView().onFailure(failReason);
                    }
                }

                @Override
                public void onSuccess(List<EmployeeBeanV2> list) {
                    if (getView() != null) {
                        getView().onSuccess(list);
                    }
                }
            });
        }
    }
}
