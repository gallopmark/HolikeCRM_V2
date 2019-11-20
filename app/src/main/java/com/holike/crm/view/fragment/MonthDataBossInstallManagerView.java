package com.holike.crm.view.fragment;

import com.holike.crm.base.BaseView;
import com.holike.crm.bean.MonthDataBossBean;
import com.holike.crm.bean.MonthDataInstallManagerBean;

/**
 * Created by pony on 2019/8/9.
 * Copyright holike possess 2019.
 */
public interface MonthDataBossInstallManagerView extends BaseView {

    void onSuccess(MonthDataInstallManagerBean bean);

    void onFailure(String failReason);
}
