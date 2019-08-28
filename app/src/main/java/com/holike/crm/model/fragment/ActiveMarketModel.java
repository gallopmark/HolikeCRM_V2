package com.holike.crm.model.fragment;

import com.holike.crm.base.BaseModel;
import com.holike.crm.bean.ActiveMarketBean;
import com.holike.crm.http.MyHttpClient;
import com.holike.crm.http.RequestCallBack;
import com.holike.crm.http.UrlPath;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wqj on 2018/6/21.
 * 主动营销数据报表
 */

public class ActiveMarketModel extends BaseModel {

    /**
     * 获取数据
     */
    public void getData(String cityCode, String start, String end, String time, final GetDataListener listener) {
        Map<String, String> params = new HashMap<>();
        params.put("cityCode", cityCode);
        params.put("start", start);
        params.put("end", end);
        params.put("time", time);
        addDisposable(MyHttpClient.postByTimeout(UrlPath.URL_ACTIVE_MARKET_REPORT, null, params, 60, new RequestCallBack<ActiveMarketBean>() {
            @Override
            public void onFailed(String failReason) {
                listener.fainled(failReason);
            }

            @Override
            public void onSuccess(ActiveMarketBean result) {
                listener.success(result);
            }
        }));
    }

    public interface GetDataListener {
        void success(ActiveMarketBean bean);

        void fainled(String failed);
    }
}
