package com.holike.crm.model.activity;


import com.holike.crm.base.BaseModel;
import com.holike.crm.bean.LoginBean;
import com.holike.crm.http.MyHttpClient;
import com.holike.crm.http.RequestCallBack;
import com.holike.crm.http.UrlPath;

import java.util.Map;

/**
 * Created by wqj on 2017/9/28.
 * 登录
 */

public class LoginModel extends BaseModel {

    /**
     * 登录
     */
    public void Login(Map<String, String> header,Map<String, String> params, final LoginListener loginListener) {
        addDisposable(MyHttpClient.post(UrlPath.URL_LOGIN, header,params, new RequestCallBack<LoginBean>() {
            @Override
            public void onFailed(String failReason) {
                loginListener.failed(failReason);
            }

            @Override
            public void onSuccess(LoginBean result) {
                loginListener.success(result);
            }

        }));
    }

    public interface LoginListener {
        void success(LoginBean loginBean);

        void failed(String err);
    }
}
