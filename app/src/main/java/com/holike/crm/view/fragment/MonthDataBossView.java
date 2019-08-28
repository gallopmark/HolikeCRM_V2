package com.holike.crm.view.fragment;

import com.holike.crm.base.BaseView;
import com.holike.crm.bean.MonthDataBossBean;

/**
 * Created by gallop on 2019/8/9.
 * Copyright holike possess 2019.
 */
public interface MonthDataBossView extends BaseView {

    void onSuccess(MonthDataBossBean bean);

    void onFailure(String failReason);
}
