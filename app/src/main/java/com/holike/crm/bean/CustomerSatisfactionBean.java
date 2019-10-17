package com.holike.crm.bean;

import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by gallop on 2019/9/20.
 * Copyright holike possess 2019.
 * 客户满意度
 */
public class CustomerSatisfactionBean {

    List<SelectDataBean> selectData; //正常数据
    @SuppressWarnings("WeakerAccess")
    List<ShopDataBean> shopData; //门店数据
    public String time; //时间描述
    @SuppressWarnings("WeakerAccess")
    String isShop; //是否是门店数据

    public boolean isShop() {
        return TextUtils.equals(isShop, "1");
    }

    public List<SelectDataBean> getSelectData() {
        return selectData == null ? new ArrayList<>() : selectData;
    }

    public List<ShopDataBean> getShopData() {
        return shopData == null ? new ArrayList<>() : shopData;
    }

    public static class SelectDataBean {
        public String area; //地区
        public String delivery; // 配送
        public String cityCode; //城市代码

        public String type; //类型
        String isClick; //是否可以点击
        public String install; //安装
        public String service; //服务
        public String design; //设计
        public String name; //负责人
        public String isChange; //是否变色

        public boolean isClickable() {
            return TextUtils.equals(isClick, "1");
        }

        public boolean isChange() {
            return TextUtils.equals(isChange, "1");
        }
    }

    public static class ShopDataBean {
        public String orderId;
        public String delivery;
        public String install;
        public String service;
        public String design; //设计
        @SerializedName("phone_number")
        public String phoneNumber;
        public String time;//下单时间
        public String content;
        String image; //图片或视频链接用@隔开
        @SerializedName("url")
        List<UrlBean> urlList;

        public List<String> getImages() {
            if (TextUtils.isEmpty(image)) {
                return new ArrayList<>();
            }
            try {
                String[] images = image.split("@");
                return Arrays.asList(images);
            } catch (Exception e) {
                return new ArrayList<>();
            }
        }

        @NonNull
        public List<UrlBean> getUrlList() {
            return urlList == null ? new ArrayList<>() : urlList;
        }

        public static class UrlBean {
            String type;
            public String url; //图片链接
            String image; //视频缩略图

            public boolean isMp4() {
                return TextUtils.equals(type, "mp4");
            }

            public String getShowUrl() {
                if (isMp4()) {  //如果是视频格式，则展示缩略图
                    return image;
                } else {
                    return url;
                }
            }
        }
    }
}
