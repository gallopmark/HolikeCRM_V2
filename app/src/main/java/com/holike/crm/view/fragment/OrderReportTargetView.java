package com.holike.crm.view.fragment;

import com.holike.crm.base.BaseView;
import com.holike.crm.bean.OrderReportTargetBean;

/**
 * Created by wqj on 2018/5/30.
 * 订单交易报表-填写目标
 */

public interface OrderReportTargetView extends BaseView {
    void getDataSuccess(OrderReportTargetBean bean);

    void getDataFailed(String failed);

    void saveTargetSuccess(String success);

    void saveTargetFailed(String failed);
}
