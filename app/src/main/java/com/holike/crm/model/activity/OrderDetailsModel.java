package com.holike.crm.model.activity;

import com.holike.crm.base.BaseModel;
import com.holike.crm.bean.OrderDetailsBean;
import com.holike.crm.http.MyHttpClient;
import com.holike.crm.http.RequestCallBack;
import com.holike.crm.http.UrlPath;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wqj on 2018/2/25.
 * 订单详情
 */

public class OrderDetailsModel extends BaseModel {
    /**
     * 获取订单详情
     */
    public void getOrderDetails(String orderId, String messageId, final GetOrderDetailsListener listener) {
        Map<String, String> params = new HashMap<>();
        params.put("orderId", orderId);
        params.put("messageId", messageId);
        addDisposable(MyHttpClient.postByCliId(UrlPath.URL_GET_ORDER_DETAILS, null, params, new RequestCallBack<OrderDetailsBean>() {
            @Override
            public void onFailed(String failReason) {
                listener.failed(failReason);
            }

            @Override
            public void onSuccess(OrderDetailsBean result) {
                listener.success(result);
            }

        }));
    }

    public interface GetOrderDetailsListener {
        void success(OrderDetailsBean bean);

        void failed(String failed);
    }
}
