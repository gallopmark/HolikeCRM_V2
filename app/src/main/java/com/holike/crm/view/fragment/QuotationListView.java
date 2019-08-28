package com.holike.crm.view.fragment;

import com.holike.crm.base.BaseView;
import com.holike.crm.bean.QuotationBean;

import java.util.List;

public interface QuotationListView extends BaseView {
    void onSuccess(QuotationBean bean);
    void onFail(String errorMsg);
}
