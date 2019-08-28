package com.holike.crm.model.fragment;

import com.holike.crm.base.BaseModel;
import com.holike.crm.bean.UserInfoBean;
import com.holike.crm.http.MyHttpClient;
import com.holike.crm.http.RequestCallBack;
import com.holike.crm.http.UrlPath;

/**
 * Created by wqj on 2018/2/24.
 * 我的
 */

public class MineModel extends BaseModel {

    public void getUserInfo(final GetUserInfoListener listener) {
       addDisposable(MyHttpClient.postByCliId(UrlPath.URL_GET_USERINFO, null, null, new RequestCallBack<UserInfoBean>() {
            @Override
            public void onFailed(String failReason) {
                listener.failed(failReason);
            }

            @Override
            public void onSuccess(UserInfoBean result) {
                listener.success(result);
            }
        }));
    }

    public interface GetUserInfoListener {
        void success(UserInfoBean userInfoBean);

        void failed(String failed);
    }
}
