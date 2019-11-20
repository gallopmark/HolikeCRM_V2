package com.holike.crm.bean;

import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pony on 2019/11/2.
 * Version v3.0 app报表
 * 家装渠道-营销人员数据
 */
public class HomeDollBean {

    public String dateToDate;
    @SuppressWarnings("WeakerAccess")
    List<DataBean> resultList;

    public List<DataBean> getDatas() {
        return resultList == null ? new ArrayList<>() : resultList;
    }

    public static class DataBean {
        public String division; //划分
        public String principal; //负责人
        public String type;
        @SerializedName("for_year")
        public String forYear; //全年
        @SerializedName("for_month")
        public String forMonth; //本月
        public String cityCode; //
        public String isClick;
        //以下为经销商数据  其中全年、本月字段公用
        @SerializedName("shop_code")
        public String shopCode;
        @SerializedName("shop_name")
        public String shopName;
        @SerializedName("shop_principal")
        public String shopPrincipal;

        public boolean isClickable() {
            return TextUtils.equals(isClick, "1");
        }
    }
}
