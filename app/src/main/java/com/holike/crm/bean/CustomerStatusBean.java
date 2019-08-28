package com.holike.crm.bean;

import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gallop on 2019/8/5.
 * Copyright holike possess 2019.
 */
public class CustomerStatusBean {
    public String total;
    public String today;
    List<InnerBean> list;
    private List<CodeBean> customerStatusList; //客户状态
    @SerializedName("customerIntentionStstus")
    private List<CodeBean> customerIntentionStatus; // 意向客户评级

    @SerializedName("customerEarnestStstus")
    private List<CodeBean> customerEarnestStatus; //客户收定金状态

    @SerializedName("customerTailStstus")
    private List<CodeBean> customerTailStatus; //尾款情况

    @SerializedName("customerSeaStstus")
    private List<CodeBean> customerSeaStatus; //公海分类

    public long getTotal() {
        try {
            return Long.parseLong(total);
        } catch (Exception e) {
            return 0;
        }
    }

    @NonNull
    public List<InnerBean> getList() {
        return list == null ? new ArrayList<>() : list;
    }

    @NonNull
    public List<CodeBean> getCustomerStatusList() {
        return customerStatusList == null ? new ArrayList<>() : customerStatusList;
    }

    @NonNull
    public List<CodeBean> getCustomerIntentionStatus() {
        return customerIntentionStatus == null ? new ArrayList<>() : customerIntentionStatus;
    }

    @NonNull
    public List<CodeBean> getCustomerEarnestStatus() {
        return customerEarnestStatus == null ? new ArrayList<>() : customerEarnestStatus;
    }

    @NonNull
    public List<CodeBean> getCustomerTailStatus() {
        return customerTailStatus == null ? new ArrayList<>() : customerTailStatus;
    }

    @NonNull
    public List<CodeBean> getCustomerSeaStatus() {
        return customerSeaStatus == null ? new ArrayList<>() : customerSeaStatus;
    }

    public static class InnerBean implements MultiItem {
        @SerializedName("appointment_time")
        public String appointmentTime; //预约量尺日期
        @SerializedName("wx_number")
        public String wxNumber; //微信号
        @SerializedName("personal_id")
        public String personalId;   //客户id
        @SerializedName("house_id")
        public String houseId; //房屋id
        public String address; //地址
        @SerializedName("user_name")
        public String userName; //客户名
        @SerializedName("appoint_measure_by")
        public String appointMeasureBy; //量尺人员
        @SerializedName("customer_protect_time")
        public String customerProtectTime; //客户保护时间
        @SerializedName("phone_number")
        public String phoneNumber; //手机号
        public String source;  //客户来源
        public String type; //1显示手机号 2显示微信
        String isRed; //1记录标红 0不标红
        @SerializedName("amount_of_date")
        public String amountOfDate; //量房日期
        @SerializedName("measure_app_comfirm_time")
        public String measureAppConfirmTime; //确图日期
        @SerializedName("upload_plan_date")
        public String uploadPlanDate; //上传方案时间
        @SerializedName("status_code")
        public String statusCode; //客户状态
        @SerializedName("sales_amount")
        public String salesAmount; //签单金额
        @SerializedName("last_remaining")
        public String lastRemaining; //未收尾款
        @SerializedName("contract_date")
        public String contractDate;//签约时间
        @SerializedName("app_delivery_date")
        public String appDeliveryDate; //预约交货时间
        @SerializedName("appointment_to_install_date")
        public String appointmentInstallDate; //预约安装日期
        @SerializedName("install_user_name")
        public String installUserName; //安装工
        @SerializedName("order_date")
        public String orderDate; //下单日期
        @SerializedName("ending_install_date")
        public String endingInstallDate; //安装日期
        @SerializedName("high_seas_type")
        public String highSeasType; //公海分类
        @SerializedName("create_date")
        public String createDate; //创建时间
        @SerializedName("sales_name")
        public String salesName; //导购名字
        @SerializedName("next_follow_up_date")
        public String nextFollowupDate;//下次跟进时间
        @SerializedName("last_follow_time")
        public String lastFollowTime; //最新跟进时间
        @SerializedName("designer_id")
        public String designer; //设计师名字
        @SerializedName("intention_level")
        public String intentionLevel; //意向评级

        //是否是微信号
        public boolean isWxType() {
            return TextUtils.equals(type, "2");
        }

        public boolean isRed() {
            return TextUtils.equals(isRed, "1");
        }

        @Override
        public int getItemType() {
            return 1;
        }
    }

    public static class CodeBean {
        public String code;
        public String name;
        String isSelect;

        public boolean isSelected() {
            return TextUtils.equals(isSelect, "1"); //是否选中 1是 0不是
        }
    }
}
