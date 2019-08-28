package com.holike.crm.model.activity;

import com.holike.crm.base.BaseModel;
import com.holike.crm.bean.TypeListBean;
import com.holike.crm.http.MyHttpClient;
import com.holike.crm.http.RequestCallBack;
import com.holike.crm.http.UrlPath;
import com.holike.crm.util.TimeUtil;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by wqj on 2018/2/25.
 * 订单中心
 */

public class OrderCenterModel extends BaseModel {
    /**
     * 获取订单中心数据
     *
     * @param content       搜索内容
     * @param orderStatusId 订单状态id
     * @param orderTypeId   订单类型id
     * @param startDate     开始时间戳（10位）
     * @param endDate       结束时间戳（10位）
     * @param pageNo        页码
     * @param pageSize      页码大小
     * @param listener      回调
     */
    public void getOrderList(String content, String orderStatusId, String orderTypeId,
                             Date startDate, Date endDate,
                             String pageNo, String pageSize, final GetOrderListListener listener) {
        Map<String, String> params = new HashMap<>();
        params.put("content", content);
        params.put("orderStatusId", orderStatusId);
        params.put("orderTypeId", orderTypeId);
        if (startDate != null) params.put("startTime", TimeUtil.dateToStamp(startDate, false));
        if (endDate != null) params.put("endTime", TimeUtil.dateToStamp(endDate, true));
        params.put("pageNo", pageNo);
        params.put("pageSize", pageSize);
        addDisposable(MyHttpClient.postByTimeout(UrlPath.URL_GET_ORDER_LIST, null, params, 30, new RequestCallBack<String>() {
            @Override
            public void onFailed(String failReason) {
                listener.failed(failReason);
            }

            @Override
            public void onSuccess(String result) {
                listener.success(result);
            }
        }));
    }

    public interface GetOrderListListener {
        void success(String result);

        void failed(String failed);
    }

    /**
     * 获取客户选择条件数据
     */
    public void getTypeList(final GetTypeListListener listener) {
        addDisposable(MyHttpClient.postByCliId(UrlPath.URL_ORDER_CENTER_SELECT, null, null, new RequestCallBack<TypeListBean>() {
            @Override
            public void onFailed(String failReason) {
                listener.failed(failReason);
            }

            @Override
            public void onSuccess(TypeListBean result) {
                listener.success(result);
            }

        }));
    }

    public interface GetTypeListListener {
        void success(TypeListBean result);

        void failed(String failed);
    }
}
