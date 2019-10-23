package com.holike.crm.model;

import com.holike.crm.base.BaseModel;
import com.holike.crm.http.ParamHelper;
import com.holike.crm.http.RequestCallBack;
import com.holike.crm.http.UrlPath;

import java.util.HashMap;
import java.util.Map;

public class SettingsModel extends BaseModel {

    public void setRule(String id, String param2, RequestCallBack<String> callBack) {
        Map<String, String> params = ParamHelper.general();
        params.put("id", ParamHelper.noNullWrap(id));
        params.put("param2", ParamHelper.noNullWrap(param2));
        post(UrlPath.URL_SETTINGS_RULE, params, callBack);
    }
}
