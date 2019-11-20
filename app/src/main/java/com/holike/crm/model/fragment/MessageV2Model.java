package com.holike.crm.model.fragment;

import com.holike.crm.base.BaseModel;
import com.holike.crm.bean.MessageResultBean;
import com.holike.crm.http.CustomerUrlPath;
import com.holike.crm.http.ParamHelper;
import com.holike.crm.http.RequestCallBack;
import com.holike.crm.http.UrlPath;
import com.holike.crm.util.Constants;
import com.holike.crm.util.SharedPreferencesUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by pony on 2019/8/21.
 * Copyright holike possess 2019.
 */
public class MessageV2Model extends BaseModel {

    public void getMessage(String type, int pageNo, int pageSize, RequestCallBack<MessageResultBean> callBack) {
        Map<String, String> header = new HashMap<>();
        header.put(Constants.CLI_ID, SharedPreferencesUtils.getString(Constants.CLI_ID));
        Map<String, String> params = new HashMap<>();
        params.put("pageNo", String.valueOf(pageNo));
        params.put("pageSize", String.valueOf(pageSize));
        params.put("type", type);
        post(UrlPath.URL_GET_MESSAGE_LIST, header, params, callBack);
    }

    /*客户管理-同意/拒绝分配*/
    public void redistribute(String noticeUserId, String type, RequestCallBack<String> callBack) {
        Map<String, String> params = new HashMap<>();
        params.put("noticeUserId", String.valueOf(noticeUserId));
        params.put("type", String.valueOf(type));
        String body = ParamHelper.toBody(params);
        postByBody(CustomerUrlPath.URL_REDISTRIBUTE, body, callBack);
    }
}
