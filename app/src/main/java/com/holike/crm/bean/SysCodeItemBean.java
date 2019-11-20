package com.holike.crm.bean;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by pony on 2019/7/19.
 * Copyright holike possess 2019.
 * 业务字典
 */
@SuppressWarnings("all")
public class SysCodeItemBean {
    @Nullable
    @SerializedName("dic-CUSTOMER_BUDGET_TYPE")
    private Map<String, String> customerBudgetType; //预算
    @Nullable
    @SerializedName("dic-CUSTOMER_GENDER_CODE")
    private Map<String, String> customerGenderCode; //性别
    @Nullable
    @SerializedName("dic-CUSTOMER_AGE_TYPE")
    private Map<String, String> customerAgeType; //年龄段

    @Nullable
    @SerializedName("dic-CUSTOMER_SOURCE_CODE")
    private Map<String, String> customerSourceCode; //客户来源
    @Nullable
    @SerializedName("dic-CUSTOMER_EARNESTHOUSE_TYPE")
    private Map<String, String> customerEarnestHouse; //定制品类
    @Nullable
    @SerializedName("dic-CUSTOMER_STATUS_MOVE")
    private Map<String, String> customerStatusMove; //房屋当前状态
    @Nullable
    @SerializedName("dic-HOUSE_INSTALL_STATE")
    private Map<String, String> houseInstallState; //房屋安装状态
    @Nullable
    @SerializedName("dic-INSTALL_FEEDBACK_STATE")
    private Map<String, String> installFeedbackState; //安装反馈状态
    //    @Nullable
//    @SerializedName("dic-DIGITAL_MARKETING_CUSTOMER_CUSTOM_MADE")
//    private Map<String, String> customerMeasureSpace; //用不到
    @Nullable
    @SerializedName("dic-CUSTOMER_AREA_TYPE")
    private Map<String, String> customerAreaType; //安装面积
    @Nullable
    @SerializedName("dic-CUSTOMER_HOUSE_TYPE")
    private Map<String, String> customerHouseType; //户型
    @Nullable
    @SerializedName("dic-FLAMILY_MEMBER")
    private Map<String, String> familyMember; //家庭成员
    @Nullable
    @SerializedName("dic-CUSTOMER_HOUSING_PRICE_TYPE")
    private Map<String, String> housePriceType; // 房价
    @Nullable
    @SerializedName("dic-DIGITAL_MARKETING_CUSTOMER_FURNITURE_DEMAND")
    private Map<String, String> furnitureDemand; //家具需求
    @Nullable
    @SerializedName("dic-DIGITAL_MARKETING_CUSTOMER_DECORATION_STYLE")
    private Map<String, String> decorationStyle; //装修风格
    @Nullable
    @SerializedName("dic-CUSTOMER_DECORATE_PROPERTIES")
    private Map<String, String> houseStatus; //房屋状态
    @Nullable
    @SerializedName("dic-CUSTOMER_DECORATE_PROGRESS")
    private Map<String, String> decorateProgress; //装修进度
    @Nullable
    @SerializedName("dic-CUSTOMER_PRODUCT")
    private Map<String, String> customerProduct; //产品
    @Nullable
    @SerializedName("dic-WHOLE_HOUSE_PRODUCT_SERIES")
    private Map<String, String> houseSeries; //全屋定制系列
    @Nullable
    @SerializedName("dic-CUPBOARD_PRODUCT_SERIES")
    private Map<String, String> cupboardSeries; //橱柜系列
    @Nullable
    @SerializedName("dic-DOOR_PRODUCT_SERIES")
    private Map<String, String> doorSeries; //木门系列
    @Nullable
    @SerializedName("dic-LOSE_OF_THE_REASON")
    private Map<String, String> loseReason; //流失原因
    @Nullable
    @SerializedName("dic-LOSE_OF_THE_BRAND")
    private Map<String, String> customerBrand; //客户去向
    @Nullable
    @SerializedName("dic-PARTNER_SHOP_TYPE")
    private Map<String, String> partnerShopType; //店面类型,门店渠道类型
    @Nullable
    @SerializedName("dic-REVIEW_HOUSE_PLAN")
    private Map<String, String> reviewHousePlan; //主管查房结果
    @Nullable
    @SerializedName("dic-HIGH_SEAS_TYPE")
    private Map<String, String> highSeasType; //公海客户分类
    @Nullable
    @SerializedName("dic-INTENTION_LEVEL")
    private Map<String, String> intentionLevel; //意向评级

