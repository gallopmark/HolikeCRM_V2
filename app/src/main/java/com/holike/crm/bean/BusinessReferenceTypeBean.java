package com.holike.crm.bean;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pony on 2019/11/4.
 * Version v3.0 app报表
 * 生意内参-花色、系列、宅配数据
 */
public class BusinessReferenceTypeBean {
    public String dateToDate;
    @SuppressWarnings("WeakerAccess")
    List<String> monList;
    @SuppressWarnings("WeakerAccess")
    List<ShopBean> shopList;
    @SuppressWarnings("WeakerAccess")
    List<DimensionOfCliBean> dimensionOfCliList;
    @SuppressWarnings("WeakerAccess")
    List<DataBean> resultList;

    public List<String> getMonList() {
        return monList == null ? new ArrayList<>() : monList;
    }

    public List<ShopBean> getShopList() {
        return shopList == null ? new ArrayList<>() : shopList;
    }

    public List<DimensionOfCliBean> getDimensionOfCliList() {
        return dimensionOfCliList == null ? new ArrayList<>() : dimensionOfCliList;
    }

    public List<DataBean> getResultList() {
        return resultList == null ? new ArrayList<>() : resultList;
    }

    /*门店数据*/
    public static class ShopBean {
        @SerializedName("shop_code")
        public String shopCode;
        @SerializedName("shop_name")
        public String shopName;
    }

    public static class DimensionOfCliBean {
        public String type;
        public String name;
    }

    public static class DataBean {
        public String performance; //业绩
        public String proportion; //占比
        public String name; //名称
        public String ranking; //排名
    }
}
