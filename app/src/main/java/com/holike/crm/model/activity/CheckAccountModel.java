package com.holike.crm.model.activity;

import com.holike.crm.base.BaseModel;
import com.holike.crm.bean.CheckAccountBean;
import com.holike.crm.http.MyHttpClient;
import com.holike.crm.http.RequestCallBack;
import com.holike.crm.http.UrlPath;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wqj on 2018/2/24.
 * 查询账号
 */

public class CheckAccountModel extends BaseModel {

    /**
     * 查询账号
     */
    public void checkAccount(String code, String crmAccount, String crmPassword, final CheckListener listener) {
        Map<String, String> params = new HashMap<>();
        params.put("code", code);
        params.put("crmAccount", crmAccount);
        params.put("crmPassword", crmPassword);

        addDisposable(MyHttpClient.post(UrlPath.URL_CHECK_ACCOUNT, null, params, new RequestCallBack<CheckAccountBean>() {
            @Override
            public void onFailed(String failReason) {
                listener.failed(failReason);
            }

            @Override
            public void onSuccess(CheckAccountBean result) {
                listener.success(result);
            }
        }));
    }

    public interface CheckListener {
        void success(CheckAccountBean result);

        void failed(String failed);
    }
}
