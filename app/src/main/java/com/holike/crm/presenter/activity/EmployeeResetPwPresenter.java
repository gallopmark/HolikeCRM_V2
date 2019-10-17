package com.holike.crm.presenter.activity;

import com.holike.crm.base.BasePresenter;
import com.holike.crm.http.MyJsonParser;
import com.holike.crm.http.RequestCallBack;
import com.holike.crm.model.fragment.EmployeeModel;
import com.holike.crm.view.activity.EmployeeResetPwView;

/**
 * Created by gallop on 2019/9/19.
 * Copyright holike possess 2019.
 */
public class EmployeeResetPwPresenter extends BasePresenter<EmployeeResetPwView, EmployeeModel> {

    public void resetPassword(String userId, String newPassword) {
        if (getModel() != null) {
            getModel().resetPassword(userId, newPassword, new RequestCallBack<String>() {
                @Override
                public void onFailed(String failReason) {
                    if (getView() != null) {
                        getView().onResetFailure(failReason);
                    }
                }

                @Override
                public void onSuccess(String result) {
                    if (getView() != null) {
                        getView().onResetSuccess(MyJsonParser.getShowMessage(result));
                    }
                }
            });
        }
    }
}
