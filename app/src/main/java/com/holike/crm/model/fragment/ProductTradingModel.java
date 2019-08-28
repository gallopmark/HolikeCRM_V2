package com.holike.crm.model.fragment;

import com.holike.crm.base.BaseModel;
import com.holike.crm.bean.ProductTradingBean;
import com.holike.crm.http.MyHttpClient;
import com.holike.crm.http.RequestCallBack;
import com.holike.crm.http.UrlPath;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wqj on 2018/5/9.
 * 成品交易报表
 */

public class ProductTradingModel extends BaseModel {

    /**
     * 获取报表数据
     */
    public void getData(String cityCode, String selectTime, String type, final GetDataListener listener) {
        Map<String, String> params = new HashMap<>();
        params.put("cityCode", cityCode);
        params.put("selectTime", selectTime);
        params.put("type", type);
       addDisposable(MyHttpClient.postByTimeout(UrlPath.URL_GET_PRODUCT_TRADING_REPORT, null, params, 60, new RequestCallBack<ProductTradingBean>() {
            @Override
            public void onFailed(String failReason) {
                listener.failed(failReason);
            }

            @Override
            public void onSuccess(ProductTradingBean result) {
                listener.success(result);
            }
        }));
    }

    public interface GetDataListener {
        void failed(String failed);

        void success(ProductTradingBean bean);
    }
}
