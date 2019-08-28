package com.holike.crm.presenter.activity;

import com.holike.crm.base.BasePresenter;
import com.holike.crm.bean.CheckAccountBean;
import com.holike.crm.model.activity.CheckAccountModel;
import com.holike.crm.view.activity.CheckAccountView;

/**
 * Created by wqj on 2018/2/24.
 * 查询账号
 */

public class CheckAccountPresenter extends BasePresenter<CheckAccountView, CheckAccountModel> {
    /**
     * 查询账号
     *
     * @param code
     * @param crmAccount
     * @param crmPassword
     */
    public void checkAccount(String code, String crmAccount, String crmPassword) {
        model.checkAccount(code, crmAccount, crmPassword, new CheckAccountModel.CheckListener() {
            @Override
            public void success(CheckAccountBean result) {
                if (getView() != null)
                    getView().checkSuccess(result);
            }

            @Override
            public void failed(String failed) {
                if (getView() != null)
                    getView().checkFailed(failed);
            }
        });
    }
}
