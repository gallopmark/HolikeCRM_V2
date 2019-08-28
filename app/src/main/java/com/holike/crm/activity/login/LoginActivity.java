package com.holike.crm.activity.login;

import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.holike.crm.R;
import com.holike.crm.activity.main.MainActivity;
import com.holike.crm.base.BaseActivity;
import com.holike.crm.bean.LoginBean;
import com.holike.crm.helper.BindViewHelper;
import com.holike.crm.presenter.activity.LoginPresenter;
import com.holike.crm.util.Constants;
import com.holike.crm.view.activity.LoginView;

import butterknife.BindView;
import butterknife.OnClick;

import static com.holike.crm.presenter.activity.LoginPresenter.showPassword;

/**
 * 登录
 */
public class LoginActivity extends BaseActivity<LoginPresenter, LoginView> implements LoginView {

    @BindView(R.id.et_login_account)
    EditText etAccount;
    @BindView(R.id.iv_login_clearAccount)
    ImageView mClearImageView;
    @BindView(R.id.et_login_password)
    EditText etPassword;
    @BindView(R.id.iv_login_showpassword)
    ImageView ivShowpassword;
    @BindView(R.id.btn_login_login)
    TextView btnLogin;

    private boolean isShowPassword = false;

    @Override
    protected int setContentViewId() {
        return R.layout.activity_login;
    }

    @Override
    protected LoginPresenter attachPresenter() {
        return new LoginPresenter();
    }

    @Override
    protected void init() {
        BindViewHelper.bindBgView(btnLogin, etAccount, etPassword);
        etAccount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(s.toString().trim())) {
                    mClearImageView.setVisibility(View.VISIBLE);
                } else {
                    mClearImageView.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    /**
     * 登录成功
     *
     * @param loginBean
     */
    @Override
    public void loginSuccess(LoginBean loginBean) {
        dismissLoading();
        startActivity(MainActivity.class);
        finish();
    }

    /**
     * 登录失败
     */

    @Override
    public void loginFailed(String err) {
        dismissLoading();
        showShortToast(err);
    }

    @OnClick({R.id.iv_login_clearAccount, R.id.iv_login_showpassword, R.id.btn_login_login, R.id.tv_login_check_account})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_login_clearAccount:
                etAccount.setText("");
                break;
            case R.id.iv_login_showpassword:
                isShowPassword = showPassword(isShowPassword, etPassword, ivShowpassword);
                break;
            case R.id.btn_login_login:
                showLoading();
                mPresenter.login(etAccount.getText().toString(), etPassword.getText().toString());
                break;
            case R.id.tv_login_check_account:
                startActivity(CheckAccountActivity.class);
                break;
        }
    }

    @Override
    protected void onActivityResult(int resultCode, Intent data) {
        super.onActivityResult(resultCode, data);
        if (resultCode == Constants.RESULT_CODE_CHECK_ACCOUNT) {
            etAccount.setText(data.getStringExtra(Constants.CHECK_ACCOUNT_LOGIN_ACCOUNT));
        }
    }
}
