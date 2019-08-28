package com.holike.crm.model.fragment;

import com.holike.crm.base.BaseModel;
import com.holike.crm.bean.BuildStoreBean;
import com.holike.crm.http.MyHttpClient;
import com.holike.crm.http.RequestCallBack;
import com.holike.crm.http.UrlPath;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wqj on 2018/5/9.
 * 建店数据报表
 */

public class BuildStoreModel extends BaseModel {
    /**
     * 获取建店报表数据
     */
    public void getData(String cityCode, String time, String type, final GetDataListener listener) {
        Map<String, String> params = new HashMap<>();
        params.put("cityCode", cityCode);
        params.put("time", time);
        params.put("type", type);
        addDisposable(MyHttpClient.postByTimeout(UrlPath.URL_GET_BUILD_SHOP_REPORT, null, params, 60, new RequestCallBack<BuildStoreBean>() {
            @Override
            public void onFailed(String failReason) {
                listener.failed(failReason);
            }

            @Override
            public void onSuccess(BuildStoreBean result) {
                listener.success(result);
            }
        }));
    }

    public interface GetDataListener {
        void failed(String failed);

        void success(BuildStoreBean bean);
    }
}
