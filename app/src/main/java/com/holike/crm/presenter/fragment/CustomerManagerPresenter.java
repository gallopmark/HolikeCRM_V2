package com.holike.crm.presenter.fragment;

import com.holike.crm.base.BasePresenter;
import com.holike.crm.bean.CustomerManagerV2Bean;
import com.holike.crm.http.MyJsonParser;
import com.holike.crm.http.ParamHelper;
import com.holike.crm.http.RequestCallBack;
import com.holike.crm.model.customer.GeneralCustomerModel;
import com.holike.crm.view.fragment.CustomerManagerView;

/**
 * Created by gallop on 2019/7/16.
 * Copyright holike possess 2019.
 * 客户管理页面 presenter
 */
public class CustomerManagerPresenter extends BasePresenter<CustomerManagerView, GeneralCustomerModel> {

    /*get请求*/
    public void getCustomerDetail(String personalId) {
//        personalId = "C2019070000747"; //测试公海客户
        if (getModel() != null) {
            getModel().doGet(personalId, new RequestCallBack<CustomerManagerV2Bean>() {
                @Override
                public void onFailed(String failReason) {
                    if (getView() != null)
                        getView().onFailure(failReason);
                }

                @Override
                public void onSuccess(CustomerManagerV2Bean bean) {
                    if (getView() != null)
                        getView().onSuccess(bean);
                }
            });
        }
    }

    /*已改为get请求*/
    @Deprecated
    public void request(String personalId) {
        if (getModel() != null) {
            getModel().doRequest(personalId, new RequestCallBack<CustomerManagerV2Bean>() {
                @Override
                public void onFailed(String failReason) {
                    if (getView() != null)
                        getView().onFailure(failReason);
                }

                @Override
                public void onSuccess(CustomerManagerV2Bean bean) {
                    if (getView() != null)
                        getView().onSuccess(bean);
                }
            });
        }
    }

    /*发布留言*/
    public void publishMessage(String houseId, String message) {
        if (getModel() != null) {
            getModel().publishMessage(houseId, message, new RequestCallBack<String>() {
                @Override
                public void onFailed(String failReason) {
                    if (getView() != null) {
                        getView().onPublishMessageFailure(failReason);
                    }
                }

                @Override
                public void onSuccess(String result) {
                    if (getView() != null) {
                        getView().onPublishMessageSuccess(MyJsonParser.getShowMessage(result));
                    }
                }
            });
        }
    }

    /*公海客户领取房屋*/
    public void receiveHouse(String houseId, String shopId, String groupId, String salesId) {
        if (getModel() != null) {
            String body = ParamHelper.Customer.receiveHouse(houseId, shopId, groupId, salesId);
            getModel().receiveHouse(body, new RequestCallBack<String>() {
                @Override
                public void onFailed(String failReason) {
                    if (getView() != null) {
                        getView().onReceiveHouseFailure(failReason);
                    }
                }

                @Override
                public void onSuccess(String result) {
                    if (getView() != null) {
                        getView().onReceiveHouseSuccess(MyJsonParser.getShowMessage(result));
                    }
                }
            });
        }
    }
}
