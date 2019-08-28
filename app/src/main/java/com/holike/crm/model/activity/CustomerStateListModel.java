package com.holike.crm.model.activity;

import com.holike.crm.base.BaseModel;
import com.holike.crm.bean.CustomerStateListBean;
import com.holike.crm.http.MyHttpClient;
import com.holike.crm.http.RequestCallBack;
import com.holike.crm.http.UrlPath;
import com.holike.crm.util.Constants;
import com.holike.crm.util.SharedPreferencesUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wqj on 2018/8/22.
 * 客户状态列表
 */

public class CustomerStateListModel extends BaseModel {
    public void getDate(String statusMove, String pageNo,String pageSize,final GetDateListener listener) {
        Map<String, String> params = new HashMap<>();
        params.put("statusMove", statusMove);
        params.put("pageNo", pageNo);
        params.put("pageSize", pageSize);
        Map<String, String> header = new HashMap<>();
        header.put(Constants.USER_ID, SharedPreferencesUtils.getString(Constants.USER_ID, ""));
        header.put(Constants.CLI_ID, SharedPreferencesUtils.getString(Constants.CLI_ID, ""));
        header.put(Constants.COOKIE, SharedPreferencesUtils.getString(Constants.COOKIE));
        addDisposable(MyHttpClient.post(UrlPath.URL_CUSTOMER_STATE_LIST, header, params, new RequestCallBack<CustomerStateListBean>() {
            @Override
            public void onFailed(String failReason) {
                listener.failed(failReason);
            }

            @Override
            public void onSuccess(CustomerStateListBean result) {
                listener.success(result);
            }
        }));
    }

    public interface GetDateListener {
        void success(CustomerStateListBean bean);

        void failed(String failed);
    }
}
