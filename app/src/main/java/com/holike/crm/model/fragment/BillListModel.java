package com.holike.crm.model.fragment;

import com.holike.crm.base.BaseModel;
import com.holike.crm.bean.BillListBean;
import com.holike.crm.http.MyHttpClient;
import com.holike.crm.http.RequestCallBack;
import com.holike.crm.http.UrlPath;
import com.holike.crm.util.Constants;
import com.holike.crm.util.SharedPreferencesUtils;

import java.util.HashMap;
import java.util.Map;

public class BillListModel extends BaseModel {
    public void getData(String pageNo, String startTime, String endTime, BillListListener listener) {
        String pageSize = "10";
        Map<String, String> maps = new HashMap<>(0);
        maps.put("crmAccount", SharedPreferencesUtils.getString(Constants.USER_ID, ""));
        maps.put("crmPassword", SharedPreferencesUtils.getString(Constants.USER_PSW, ""));
        maps.put("pageNo", pageNo);
        maps.put("pageSize", pageSize);
        maps.put("startTime", startTime);
        maps.put("endTime", endTime);

        addDisposable(MyHttpClient.postByCliId(UrlPath.URL_QUERY_DEALER_DETAIL, null, maps, new RequestCallBack<BillListBean>() {
            @Override
            public void onFailed(String failReason) {
                listener.onFail(failReason);
            }

            @Override
            public void onSuccess(BillListBean result) {
                listener.onSuccess(result);
            }


        }));


    }


    public interface BillListListener {
        void onSuccess(BillListBean bean);

        void onFail(String errorMsg);
    }
}
