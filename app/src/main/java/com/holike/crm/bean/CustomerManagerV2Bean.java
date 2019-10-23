package com.holike.crm.bean;


import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.annotations.SerializedName;
import com.holike.crm.base.IntentValue;
import com.holike.crm.bean.internal.SystemCodePattern;
import com.holike.crm.util.TimeUtil;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by gallop on 2019/7/16.
 * Copyright holike possess 2019.
 */
public class CustomerManagerV2Bean {
    private PersonalInfoBean personalInfo;
    private List<HouseInfoBean> houseInfoList;

    @Nullable
    public PersonalInfoBean getPersonalInfo() {
        return personalInfo;
    }

    @NonNull
    public List<HouseInfoBean> getHouseInfoList() {
        return houseInfoList == null ? new ArrayList<>() : houseInfoList;
    }

    /*客户信息 */
    public static class PersonalInfoBean {
        public String personalId;
        public String activityPolicy;
        public String ageType;
        public String createBy;
        String createDate;
        public String dealerId;
        public String gender;
        public String highSeasPersonFlag;
        public String intentionLevel;
        public String nextFollowTime;
        public String phoneNumber;
        public String recordStatus;
        public String shopId;
        public String source;
        public String updateBy;
        String updateDate;
        public String userName;
        public String userType;
        public String versionNumber;
        public String wxNumber;
        String isValidCustomer;

        public String getAgeType() {
            if (TextUtils.isEmpty(ageType)) return "";
            SysCodeItemBean bean = IntentValue.getInstance().getSystemCode();
            if (bean == null || !bean.getCustomerAgeType().containsKey(ageType)) return "";
            return bean.getCustomerAgeType().get(ageType);
        }

        public String getIntentionLevel() {
            if (TextUtils.isEmpty(intentionLevel)) return "";
            SysCodeItemBean bean = IntentValue.getInstance().getSystemCode();
            if (bean == null || !bean.getIntentionLevel().containsKey(intentionLevel)) return "";
            return bean.getIntentionLevel().get(intentionLevel);
        }

        public String getGender() {
            if (TextUtils.isEmpty(gender)) return "";
            SysCodeItemBean bean = IntentValue.getInstance().getSystemCode();
            if (bean == null || !bean.getCustomerGenderCode().containsKey(gender)) return "";
            return bean.getCustomerGenderCode().get(gender);
        }

        public String getSource() {
            if (TextUtils.isEmpty(source)) return "";
            SysCodeItemBean bean = IntentValue.getInstance().getSystemCode();
            if (bean == null || !bean.getCustomerSourceCode().containsKey(source)) return "";
            return bean.getCustomerSourceCode().get(source);
        }

        public String getCreateDate() {
            return TimeUtil.timeMillsFormat(createDate);
        }

        public String getNextFollowTime() {
            return TimeUtil.timeMillsFormat(nextFollowTime);
        }

        public String getUpdateDate() {
            return TimeUtil.timeMillsFormat(updateDate);
        }

        /*主要用于判断是否可以编辑手机号码*/
        public boolean isValidCustomer() {
            return TextUtils.equals(isValidCustomer, "Y") || TextUtils.equals(isValidCustomer, "y");
        }
    }

    /*房屋相关信息*/
    public static class HouseInfoBean {
        HouseDetailBean houseDetail;    //房屋详情
        List<OperateItemBean> mobileIcon;
        List<PhoneRecordBean> phoneRecord; //通话记录列表
        List<PaymentBean> paymentList; //收款
        List<MeasureResultImgBean> measureImgList;
        List<MessageBoardBean> messageBoard;    //留言板列表
        List<HistoryBean> historyList;  //历史记录列表
        List<InstalledInfoBean> finishInstallInfo;
        List<ContractImgBean> contractImgList; //合同登记图片集合
        List<InstallImgBean> installImg;//安装图纸列表
        List<GenerateOrderBean> orderList; //生成订单列表
        List<DesignRenderImgBean> designRenderImgList; //方案图片
        List<InstallUserBean> installUserInfo;
        InstallInfoBean installInfo;

