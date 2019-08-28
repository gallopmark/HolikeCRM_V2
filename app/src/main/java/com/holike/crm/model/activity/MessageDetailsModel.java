package com.holike.crm.model.activity;

import com.holike.crm.base.BaseModel;
import com.holike.crm.bean.MessageDetailsBean;
import com.holike.crm.http.MyHttpClient;
import com.holike.crm.http.RequestCallBack;
import com.holike.crm.http.UrlPath;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wqj on 2018/2/25.
 * 消息详情
 */

public class MessageDetailsModel extends BaseModel {

    /**
     * 获取消息详情
     */
    public void getMessageDetails(String messageId, final GetMessageDetailsListener listener) {
        Map<String, String> params = new HashMap<>();
        params.put("messageId", messageId);
        addDisposable(MyHttpClient.postByCliId(UrlPath.URL_GET_MESSAGE_INFO, null, params, new RequestCallBack<MessageDetailsBean>() {
            @Override
            public void onFailed(String failReason) {
                listener.failed(failReason);
            }

            @Override
            public void onSuccess(MessageDetailsBean result) {
                listener.success(result);
            }
        }));
    }

    public interface GetMessageDetailsListener {
        void success(MessageDetailsBean messageDetailsBean);

        void failed(String failed);
    }
}
