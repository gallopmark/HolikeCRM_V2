package com.holike.crm.model.fragment;

import com.holike.crm.base.BaseModel;
import com.holike.crm.bean.ActiveMarketRankBean;
import com.holike.crm.http.MyHttpClient;
import com.holike.crm.http.RequestCallBack;
import com.holike.crm.http.UrlPath;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wqj on 2018/6/21.
 * 主动营销排行榜
 */

public class ActiveMarketRankModel extends BaseModel {

    /**
     * 获取数据
     */
    public void getData(String start, String end, final GetDataListener listener) {
        Map<String, String> params = new HashMap<>();
        params.put("start", start);
        params.put("end", end);
        addDisposable(MyHttpClient.postByTimeout(UrlPath.URL_ACTIVE_MARKET_RANK_REPORT, null, params, 60, new RequestCallBack<ActiveMarketRankBean>() {
            @Override
            public void onFailed(String failReason) {
                listener.failed(failReason);
            }

            @Override
            public void onSuccess(ActiveMarketRankBean result) {
                listener.success(result);
            }

        }));
    }

    public interface GetDataListener {
        void success(ActiveMarketRankBean bean);

        void failed(String failed);
    }
}
