package com.holike.crm.view;

import com.holike.crm.base.BaseView;

public interface GeneralReportView extends BaseView {

    void onSuccess(Object obj);

    void onFailure(String failReason);
}
