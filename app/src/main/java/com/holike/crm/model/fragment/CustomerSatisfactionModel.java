package com.holike.crm.model.fragment;

import com.holike.crm.base.BaseModel;
import com.holike.crm.bean.CustomerSatisfactionBean;
import com.holike.crm.http.RequestCallBack;
import com.holike.crm.http.UrlPath;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by gallop on 2019/9/20.
 * Copyright holike possess 2019.
 */
public class CustomerSatisfactionModel extends BaseModel {

    public void onQueryRequest(String type, String cityCode, String datetime, RequestCallBack<CustomerSatisfactionBean> callBack) {
        Map<String, String> params = new HashMap<>();
        params.put("type", type);
        params.put("cityCode", cityCode);
        params.put("time", datetime);
        postByTimeout(UrlPath.URL_EVALUATION_REPORT, params, 60, callBack);
    }
}
