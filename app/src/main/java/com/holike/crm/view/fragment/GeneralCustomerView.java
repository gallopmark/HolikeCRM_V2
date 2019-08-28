package com.holike.crm.view.fragment;

import com.holike.crm.base.BaseView;

/**
 * Created by gallop on 2019/8/1.
 * Copyright holike possess 2019.
 */
public interface GeneralCustomerView extends BaseView {

    void onSuccess(Object object);

    void onFailure(String failReason);
}