        @Nullable
        public HouseDetailBean getHouseDetail() {
            return houseDetail;
        }

        @NonNull
        public List<OperateItemBean> getMobileIcon() {
            return mobileIcon == null ? new ArrayList<>() : mobileIcon;
        }

        @NonNull
        public List<PaymentBean> getPaymentList() {
            return paymentList == null ? new ArrayList<>() : paymentList;
        }

        @NonNull
        public List<MeasureResultImgBean> getMeasureImgList() {
            return measureImgList == null ? new ArrayList<>() : measureImgList;
        }

        @NonNull
        public List<MessageBoardBean> getMessageBoard() {
            return messageBoard == null ? new ArrayList<>() : messageBoard;
        }

        @NonNull
        public List<HistoryBean> getHistoryList() {
            return historyList == null ? new ArrayList<>() : historyList;
        }

        @NonNull
        public List<PhoneRecordBean> getPhoneRecord() {
            return phoneRecord == null ? new ArrayList<>() : phoneRecord;
        }

        @NonNull
        public List<ContractImgBean> getContractImgList() {
            return contractImgList == null ? new ArrayList<>() : contractImgList;
        }

        @NonNull
        public List<InstalledInfoBean> getFinishInstallInfo() {
            return finishInstallInfo == null ? new ArrayList<>() : finishInstallInfo;
        }

        @NonNull
        public List<InstallImgBean> getInstallImgList() {
            return installImg == null ? new ArrayList<>() : installImg;
        }

        @NonNull
        public List<GenerateOrderBean> getOrderList() {
            return orderList == null ? new ArrayList<>() : orderList;
        }

        @NonNull
        public List<DesignRenderImgBean> getDesignRenderImgList() {
            return designRenderImgList == null ? new ArrayList<>() : designRenderImgList;
        }

        @Nullable
        public InstallInfoBean getInstallInfo() {
            return installInfo;
        }

        @NonNull
        public List<InstallUserBean> getInstallUserInfo() {
            return installUserInfo == null ? new ArrayList<>() : installUserInfo;
        }
    }

