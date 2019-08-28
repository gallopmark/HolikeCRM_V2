package com.holike.crm.model.fragment;

import com.holike.crm.base.BaseModel;
import com.holike.crm.bean.TranslateReportBean;
import com.holike.crm.http.MyHttpClient;
import com.holike.crm.http.RequestCallBack;
import com.holike.crm.http.UrlPath;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wqj on 2018/4/12.
 * 订金转化报表
 */

public class TranslateReportModel extends BaseModel {

    /**
     * 获取数据
     */
    public void getData(String cityCode, String startTime, String endTime, String type, final GetDataListener listener) {
        Map<String, String> params = new HashMap<>();
        params.put("cityCode", cityCode);
        params.put("startTime", startTime);
        params.put("endTime", endTime);
        params.put("type", type);
        addDisposable(MyHttpClient.postByTimeout(UrlPath.URL_GET_TRANSLATE_REPORT, null, params, 60, new RequestCallBack<TranslateReportBean>() {
            @Override
            public void onFailed(String failReason) {
                listener.failed(failReason);
            }

            @Override
            public void onSuccess(TranslateReportBean result) {
                listener.success(result);
            }
        }));
    }

    public interface GetDataListener {
        void success(TranslateReportBean bean);

        void failed(String failed);
    }
}
