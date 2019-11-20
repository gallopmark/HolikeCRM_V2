package com.holike.crm.view.fragment;

import com.holike.crm.base.BaseView;
import com.holike.crm.bean.CustomerSatisfactionBean;

/**
 * Created by pony on 2019/9/20.
 * Copyright holike possess 2019.
 */
public interface CustomerSatisfactionView extends BaseView {
    void onSuccess(CustomerSatisfactionBean bean);

    void onFailure(String failReason);
}
