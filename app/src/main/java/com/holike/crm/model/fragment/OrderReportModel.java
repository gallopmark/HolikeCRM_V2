package com.holike.crm.model.fragment;

import com.holike.crm.base.BaseModel;
import com.holike.crm.bean.MonthCompleteBean;
import com.holike.crm.bean.OrderReportBean;
import com.holike.crm.http.MyHttpClient;
import com.holike.crm.http.RequestCallBack;
import com.holike.crm.http.UrlPath;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wqj on 2018/3/7.
 * 定单交易情况
 */

public class OrderReportModel extends BaseModel {

    /**
     * 获取订单交易报表数据
     */
    public void getOrderReport(String cityCode, String startTime, String endTime, String type, String time, final GetOrderReportListener listener) {
        Map<String, String> params = new HashMap<>();
        params.put("cityCode", cityCode);
        params.put("startTime", startTime);
        params.put("endTime", endTime);
        params.put("type", type);
        params.put("time", time);
        addDisposable(MyHttpClient.postByTimeout(UrlPath.URL_GET_DEPOSIT_REPORT, null, params, 60,
                new RequestCallBack<OrderReportBean>() {
                    @Override
                    public void onFailed(String failReason) {
                        listener.failed(failReason);
                    }

                    @Override
                    public void onSuccess(OrderReportBean result) {
                        listener.success(result);
                    }
                }));
    }

    public interface GetOrderReportListener {
        void success(OrderReportBean orderReportBean);

        void failed(String failed);
    }

    /**
     * 获取各月完成率
     */
    public void getCompleteData(String cityCode, String type, final GetCompleteListener listener) {
        Map<String, String> params = new HashMap<>();
        params.put("cityCode", cityCode);
        params.put("type", type);
        addDisposable(MyHttpClient.postByTimeout(UrlPath.URL_GET_MONTH_COMPLETE, null, params, 60, new RequestCallBack<List<MonthCompleteBean>>() {
            @Override
            public void onFailed(String failReason) {
                listener.failed(failReason);
            }

            @Override
            public void onSuccess(List<MonthCompleteBean> result) {
                listener.success(result);
            }

        }));
    }

    public interface GetCompleteListener {
        void success(List<MonthCompleteBean> beans);

        void failed(String failed);
    }
}
