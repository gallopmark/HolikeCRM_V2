package com.holike.crm.view.activity;

import com.holike.crm.base.BaseView;

/**
 * Created by wqj on 2018/2/25.
 * 修改密码
 */

public interface ChangePasswordView extends BaseView {
    void changePasswordSuccess();

    void changePasswordFailed(String failed);

    void warn(String text);

}
