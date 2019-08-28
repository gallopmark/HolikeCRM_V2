package com.holike.crm.model.fragment;

import com.holike.crm.base.BaseModel;
import com.holike.crm.bean.TerminalCheckBean;
import com.holike.crm.http.MyHttpClient;
import com.holike.crm.http.RequestCallBack;
import com.holike.crm.http.UrlPath;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wqj on 2018/5/31.
 * 终端大检查
 */

public class TerminalCheckModel extends BaseModel {

    /**
     * 获取数据
     *
     */
    public void getData(String cityCode, String type, final GetDataListener listener) {
        Map<String, String> params = new HashMap<>();
        params.put("cityCode", cityCode);
        params.put("type", type);
        addDisposable(MyHttpClient.postByTimeout(UrlPath.URL_GET_TERMINAL_CHECK_REPORT, null, params, 60, new RequestCallBack<TerminalCheckBean>() {
            @Override
            public void onFailed(String failReason) {
                listener.failed(failReason);
            }

            @Override
            public void onSuccess(TerminalCheckBean result) {
                listener.success(result);
            }
        }));
    }

    public interface GetDataListener {
        void success(TerminalCheckBean bean);

        void failed(String failed);
    }
}
