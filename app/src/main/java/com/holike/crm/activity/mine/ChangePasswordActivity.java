package com.holike.crm.activity.mine;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.holike.crm.R;
import com.holike.crm.base.MyFragmentActivity;
import com.holike.crm.presenter.activity.ChangePasswordPresenter;
import com.holike.crm.presenter.fragment.MinePresenter;
import com.holike.crm.view.activity.ChangePasswordView;

import butterknife.BindView;
import butterknife.OnClick;

import static com.holike.crm.presenter.activity.LoginPresenter.showPassword;

/**
 * 修改密码
 */
public class ChangePasswordActivity extends MyFragmentActivity<ChangePasswordPresenter, ChangePasswordView> implements ChangePasswordView {


    @BindView(R.id.et_change_password_oldpassword)
    EditText etOldpassword;
    @BindView(R.id.et_change_password_newpassword)
    EditText etNewpassword;
    @BindView(R.id.et_change_password_surepassword)
    EditText etSurepassword;
    @BindView(R.id.iv_change_password_oldpassword)
    ImageView ivOldpassword;
    @BindView(R.id.iv_change_password_newpassword)
    ImageView ivNewpassword;
    @BindView(R.id.iv_change_password_surepassword)
    ImageView ivSurepassword;

    private boolean isShowOld, isShowNew, isShowSure;

    @Override
    protected int setContentViewId() {
        return R.layout.activity_change_password;
    }

    @Override
    protected ChangePasswordPresenter attachPresenter() {
        return new ChangePasswordPresenter();
    }

    @Override
    protected void init() {
        setTitle(getString(R.string.my_change_password));
    }

    @Override
    public void changePasswordSuccess() {
        dismissLoading();
        showShortToast(R.string.tips_change_success);
        MinePresenter.logout(this);
    }

    @Override
    public void changePasswordFailed(String failed) {
        dismissLoading();
        showShortToast(failed);
    }

    @Override
    public void warn(String text) {
        dismissLoading();
        showShortToast(text);
    }

    @OnClick({R.id.iv_change_password_oldpassword, R.id.iv_change_password_newpassword, R.id.iv_change_password_surepassword, R.id.btn_change_password_save})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_change_password_oldpassword:
                isShowOld = showPassword(isShowOld, etOldpassword, ivOldpassword);
                break;
            case R.id.iv_change_password_newpassword:
                isShowNew = showPassword(isShowNew, etNewpassword, ivNewpassword);
                break;
            case R.id.iv_change_password_surepassword:
                isShowSure = showPassword(isShowSure, etSurepassword, ivSurepassword);
                break;
            case R.id.btn_change_password_save:
                showLoading();
                mPresenter.changePassword(etOldpassword.getText().toString(), etNewpassword.getText().toString(), etSurepassword.getText().toString());
                break;
        }
    }
}
