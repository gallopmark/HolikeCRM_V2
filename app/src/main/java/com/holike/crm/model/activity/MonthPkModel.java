package com.holike.crm.model.activity;

import com.holike.crm.base.BaseModel;
import com.holike.crm.bean.MonthPkBean;
import com.holike.crm.http.MyHttpClient;
import com.holike.crm.http.RequestCallBack;
import com.holike.crm.http.UrlPath;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wqj on 2018/6/7.
 * 月度PK
 */

public class MonthPkModel extends BaseModel {

    /**
     * 获取月度pk数据
     */
    public void getData(String group, final GetDataListener listener) {
        Map<String, String> params = new HashMap<>();
        params.put("group", group);
        addDisposable(MyHttpClient.postByTimeout(UrlPath.URL_MONTH_PK_REPORT, null, params, 60, new RequestCallBack<MonthPkBean>() {
            @Override
            public void onFailed(String failReason) {
                listener.failed(failReason);
            }

            @Override
            public void onSuccess(MonthPkBean result) {
                listener.success(result);
            }

        }));
    }

    public interface GetDataListener {
        void success(MonthPkBean bean);

        void failed(String failed);
    }
}
