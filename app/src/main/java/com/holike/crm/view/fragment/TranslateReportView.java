package com.holike.crm.view.fragment;

import com.holike.crm.base.BaseView;
import com.holike.crm.bean.TranslateReportBean;

/**
 * Created by wqj on 2018/4/12.
 * 订金转化报表
 */

public interface TranslateReportView extends BaseView {

    void getDataSuccess(TranslateReportBean bean);

    void getDataFailed(String failed);

    void showDate(String startTime, String endTime);

    void getData();
}
