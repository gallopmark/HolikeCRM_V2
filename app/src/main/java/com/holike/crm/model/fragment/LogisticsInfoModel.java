package com.holike.crm.model.fragment;

import com.holike.crm.base.BaseModel;
import com.holike.crm.bean.LogisticsInfoBean;
import com.holike.crm.http.MyHttpClient;
import com.holike.crm.http.RequestCallBack;
import com.holike.crm.http.UrlPath;
import com.holike.crm.util.Constants;
import com.holike.crm.util.SharedPreferencesUtils;

import java.util.HashMap;
import java.util.Map;

public class LogisticsInfoModel extends BaseModel {

    public void getData(String orderId, final LogisticsInfoModel.LogisticsInfoListener listener) {

        Map<String, String> header = new HashMap<>();
        Map<String, String> params = new HashMap<>();
        params.put("orderId", orderId);
        header.put(Constants.CLIENT, SharedPreferencesUtils.getString(Constants.CLIENT, ""));
        addDisposable(MyHttpClient.postByCliId(UrlPath.URL_GET_LOGISTICS_INFO, header, params, new RequestCallBack<LogisticsInfoBean>() {
            @Override
            public void onFailed(String failReason) {
                listener.failed(failReason);
            }

            @Override
            public void onSuccess(LogisticsInfoBean results) {
                listener.success(results);
            }
        }));
    }

    public interface LogisticsInfoListener {
        void success(LogisticsInfoBean messageBean);

        void failed(String failed);
    }
}
