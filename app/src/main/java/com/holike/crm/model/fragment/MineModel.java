package com.holike.crm.model.fragment;

import com.holike.crm.base.BaseModel;
import com.holike.crm.bean.UpdateBean;
import com.holike.crm.bean.UserInfoBean;
import com.holike.crm.http.MyHttpClient;
import com.holike.crm.http.RequestCallBack;
import com.holike.crm.http.UrlPath;

/**
 * Created by wqj on 2018/2/24.
 * 我的
 */

public class MineModel extends BaseModel {

    /*检测版本更新*/
    public void checkVersion(RequestCallBack<UpdateBean> callBack) {
        addDisposable(MyHttpClient.post(UrlPath.URL_CHECK_VERSION, callBack));
    }

    public void getUserInfo(RequestCallBack<UserInfoBean> callBack) {
        addDisposable(MyHttpClient.postByCliId(UrlPath.URL_GET_USERINFO, null, null, callBack));
    }
}
