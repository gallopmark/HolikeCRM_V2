package com.holike.crm.view.fragment;

import com.holike.crm.base.BaseView;
import com.holike.crm.bean.MonthDataFinanceBean;

/**
 * Created by gallop on 2019/8/9.
 * Copyright holike possess 2019.
 */
public interface MonthDataFinanceView extends BaseView {

    void onSuccess(MonthDataFinanceBean bean);

    void onFailure(String failReason);
}
