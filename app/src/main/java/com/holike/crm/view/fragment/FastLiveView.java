package com.holike.crm.view.fragment;

import com.holike.crm.base.BaseView;
import com.holike.crm.bean.FastLiveBean;
import com.holike.crm.bean.MonthCompleteBean;
import com.holike.crm.bean.OrderReportBean;

import java.util.List;

/**
 * Created by wqj on 2018/3/7.
 * 定单交易情况
 */

public interface FastLiveView extends BaseView {

    void getOrderReportSuccess(FastLiveBean orderReportBean);

    void getDataFailed(String failed);

    void showDate(String startTime, String endTime);

    void getData();

    void getCompleteDataSuccess(List<MonthCompleteBean> bean, String title);
}
