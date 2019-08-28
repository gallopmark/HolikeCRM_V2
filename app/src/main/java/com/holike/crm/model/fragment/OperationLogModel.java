package com.holike.crm.model.fragment;

import com.holike.crm.base.BaseModel;
import com.holike.crm.bean.OpreationLogBean;
import com.holike.crm.http.MyHttpClient;
import com.holike.crm.http.RequestCallBack;
import com.holike.crm.http.UrlPath;
import com.holike.crm.util.Constants;
import com.holike.crm.util.SharedPreferencesUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OperationLogModel extends BaseModel {
    public void getData(String orderId, final OperationLogListener listener) {
        Map<String, String> header = new HashMap<>();
        Map<String, String> params = new HashMap<>();
        params.put("orderId", orderId);
        header.put(Constants.CLIENT, SharedPreferencesUtils.getString(Constants.CLIENT, ""));
        addDisposable(MyHttpClient.postByCliId(UrlPath.URL_SELECT_LOG_BY_SPACEID, header, params, new RequestCallBack<List<OpreationLogBean>>() {
            @Override
            public void onFailed(String failReason) {
                listener.fail(failReason);
            }

            @Override
            public void onSuccess(List<OpreationLogBean> results) {
                listener.success(results);
            }
        }));
    }

    public interface OperationLogListener {
        void success(List<OpreationLogBean> beans);

        void fail(String errorMsg);
    }
}
