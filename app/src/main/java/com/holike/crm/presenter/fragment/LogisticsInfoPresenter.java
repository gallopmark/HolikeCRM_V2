package com.holike.crm.presenter.fragment;

import com.holike.crm.base.BasePresenter;
import com.holike.crm.bean.LogisticsInfoBean;
import com.holike.crm.bean.ReceiptDeliveryManifestBean;
import com.holike.crm.model.fragment.LogisticsInfoModel;
import com.holike.crm.view.fragment.LogisticsInfoView;

import java.util.List;

public class LogisticsInfoPresenter extends BasePresenter<LogisticsInfoView, LogisticsInfoModel> {

    public void getData(String orderId) {
        model.getData(orderId, new LogisticsInfoModel.LogisticsInfoListener() {
            @Override
            public void success(LogisticsInfoBean messageBean) {
                if (getView() != null)
                    getView().onSuccess(messageBean);
            }

            @Override
            public void failed(String failed) {

            }
        });
    }
}
