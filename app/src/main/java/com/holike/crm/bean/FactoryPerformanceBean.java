package com.holike.crm.bean;

import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pony on 2019/11/6.
 * Version v3.0 app报表
 * 出产业绩数据
 */
public class FactoryPerformanceBean {
    public String dateToDate;
    public String month;
    public String year;
    public String dimensionOf;  //维度, 1:品类(默认) 2:空间 3:客户渠道
    @SuppressWarnings("WeakerAccess")
    List<String> monList;  //可选月份
    @SuppressWarnings("WeakerAccess")
    List<DimensionOfBean> dimensionOfList;
    @SuppressWarnings("WeakerAccess")
    List<CategoryDataBean> categoryData;
    @SuppressWarnings("WeakerAccess")
    List<SpaceDataBean> spaceData;
    @SuppressWarnings("WeakerAccess")
    List<ChannelDataBean> channelData;

    public List<String> getMonList() {
        return monList == null ? new ArrayList<>() : monList;
    }

    public List<DimensionOfBean> getDimensionOfList() {
        return dimensionOfList == null ? new ArrayList<>() : dimensionOfList;
    }

    public List<CategoryDataBean> getCategoryData() {
        return categoryData == null ? new ArrayList<>() : categoryData;
    }

    public List<SpaceDataBean> getSpaceData() {
        return spaceData == null ? new ArrayList<>() : spaceData;
    }

    public List<ChannelDataBean> getChannelData() {
        return channelData == null ? new ArrayList<>() : channelData;
    }

    private static String getShowText(String source) {
        if (TextUtils.isEmpty(source)) return "";
        String text = source;
        if (source.contains("(")) {
            int index = source.indexOf("(");
            text = source.substring(0, index) + "\n";
            text += source.substring(index);
        } else if (source.contains("（")) {
            int index = source.indexOf("（");
            text = source.substring(0, index) + "\n";
            text += source.substring(index);
        }
        return text;
    }

    /*品类数据*/
    public static class CategoryDataBean {
        public String total;
        @SerializedName("shop_code")
        public String shopCode;
        @SerializedName("wooden_door")
        public String woodenDoor; //木门
        public String finished; //成品
        @SerializedName("shop_name")
        public String shopName; //店铺名称
        public String customize; //定制
        public String cupboard; //橱柜
        public String curtain; //窗帘
        public String bigHouse;//大家居

        public String getShowCustomize() {
            return getShowText(customize);
        }

        public String getShowCupboard() {
            return getShowText(cupboard);
        }

        public String getShowWoodenDoor() {
            return getShowText(woodenDoor);
        }

        public String getShowFinished() {
            return getShowText(finished);
        }

        public String getShowCurtain() {
            return getShowText(curtain);
        }

        public String getShowBigHouse() {
            return getShowText(bigHouse);
        }


    }

    /*空间数据*/
    public static class SpaceDataBean {
        public String study; //书房
        public String other; //其他
        @SerializedName("shop_code")
        public String shopCode; //店铺编码
        public String restaurant; //餐厅
        @SerializedName("master_bedroom")
        public String masterBedroom; //主卧
        public String hall; //门厅
        public String elder; //长辈房
        @SerializedName("shop_name")
        public String shopName; //店铺名称
        public String balcony; //阳台
        public String multifunction; //多功能房
        @SerializedName("guest_bedroom")
        public String guestBedroom; //客卧
        public String total; //
        public String kitchen; //厨房
        public String child; //儿童房

        public String getShowRestaurant() {
            return getShowText(restaurant);
        }

        public String getShowHall() {
            return getShowText(hall);
        }

        public String getShowBalcony() {
            return getShowText(balcony);
        }

        public String getShowMasterBedroom() {
            return getShowText(masterBedroom);
        }

        public String getShowGuestBedroom() {
            return getShowText(guestBedroom);
        }

        public String getShowChild() {
            return getShowText(child);
        }

        public String getShowElder() {
            return getShowText(elder);
        }

        public String getShowStudy() {
            return getShowText(study);
        }

        public String getShowMultifunction() {
            return getShowText(multifunction);
        }

        public String getShowKitchen() {
            return getShowText(kitchen);
        }

        public String getShowOther() {
            return getShowText(other);
        }
    }

    /*渠道数据*/
    public static class ChannelDataBean {
        @SerializedName("on_line")
        public String onLine; //总部线上引流
        public String total; //
        public String conventional; //常规
        @SerializedName("shop_code")
        public String shopCode;
        public String bag; //拎包
        @SerializedName("shop_name")
        public String shopName; //门店名称
        public String home; //家装

        public String getShowConventional() {
            return getShowText(conventional);
        }

        public String getShowOnLine() {
            return getShowText(onLine);
        }

        public String getShowBag() {
            return getShowText(bag);
        }

        public String getShowHome() {
            return getShowText(home);
        }
    }

    public static class DimensionOfBean {
        public String type;
        public String name;
    }
}
