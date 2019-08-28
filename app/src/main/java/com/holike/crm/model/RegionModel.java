package com.holike.crm.model;

import com.holike.crm.base.BaseModel;
import com.holike.crm.bean.RegionBean;
import com.holike.crm.http.CustomerUrlPath;
import com.holike.crm.http.RequestCallBack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by gallop on 2019/8/2.
 * Copyright holike possess 2019.
 */
public class RegionModel extends BaseModel {

    /*获取省份*/
    public void getProvince(RequestCallBack<List<RegionBean>> callBack) {
        get(CustomerUrlPath.URL_REGION_PROVINCE, callBack);
    }

    public void getChildRegion(String parentRegionCode, String regionLevel, RequestCallBack<List<RegionBean>> callBack) {
        Map<String, String> params = new HashMap<>();
        params.put("parentRegionCode", parentRegionCode);
        params.put("regionLevel", regionLevel);
        get(CustomerUrlPath.URL_REGION_CHILD, params, callBack);
    }
}
