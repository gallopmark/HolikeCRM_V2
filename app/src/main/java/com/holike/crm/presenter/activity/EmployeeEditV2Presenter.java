package com.holike.crm.presenter.activity;

import com.holike.crm.base.BasePresenter;
import com.holike.crm.bean.EmployeeEditResultBean;
import com.holike.crm.http.MyJsonParser;
import com.holike.crm.http.RequestCallBack;
import com.holike.crm.model.employee.GeneralEmployeeModel;
import com.holike.crm.view.activity.EmployeeEditV2View;

import java.util.Map;

/**
 * Created by pony on 2019/8/6.
 * Copyright holike possess 2019.
 */
public class EmployeeEditV2Presenter extends BasePresenter<EmployeeEditV2View, GeneralEmployeeModel> {

    /*新增或修改员工 type:-1新增 1保存基础信息 2关联组织 3角色权限*/
    public void saveEmployee(int type, Map<String, String> params) {
        params.put("type", String.valueOf(type));
        if (getModel() != null) {
            getModel().saveEmployee(params, new RequestCallBack<String>() {
                @Override
                public void onFailed(String failReason) {
                    if (getView() != null) {
                        getView().onSaveFailure(failReason);
                    }
                }

                @Override
                public void onSuccess(String result) {
                    if (getView() != null) {
                        getView().onSaveSuccess(MyJsonParser.fromJson(result, EmployeeEditResultBean.class));
                    }
                }
            });
        }
    }
}
