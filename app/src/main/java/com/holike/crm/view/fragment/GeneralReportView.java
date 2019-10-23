package com.holike.crm.view.fragment;

import com.holike.crm.base.BaseView;

public interface GeneralReportView extends BaseView {

    void onSuccess(Object obj);

    void onFailure(String failReason);
}
