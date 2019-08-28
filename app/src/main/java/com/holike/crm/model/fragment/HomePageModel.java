package com.holike.crm.model.fragment;

import com.holike.crm.base.BaseModel;
import com.holike.crm.bean.HomepageBean;
import com.holike.crm.http.MyHttpClient;
import com.holike.crm.http.RequestCallBack;
import com.holike.crm.http.UrlPath;
import com.holike.crm.util.Constants;
import com.holike.crm.util.SharedPreferencesUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wqj on 2018/2/24.
 * 首页
 */

public class HomePageModel extends BaseModel {

    /**
     * 获取首页数据
     */
    public void getHomepageData(final GetHomepageDataListener listener) {
        Map<String, String> params = new HashMap<>();
        params.put("crmAccount", SharedPreferencesUtils.getString(Constants.USER_ID));
        params.put("crmPassword", SharedPreferencesUtils.getString(Constants.USER_PSW));
        addDisposable(MyHttpClient.postByTimeout(UrlPath.URL_HOMEPAGE_DATA, null, params, 60, new RequestCallBack<HomepageBean>() {
            @Override
            public void onFailed(String failReason) {
                listener.failed(failReason);
            }

            @Override
            public void onSuccess(HomepageBean result) {
                listener.success(result);
            }
        }));
    }

    public interface GetHomepageDataListener {
        void success(HomepageBean bean);

        void failed(String failed);
    }
}
