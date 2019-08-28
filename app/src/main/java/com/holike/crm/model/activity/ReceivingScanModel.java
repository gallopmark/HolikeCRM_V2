package com.holike.crm.model.activity;

import com.holike.crm.base.BaseModel;
import com.holike.crm.http.MyHttpClient;
import com.holike.crm.http.RequestCallBack;
import com.holike.crm.http.UrlPath;
import com.holike.crm.model.event.EventQRCodeScanResult;
import com.holike.crm.util.Constants;
import com.holike.crm.util.SharedPreferencesUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReceivingScanModel extends BaseModel {

    public void submitCodeInfo(List<EventQRCodeScanResult> codes, final ReceivingScanListener listener) {
        Map<String, String> header = new HashMap<>();
        Map<String, String> params = new HashMap<>();
        params.put("code", params(codes));
        header.put(Constants.CLIENT, SharedPreferencesUtils.getString(Constants.CLIENT, ""));
        addDisposable(MyHttpClient.postByCliId(UrlPath.URL_SUBMIT_CODE_INFO, header, params, new RequestCallBack<String>() {
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

    /**
     * 获取数据
     */
    public void getCodeInfo(String code, final ReceivingScanCodeListener listener) {
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


    private String params(List<EventQRCodeScanResult> codes) {
        StringBuilder sb = new StringBuilder();
        int i = 0;
        for (EventQRCodeScanResult bean : codes) {
            sb.append(i == 0 || i == codes.size() ? bean.getResult() : "@" + bean.getResult());
            i++;
        }
        return sb.toString();
    }

    public interface ReceivingScanListener {
        void success(String result);

        void failed(String result);

    }

    public interface ReceivingScanCodeListener {
        void success(String result);

        void failed(String result);

    }
}
