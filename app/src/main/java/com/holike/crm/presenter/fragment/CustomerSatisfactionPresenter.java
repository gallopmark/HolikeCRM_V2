package com.holike.crm.presenter.fragment;

import com.holike.crm.base.BasePresenter;
import com.holike.crm.bean.CustomerSatisfactionBean;
import com.holike.crm.http.RequestCallBack;
import com.holike.crm.model.fragment.CustomerSatisfactionModel;
import com.holike.crm.view.fragment.CustomerSatisfactionView;

/**
 * Created by gallop on 2019/9/20.
 * Copyright holike possess 2019.
 */
public class CustomerSatisfactionPresenter extends BasePresenter<CustomerSatisfactionView, CustomerSatisfactionModel> {

    public void doRequest(String type, String cityCode, String datetime) {
        if (getModel() != null) {
            getModel().onQueryRequest(type, cityCode, datetime, new RequestCallBack<CustomerSatisfactionBean>() {
                @Override
                public void onFailed(String failReason) {
                    if (getView() != null) {
                        getView().onFailure(failReason);
                    }
                }

                @Override
                public void onSuccess(CustomerSatisfactionBean result) {
                    if (getView() != null) {
                        getView().onSuccess(result);
                    }
                }
            });
        }
    }
}
