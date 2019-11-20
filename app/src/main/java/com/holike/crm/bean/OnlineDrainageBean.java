package com.holike.crm.bean;

import android.text.TextUtils;

import com.holike.crm.util.ParseUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pony on 2019/11/13.
 * Version v3.0 app报表
 * 线上引流数据
 */
public class OnlineDrainageBean {
    String isDealer;  //是否是经销商页面
    public String timeData; //时间描述
    public String scaleCount; //量尺数
    public String customerYesPercent; //有效客户转化率
    public String earnestCount; //订金数
    public String signingCount; //签约数
    public String signingCountPercent; //分配转化率
    public String customerTotal; //分配数
    public String customerYes; //有效客户数
    @SuppressWarnings("WeakerAccess")
    String isPrice; //是否展示 下单数、下单转化率、下单金额

    List<PercentDataBean> percentData;

    public boolean isDealer() {
        return TextUtils.equals(isDealer, "1");
    }

    public boolean isPrice() {
        return TextUtils.equals(isPrice, "1");
    }

    public long getScaleCount() {
        return ParseUtils.parseLong(scaleCount);
    }

    public long getEarnestCount() {
        return ParseUtils.parseLong(earnestCount);
    }

    public long getSigningCount() {
        return ParseUtils.parseLong(signingCount);
    }

    public long getCustomerYes() {
        return ParseUtils.parseLong(customerYes);
    }

    public long getCustomerTotal() {
        return ParseUtils.parseLong(customerTotal);
    }

    public List<PercentDataBean> getPercentData() {
        return percentData == null ? new ArrayList<>() : percentData;
    }

    public static class PercentDataBean {
        public String area; //地区
        public String scaleCount; //量尺数
        public String orderPercent; //订单转化率
        public String orderMoney; //订单金额
        public String cityCode;
        public String scaleCountPercent; //量尺率
        public String customerYes; //有效客户数
        public String type;
        public String orderCounts; //订单数
        public String customerNoPercent; //无效率
        String isClick;
        public String earnestPercent; //订金转化率
        public String earnestCount; //订金数
        public String earnestMoney; //订金金额
        public String signingCount; //签约数
        public String signingMoney; //签约金额
        public String signingCountPercent; //合同转化率
        public String name; //名字
        public String customerTotal; //下发客户数
        public String customerNo; //无效客户数
        String isChange; //是否变色

        public boolean isClickable() {
            return TextUtils.equals(isClick, "1");
        }

        public boolean isChange() {
            return TextUtils.equals(isChange, "1");
        }
    }
}
