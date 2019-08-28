package com.holike.crm.model.fragment;

import com.holike.crm.base.BaseModel;
import com.holike.crm.bean.CreditInquiryBean;
import com.holike.crm.http.MyHttpClient;
import com.holike.crm.http.RequestCallBack;
import com.holike.crm.http.UrlPath;
import com.holike.crm.util.Constants;
import com.holike.crm.util.SharedPreferencesUtils;

import java.util.HashMap;
import java.util.Map;

public class CreditInquiryModel extends BaseModel {

    public void getData(String pageNo, CreditInquiryListener listener) {
        String pageSize = "10";
        Map<String, String> maps = new HashMap<>(0);
        maps.put("crmAccount", SharedPreferencesUtils.getString(Constants.USER_ID, ""));
        maps.put("crmPassword", SharedPreferencesUtils.getString(Constants.USER_PSW, ""));
        maps.put("pageNo", pageNo);
        maps.put("pageSize", pageSize);
        addDisposable(MyHttpClient.postByCliId(UrlPath.URL_CREDIT_CHECKING_DEALER, null, maps, new RequestCallBack<CreditInquiryBean>() {
            @Override
            public void onFailed(String failReason) {
                listener.onFail(failReason);
            }

            @Override
            public void onSuccess(CreditInquiryBean result) {
                listener.onSuccess(result);
            }
        }));


    }


    public interface CreditInquiryListener {
        void onSuccess(CreditInquiryBean bean);

        void onFail(String errorMsg);
    }
}
