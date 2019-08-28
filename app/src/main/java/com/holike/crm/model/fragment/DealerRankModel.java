package com.holike.crm.model.fragment;

import com.holike.crm.base.BaseModel;
import com.holike.crm.bean.DealerRankBean;
import com.holike.crm.http.MyHttpClient;
import com.holike.crm.http.RequestCallBack;
import com.holike.crm.http.UrlPath;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wqj on 2018/5/28.
 * 经销商排行
 */

public class DealerRankModel extends BaseModel {

    /**
     * 获取数据
     */
    public void getData(String cityCode, final GetDataListener listener) {
        Map<String, String> params = new HashMap<>();
        params.put("cityCode", cityCode);
       addDisposable(MyHttpClient.postByTimeout(UrlPath.URL_GET_DEALER_RANK_REPORT, null, params, 60, new RequestCallBack<DealerRankBean>() {
            @Override
            public void onFailed(String failReason) {
                listener.failed(failReason);
            }

            @Override
            public void onSuccess(DealerRankBean result) {
                listener.success(result);
            }
        }));
    }

    public interface GetDataListener {
        void success(DealerRankBean bean);

        void failed(String failed);
    }
}