    /*房屋信息*/
    public static class HouseDetailBean implements Serializable {
        public String houseId;
        public String address; //详细地址
        public String amountOfDate; //量房完成时间
        public String appDeliveryDate; //预约交货时间
        public String appointMeasureBy; //预约量尺人id
        public String appointMeasureByName; //预约量尺人名字
        public String appointMeasureSpace;
        public String appointShopId; //预约量尺门店id
        public String appointShopName;  //预约量尺门店名
        public String appointmentTime;  //预约量尺时间
        String appointmentToInstallDate;    //预约安装时间
        public String areaType; //房屋面积
        String assignDesignerTime; //分配设计师时间
        String assignPromoterTime; //分配业务员时间
        String assignSalesTime; //分配导购时间
        String contractFlag; //合同标识 是否合同登记
        public String bookOrderDate;   //上传方案预约确图时间
        public String budgetTypeCode; //定制预算
        public String cityCode; //城市代码
        public String cityName; //城市名称
        public String contractDate; //合同登记时间
        public String contracteByName; //合同登记人
        public String contractor; //合同登记人id
        public String createBy; //房屋创建人id
        String createDate; //房屋创建时间
        public String customizeTheSpace; //定制空间
        public String dealerId; //经销商id
        public String dealerName; //经销商名称
        public String decorateProgress; //装修进度
        public String decorateProperties; //房屋状态
        String depositTime; //交订金时间
        public String designerId;   //设计师id
        public String designerName; //设计师姓名
        public String designerShopId;   //设计师门店id
        public String designerShopName; //设计师门店名
        public String districtCode;
        public String districtName;
        public String earnestHouse; //定金
        String endingInstallDate;   //实际安装时间
        public String flamilyMember;    //家庭成员
        public String furnitureDemand;  //家具需求
        public String highSeasHouseFlag;    //公海房屋标识
        @SerializedName("highSeasHisytoryFlag")
        String highSeasHistoryFlag; //当前房屋是否存在历史记录
        public String housePriceTypeCode;   //每平米房价
        public String houseType; //户型
        String lastFollowTime;
        public String lastRemaining; //还剩尾款(欠款)
        public String leaveFlag; //房屋流失标识
        public String leaveReason; //流失原因
        public String leaveToSeries; //流失去向
        public String measureAppComfirmTime; //预约确图时间
        public String measureBudgetTypeCode; //量尺沟通预算
        public String measureShopId; //实际量尺门店
        public String measureShopName; //实际量尺门店名称
        public String measureBy; //量尺人id
        public String measureByName; //量尺人姓名
        public String payAmount; //总收款
        public String personalId;
        public String plannedStayDate; //计划入住时间
        public String preferenceStyle; //装修风格
        public String product;  //产品
        public String promoter; //业务员名字
        public String promoterId; //业务员id
        public String promoterShopId; //业务员门店id
        public String promoterShopName; //业务员门店名
        public String provinceCode; //省编码
        public String provinceName; //省名称
        public String recordStatus; //房屋记录状态
        public String remark;
        public String salesAmount; //成交金额
        public String salesId; //导购id
        public String salesName; //导购姓名
        public String series; //系列
        public String shopId; //导购门店id
        public String shopName; //导购门店名
        public String groupId;
        public String groupName;
        public String spareContact; //备用联系人
        public String spareContactPhone; //备用联系电话
        public String statusCode; //房屋当前状态
        public String style; //风格
        String uploadPlanDate; //上传方案时间
        public String updateBy; //房屋修改Id
        String updateDate;  //房屋修改时间
        public String validatePassed; //查房结果
        String validatePassedTime; // 查房时间
        public String versionNumber;    // 房屋版本号
        public String contractPayAmount; //合同付款金额
        @SerializedName("installFalg")
        public String installFlag; //是否安装完成(Y=是，N=否)

        /*是否是公海房屋*/
        public boolean isHighSeasHouse() {
            return TextUtils.equals(highSeasHouseFlag, "Y") || TextUtils.equals(highSeasHouseFlag, "y");
        }

        /*当前房屋状态*/
        public String getCurrentStatus() {
            return SystemCodePattern.getHouseStatus(statusCode);
        }

        /*通过字典匹配定制预算值*/
        public String getBudget() {
            return SystemCodePattern.getCustomBudget(budgetTypeCode);
        }

        /*装修风格*/
        public String getDecorateProgress() {
            return SystemCodePattern.getDecorateProgress(decorateProgress);
        }

        /*房屋状态*/
        public String getDecorateProperties() {
            return SystemCodePattern.getDecorateProperties(decorateProperties);
        }

        /*家庭成员*/
        public String getFamilyMember() {
            return SystemCodePattern.getFamilyMember(flamilyMember);
        }

        /*家具需求*/
        public String getFurnitureDemand() {
            return SystemCodePattern.getFurnitureDemand(furnitureDemand);
        }

        /*每平方米房价*/
        public String getHousePrice() {
            return SystemCodePattern.getHousePrice(housePriceTypeCode);
        }

        /*量尺沟通预算*/
        public String getMeasureBudget() {
            return SystemCodePattern.getMeasureBudget(measureBudgetTypeCode);
        }

        /*户型*/
        public String getHouseType() {
            return SystemCodePattern.getHouseType(houseType);
        }

        /*装修风格*/
        public String getPreferenceStyle() {
            return SystemCodePattern.getPreferenceStyle(preferenceStyle);
        }

        public String getAppointMeasureSpace() {
            return SystemCodePattern.getAppointMeasureSpace(appointMeasureSpace);
        }

        /*接口返回的是房屋面积字典code*/
        public String getHouseArea() {
            return SystemCodePattern.getHouseArea(areaType);
        }