    @Nullable
    @SerializedName("dic-PAY_TYPE")
    private Map<String, String> paymentType; //付款类型

    @SerializedName("dic-CUSTOMIZATION_SPACE")
    private Map<String, String> customizationSpace; //量尺空间

    @SerializedName("dic-DIGITAL_INVALIDITY_RESON")
    private Map<String, String> invalidReturn; //无效退回原因字典

    @NonNull
    public Map<String, String> getCupboardSeries() {
        return nonNullWrap(cupboardSeries);
    }

    @NonNull
    public Map<String, String> getCustomerEarnestHouse() {
        return nonNullWrap(customerEarnestHouse);
    }

    @NonNull
    public Map<String, String> getCustomerGenderCode() {
        return nonNullWrap(customerGenderCode);
    }

    @NonNull
    public Map<String, String> getCustomerBudgetType() {
        return nonNullWrap(customerBudgetType);
    }

    @NonNull
    public Map<String, String> getCustomerSourceCode() {
        return nonNullWrap(customerSourceCode);
    }

    @NonNull
    public Map<String, String> getCustomerAgeType() {
        return nonNullWrap(customerAgeType);
    }

    @NonNull
    public Map<String, String> getCustomerStatusMove() {
        return nonNullWrap(customerStatusMove);
    }

    @NonNull
    public Map<String, String> getHouseInstallState() {
        return nonNullWrap(houseInstallState);
    }

    @NonNull
    public Map<String, String> getInstallFeedbackState() {
        return nonNullWrap(installFeedbackState);
    }

    /*量尺空间*/
    @NonNull
    public Map<String, String> getCustomerMeasureSpace() {
        return nonNullWrap(customizationSpace);
    }

    @NonNull
    public Map<String, String> getCustomerAreaType() {
        return nonNullWrap(customerAreaType);
    }

    @NonNull
    public Map<String, String> getCustomerHouseType() {
        return nonNullWrap(customerHouseType);
    }

    @NonNull
    public Map<String, String> getFamilyMember() {
        return nonNullWrap(familyMember);
    }

    @NonNull
    public Map<String, String> getHousePriceType() {
        return nonNullWrap(housePriceType);
    }

    @NonNull
    public Map<String, String> getFurnitureDemand() {
        return nonNullWrap(furnitureDemand);
    }

    @NonNull
    public Map<String, String> getDecorationStyle() {
        return nonNullWrap(decorationStyle);
    }

    @NonNull
    public Map<String, String> getHouseStatus() {
        return nonNullWrap(houseStatus);
    }

    @NonNull
    public Map<String, String> getDecorateProgress() {
        return nonNullWrap(decorateProgress);
    }

    @NonNull
    public Map<String, String> getCustomerProduct() {
        return nonNullWrap(customerProduct);
    }

    @NonNull
    public Map<String, String> getHouseSeries() {
        return nonNullWrap(houseSeries);
    }

    @NonNull
    public Map<String, String> getDoorSeries() {
        return nonNullWrap(doorSeries);
    }

    @NonNull
    public Map<String, String> getLoseReason() {
        return nonNullWrap(loseReason);
    }

    @Deprecated
    @NonNull
    public Map<String, String> getCustomerBrand() {
        return nonNullWrap(customerBrand);
    }

    @NonNull
    public Map<String, String> getPartnerShopType() {
        return nonNullWrap(partnerShopType);
    }

    @NonNull
    public Map<String, String> getReviewHousePlan() {
        return nonNullWrap(reviewHousePlan);
    }

    @NonNull
    public Map<String, String> getHighSeasType() {
        return nonNullWrap(highSeasType);
    }

    @NonNull
    public Map<String, String> getIntentionLevel() {
        return nonNullWrap(intentionLevel);
    }

    @NonNull
    public Map<String, String> getPaymentType() {
        return nonNullWrap(paymentType);
    }

    @NonNull
    public Map<String, String> getCustomizationSpace() {
        return nonNullWrap(customizationSpace);
    }

    @NonNull
    public Map<String, String> getInvalidReturn() {
        return nonNullWrap(invalidReturn);
    }

    private Map<String, String> nonNullWrap(@Nullable Map<String, String> origin) {
        if (origin == null) return new HashMap<>();
        return origin;
    }
}
