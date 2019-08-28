package com.holike.crm.model.activity;

import com.holike.crm.base.BaseModel;
import com.holike.crm.http.MyHttpClient;
import com.holike.crm.http.RequestCallBack;
import com.holike.crm.http.UrlPath;

/**
 * Created by wqj on 2018/4/13.
 * 启动页
 */

public class BootingModel extends BaseModel {

    /**
     * 获取广告页图片
     */
    public void getAd(final GetAdListener listener) {
        addDisposable(MyHttpClient.postByCliId(UrlPath.URL_GET_AD, null, null, new RequestCallBack<String>() {
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

    public interface GetAdListener {
        void success(String url);

        void failed(String failed);
    }
}
