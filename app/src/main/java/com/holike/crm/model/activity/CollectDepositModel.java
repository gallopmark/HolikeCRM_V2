package com.holike.crm.model.activity;

import com.holike.crm.base.BaseModel;
import com.holike.crm.bean.CollectDepositListBean;
import com.holike.crm.bean.CustomerDetailBean;
import com.holike.crm.http.MyHttpClient;
import com.holike.crm.http.RequestCallBack;
import com.holike.crm.http.UrlPath;
import com.holike.crm.util.Constants;
import com.holike.crm.util.SharedPreferencesUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wqj on 2018/8/20.
 * 首页进入收取订金页
 */

public class CollectDepositModel extends BaseModel {
    /**
     * 获取收取订金列表数据
     */
    public void getData(String pageIndex, String pageSize, String searchContent, final GetDataListener listener) {
        Map<String, String> header = new HashMap<>();
        header.put(Constants.USER_ID, SharedPreferencesUtils.getString(Constants.USER_ID, ""));
        Map<String, String> params = new HashMap<>();
        params.put("pageIndex", pageIndex);
        params.put("pageSize", pageSize);
        params.put("searchContent", searchContent);
        addDisposable(MyHttpClient.post(UrlPath.URL_COLLECT_DEPOSIT_LIST, header, params, new RequestCallBack<List<CollectDepositListBean>>() {
            @Override
            public void onFailed(String failReason) {
                listener.failed(failReason);
            }

            @Override
            public void onSuccess(List<CollectDepositListBean> result) {
                listener.success(result);
            }

        }));
    }

    public interface GetDataListener {
        void failed(String failed);

        void success(List<CollectDepositListBean> beans);
    }

    /**
     * 获取客户详情
     */
    public void getCustomerDetail(String personalId, final GetCustomerDetailListener listener) {
        Map<String, String> params = new HashMap<>();
        params.put("personalId", personalId);
        Map<String, String> header = new HashMap<>();
        header.put(Constants.USER_ID, SharedPreferencesUtils.getString(Constants.USER_ID, ""));
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

    public interface GetCustomerDetailListener {
        void success(CustomerDetailBean customerDetailBean);

        void failed(String failed);
    }
}
