package com.holike.crm.model.fragment;

import com.holike.crm.base.BaseModel;
import com.holike.crm.bean.SpaceManifestBean;
import com.holike.crm.http.MyHttpClient;
import com.holike.crm.http.RequestCallBack;
import com.holike.crm.http.UrlPath;
import com.holike.crm.util.Constants;
import com.holike.crm.util.SharedPreferencesUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SpaceManifestModel extends BaseModel {
    public void getData(String orderId,final SpaceManifestListener listener) {
        Map<String, String> header = new HashMap<>();
        Map<String, String> params = new HashMap<>();
        params.put("orderId", orderId);
        header.put(Constants.CLIENT, SharedPreferencesUtils.getString(Constants.CLIENT, ""));
        addDisposable(MyHttpClient.postByCliId(UrlPath.URL_GET_PRODUCT_LIST, header, params, new RequestCallBack<List<SpaceManifestBean>>() {
            @Override
            public void onFailed(String failReason) {
                listener.fail(failReason);
            }

            @Override
            public void onSuccess(List<SpaceManifestBean> results) {
                listener.success(results);
            }
        }));
    }

    public interface SpaceManifestListener {
        void success(List<SpaceManifestBean> beans);

        void fail(String errorMsg);
    }
}
