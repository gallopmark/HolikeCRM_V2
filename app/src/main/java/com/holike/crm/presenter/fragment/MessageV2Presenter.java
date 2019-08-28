package com.holike.crm.presenter.fragment;

import com.holike.crm.base.BasePresenter;
import com.holike.crm.bean.MessageResultBean;
import com.holike.crm.http.CustomerUrlPath;
import com.holike.crm.http.ParamHelper;
import com.holike.crm.http.RequestCallBack;
import com.holike.crm.model.fragment.MessageV2Model;
import com.holike.crm.view.fragment.MessageV2View;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by gallop on 2019/8/21.
 * Copyright holike possess 2019.
 */
public class MessageV2Presenter extends BasePresenter<MessageV2View, MessageV2Model> {

    public void getMessageList(String type, int pageNo, int pageSize) {
        if (getModel() != null) {
            getModel().getMessage(type, pageNo, pageSize, new RequestCallBack<MessageResultBean>() {
                @Override
                public void onFailed(String failReason) {
                    if (getView() != null) {
                        getView().onFailure(failReason);
                    }
                }

                @Override
                public void onSuccess(MessageResultBean result) {
                    if (getView() != null) {
                        getView().onSuccess(result);
                    }
                }
            });
        }
    }

    public void redistribute(String noticeUserId, String type) {
        if (getModel() != null) {
            getModel().redistribute(noticeUserId, type, new RequestCallBack<String>() {
                @Override
                public void onFailed(String failReason) {
                    if (getView() != null) {
                        getView().onRedistributeFailure(failReason);
                    }
                }

                @Override
                public void onSuccess(String result) {
                    if (getView() != null) {
                        getView().onRedistributeSuccess(result);
                    }
                }
            });
        }
    }
}
