package com.holike.crm.bean;

import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;
import com.holike.crm.util.ParseUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pony on 2019/11/6.
 * Version v3.0 app报表
 * 客户转化数据
 */
public class CustomerConversionBean {
    public String scaleCount; //量尺数
    public String ordersCustomer; //订金客户数
    public String timeDetail; //时间描述
    public String newCount; //新建客户数
    public String contractCount; //签约数
    public String ordersCustomerPercent; //订金转化率
    public String prescaleCount; //预约量尺数
    public String scaleCountPercent; //量尺转化率
    public String entryPercent; //进店成交率
    public String scalePercent;  //量尺成交率
    @SuppressWarnings("WeakerAccess")
    List<SelectShopBean> selectShop;

    public long getScaleCount() {
        return getCount(scaleCount);
    }

    public long getOrdersCustomer() {
        return getCount(ordersCustomer);
    }

    public long getNewCount() {
        return getCount(newCount);
    }

    public long getContractCount() {
        return getCount(contractCount);
    }

    public long getPreScaleCount() {
        return getCount(prescaleCount);
    }

    private long getCount(String source) {
        if (TextUtils.isEmpty(source)) return 0;
        return ParseUtils.parseLong(source.replaceAll(",", ""));
    }

    public List<SelectShopBean> getSelectShop() {
        return selectShop == null ? new ArrayList<>() : selectShop;
    }

    public static class SelectShopBean {
        @SerializedName("status_code")
        public String statusCode;
        @SerializedName("shop_id")
        public String shopId;
        @SerializedName("shop_name")
        public String shopName;
    }
}
