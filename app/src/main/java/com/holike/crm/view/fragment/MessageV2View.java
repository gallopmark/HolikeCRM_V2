package com.holike.crm.view.fragment;

import com.holike.crm.base.BaseView;
import com.holike.crm.bean.MessageResultBean;

/**
 * Created by pony on 2019/8/21.
 * Copyright holike possess 2019.
 */
public interface MessageV2View extends BaseView {
    void onSuccess(MessageResultBean resultBean);

    void onFailure(String failReason);

    void onRedistributeSuccess(String message);

    void onRedistributeFailure(String failReason);
}
