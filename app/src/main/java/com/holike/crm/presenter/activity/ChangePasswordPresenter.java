package com.holike.crm.presenter.activity;

import android.content.Context;
import android.text.TextUtils;

import com.holike.crm.R;
import com.holike.crm.base.BasePresenter;
import com.holike.crm.model.activity.ChangePasswordModel;
import com.holike.crm.util.CheckUtils;
import com.holike.crm.view.activity.ChangePasswordView;

/**
 * Created by wqj on 2018/2/25.
 * 修改密码
 */

public class ChangePasswordPresenter extends BasePresenter<ChangePasswordView, ChangePasswordModel> {

    /**
     * 修改密码
     *
     * @param oldPassword  旧密码
     * @param newPassword  新密码
     * @param surePassword 确认密码
     */
    public void changePassword(Context context, String oldPassword, String newPassword, String surePassword) {
        if (TextUtils.isEmpty(oldPassword)) {
            onWarn(context.getString(R.string.change_password_oldpassword_et));
        } else {
            if (TextUtils.isEmpty(newPassword)) {
                onWarn(context.getString(R.string.change_password_newpassword_et));
            } else {
                if (TextUtils.isEmpty(surePassword)) {
                    onWarn(context.getString(R.string.change_password_surepassword_et));
                } else {
                    if (!CheckUtils.isPassword(newPassword)) {
                        onWarn(context.getString(R.string.hint_password_regex_tips));
                    } else {
                        if (!TextUtils.equals(newPassword, surePassword)) {
                            onWarn(context.getString(R.string.reset_password_error_tips));
                        } else {
                            if (getModel() != null) {
                                getModel().changePassword(newPassword, oldPassword, new ChangePasswordModel.ChangePasswordListener() {
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
                            }
                        }
                    }
                }
            }
        }
//        if (TextUtils.isEmpty(oldPassword) || TextUtils.isEmpty(newPassword) || TextUtils.isEmpty(surePassword)) {
//            if (getView() != null) {
//                getView().warn(context.getString(R.string.tips_complete_information));
//            }
//        } else {
//
//        } else if (newPassword.equals(surePassword)) {
//            model.changePassword(newPassword, oldPassword, new ChangePasswordModel.ChangePasswordListener() {
//                @Override
//                public void success() {
//                    if (getView() != null)
//                        getView().changePasswordSuccess();
//                }
//
//                @Override
//                public void failed(String err) {
//                    if (getView() != null)
//                        getView().changePasswordFailed(err);
//                }
//            });
//        } else {
//            if (getView() != null)
//                getView().warn("新密码不一致");
//        }
    }

    private void onWarn(String text) {
        if (getView() != null) {
            getView().warn(text);
        }
    }
}
