package com.holike.crm.model.activity;

import com.holike.crm.base.BaseModel;
import com.holike.crm.bean.OrderRankingBean;
import com.holike.crm.http.MyHttpClient;
import com.holike.crm.http.RequestCallBack;
import com.holike.crm.http.UrlPath;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wqj on 2018/4/27.
 * 签单排行榜
 */

public class OrderRankingModel extends BaseModel {

    /**
     * 获取数据
     */
    public void getData(String cityCode, String type, String pageSize, String pageNo, String startTime, String endTime, final GetDataListener listener) {
        Map<String, String> params = new HashMap<>();
        params.put("cityCode", cityCode);
        params.put("type", type);
        params.put("pageSize", pageSize);
        params.put("pageNo", pageNo);
        params.put("startTime", startTime);
        params.put("endTime", endTime);
        addDisposable(MyHttpClient.postByTimeout(UrlPath.URL_GET_ORDER_RANKINF_REPORT, null, params, 60, new RequestCallBack<OrderRankingBean>() {
            @Override
            public void onFailed(String failReason) {
                listener.failed(failReason);
            }

            @Override
            public void onSuccess(OrderRankingBean result) {
                listener.success(result);
            }

        }));
    }

    public interface GetDataListener {
        void success(OrderRankingBean orderRankingBean);

        void failed(String failed);
    }
}
