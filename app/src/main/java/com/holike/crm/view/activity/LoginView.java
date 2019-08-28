package com.holike.crm.view.activity;

import com.holike.crm.base.BaseView;
import com.holike.crm.bean.LoginBean;

/**
 * Created by wqj on 2017/9/28.
 * 登录
 */

public interface LoginView extends BaseView {

    void loginSuccess(LoginBean loginBean);


    void loginFailed(String err);
}
