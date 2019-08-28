package com.holike.crm.model.fragment;

import com.holike.crm.base.BaseModel;
import com.holike.crm.bean.QuotationBean;
import com.holike.crm.http.MyHttpClient;
import com.holike.crm.http.RequestCallBack;
import com.holike.crm.http.UrlPath;
import com.holike.crm.util.Constants;
import com.holike.crm.util.SharedPreferencesUtils;

import java.util.HashMap;
import java.util.Map;

public class QuotationListModel extends BaseModel {
    public void getData(String orderId, final QuotationListListener listener) {
        Map<String, String> header = new HashMap<>();
        Map<String, String> params = new HashMap<>();
        params.put("orderId", orderId);
        header.put(Constants.CLIENT, SharedPreferencesUtils.getString(Constants.CLIENT, ""));
        addDisposable(MyHttpClient.postByCliId(UrlPath.URL_QUERY_QUOTE_INFO, header, params, new RequestCallBack<QuotationBean>() {
            @Override
            public void onFailed(String failReason) {
                listener.fail(failReason);
            }

            @Override
            public void onSuccess(QuotationBean results) {
                listener.success(results);
            }
        }));
    }

    public interface QuotationListListener {
        void success(QuotationBean beans);

        void fail(String errorMsg);
    }
}
