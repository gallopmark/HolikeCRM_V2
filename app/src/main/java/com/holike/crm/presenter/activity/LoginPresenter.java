package com.holike.crm.presenter.activity;


import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.widget.EditText;
import android.widget.ImageView;

import com.holike.crm.R;
import com.holike.crm.base.BasePresenter;
import com.holike.crm.bean.LoginBean;
import com.holike.crm.model.activity.LoginModel;
import com.holike.crm.util.Constants;
import com.holike.crm.util.PackageUtil;
import com.holike.crm.util.SharedPreferencesUtils;
import com.holike.crm.view.activity.LoginView;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by wqj on 2017/9/28.
 * 登录
 */

public class LoginPresenter extends BasePresenter<LoginView, LoginModel> {

    /**
     * 登录
     *
     * @param account
     * @param password
     */
    public void login(String account, final String password) {
        Map<String, String> params = new HashMap<>();
        params.put("crmAccount", account);
        params.put("crmPassword", password);
        Map<String, String> header = new HashMap<>();
        header.put(Constants.VERSION, String.valueOf(PackageUtil.getVersionCode()));
        header.put(Constants.CLIENT, Constants.ANDROID);
        model.Login(header, params, new LoginModel.LoginListener() {
            @Override
            public void success(LoginBean loginBean) {
                loginBean.setPsw(password);
                saveLoginInfo(loginBean);
                if (getView() != null)
                    getView().loginSuccess(loginBean);
            }

            @Override
            public void failed(String err) {
                if (getView() != null)
                    getView().loginFailed(err);
            }
        });
    }

    /**
     * 保存登录信息
     */
    private void saveLoginInfo(LoginBean loginBean) {
        SharedPreferencesUtils.saveUserInfo(loginBean);
//        SharedPreferencesUtils.saveString(Constants.CLI_ID, loginBean.getCliId());
//        SharedPreferencesUtils.saveString(Constants.USER_ID, loginBean.getUserId());
//        SharedPreferencesUtils.saveString(Constants.USER_PSW, loginBean.getPsw());
    }

    /**
     * 显示/隐藏密码
     */
    public static boolean showPassword(boolean isShowPassword, EditText editText, ImageView imageView) {
        if (isShowPassword) {
            isShowPassword = false;
            editText.setTransformationMethod(PasswordTransformationMethod.getInstance());
            imageView.setImageResource(R.drawable.eye_close);
        } else {
            isShowPassword = true;
            editText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            imageView.setImageResource(R.drawable.eye_open);
        }
        editText.setSelection(editText.getText().length());
        return isShowPassword;
    }

}
