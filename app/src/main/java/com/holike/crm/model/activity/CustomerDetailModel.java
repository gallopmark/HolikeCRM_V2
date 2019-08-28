package com.holike.crm.model.activity;

import com.holike.crm.base.BaseModel;
import com.holike.crm.bean.CustomerDetailBean;
import com.holike.crm.http.MyHttpClient;
import com.holike.crm.http.RequestCallBack;
import com.holike.crm.http.UrlPath;
import com.holike.crm.util.Constants;
import com.holike.crm.util.SharedPreferencesUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wqj on 2018/8/17.
 * 客户管理
 */

public class CustomerDetailModel extends BaseModel {

    /**
     * 获取客户详情
     */
    public void getData(String personalId, final GetDataListener listener) {
        Map<String, String> params = new HashMap<>();
        params.put("personalId", personalId);
        Map<String, String> header = new HashMap<>();
        header.put(Constants.USER_ID, SharedPreferencesUtils.getString(Constants.USER_ID, ""));
        header.put(Constants.COOKIE, SharedPreferencesUtils.getString(Constants.COOKIE));
        addDisposable(MyHttpClient.post(UrlPath.URL_COMSTOMER_INFO, header, params, new RequestCallBack<CustomerDetailBean>() {
            @Override
            public void onFailed(String failReason) {
                listener.failed(failReason);
            }

            @Override
            public void onSuccess(CustomerDetailBean result) {
                listener.success(result);
            }
        }));
    }

    public void revoke(String cancelReason, String customerStatus, String houseId, String optCode, final RevokeListener listener) {
        Map<String, String> params = new HashMap<>();
        params.put("cancelReason", cancelReason);
        params.put("customerStatus", customerStatus);
        params.put("houseId", houseId);
        params.put("optCode", optCode);
        Map<String, String> header = new HashMap<>();
        header.put(Constants.USER_ID, SharedPreferencesUtils.getString(Constants.USER_ID, ""));
        header.put(Constants.COOKIE, SharedPreferencesUtils.getString(Constants.COOKIE));
        addDisposable(MyHttpClient.post(UrlPath.URL_CANCEL, header, params, new RequestCallBack<String>() {
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

    public interface GetDataListener {
        void success(CustomerDetailBean customerDetailBean);

        void failed(String failed);
    }

    public interface RevokeListener {
        void success(String success);

        void failed(String failed);

    }

}
