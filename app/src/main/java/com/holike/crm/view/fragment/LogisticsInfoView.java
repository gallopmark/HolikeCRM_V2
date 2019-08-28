package com.holike.crm.view.fragment;

import com.holike.crm.base.BaseView;
import com.holike.crm.bean.LogisticsInfoBean;

public interface LogisticsInfoView extends BaseView {
    void onSuccess(LogisticsInfoBean bean);
    void onFail(String errorMsg);
}
