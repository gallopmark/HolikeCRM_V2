package com.holike.crm.model.activity;

import com.holike.crm.base.BaseModel;
import com.holike.crm.bean.AssociateBean;
import com.holike.crm.bean.CustomerBean;
import com.holike.crm.bean.TypeListBean;
import com.holike.crm.http.MyHttpClient;
import com.holike.crm.http.RequestCallBack;
import com.holike.crm.http.UrlPath;
import com.holike.crm.util.Constants;
import com.holike.crm.util.SharedPreferencesUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wqj on 2018/2/25.
 * 添加客户
 */

public class AddCustomerModel extends BaseModel {


    /**
     * 获取客户选择条件数据
     */
    public void getTypeList(final GetTypeListListener listener) {
       addDisposable(MyHttpClient.postByCliId(UrlPath.URL_GET_TYPE_ID, null, null, new RequestCallBack<TypeListBean>() {
            @Override
            public void onFailed(String failReason) {
                listener.failed(failReason);
            }

            @Override
            public void onSuccess(TypeListBean result) {
                listener.success(result);
            }

        }));
    }

    public interface GetTypeListListener {
        void success(TypeListBean result);

        void failed(String failed);
    }

    /**
     * 新增/编辑客户或新增
     */
    public void addCustomer(CustomerBean customerBean, String relationId, final EditCustomerListener listener) {
        Map<String, String> params = new HashMap<>();
        params.put("customerPersonalInfo", customerBean.toString());
        params.put("relationId", relationId);
        Map<String, String> header = new HashMap<>();
        header.put(Constants.USER_ID, SharedPreferencesUtils.getString(Constants.USER_ID));
        header.put(Constants.COOKIE, SharedPreferencesUtils.getString(Constants.COOKIE));
        addDisposable(MyHttpClient.post(UrlPath.URL_ADD_DEPOSIT, header, params, new RequestCallBack<String>() {
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


    public interface EditCustomerListener {

        void success(String success);

        void failed(String failed);
    }

    /**
     * 获取所属人
     */
    public void getAssociateData(String shopId, final GetAssociateListener listener) {
        Map<String, String> params = new HashMap<>();
        params.put("shopId", shopId);
        Map<String, String> header = new HashMap<>();
        header.put(Constants.USER_ID, SharedPreferencesUtils.getString(Constants.USER_ID));
        header.put(Constants.COOKIE, SharedPreferencesUtils.getString(Constants.COOKIE));
        addDisposable(MyHttpClient.post(UrlPath.URL_GET_ASSOCIATE, header, params, new RequestCallBack<AssociateBean>() {
            @Override
            public void onFailed(String failReason) {
                listener.failed(failReason);
            }

            @Override
            public void onSuccess(AssociateBean result) {
                listener.success(result);
            }
        }));
    }

    public interface GetAssociateListener {
        void success(AssociateBean bean);

        void failed(String failed);
    }

}
