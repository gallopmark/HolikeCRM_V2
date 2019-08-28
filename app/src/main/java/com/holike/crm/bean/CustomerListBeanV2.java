package com.holike.crm.bean;

import android.text.TextUtils;

import androidx.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by gallop on 2019/8/2.
 * Copyright holike possess 2019.
 */
public class CustomerListBeanV2 {
    public String total;
    List<CustomerBean> list;
    private String customerStatusId; //默认选择哪些状态 (当前选中的客户状态 多个用@隔开)

    public List<CustomerBean> getList() {
        return list == null ? new ArrayList<>() : list;
    }

    public List<String> getCustomerStatusId() {
        if (TextUtils.isEmpty(customerStatusId))
            return new ArrayList<>();
        try {
            String[] array = customerStatusId.split("@");
            return Arrays.asList(array);
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    public class CustomerBean implements MultiItem {
        @SerializedName("personal_id")
        public String personalId;
        public String detailsAddress;  //省市区
        public String address;
        @SerializedName("earnest_house")
        public String deposit; //订金
        @SerializedName("last_follow_time")
        public String lastFollowTime; //最新跟进时间
        @SerializedName("user_name")
        public String userName; // 姓名
        @SerializedName("phone_number")
        public String phoneNumber; //电话
        public String source; //来源
        @SerializedName("create_date")
        public String createDate; //创建时间
        public String status; //状态
        @SerializedName("intention_level")
        public String intentionLevel; //意向评级
        public String type; //1显示手机号 2显示微信号
        @SerializedName("shop_id")
        public String shopId;
        @SerializedName("house_id")
        public String houseId;
        public String site;
        public String digitalRemark;
        public String dealerId;
        @SerializedName("wx_number")
        public String wxNumber;  //微信号
        public String remark;

        public boolean isPhoneNumber() {
            return TextUtils.equals(type, "1");
        }

        @Override
        public int getItemType() {
            return 1;
        }
    }
}
