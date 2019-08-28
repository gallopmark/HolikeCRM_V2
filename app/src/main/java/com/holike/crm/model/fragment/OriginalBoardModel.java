package com.holike.crm.model.fragment;

import com.holike.crm.base.BaseModel;
import com.holike.crm.bean.OriginalBoardBean;
import com.holike.crm.http.MyHttpClient;
import com.holike.crm.http.RequestCallBack;
import com.holike.crm.http.UrlPath;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wqj on 2018/5/25.
 * 原态板占比
 */

public class OriginalBoardModel extends BaseModel {

    /**
     * 获取原态板占比数据
     */
    public void getData(String cityCode, String startTime, String endTime, String time, String type, final getDataListener listener) {
        Map<String, String> params = new HashMap<>();
        params.put("cityCode", cityCode);
        params.put("startTime", startTime);
        params.put("endTime", endTime);
        params.put("time", time);
        params.put("type", type);
       addDisposable(MyHttpClient.postByTimeout(UrlPath.URL_GET_ORIGNAL_BOARD_REPORT, null, params, 60, new RequestCallBack<OriginalBoardBean>() {
            @Override
            public void onFailed(String failReason) {
                listener.failed(failReason);
            }

            @Override
            public void onSuccess(OriginalBoardBean result) {
                listener.success(result);
            }

        }));
    }

    public interface getDataListener {
        void success(OriginalBoardBean bean);

        void failed(String failed);
    }
}
