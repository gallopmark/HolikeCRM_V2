package com.holike.crm.model.fragment;

import com.holike.crm.base.BaseModel;
import com.holike.crm.http.MyHttpClient;
import com.holike.crm.http.RequestCallBack;
import com.holike.crm.http.UrlPath;
import com.holike.crm.util.Constants;
import com.holike.crm.util.SharedPreferencesUtils;


import java.util.HashMap;
import java.util.Map;

public class PayDetailsModel extends BaseModel {

    public void getDataOnline(String creditAmount, String date, String id,
                              String recipAccNo, String recipBkName, String recipBkNo, String recipName, String relationId,
                              String status, PayDetailsListner listner) {
        Map<String, String> params = new HashMap<>();
        params.put("creditAmount", creditAmount);
        params.put("crmAccount", SharedPreferencesUtils.getString(Constants.USER_ID, ""));
        params.put("crmPassword", SharedPreferencesUtils.getString(Constants.USER_PSW, ""));
        params.put("date", date);
        params.put("id", id);
        params.put("recipAccNo", recipAccNo);
        params.put("recipBkName", recipBkName);
        params.put("recipBkNo", recipBkNo);
        params.put("recipName", recipName);
        params.put("relationId", relationId);
        params.put("status", status);
        addDisposable(MyHttpClient.postByCliId(UrlPath.URL_EDIT_STATUS, null, params, new RequestCallBack<String>() {

            @Override
            public void onFailed(String failReason) {
                listner.onFail(failReason);
            }

            @Override
            public void onSuccess(String result) {
                listner.onSuccess();
            }
        }));
    }

    public void getDataPayList(String id, String status, String relationId, PayDetailsListner listner) {
        Map<String, String> params = new HashMap<>();
        params.put("crmAccount", SharedPreferencesUtils.getString(Constants.USER_ID, ""));
        params.put("crmPassword", SharedPreferencesUtils.getString(Constants.USER_PSW, ""));
        params.put("id", id);
        params.put("relationId", relationId);
        params.put("status", status);
        addDisposable(MyHttpClient.postByCliId(UrlPath.URL_CONFIRM, null, params, new RequestCallBack<String>() {

            @Override
            public void onFailed(String failReason) {
                listner.onFail(failReason);
            }

            @Override
            public void onSuccess(String result) {
                listner.onSuccess();
            }
        }));
    }

    public interface PayDetailsListner {
        void onSuccess();

        void onFail(String errorMsg);
    }
}
