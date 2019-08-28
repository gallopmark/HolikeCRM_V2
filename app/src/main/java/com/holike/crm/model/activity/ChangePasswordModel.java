package com.holike.crm.model.activity;

import com.holike.crm.base.BaseModel;
import com.holike.crm.http.MyHttpClient;
import com.holike.crm.http.RequestCallBack;
import com.holike.crm.http.UrlPath;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wqj on 2018/2/25.
 * 修改密码
 */

public class ChangePasswordModel extends BaseModel {

    /**
     * 修改密码
     */
    public void changePassword(String newPassword, String oldPassword, final ChangePasswordListener listener) {
        Map<String, String> params = new HashMap<>();
        params.put("newPassword", newPassword);
        params.put("oldPassword", oldPassword);
       addDisposable(MyHttpClient.postByCliId(UrlPath.URL_CHANGE_PASSWORD, null, params, new RequestCallBack<String>() {
            @Override
            public void onFailed(String failReason) {
                listener.failed(failReason);
            }

            @Override
            public void onSuccess(String result) {
                listener.success();
            }

        }));

    }

    public interface ChangePasswordListener {
        void success();

        void failed(String failed);
    }
}
