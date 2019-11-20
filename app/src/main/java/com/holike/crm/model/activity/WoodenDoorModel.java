package com.holike.crm.model.activity;

import com.holike.crm.base.BaseModel;
import com.holike.crm.bean.WoodenDoorBean;
import com.holike.crm.http.MyHttpClient;
import com.holike.crm.http.RequestCallBack;
import com.holike.crm.http.UrlPath;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by pony on 2019/8/28.
 * Copyright holike possess 2019.
 */
public class WoodenDoorModel extends BaseModel {

    public void getData(String cityCode, String selectTime, String type, RequestCallBack<WoodenDoorBean> callBack) {
        Map<String, String> params = new HashMap<>();
        params.put("cityCode", cityCode);
        params.put("selectTime", selectTime);
        params.put("type", type);
        postByTimeout(UrlPath.URL_WOODEN_DOOR, params, 60, callBack);
    }
}
