package com.holike.crm.model.fragment;

import com.holike.crm.base.BaseModel;
import com.holike.crm.bean.CupboardBean;
import com.holike.crm.bean.PerformanceBean;
import com.holike.crm.http.MyHttpClient;
import com.holike.crm.http.RequestCallBack;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wqj on 2018/5/16.
 * 业绩报表
 */

public class PerformanceModel extends BaseModel {
    /**
     * 获取业绩报表数据
     */
    public void getData(String cityCode, String selectTime, String type, String url, final GetDataListener listener) {
        Map<String, String> params = new HashMap<>();
        params.put("cityCode", cityCode);
        params.put("selectTime", selectTime);
        params.put("type", type);
        addDisposable(MyHttpClient.postByTimeout(url, null, params, 60, new RequestCallBack<PerformanceBean>() {
            @Override
            public void onFailed(String failReason) {
                listener.failed(failReason);
            }

            @Override
            public void onSuccess(PerformanceBean result) {
                listener.success(result);
            }
        }));
    }

    public interface GetDataListener {
        void failed(String failed);

        void success(PerformanceBean bean);
    }

    /**
     * 获取橱柜业绩报表数据
     */
    public void getCupboardData(String cityCode, String time, String type, String url, final GetCupboardListener listener) {
        Map<String, String> params = new HashMap<>();
        params.put("cityCode", cityCode);
        params.put("time", time);
        params.put("type", type);
        addDisposable(MyHttpClient.postByTimeout(url, null, params, 60, new RequestCallBack<CupboardBean>() {
            @Override
            public void onFailed(String failReason) {
                listener.failed(failReason);
            }

            @Override
            public void onSuccess(CupboardBean result) {
                listener.success(result);
            }
        }));
    }

    public interface GetCupboardListener {
        void success(CupboardBean bean);

        void failed(String failed);
    }
}
