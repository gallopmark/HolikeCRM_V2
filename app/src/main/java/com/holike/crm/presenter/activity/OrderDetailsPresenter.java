package com.holike.crm.presenter.activity;

import com.holike.crm.base.BasePresenter;
import com.holike.crm.bean.OrderDetailsBean;
import com.holike.crm.model.activity.OrderDetailsModel;
import com.holike.crm.view.activity.OrderDetailsView;

/**
 * Created by wqj on 2018/2/25.
 * 订单详情
 */

public class OrderDetailsPresenter extends BasePresenter<OrderDetailsView, OrderDetailsModel> {

    /**
     * 获取订单详情
     *
     * @param orderId
     * @param messageId
     */
    public void getOrderDetails(final String orderId, String messageId) {
        model.getOrderDetails(orderId, messageId == null ? "" : messageId, new OrderDetailsModel.GetOrderDetailsListener() {
            @Override
            public void success(OrderDetailsBean bean) {
                if (getView() != null)
                    getView().getDetailsSuccess(bean);
            }

            @Override
            public void failed(String failed) {
                if (getView() != null)
                    getView().getDetailsFailed(failed);
            }
        });
    }
}
