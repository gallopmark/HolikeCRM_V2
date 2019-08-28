package com.holike.crm.presenter.activity;

import com.holike.crm.base.BasePresenter;
import com.holike.crm.bean.MessageDetailsBean;
import com.holike.crm.model.activity.MessageDetailsModel;
import com.holike.crm.view.activity.MessageDetailsView;

/**
 * Created by wqj on 2018/2/25.
 * 消息详情
 */

public class MessageDetailsPresenter extends BasePresenter<MessageDetailsView, MessageDetailsModel> {

    /**
     * 获取消息详情
     *
     * @param messageId
     */
    public void getMessageDetails(String messageId) {
        model.getMessageDetails(messageId, new MessageDetailsModel.GetMessageDetailsListener() {
            @Override
            public void success(MessageDetailsBean messageDetailsBean) {
                if (getView() != null)
                    getView().getMessageDetailsSuccess(messageDetailsBean);
            }

            @Override
            public void failed(String failed) {
                if (getView() != null)
                    getView().getMessageDetailsFailed(failed);
            }
        });
    }
}
