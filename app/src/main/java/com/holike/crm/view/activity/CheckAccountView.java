package com.holike.crm.view.activity;

import com.holike.crm.base.BaseView;
import com.holike.crm.bean.CheckAccountBean;

/**
 * Created by wqj on 2018/2/24.
 * 查询账号
 */

public interface CheckAccountView extends BaseView {
    void checkSuccess(CheckAccountBean bean);

    void checkFailed(String failed);
}
