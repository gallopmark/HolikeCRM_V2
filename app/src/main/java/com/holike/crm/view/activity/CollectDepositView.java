package com.holike.crm.view.activity;

import com.holike.crm.base.BaseView;
import com.holike.crm.bean.CollectDepositListBean;
import com.holike.crm.bean.CustomerDetailBean;

import java.util.List;

/**
 * Created by wqj on 2018/8/20.
 * 首页进入收取订金页
 */

public interface CollectDepositView extends BaseView {
    void success(List<CollectDepositListBean> beans);

    void failed(String failed);

    void getCustomerFailed(String failed);

    void getCustomerSuccess(CustomerDetailBean customerDetailBean);
}
