package com.holike.crm.model.activity;


import com.holike.crm.base.BaseModel;
import com.holike.crm.bean.UpdateBean;
import com.holike.crm.http.MyHttpClient;
import com.holike.crm.http.RequestCallBack;
import com.holike.crm.http.UrlPath;

/**
 * Created by wqj on 2017/11/27.
 * 主页Model
 */

public class MainModel extends BaseModel {

    /**
     * 检测系统版本更新
     */
    public void checkVersion(RequestCallBack<UpdateBean> callBack) {
        addDisposable(MyHttpClient.post(UrlPath.URL_CHECK_VERSION, callBack));
    }
}
