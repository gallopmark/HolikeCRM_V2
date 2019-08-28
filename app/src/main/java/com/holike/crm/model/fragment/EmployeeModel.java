package com.holike.crm.model.fragment;

import androidx.annotation.NonNull;

import com.holike.crm.base.BaseModel;
import com.holike.crm.bean.DistributionStoreBean;
import com.holike.crm.bean.EmployeeBean;
import com.holike.crm.bean.EmployeeDetailBean;
import com.holike.crm.bean.HomepageBean;
import com.holike.crm.bean.RoleDataBean;
import com.holike.crm.http.MyHttpClient;
import com.holike.crm.http.RequestCallBack;
import com.holike.crm.http.UrlPath;
import com.holike.crm.util.Constants;
import com.holike.crm.util.SharedPreferencesUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.disposables.Disposable;

public class EmployeeModel extends BaseModel {

    /*获取员工列表*/
    public void getEmployeeList(@NonNull String content, @NonNull String shopId, @NonNull String status, final GetEmployeeListListener listener) {
        Map<String, String> params = new HashMap<>();
        params.put("content", content);
        params.put("shopId", shopId);
        params.put("status", status);
        Map<String, String> header = new HashMap<>();
        header.put(Constants.USER_ID, SharedPreferencesUtils.getString(Constants.USER_ID));
        addDisposable(MyHttpClient.postByTimeout(UrlPath.URL_EMPLOYEE_LIST, header, params, 60, new RequestCallBack<List<EmployeeBean>>() {
            @Override
            public void onStart(Disposable d) {
                if (listener != null)
                    listener.onStart();
            }

            @Override
            public void onFailed(String failReason) {
                if (listener != null)
                    listener.onFailure(failReason);
            }

            @Override
            public void onSuccess(List<EmployeeBean> result) {
                if (listener != null)
                    listener.onSuccess(result);
            }

            @Override
            public void onFinished() {
                if (listener != null)
                    listener.onFinished();
            }
        }));
    }

    /*获取门店*/
    public void getStoreList(@NonNull OnGetStoreCallback callback) {
        Map<String, String> params = new HashMap<>();
        params.put("crmAccount", SharedPreferencesUtils.getString(Constants.USER_ID));
        params.put("crmPassword", SharedPreferencesUtils.getString(Constants.USER_PSW));
        addDisposable(MyHttpClient.postByTimeout(UrlPath.URL_HOMEPAGE_DATA, null, params, 60, new RequestCallBack<HomepageBean>() {
            @Override
            public void onStart(Disposable d) {
                callback.onGetStoreStart();
            }

            @Override
            public void onFailed(String failReason) {
                callback.onGetStoreFalure(failReason);
            }

            @Override
            public void onSuccess(HomepageBean bean) {
                List<DistributionStoreBean> list = new ArrayList<>();
                if (bean.getTypeList() != null && bean.getTypeList().getShopData() != null) {
                    list = bean.getTypeList().getShopData();
                }
                callback.onGetStoreList(list);
            }

            @Override
            public void onFinished() {
                callback.onGetStoreComplete();
            }
        }));
    }

    /*增加或修改员工信息*/
    public void saveEmployee(Map<String, String> params, @NonNull OnSaveEmployeeCallback callback) {
        addDisposable(MyHttpClient.postByCliId(UrlPath.URL_EMPLOYEE_EDIT, null, params, new RequestCallBack<String>() {
            @Override
            public void onStart(Disposable d) {
                callback.onSaveStart();
            }

            @Override
            public void onFailed(String failReason) {
                callback.onSaveFailure(failReason);
            }

            @Override
            public void onSuccess(String result) {
                callback.onSaveSuccess();
            }

            @Override
            public void onFinished() {
                callback.onSaveComplete();
            }
        }));
    }

    /*获取员工信息*/
    public void getEmployeeDetails(String userId, @NonNull OnGetEmployeeCallback callback) {
        Map<String, String> params = new HashMap<>();
        params.put("userId", userId);
        addDisposable(MyHttpClient.postByTimeout(UrlPath.URL_EMPLOYEE_DATA, null, params, 60, new RequestCallBack<EmployeeDetailBean>() {
            @Override
            public void onStart(Disposable d) {
                callback.onGetStart();
            }

            @Override
            public void onFailed(String failReason) {
                callback.onFailure(failReason);
            }

            @Override
            public void onSuccess(EmployeeDetailBean result) {
                callback.onGetEmployee(result);
            }

            @Override
            public void onFinished() {
                callback.onGetComplete();
            }
        }));
    }

    /*重置员工密码*/
    public void resetPassword(String userId, String newPassword, @NonNull OnResetPasswordCallback callback) {
        Map<String, String> params = new HashMap<>();
        params.put("userId", userId);
        params.put("password", newPassword);
        addDisposable(MyHttpClient.postByCliId(UrlPath.URL_EMPLOYEE_EDIT_PASSWORD, null, params, new RequestCallBack<String>() {
            @Override
            public void onStart(Disposable d) {
                callback.onResetStart();
            }

            @Override
            public void onFailed(String failReason) {
                callback.onFailure(failReason);
            }

            @Override
            public void onSuccess(String result) {
                callback.onSuccess();
            }

            @Override
            public void onFinished() {
                callback.onResetComplete();
            }
        }));
    }

    /*获取权限信息*/
    public void getAuthInfo(@NonNull OnGetAuthInfoCallback callback) {
        Map<String, String> params = new HashMap<>();
        params.put("crmAccount", SharedPreferencesUtils.getString(Constants.USER_ID));
        params.put("crmPassword", SharedPreferencesUtils.getString(Constants.USER_PSW));
        addDisposable(MyHttpClient.postByTimeout(UrlPath.URL_HOMEPAGE_DATA, null, params, 60, new RequestCallBack<HomepageBean>() {

            @Override
            public void onStart(Disposable d) {
                callback.onLoading();
            }

            @Override
            public void onFailed(String failReason) {
                callback.onGetAuthInfoFailure(failReason);
            }

            @Override
            public void onSuccess(HomepageBean result) {
                List<RoleDataBean.AuthInfoBean> list = new ArrayList<>();
                if (result.getAuthInfo() != null && !result.getAuthInfo().isEmpty()) {
                    list.addAll(result.getAuthInfo());
                }
                callback.onGetAuthInfo(list);
            }

            @Override
            public void onFinished() {
                callback.onLoadingEnd();
            }
        }));
    }

    public interface GetEmployeeListListener {
        void onStart();

        void onSuccess(List<EmployeeBean> mData);

        void onFailure(String errorMessage);

        void onFinished();
    }

    public interface OnGetStoreCallback {
        void onGetStoreStart();

        void onGetStoreList(List<DistributionStoreBean> beans);

        void onGetStoreFalure(String message);

        void onGetStoreComplete();
    }

    public interface OnSaveEmployeeCallback {
        void onSaveStart();

        void onSaveSuccess();

        void onSaveFailure(String message);

        void onSaveComplete();
    }

    public interface OnGetEmployeeCallback {
        void onGetStart();

        void onGetEmployee(EmployeeDetailBean employeeBean);

        void onFailure(String message);

        void onGetComplete();
    }

    public interface OnResetPasswordCallback {
        void onResetStart();

        void onSuccess();

        void onFailure(String message);

        void onResetComplete();
    }

    public interface OnGetAuthInfoCallback {
        void onLoading();

        void onGetAuthInfo(List<RoleDataBean.AuthInfoBean> infoBeans);

        void onGetAuthInfoFailure(String message);

        void onLoadingEnd();
    }
}
