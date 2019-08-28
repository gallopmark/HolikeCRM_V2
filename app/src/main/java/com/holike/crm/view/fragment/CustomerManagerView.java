package com.holike.crm.view.fragment;

import com.holike.crm.base.BaseView;
import com.holike.crm.bean.CustomerManagerV2Bean;

/**
 * Created by gallop on 2019/7/16.
 * Copyright holike possess 2019.
 */
public interface CustomerManagerView extends BaseView {

    void onSuccess(CustomerManagerV2Bean bean);

    void onFailure(String failReason);

    void onPublishMessageSuccess(String result);

    void onPublishMessageFailure(String failReason);

    void onReceiveHouseSuccess(String message);

    void onReceiveHouseFailure(String failReason);
}