        /*获取定制空间，通过字典匹配，接口返回的是定制空间的code，并以","隔开*/
        public String getCustomizeTheSpace() {
            return SystemCodePattern.getCustomSpace(customizeTheSpace);
        }

        /*系列*/
        public String getSeries() {
            return SystemCodePattern.getSeries(product, series);
        }

        /*产品*/
        public String getProduct() {
            return SystemCodePattern.getProduct(product);
        }

        public String getStyle() {
            return SystemCodePattern.getStyle(style);
        }

        /*查房结果*/
        public String getMeasureResult() {
            return SystemCodePattern.getMeasureResult(validatePassed);
        }

        public String getAmountOfDate() {
            return TimeUtil.timeMillsFormat(amountOfDate);
        }

        public String getAppDeliveryDate() {
            return TimeUtil.timeMillsFormat(appDeliveryDate);
        }

        public String getAppointmentTime() {
            return TimeUtil.timeMillsFormat(appointmentTime);
        }

        /*预约量尺 时分秒*/
        public String getAppointmentHM() {
            return TimeUtil.timeMillsFormat(appointmentTime, "HH:mm:ss");
        }

        public String getAppointmentToInstallDate() {
            return TimeUtil.timeMillsFormat(appointmentToInstallDate);
        }

        public String getAssignDesignerTime() {
            return TimeUtil.timeMillsFormat(assignDesignerTime);
        }

        public String getAssignPromoterTime() {
            return TimeUtil.timeMillsFormat(assignPromoterTime);
        }

        public String getAssignSalesTime() {
            return TimeUtil.timeMillsFormat(assignSalesTime);
        }

        public String getBookOrderDate() {
            return TimeUtil.timeMillsFormat(bookOrderDate);
        }

        public String getContractDate() {
            return TimeUtil.timeMillsFormat(contractDate);
        }

        public String getCreateDate() {
            return TimeUtil.timeMillsFormat(createDate);
        }

        public String getDepositTime() {
            return TimeUtil.timeMillsFormat(depositTime);
        }

        public String getEndingInstallDate() {
            return TimeUtil.timeMillsFormat(endingInstallDate);
        }

        public String getLastFollowTime() {
            return TimeUtil.timeMillsFormat(lastFollowTime);
        }

        public String getMeasureAppConfirmTime() {
            return TimeUtil.timeMillsFormat(measureAppComfirmTime);
        }

        public String getPlannedStayDate() {
            return TimeUtil.timeMillsFormat(plannedStayDate);
        }

        public String getUpdateDate() {
            return TimeUtil.timeMillsFormat(updateDate);
        }

        public String getUploadPlanDate() {
            return TimeUtil.timeMillsFormat(uploadPlanDate);
        }

        public String getValidatePassedTime() {
            return TimeUtil.timeMillsFormat(validatePassedTime);
        }

        /*是否登记合同*/
        public boolean isContractRegistration() {
            return TextUtils.equals(contractFlag, "Y") || TextUtils.equals(contractFlag, "y");
        }

        /*是否安装完成*/
        public boolean isInstalled() {
            return TextUtils.equals(installFlag, "Y") || TextUtils.equals(installFlag, "y");
        }

        /*房屋是否存在公海历史记录*/
        public boolean isExistHighSeasHistory() {
            return TextUtils.equals(highSeasHistoryFlag, "Y") || TextUtils.equals(highSeasHistoryFlag, "y");
        }
    }

    public static class OperateItemBean {
        public String iconCode;
        public String iconId;
        public String iconName;
        public String iconPath;
        String isLock;

        private OperateItemBean(String iconCode) {
            this.iconCode = iconCode;
        }

        public static OperateItemBean newInstance(String iconCode) {
            return new OperateItemBean(iconCode);
        }

