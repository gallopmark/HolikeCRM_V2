package com.holike.crm.bean;

import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pony on 2019/10/28.
 * Version v3.0 app报表
 */
public class BusinessTargetBean {

    @SuppressWarnings("WeakerAccess")
    List<UserBean> userList;
    @SuppressWarnings("WeakerAccess")
    List<UserTargetBean> userTarget;
    @SuppressWarnings("WeakerAccess")
    List<ShopTargetBean> shopTarget;
    @SuppressWarnings("WeakerAccess")
    List<ShopBean> shopList;

    String money; //目标
    public String dealerId; // 开始十位时间戳
    public String dealerNo; // 结束十位时间戳
    @SuppressWarnings("WeakerAccess")
    String isShop; //是可以设置经营目标
    @SuppressWarnings("WeakerAccess")
    String isPerson; //是否可以设置员工目标

    public String getMoney() {
        if (TextUtils.isEmpty(money)) return "";
        return money.replaceAll(",", "");
    }

    public boolean canSetShop() {
        return TextUtils.equals(isShop, "1");
    }

    public boolean canSetPerson() {
        return TextUtils.equals(isPerson, "1");
    }

    public List<UserBean> getUserList() {
        return userList == null ? new ArrayList<>() : userList;
    }

    public List<UserTargetBean> getUserTarget() {
        return userTarget == null ? new ArrayList<>() : userTarget;
    }

    public List<ShopBean> getShopList() {
        return shopList == null ? new ArrayList<>() : shopList;
    }

    public List<ShopTargetBean> getShopTarget() {
        return shopTarget == null ? new ArrayList<>() : shopTarget;
    }

    public static class UserBean {
        @SerializedName("shop_id")
        public String shopId;
        @SerializedName("shop_name")
        public String shopName;

        List<ArrBean> arr;

        public List<ArrBean> getArr() {
            return arr == null ? new ArrayList<>() : arr;
        }

        public static class ArrBean {
            @SerializedName("user_id")
            public String userId;
            @SerializedName("user_name")
            public String userName;
        }
    }

    public static class UserTargetBean {
        public String id;
        @SerializedName("shop_id")
        public String shopId;
        @SerializedName("shop_name")
        public String shopName;
        String money; //目标
        public String phone;
        public String crmOrderNo; //员工id
        public String name;//员工名字

        public String getMoney() {
            if (TextUtils.isEmpty(money)) return "";
            return money.replaceAll(",", "");
        }
    }

    public static class ShopBean {
        @SerializedName("shop_id")
        public String shopId;
        @SerializedName("shop_name")
        public String shopName;
    }

    public static class ShopTargetBean {
        public String id;
        String money; //目标
        public String crmOrderNo; //员工id
        public String name;//员工名字

        public String getMoney() {
            if (TextUtils.isEmpty(money)) return "";
            return money.replaceAll(",", "");
        }
    }
}
