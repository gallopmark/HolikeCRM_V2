package com.holike.crm.view.activity;

import com.holike.crm.base.BaseView;
import com.holike.crm.bean.MessageDetailsBean;

/**
 * Created by wqj on 2018/2/25.
 * 消息详情
 */

public interface MessageDetailsView extends BaseView {
    void getMessageDetailsSuccess(MessageDetailsBean messageDetailsBean);

    void getMessageDetailsFailed(String failed);
}
