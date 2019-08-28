package com.holike.crm.model.fragment;

import com.holike.crm.base.BaseModel;
import com.holike.crm.bean.PayListBean;
import com.holike.crm.http.MyHttpClient;
import com.holike.crm.http.RequestCallBack;
import com.holike.crm.http.UrlPath;
import com.holike.crm.util.Constants;
import com.holike.crm.util.SharedPreferencesUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OnlineDeclarationModel extends BaseModel {
    public void getData(String pageNo, String startTime, String endTime, String searchContent, OnlineDeclarationListener listener) {
        String pageSize = "10";
        Map<String, String> maps = new HashMap<>(0);
        maps.put("crmAccount", SharedPreferencesUtils.getString(Constants.USER_ID, ""));
        maps.put("crmPassword", SharedPreferencesUtils.getString(Constants.USER_PSW, ""));
        maps.put("pageNo", pageNo);
        maps.put("pageSize", pageSize);
        maps.put("startTime", startTime);
        maps.put("endTime", endTime);
        maps.put("recipContent", searchContent);
        addDisposable(MyHttpClient.postByCliId(UrlPath.URL_COMPLAIN_LIST, null, maps, new RequestCallBack<List<PayListBean>>() {
            @Override
            public void onFailed(String failReason) {
                listener.fail(failReason);
            }

            @Override
            public void onSuccess(List<PayListBean> result) {
                listener.success(result);
            }
        }));
    }


    public interface OnlineDeclarationListener {
        void success(List<PayListBean> bean);

        void fail(String errorMsg);
    }
}
