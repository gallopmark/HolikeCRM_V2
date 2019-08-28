package com.holike.crm.presenter.activity;

import android.text.TextUtils;

import com.holike.crm.base.BasePresenter;
import com.holike.crm.model.activity.ChangePasswordModel;
import com.holike.crm.view.activity.ChangePasswordView;

/**
 * Created by wqj on 2018/2/25.
 * 修改密码
 */

public class ChangePasswordPresenter extends BasePresenter<ChangePasswordView, ChangePasswordModel> {

    /**
     * 修改密码
     *
     * @param oldPassword
     * @param newPassword
     * @param surePassword
     */
    public void changePassword(String oldPassword, String newPassword, String surePassword) {
        if (TextUtils.isEmpty(oldPassword) || TextUtils.isEmpty(newPassword) || TextUtils.isEmpty(surePassword)) {
            if (getView() != null)
                getView().warn("请填写完整");
        } else if (newPassword.equals(surePassword)) {
            model.changePassword(newPassword, oldPassword, new ChangePasswordModel.ChangePasswordListener() {
                @Override
                public void success() {
                    if (getView() != null)
                        getView().changePasswordSuccess();
                }

                @Override
                public void failed(String err) {
                    if (getView() != null)
                        getView().changePasswordFailed(err);
                }
            });
        } else {
            if (getView() != null)
                getView().warn("新密码不一致");
        }
    }
}
