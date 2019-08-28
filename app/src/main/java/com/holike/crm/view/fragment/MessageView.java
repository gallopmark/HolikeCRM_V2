package com.holike.crm.view.fragment;

import com.holike.crm.base.BaseView;
import com.holike.crm.bean.MessageBean;

/**
 * Created by wqj on 2018/2/24.
 * 消息
 */

public interface MessageView extends BaseView {

    void getNotifySuccess(MessageBean messageBean);

    void getNotifyFailed(String failed);

    void getAnnouncementSuccess(MessageBean messageBean);

    void getAnnouncementFailed(String failed);

    void openMessage(MessageBean.MessageListBean messageListBean);

}
