package com.holike.crm.model.fragment;

import com.holike.crm.base.BaseModel;
import com.holike.crm.bean.ReceiptDeliveryManifestBean;
import com.holike.crm.http.MyHttpClient;
import com.holike.crm.http.RequestCallBack;
import com.holike.crm.http.UrlPath;
import com.holike.crm.util.Constants;
import com.holike.crm.util.SharedPreferencesUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ReceiptDeliveryManifestModel extends BaseModel {

    public void getData(String orderId, final ReceiptDeliveryManifestListener listener) {

        Map<String, String> header = new HashMap<>();
        Map<String, String> params = new HashMap<>();
        params.put("orderId", orderId);
        header.put(Constants.CLIENT, SharedPreferencesUtils.getString(Constants.CLIENT, ""));
        addDisposable(MyHttpClient.postByCliId(UrlPath.URL_RECEIVE_DELIVERY_MANIFEST, header, params, new RequestCallBack<List<ReceiptDeliveryManifestBean>>() {
            @Override
            public void onFailed(String failReason) {
                listener.failed(failReason);
            }

            @Override
            public void onSuccess(List<ReceiptDeliveryManifestBean> results) {
                listener.success(results);
            }
        }));
    }

    public interface ReceiptDeliveryManifestListener {
        void success(List<ReceiptDeliveryManifestBean> messageBean);

        void failed(String failed);
    }
}
