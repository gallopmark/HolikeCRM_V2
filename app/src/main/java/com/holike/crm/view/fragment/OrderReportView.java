package com.holike.crm.view.fragment;

import com.holike.crm.base.BaseView;
import com.holike.crm.bean.MonthCompleteBean;
import com.holike.crm.bean.OrderReportBean;

import java.util.List;

/**
 * Created by wqj on 2018/3/7.
 * 定单交易情况
 */

public interface OrderReportView extends BaseView {

    void getOrderReportSuccess(OrderReportBean orderReportBean);

    void getDataFailed(String failed);

    void showDate(String startTime, String endTime);

    void getData();

    void getCompleteDataSuccess(List<MonthCompleteBean> bean, String title);
}