        public boolean isLock() {
            return TextUtils.equals(isLock, "Y") || TextUtils.equals(isLock, "y");
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) return false;
            if (obj == this) return true;
            if (obj instanceof OperateItemBean) {
                return TextUtils.equals(iconCode, ((OperateItemBean) obj).iconCode);
            }
            return false;
        }
    }

    public static class GeneralImageBean {
        public String houseId;
        public String createBy;
        String createDate;
        public String type;
        public String updateBy;
        String updateDate;
        public String recordStatus;
        public String resourceId;
        public String versionNumber;

        public String getCreateDate() {
            return TimeUtil.timeMillsFormat(createDate);
        }

        public String getUpdateDate() {
            return TimeUtil.timeMillsFormat(updateDate);
        }
    }

    /*方案图片*/
    public static class DesignRenderImgBean extends GeneralImageBean {
        public String schemeImgId;
    }

    /*量尺结果图片*/
    public static class MeasureResultImgBean extends GeneralImageBean {
        public String schemeImgId;
    }

    /*合同登记图片*/
    public static class ContractImgBean extends GeneralImageBean {

    }

    /*安装图纸*/
    public static class InstallImgBean extends GeneralImageBean {
        public String installId;
        public String installImgId;
        public String installUserId;
    }

    /*通话记录*/
    public static class PhoneRecordBean implements Serializable {
        public String id;
        public String houseId;
        public String createBy;
        String createDate;
        public String dailPerson;
        public String dailPersonId;
        public String personalId;
        public String phoneNumber;
        public String talkTime;
        public String updateBy;
        String updateDate;

        public String getCreateDate() {
            return TimeUtil.timeMillsFormat(createDate);
        }

        public String getTalkTime() {
            return talkTime;
        }

        public String getUpdateDate() {
            return TimeUtil.timeMillsFormat(updateDate);
        }
    }

    /*生成订单*/
    public static class GenerateOrderBean {
        public String orderId;
        public String creater;
        public String funType;
        public String houseName;
        public String orderStatus;
        public String orderStatusName;
        String createDate;

        public String getOrderId() {
            return TextUtils.isEmpty(orderId) ? "" : orderId;
        }

        public String getCreateDate() {
            return TimeUtil.timeMillsFormat(createDate);
        }
    }

    /*收款*/
    public static class PaymentBean {
        public String dealerId;
        public String houseId;
        public String personalId;
        public String payId;
        public String amount;
        public String category;
        public String createBy;
        List<GeneralImageBean> paymentImg;
        String createDate;
        String payTime;
        public String receiverId;
        public String receiver;
        public String remark;
        public String shopId;
        public String type;
        public String updateBy;
        String updateDate;

        public String getCategory() {
            return SystemCodePattern.getCustomClass(category);
        }

        public String getCreateDate() {
            return TimeUtil.timeMillsFormat(createDate);
        }

        public String getPayTime() {
            return TimeUtil.timeMillsFormat(payTime);
        }

        public String getUpdateDate() {
            return TimeUtil.timeMillsFormat(updateDate);
        }

        /*是否是收尾款*/
        public boolean isTailType() {
            return TextUtils.equals(type, "尾款");
        }

        @NonNull
        public List<GeneralImageBean> getPaymentImg() {
            return paymentImg == null ? new ArrayList<>() : paymentImg;
        }
    }

    /*留言板*/
    public static class MessageBoardBean {
        public String createBy;
        public String createName;
        String createDate;
        public String id;
        public String message;
        public String updateBy;
        String updateDate;
        String messageType;
        public String userName; //导购名字
        public String shopName; //所属门店

        /*分配导购*/
        public boolean isAssignGuide() {
            return TextUtils.equals(messageType, "assignGuide");
        }

        /*分配设计师*/
        public boolean isAssignDesigner() {
            return TextUtils.equals(messageType, "assignDesigner");
        }

        public String getCreateDate() {
            return TimeUtil.timeMillsFormat(createDate);
        }

        public String getUpdateDate() {
            return TimeUtil.timeMillsFormat(updateDate);
        }
    }

    /*历史记录*/
    public static class HistoryBean {
        public String houseId;
        public String historyId;
        public String cancelBy;
        public String cancelReason;
        String cancelTime;
        public String canceled;
        public String createBy;
        String createDate;
        String nextFollowUpDate;
        public String operateCode; //01分配导购，04预约量房，05分配设计师，06量房完成，07上传方案，
        // 08主管查房，09合同登记，11下单，12流失，15预约安装，16安装完成，17上传安装图纸
        String operateTime;
        public String operator;
        public String recordStatus;
        public String remark;
        public String updateBy;
        String updateDate;
        public String userId;
        public String versionNumber;

        public static HistoryBean newInstance(String operateCode) {
            return new HistoryBean(operateCode);
        }

        HistoryBean(String operateCode) {
            this.operateCode = operateCode;
        }

        public String getCancelTime() {
            return TimeUtil.timeMillsFormat(cancelTime);
        }

        public String getCreateDate() {
            return TimeUtil.timeMillsFormat(createDate);
        }

        public String getOperateTime() {
            return TimeUtil.timeMillsFormat(operateTime);
        }

        public String getUpdateDate() {
            return TimeUtil.timeMillsFormat(updateDate);
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) return false;
            if (obj == this) return true;
            if (obj instanceof HistoryBean) {
                return TextUtils.equals(operateCode, ((HistoryBean) obj).operateCode);
            }
            return false;
        }
    }

    public static class InstallInfoBean {
        String actualInstallDate;
        public String address;
        public String afterSaleFlag;
        public String appointmentRemark;
        public String createBy;
        public String createDate;
        public String dealerId;
        public String houseId;
        public String id;
        public String installDate;
        public String installSquare;
        public String installState;
        public String installTime;
        public String name;
        public String personalId;
        public String phone;
        public String recordStatus;
        public String updateBy;
        public String updateDate;
        public String uploadInstallDrawRemark;
        public String versionNumber;

        public String getActualInstallDate() {
            return TimeUtil.timeMillsFormat(actualInstallDate);
        }

        public String getInstallDate() {
            return TimeUtil.timeMillsFormat(installDate);
        }

        public String getInstallTime() {
            if (TextUtils.isEmpty(installTime)) return "";
            try {
                SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
                Date date = format.parse(installTime);
                return TimeUtil.timeMillsFormat(date, "HH:mm");
            } catch (ParseException e) {
                return installTime;
            }
        }
    }

    public static class InstallUserBean {
        public String houseId;
        public String id;
        String actualInstallDate;
        String appointmentFlag;
        public String createBy;
        String createDate;
        public String dealerId;
        public String feedBackFlag;
        String feedBackTime;
        public String installArea;
        public String installId;
        public String installState;
        public String installUserId;
        public String installUserName;
        public String recordStatus;
        public String remark;
        public String updateBy;
        String updateDate;

        public String getActualInstallDate() {
            return TimeUtil.timeMillsFormat(actualInstallDate);
        }

        public String getCreateDate() {
            return TimeUtil.timeMillsFormat(createDate);
        }

        public boolean isFeedback() {
            return TextUtils.equals(feedBackFlag, "Y") || TextUtils.equals(feedBackFlag, "y");
        }
//        public String getFeedBackTime() {
//            return TimeUtil.timeMillsFormat(feedBackTime);
//        }
//
//        public String getUpdateDate() {
//            return TimeUtil.timeMillsFormat(updateDate);
//        }

        public String getInstallState() {
            if (TextUtils.isEmpty(installState)) return "";
            SysCodeItemBean bean = IntentValue.getInstance().getSystemCode();
            if (bean == null || !bean.getInstallFeedbackState().containsKey(installState))
                return "";
            return bean.getInstallFeedbackState().get(installState);
        }
    }

    /*完成安装*/
    public static class InstalledInfoBean {
        @SerializedName("isntallUser")
        InstallUserBean installUserBean;
        List<GeneralImageBean> installRenderImgList;

        @Nullable
        public InstallUserBean getInstallUserBean() {
            return installUserBean;
        }

        @NonNull
        public List<GeneralImageBean> getInstallRenderImgList() {
            return installRenderImgList == null ? new ArrayList<>() : installRenderImgList;
        }
    }
}
