package com.holike.crm.presenter.activity;

import com.holike.crm.base.BasePresenter;
import com.holike.crm.bean.EmployeeDetailV2Bean;
import com.holike.crm.http.RequestCallBack;
import com.holike.crm.model.employee.GeneralEmployeeModel;
import com.holike.crm.view.activity.EmployeeDetailV2View;

/**
 * Created by gallop on 2019/8/7.
 * Copyright holike possess 2019.
 * 员工详情 v2.0
 */
public class EmployeeDetailV2Presenter extends BasePresenter<EmployeeDetailV2View, GeneralEmployeeModel> {

    /*获取员工信息*/
    public void getEmployeeInfo(String userId) {
        if (getModel() != null) {
            getModel().getEmployeeInfo(userId, new RequestCallBack<EmployeeDetailV2Bean>() {
                @Override
                public void onFailed(String failReason) {
                    if (getView() != null) {
                        getView().onFailure(failReason);
                    }
                }

                @Override
                public void onSuccess(EmployeeDetailV2Bean bean) {
                    if (getView() != null) {
                        getView().onSuccess(bean);
                    }
                }
            });
        }
    }
}
