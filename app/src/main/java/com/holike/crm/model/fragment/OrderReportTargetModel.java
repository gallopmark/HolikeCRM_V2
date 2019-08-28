package com.holike.crm.model.fragment;

import com.holike.crm.base.BaseModel;
import com.holike.crm.bean.OrderReportTargetBean;
import com.holike.crm.http.MyHttpClient;
import com.holike.crm.http.RequestCallBack;
import com.holike.crm.http.UrlPath;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wqj on 2018/5/30.
 * 订单交易报表-填写目标
 */

public class OrderReportTargetModel extends BaseModel {
    /**
     * 获取填写目标数据
     */
    public void getData(String time, final GetDataListener listener) {
        Map<String, String> params = new HashMap<>();
        params.put("time", time);
        addDisposable(MyHttpClient.postByTimeout(UrlPath.URL_WRITE_DEPOSIT_REPORT_TARGET, null, params, 60, new RequestCallBack<OrderReportTargetBean>() {
            @Override
            public void onFailed(String failReason) {
                listener.failed(failReason);
            }

            @Override
            public void onSuccess(OrderReportTargetBean result) {
                listener.success(result);
            }
        }));
    }

    public interface GetDataListener {
        void success(OrderReportTargetBean bean);

        void failed(String failed);
    }

    /**
     * 保存目标
     */
    public void saveTarget(String param, String time, final SaveTargetListener listener) {
        Map<String, String> params = new HashMap<>();
        params.put("param", param);
        params.put("time", time);
        addDisposable(MyHttpClient.postByCliId(UrlPath.URL_SAVE_DEPOSIT_REPORT_TARGET, null, params, new RequestCallBack<String>() {
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

    public interface SaveTargetListener {
        void success(String success);

        void failed(String failed);
    }
}

