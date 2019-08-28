package com.holike.crm.bean;

import java.io.Serializable;

/**
 * Created by wqj on 2018/2/27.
 * 查询账号bean
 */

public class CheckAccountBean implements Serializable {
    /**
     * LoginAccount : 13004201-0004
     * dealerNumber : 13004201
     */

    private String LoginAccount;
    private String dealerNumber;

    public String getLoginAccount() {
        return LoginAccount;
    }

    public void setLoginAccount(String LoginAccount) {
        this.LoginAccount = LoginAccount;
    }

    public String getDealerNumber() {
        return dealerNumber;
    }

    public void setDealerNumber(String dealerNumber) {
        this.dealerNumber = dealerNumber;
    }
}
