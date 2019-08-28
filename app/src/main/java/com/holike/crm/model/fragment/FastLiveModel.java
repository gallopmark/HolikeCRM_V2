package com.holike.crm.model.fragment;

import com.holike.crm.base.BaseModel;
import com.holike.crm.bean.FastLiveBean;
import com.holike.crm.http.MyHttpClient;
import com.holike.crm.http.RequestCallBack;
import com.holike.crm.http.UrlPath;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wqj on 2018/3/7.
 * 定单交易情况
 */

public class FastLiveModel extends BaseModel {

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
        addDisposable(MyHttpClient.postByTimeout(UrlPath.URL_NEW_RETAIL, null, params, 60,
                new RequestCallBack<FastLiveBean>() {
            @Override
            public void onFailed(String failReason) {
                listener.failed(failReason);
            }

            @Override
            public void onSuccess(FastLiveBean result) {
                listener.success(result);
            }
        }));
    }

    public interface GetOrderReportListener {
        void success(FastLiveBean FastLiveBean);

        void failed(String failed);
    }



}
