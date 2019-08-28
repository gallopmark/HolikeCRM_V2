package com.holike.crm.model.activity;

import com.holike.crm.base.BaseModel;
import com.holike.crm.bean.FeedbackRecordBean;
import com.holike.crm.http.MyHttpClient;
import com.holike.crm.http.RequestCallBack;
import com.holike.crm.http.UrlPath;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wqj on 2018/7/17.
 * 反馈记录
 */

public class FeedbackRecordModel extends BaseModel {

    /**
     * 获取记录列表
     */
    public void getRecord(String pageNo, String pageSize, final GetRecordListener listener) {
        Map<String, String> params = new HashMap<>();
        params.put("pageNo", pageNo);
        params.put("pageSize", pageSize);
        addDisposable(MyHttpClient.postByCliId(UrlPath.URL_FEEDBACK_RECORD, null, params, new RequestCallBack<List<FeedbackRecordBean>>() {
            @Override
            public void onFailed(String failReason) {
                listener.failed(failReason);
            }

            @Override
            public void onSuccess(List<FeedbackRecordBean> result) {
                listener.success(result);
            }

        }));
    }

    public interface GetRecordListener {
        void success(List<FeedbackRecordBean> list);

        void failed(String failed);
    }
}
