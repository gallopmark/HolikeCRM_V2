package com.holike.crm.view.activity;

import com.holike.crm.base.BaseView;
import com.holike.crm.bean.MonthPkBean;

/**
 * Created by wqj on 2018/6/7.
 * 月度PK
 */

public interface MonthPkView extends BaseView {
    void openMonthPk(MonthPkBean bean);

    void openMonthPkPersonal(MonthPkBean bean);

    void getDatafailed(String failed);
}
