package com.holike.crm.model.fragment;

import com.holike.crm.base.BaseModel;
import com.holike.crm.bean.LineAttractBean;
import com.holike.crm.http.MyHttpClient;
import com.holike.crm.http.RequestCallBack;
import com.holike.crm.http.UrlPath;

import java.util.Map;

public class OnlineAttractReportModel extends BaseModel {

    public void getData(Map<String,String> params, OnlineAttractReportListener listener) {

        addDisposable(MyHttpClient.postByTimeout(UrlPath.URL_ONLINE_DRAINAGE_REPORT, null, params,60, new RequestCallBack<LineAttractBean>() {

            @Override
            public void onFailed(String failReason) {
                listener.onFail(failReason);
            }

            @Override
            public void onSuccess(LineAttractBean result) {
                listener.onSuccess(result);
            }
        }));


    }

    public interface OnlineAttractReportListener {
        void onSuccess(LineAttractBean result);

        void onFail(String errorMsg);
    }
}
