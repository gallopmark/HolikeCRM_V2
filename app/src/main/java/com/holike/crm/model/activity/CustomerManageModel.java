package com.holike.crm.model.activity;

import androidx.annotation.Nullable;

import com.holike.crm.base.BaseModel;
import com.holike.crm.bean.AttBean;
import com.holike.crm.bean.CustomerListBean;
import com.holike.crm.bean.TypeIdBean;
import com.holike.crm.http.MyHttpClient;
import com.holike.crm.http.MyJsonParser;
import com.holike.crm.http.RequestCallBack;
import com.holike.crm.http.UrlPath;
import com.holike.crm.util.Constants;
import com.holike.crm.util.SharedPreferencesUtils;
import com.holike.crm.util.TimeUtil;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by wqj on 2018/2/25.
 * 客户管理
 */

public class CustomerManageModel extends BaseModel {

    /**
     * 获取客户列表
     */
    public void getCustomerList(String searchContent, String source, String followId,
                                String pageIndex, String pageSize,
                                Date startDate, Date endDate,
                                final GetCustomerListListener listener) {
        Map<String, String> params = new HashMap<>();
        params.put("searchContent", searchContent);
        params.put("source", source);
        params.put("followId", followId);
        if (startDate != null) {
            try {
                String start = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(startDate);
                params.put("startDate", start);
            } catch (Exception e) {
                params.put("startDate", "");
            }
        } else {
            params.put("startDate", "");
        }
        if (endDate != null) {   //往后加一天
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(endDate);
            calendar.set(Calendar.HOUR, 23);
            calendar.set(Calendar.MINUTE, 59);
            calendar.set(Calendar.SECOND, 59);
            Date commonDate = calendar.getTime();
            try {
                String end = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(commonDate);
                params.put("endDate", end);
            } catch (Exception e) {
                params.put("endDate", "");
            }
        } else {
            params.put("endDate", "");
        }
//        if (startDate != null) params.put("startDate", TimeUtil.dateToStamp(startDate, false));
//        if (endDate != null) params.put("endDate", TimeUtil.dateToStamp(endDate, true));
        params.put("pageIndex", pageIndex);
        params.put("pageSize", pageSize);
        Map<String, String> header = new HashMap<>();
        header.put(Constants.USER_ID, SharedPreferencesUtils.getString(Constants.USER_ID));
        MyHttpClient.postByTimeout(UrlPath.URL_GET_CUSTOMER_LIST, header, params, 60, new RequestCallBack<String>() {
            @Override
            public void onFailed(String result) {
                listener.failed(result);
            }

            @Override
            public void onSuccess(String result) {
                List<CustomerListBean> list = MyJsonParser.parseJsonToList(result, CustomerListBean.class);
                AttBean attBean = MyJsonParser.fromJsonToBean(MyJsonParser.getAtt(result), AttBean.class);
                listener.success(list, attBean);
            }

        });
    }

    public interface GetCustomerListListener {
        void success(List<CustomerListBean> list, @Nullable AttBean attBean);

        void failed(String failed);
    }

    /**
     * 获取类型id
     */
    public void getTypeId(final GetTypeIdListener listener) {
        Map<String, String> header = new HashMap<>();
        header.put(Constants.USER_ID, SharedPreferencesUtils.getString(Constants.USER_ID));
        MyHttpClient.post(UrlPath.URL_GET_TYPE_ID, header, null, new RequestCallBack<TypeIdBean>() {
            @Override
            public void onFailed(String failReason) {
                listener.failed(failReason);
            }

            @Override
            public void onSuccess(TypeIdBean result) {
                listener.success(result);
            }

        });
    }

    public void deleteCustomer(String houseId, final DeleteCustomerCallback callback) {
        Map<String, String> params = new HashMap<>();
        params.put("houseId", houseId);
        MyHttpClient.post(UrlPath.URL_DELETE_CUSTOMER, null, params, new RequestCallBack<String>() {
            @Override
            public void onFailed(String failReason) {
                if (callback != null) callback.deleteFailure(failReason);
            }

            @Override
            public void onSuccess(String result) {
                if (callback != null) callback.deleteSuccess(result);
            }
        });
    }

    public interface GetTypeIdListener {
        void success(TypeIdBean bean);

        void failed(String failed);
    }

    public interface DeleteCustomerCallback {
        void deleteSuccess(String result);

        void deleteFailure(String message);
    }
}
