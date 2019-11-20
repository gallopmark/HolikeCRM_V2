package com.holike.crm.bean;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pony on 2019/11/4.
 * Version v3.0 app报表
 * 门店分析数据
 */
public class ShopAnalysisBean {
    public String time;
    public String isShop;

    public boolean isShop() {
        return TextUtils.equals(isShop, "1");
    }

    @SuppressWarnings("WeakerAccess")
    List<DataBean> arr2;

    public List<DataBean> getDatas() {
        return arr2 == null ? new ArrayList<>() : arr2;
    }

    public static class DataBean {
        public String area;
        public String type;
        public String cityCode;
        String isClick;
        public String scalesCount; //量尺数
        public String receiver; //回款
        public String newCount;//新建客户数
        public String storeCount; //门店数量
        public String dealerCount; //经销商数量
        public String earnestCountPercent; //定金转化率
        public String signingCountPercent; //进店成交率
        public String scalesCountPercent; //量尺成交率
        public String preScalesCount; //预约量尺数
        public String earnestCount; //订金数
        public String money; //业绩
        public String signingCount; //签约数
        public String name; //名字
        public String scalesChangePercent; //量尺转化率
        public String evaluate; //满意度
        public String isChange; //是否变色
        public String storeArea; //门店面积
        public String storeAge; //门店平均价值
        public String plateau; //坪效
        public String humanTotal; //总人效
        public String humanSales; //销售人效
        public String customerMoney; //客单价
        public String day; //平均成交周期

        public boolean isClickable() {
            return TextUtils.equals(isClick, "1");
        }
    }
}
