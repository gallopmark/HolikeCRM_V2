package com.holike.crm.helper;

import com.holike.crm.bean.DistributionGuiderBean;
import com.holike.crm.bean.DistributionStoreBean;
import com.holike.crm.http.MyHttpClient;
import com.holike.crm.http.RequestCallBack;
import com.holike.crm.http.UrlPath;
import com.holike.crm.util.Constants;
import com.holike.crm.util.SharedPreferencesUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by wqj on 2018/9/19.
 */
@Deprecated
public class ShopListRequestHelper {

    private ShopListCallback mCallback;

    public ShopListRequestHelper(ShopListCallback callback) {
        this.mCallback = callback;
    }

    public void requestStore(String dealerId) {
        mCallback.onBeginRequest();
        Map<String, String> header = new HashMap<>();
        Map<String, String> params = new HashMap<>();
        header.put(Constants.USER_ID, SharedPreferencesUtils.getString(Constants.USER_ID, ""));
        header.put(Constants.COOKIE, SharedPreferencesUtils.getString(Constants.COOKIE));
        params.put(Constants.DEALER_ID,dealerId );
        MyHttpClient.post(UrlPath.URL_DISTRIBUTION_STORE, header, params, new RequestCallBack<List<DistributionStoreBean>>() {
            @Override
            public void onFailed(String failReason) {
                mCallback.onFail(failReason);
            }

            @Override
            public void onSuccess(List<DistributionStoreBean> result) {
                mCallback.onStoreDataSuccess(result);
            }
        });
    }

    public void updateStore(String houseId, String personalId, String shopId, String salesId) {
        Map<String, String> header = new HashMap<>();
        Map<String, String> params = new HashMap<>();
        header.put(Constants.USER_ID, SharedPreferencesUtils.getString(Constants.USER_ID, ""));
        header.put(Constants.COOKIE, SharedPreferencesUtils.getString(Constants.COOKIE));
        params.put("houseId", houseId);
        params.put("personalId", personalId);
        params.put("shopId", shopId);
        params.put("salesId", salesId);
        MyHttpClient.post(UrlPath.URL_UPDATE_HOUSE_SHOP, header, params, new RequestCallBack() {
            @Override
            public void onFailed(String failReason) {
                mCallback.onUpgradeFail(failReason);
            }

            @Override
            public void onSuccess(Object result) {
                mCallback.onUpgradeSuccess();
            }
        });

    }

    public void queryPer(String shopId) {
        Map<String, String> header = new HashMap<>();
        header.put(Constants.USER_ID, SharedPreferencesUtils.getString(Constants.USER_ID, ""));
        header.put(Constants.COOKIE, SharedPreferencesUtils.getString(Constants.COOKIE));
        Map<String, String> params = new HashMap<>();
        params.put("shopId", shopId);
        MyHttpClient.post(UrlPath.URL_QUERY_PER_SHOP, header, params, new RequestCallBack<DistributionGuiderBean>() {
            @Override
            public void onFailed(String failReason) {
                mCallback.onUpgradeFail(failReason);
            }

            @Override
            public void onSuccess(DistributionGuiderBean result) {
                mCallback.onGuiderDataSuccess(result);
            }


        });


    }


    public interface ShopListCallback {
        void onBeginRequest();

        void onStoreDataSuccess(List<DistributionStoreBean> dataBeans);

        void onGuiderDataSuccess(DistributionGuiderBean dataBeans);

        void onFail(String errorMsg);

        void onUpgradeSuccess();

        void onUpgradeFail(String errorMsg);
    }


}
