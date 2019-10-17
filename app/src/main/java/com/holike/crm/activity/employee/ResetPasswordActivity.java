package com.holike.crm.activity.employee;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.holike.crm.R;
import com.holike.crm.base.MyFragmentActivity;
import com.holike.crm.presenter.activity.EmployeeResetPasswordPresenter;
import com.holike.crm.util.KeyBoardUtil;
import com.holike.crm.view.activity.EmployeeResetPasswordView;

import butterknife.BindView;

/*员工信息 重置密码*/
@Deprecated
public class ResetPasswordActivity extends MyFragmentActivity<EmployeeResetPasswordPresenter, EmployeeResetPasswordView> implements EmployeeResetPasswordView {

    @BindView(R.id.mNewPassword)
    EditText mNewPassword;
    @BindView(R.id.mClearPwdIv)
    ImageView mClearPwdIv;
    @BindView(R.id.mEyePwdIv)
    ImageView mEyePwdIv;
    @BindView(R.id.mConfirmPassword)
    EditText mConfirmPassword;
    @BindView(R.id.mClearPwdIv2)
    ImageView mClearPwdIv2;
    @BindView(R.id.mEyePwdIv2)
    ImageView mEyePwdIv2;
    @BindView(R.id.mSaveTextView)
    TextView mSaveTextView;

    @Override
    protected EmployeeResetPasswordPresenter attachPresenter() {
        return new EmployeeResetPasswordPresenter();
    }

    @Override
    protected int setContentViewId() {
        return R.layout.activity_employee_resetpassword;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        setTitle(getString(R.string.reset_password_title));
        mPresenter.watcher(mNewPassword, mConfirmPassword, mClearPwdIv, mClearPwdIv2, mSaveTextView);
        mPresenter.onShowPassword(mNewPassword, mEyePwdIv);
        mPresenter.onShowPassword(mConfirmPassword, mEyePwdIv2);
        mSaveTextView.setOnClickListener(view -> onResetPassword());
    }

    private void onResetPassword() {
        String password = mNewPassword.getText().toString();
        String confrimPassword = mConfirmPassword.getText().toString();
        if (!TextUtils.equals(password, confrimPassword)) {
            showShortToast(R.string.reset_password_error_tips);
        } else {
            String employeeId = getIntent().getExtras() == null ? "" : getIntent().getExtras().getString("employeeId", "");
            mPresenter.onResetPassword(employeeId, mNewPassword.getText().toString());
        }
    }

    @Override
    public void onShowLoading() {
        showLoading();
    }

    @Override
    public void onResetSuccess() {
        showShortToast(R.string.employee_reset_password_success);
        finish();
    }

    @Override
    public void onResetFailure(String message) {
        showShortToast(message);
    }

    @Override
    public void onHideLoading() {
        dismissLoading();
    }

    @Override
    public void finish() {
        KeyBoardUtil.hideSoftInput(this);
        super.finish();
    }
}
