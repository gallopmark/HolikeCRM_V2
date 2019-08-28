package com.holike.crm.view.activity;

import com.holike.crm.base.BaseView;
import com.holike.crm.bean.OrderDetailsBean;

/**
 * Created by wqj on 2018/2/25.
 * 订单详情
 */

public interface OrderDetailsView extends BaseView {
    void getDetailsSuccess(OrderDetailsBean orderDetailsBean);

    void getDetailsFailed(String failed);
}
