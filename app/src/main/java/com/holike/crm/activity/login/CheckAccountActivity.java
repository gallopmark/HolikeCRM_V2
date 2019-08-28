package com.holike.crm.activity.login;

import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.holike.crm.R;
import com.holike.crm.base.BaseActivity;
import com.holike.crm.bean.CheckAccountBean;
import com.holike.crm.dialog.CheckAccountDialog;
import com.holike.crm.helper.BindViewHelper;
import com.holike.crm.presenter.activity.CheckAccountPresenter;
import com.holike.crm.presenter.activity.LoginPresenter;
import com.holike.crm.util.Constants;
import com.holike.crm.view.activity.CheckAccountView;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * 查询账号
 */
public class CheckAccountActivity extends BaseActivity<CheckAccountPresenter, CheckAccountView> implements CheckAccountView {

    @BindView(R.id.et_checkAccount_oldDealers)
    EditText etOldDealers;
    @BindView(R.id.et_checkAccount_oldUser)
    EditText etOldUser;
    @BindView(R.id.et_checkAccount_oldPassword)
    EditText etOldPassword;
    @BindView(R.id.btn_checkAccount_check)
    TextView btnCheck;
    @BindView(R.id.iv_login_showpassword)
    ImageView ivShowpassword;

    private boolean isShowPassword = false;

    @Override
    protected CheckAccountPresenter attachPresenter() {
        return new CheckAccountPresenter();
    }

    @Override
    protected int setContentViewId() {
        return R.layout.activity_check_account;
    }


    @Override
    protected void init() {
        BindViewHelper.bindBgView(btnCheck, etOldDealers, etOldPassword, etOldUser);
    }

    @OnClick({R.id.iv_back, R.id.iv_login_clearOldDealers, R.id.iv_login_clearOldUser, R.id.iv_login_showpassword, R.id.btn_checkAccount_check})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_login_clearOldDealers:
                etOldDealers.setText("");
                break;
            case R.id.iv_login_clearOldUser:
                etOldUser.setText("");
                break;
            case R.id.iv_login_showpassword:
                isShowPassword = LoginPresenter.showPassword(isShowPassword, etOldPassword, ivShowpassword);
                break;
            case R.id.btn_checkAccount_check:
                showLoading();
                mPresenter.checkAccount(etOldDealers.getText().toString(), etOldUser.getText().toString(), etOldPassword.getText().toString());
                break;
        }
    }

    /**
     * 查询成功
     *
     */
    @Override
    public void checkSuccess(final CheckAccountBean bean) {
        dismissLoading();
        new CheckAccountDialog(this).setData(bean.getDealerNumber(), bean.getLoginAccount()).setListener(new CheckAccountDialog.ClickListener() {
            @Override
            public void login() {
                Intent intent = new Intent();
                intent.putExtra(Constants.CHECK_ACCOUNT_LOGIN_ACCOUNT, bean.getLoginAccount());
                setResult(Constants.RESULT_CODE_CHECK_ACCOUNT, intent);
                finish();
            }
        }).show();
    }

    /**
     * 查询失败
     */
    @Override
    public void checkFailed(String failed) {
        dismissLoading();
        showShortToast(failed);
    }
}
