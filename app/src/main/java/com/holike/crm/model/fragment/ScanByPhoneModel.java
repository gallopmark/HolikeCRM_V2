package com.holike.crm.model.fragment;

import com.holike.crm.base.BaseModel;
import com.holike.crm.http.MyHttpClient;
import com.holike.crm.http.RequestCallBack;
import com.holike.crm.http.UrlPath;
import com.holike.crm.util.Constants;
import com.holike.crm.util.SharedPreferencesUtils;

import java.util.HashMap;
import java.util.Map;

public class ScanByPhoneModel extends BaseModel {
    /**
     * 获取数据
     */
    public void getCodeInfo(String code, final ScanByPhoneListener listener) {
        Map<String, String> header = new HashMap<>();
        Map<String, String> params = new HashMap<>();
        params.put("code", code);
        header.put(Constants.CLIENT, SharedPreferencesUtils.getString(Constants.CLIENT, ""));
        addDisposable(MyHttpClient.postByCliId(UrlPath.URL_GET_CODE_INFO, header, params, new RequestCallBack<String>() {
            @Override
            public void onFailed(String failReason) {
                listener.failed(failReason);
            }

            @Override
            public void onSuccess(String result) {
                listener.success(result);
            }
        }));
    }

    public interface ScanByPhoneListener {
        void success(String messageBean);

        void failed(String failed);
    }

}
