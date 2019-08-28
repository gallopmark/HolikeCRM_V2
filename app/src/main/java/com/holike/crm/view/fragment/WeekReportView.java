package com.holike.crm.view.fragment;

import com.holike.crm.base.BaseView;
import com.holike.crm.bean.DayDepositBean;
import com.holike.crm.bean.WeekDepositBean;

import java.util.List;

/**
 * Created by wqj on 2018/2/25.
 * 订单交易对趋势
 */

public interface WeekReportView extends BaseView {
    void getDepositListSuccess(WeekDepositBean weekDepositBean);

    void getDepositListFailed(String failed);

    void getDepositInfoSuccess(List<DayDepositBean> dayDepositBeans, String time, String money);

    void getDepositInfoFailed(String failed);
}
