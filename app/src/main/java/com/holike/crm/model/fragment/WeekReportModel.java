package com.holike.crm.model.fragment;

import com.holike.crm.base.BaseModel;
import com.holike.crm.bean.DayDepositBean;
import com.holike.crm.bean.WeekDepositBean;
import com.holike.crm.http.MyHttpClient;
import com.holike.crm.http.RequestCallBack;
import com.holike.crm.http.UrlPath;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wqj on 2018/2/25.
 * 订单交易对趋势
 */

public class WeekReportModel extends BaseModel {

    /**
     * 查询周/月订金
     */
    public void getDepositList(String type, final GetDepositListListener listener) {
        Map<String, String> params = new HashMap<>();
        params.put("type", type);
        addDisposable(MyHttpClient.postByCliId(UrlPath.URL_GET_DEPOSIT, null, params, new RequestCallBack<WeekDepositBean>() {
            @Override
            public void onFailed(String failReason) {
                listener.failed(failReason);
            }

            @Override
            public void onSuccess(WeekDepositBean result) {
                listener.success(result);
            }
        }));
    }

    public interface GetDepositListListener {
        void success(WeekDepositBean weekDepositBean);

        void failed(String failed);
    }

    /**
     * 查询当天订金
     */
    public void getDepositInfo(String time, String type, final GetDepositInfoListener listener) {
        Map<String, String> params = new HashMap<>();
        params.put("time", time);
        params.put("type", type);
        addDisposable(MyHttpClient.postByCliId(UrlPath.URL_GET_DEPOSIT_INFO, null, params, new RequestCallBack<List<DayDepositBean>>() {
            @Override
            public void onFailed(String failReason) {
                listener.failed(failReason);
            }

            @Override
            public void onSuccess(List<DayDepositBean> result) {
                listener.success(result);
            }
        }));
    }

    public interface GetDepositInfoListener {
        void success(List<DayDepositBean> dayDepositBeans);

        void failed(String failed);
    }
}
