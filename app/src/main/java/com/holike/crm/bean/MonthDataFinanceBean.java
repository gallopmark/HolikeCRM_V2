package com.holike.crm.bean;

import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gallop on 2019/8/9.
 * Copyright holike possess 2019.
 */
public class MonthDataFinanceBean {
    public String timeData;  //时间描述
    public String shopName; //门店名称
    public String userName; //用户名称
    public String isShop; //1门店数据 2门店人员收款数据
    List<ArrBean> arr;
    List<ArrBean2> arr2;

    public List<ArrBean> getArr() {
        return arr == null ? new ArrayList<>() : arr;
    }

    public List<ArrBean2> getArr2() {
        return arr2 == null ? new ArrayList<>() : arr2;
    }

    public static class ArrBean {
        public String area;
        public String tail; //尾款
        public String cityCode;
        public String contract; //合同款
        public String installed;
        public String deposit; //定金
        public String receivables; //已收款
        public String contractTotal; //成交总金额
        public String type;
        public String name;
        String isClick;
        String isClick2;  //收款是否可以点击

        public boolean isClickable() {
            return TextUtils.equals(isClick, "1");
        }

        public boolean isClickable2() {
            return TextUtils.equals(isClick2, "1");
        }
    }

    public static class ArrBean2 {
        public String money;    //收款金额
        public String name; //名字
        public String type; //收款类别
        @SerializedName("pay_time")
        public String payTime;  //收款时间
    }
}
