package com.holike.crm.view.fragment;

import com.holike.crm.base.BaseView;
import com.holike.crm.bean.CupboardBean;
import com.holike.crm.bean.PerformanceBean;

/**
 * Created by wqj on 2018/5/16.
 * 业绩报表
 */

public interface PerformanceView extends BaseView {
    void getData();

    void getDataSuccess(PerformanceBean performanceBean);

    void getCupboardDataSuccess(CupboardBean bean);

    void getDataFailed(String failed);
}
