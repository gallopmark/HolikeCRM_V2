package com.holike.crm.view.activity;

import com.holike.crm.base.BaseView;
import com.holike.crm.bean.CustomerStatusBean;

/**
 * Created by pony 2019/7/8
 * Copyright (c) 2019 holike
 */
public interface CustomerStatusListView extends BaseView {

    void onSuccess(CustomerStatusBean bean);

    void onFailed(String failReason);
}
