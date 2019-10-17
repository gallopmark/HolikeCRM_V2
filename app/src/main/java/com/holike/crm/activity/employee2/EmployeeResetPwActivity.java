package com.holike.crm.activity.employee2;

import androidx.annotation.NonNull;

import com.holike.crm.R;
import com.holike.crm.base.CommonActivity;
import com.holike.crm.presenter.activity.EmployeeResetPasswordPresenter;
import com.holike.crm.presenter.activity.EmployeeResetPwPresenter;
import com.holike.crm.util.KeyBoardUtil;
import com.holike.crm.view.activity.EmployeeResetPasswordView;
import com.holike.crm.view.activity.EmployeeResetPwView;

/**
 * 员工信息 重置密码
 */
public class EmployeeResetPwActivity extends CommonActivity<EmployeeResetPwPresenter, EmployeeResetPwView, EmployeeResetPwHelper>
        implements EmployeeResetPwHelper.Callback, EmployeeResetPwView {

    @Override
    protected EmployeeResetPwPresenter attachPresenter() {
        return new EmployeeResetPwPresenter();
    }

    @Override
    protected int setContentViewId() {
        return R.layout.activity_employee_resetpw2;
    }

    @NonNull
    @Override
    protected EmployeeResetPwHelper newHelper() {
        return new EmployeeResetPwHelper(this, this);
    }

    @Override
    protected void setup() {
        setTitle(getString(R.string.reset_password_title));
    }

    @Override
    public void onSave(String userId, String newPassword) {
        showLoading();
        mPresenter.resetPassword(userId, newPassword);
    }

    @Override
    public void onResetSuccess(String message) {
        dismissLoading();
        showShortToast(message);
        finish();
    }

    @Override
    public void onResetFailure(String failReason) {
        dismissLoading();
        showShortToast(failReason);
    }

    @Override
    public void finish() {
        KeyBoardUtil.hideSoftInput(this);
        super.finish();
    }
}
