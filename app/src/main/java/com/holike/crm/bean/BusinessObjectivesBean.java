package com.holike.crm.bean;

import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;
import com.holike.crm.util.ParseUtils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.annotations.Nullable;

/**
 * Created by pony on 2019/10/30.
 * Version v3.0 app报表
 * 经营目标数据
 */
public class BusinessObjectivesBean {
    public String type; //1 展示经营目标 2展示个人目标
    @SuppressWarnings("WeakerAccess")
    String isShop; //是否展示门店业绩
    @SuppressWarnings("WeakerAccess")
    String isPerson; //是否暂时个人业绩
    public String time;
    public String moneyTotal; //总目标
    public String countTotal; //总业绩
    String percent; //总百分比
    @SuppressWarnings("WeakerAccess")
    List<ShopDataBean> shopData;
    @SuppressWarnings("WeakerAccess")
    List<UserDataBean> userData;
    @SuppressWarnings("WeakerAccess")
    List<ShopSelectBean> shopSelect;
    @SuppressWarnings("WeakerAccess")
    PersonDataBean personData;

    public int getMoneyTotal() {
        if (TextUtils.isEmpty(moneyTotal)) return 100;
        return (int) ParseUtils.parseDouble(moneyTotal.replace(",", ""));
    }

    public int getCountTotal() {
        if (TextUtils.isEmpty(countTotal)) return 0;
        return (int) ParseUtils.parseDouble(countTotal.replace(",", ""));
    }

    public String getPercent() {
        if (TextUtils.isEmpty(percent)) return "0.0%";
        return percent + "%";
    }

    public boolean isShop() {
        return TextUtils.equals(isShop, "1");
    }

    public boolean isPerson() {
        return TextUtils.equals(isPerson, "1");
    }

    public List<ShopDataBean> getShopData() {
        return shopData == null ? new ArrayList<>() : shopData;
    }

    public List<UserDataBean> getUserData() {
        return userData == null ? new ArrayList<>() : userData;
    }

    public List<ShopSelectBean> getShopSelect() {
        return shopSelect == null ? new ArrayList<>() : shopSelect;
    }

    @Nullable
    public PersonDataBean getPersonData() {
        return personData;
    }

    public static class ShopDataBean {
        @SerializedName("shop_name")
        public String shopName;
        public String money; //目标
        public String count; //业绩
        public String percent; //百分比
    }

    public static class UserDataBean {
        public String name;
        public String scaleCount; //量尺数
        public String newCountTarget; //新建客户数目标
        public String newCount; //新建客户数
        public String receiver; //回款
        public String prescaleCount; //预约量尺数
        public String scaleCountPercent; //量尺数百分比
        public String prescaleCountTarget; //预约量尺数目标
        public String contractTotal; //成交额
        public String receiverTarget; //回款目标
        public String orderTotal; //下单额
        public String scaleCountTarget; //量尺目标
        public String newCountPercent; //新建客户数百分比
        public String contractTotalPercent; //成交额百分比
        public String receiverPercent; //回款百分比
        public String orderTotalTarget; //下单额目标
        public String orderTotalPercent; //下单额百分比
        public String contractTotalTarget; //成交额目标
        public String prescaleCountPercent; //预约量尺数百分比
        public String picCount; //出图数
        public String picCountTarget; //出图目标
        public String picCountTargetPercent; //出图百分比
    }

    public static class ShopSelectBean {
        @SerializedName("shop_id")
        public String shopId;
        @SerializedName("shop_name")
        public String shopName;
    }

    public static class PersonDataBean {
        public String newCount;  //新建客户数
        public String newCountTarget;  //新建客户数目标
        String newCountPercent;  //新建客户数百分比
        public String receiver;  //回款
        public String receiverTarget;  //回款目标
        String receiverPercent;  //回款百分比
        public String contractTotal;  //成交额
        public String contractTotalTarget;  //成交额目标
        String contractTotalPercent;  //成交额百分比
        public String orderTotal;  //下单额
        public String orderTotalTarget;  //下单额目标
        String orderTotalPercent;  //下单额百分比
        public String prescaleCount;  //预约量尺数
        public String prescaleCountTarget;  //预约量尺数目标
        String prescaleCountPercent;  //预约量尺数百分比
        public String scaleCount;  //量尺数
        public String scaleCountTarget;  //量尺数目标
        String scaleCountPercent;  //量尺数百分比
        public String picCount;  //出图数
        public String picCountTarget;  //出图数目标
        String picCountPercent;  //出图数百分比
        String isShow;  //是否要展示数据页面

        public boolean isShow() {
            return TextUtils.equals(isShow, "1");
        }

        public int getContractTotalTarget() {
            return getProgressValue(contractTotalTarget, 100);
        }

        public int getContractTotal() {
            return getProgressValue(contractTotal, 0);
        }

        public String getContractTotalPercent() {
            return getPercent(contractTotalPercent);
        }

        public int getNewCountTarget() {
            return getProgressValue(newCountTarget, 100);
        }

        public int getNewCount() {
            return getProgressValue(newCount, 0);
        }

        public String getNewCountPercent() {
            return getPercent(newCountPercent);
        }

        public String getPrescaleCountPercent() {
            return getPercent(prescaleCountPercent);
        }

        public int getPrescaleCountTarget() {
            return getProgressValue(prescaleCountTarget, 100);
        }

        public int getPrescaleCount() {
            return getProgressValue(prescaleCount, 0);
        }

        public String getScaleCountPercent() {
            return getPercent(scaleCountPercent);
        }

        public int getScaleCountTarget() {
            return getProgressValue(scaleCountTarget, 100);
        }

        public int getScaleCount() {
            return getProgressValue(scaleCount, 0);
        }

        public String getReceiverPercent() {
            return getPercent(receiverPercent);
        }

        public int getReceiverTarget() {
            return getProgressValue(receiverTarget, 100);
        }

        public int getReceiver() {
            return getProgressValue(receiver, 0);
        }

        public String getPicCountPercent() {
            return getPercent(picCountPercent);
        }

        public int getPicCountTarget() {
            return getProgressValue(picCountTarget, 100);
        }

        public int getPicCount() {
            return getProgressValue(picCount, 0);
        }

        public String getOrderTotalPercent() {
            return getPercent(orderTotalPercent);
        }

        public int getOrderTotalTarget() {
            return getProgressValue(orderTotalTarget, 100);
        }

        public int getOrderTotal() {
            return getProgressValue(orderTotal, 100);
        }

        private String getPercent(String source) {
            if (TextUtils.isEmpty(source)) return "0.0%";
            if (!source.contains("%")) return source + "%";
            return source;
        }

        private int getProgressValue(String source, int defaultValue) {
            if (TextUtils.isEmpty(source)) return defaultValue;
            return (int) ParseUtils.parseDouble(source.replace(",", ""));
        }
    }

}
