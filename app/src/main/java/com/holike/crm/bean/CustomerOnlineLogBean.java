package com.holike.crm.bean;

import androidx.annotation.Nullable;

import com.google.gson.annotations.SerializedName;
import com.holike.crm.util.TimeUtil;

import java.util.ArrayList;
import java.util.List;

public class CustomerOnlineLogBean {

    private List<PromotionBean> promotionList;
    private CustomerLogBean customerLogBean;

    public void setPromotionList(List<PromotionBean> promotionList) {
        this.promotionList = promotionList;
    }

    public List<PromotionBean> getPromotionList() {
        return promotionList;
    }

    public void setCustomerLogBean(CustomerLogBean customerLogBean) {
        this.customerLogBean = customerLogBean;
    }

    @Nullable
    public CustomerLogBean getCustomerLogBean() {
        return customerLogBean;
    }

    public static class PromotionBean {
        @SerializedName("parent_name")
        public String parentName; //一级渠道
        public String twoAdName; //二级渠道
        public String adName;//方案名称
        public String URL; //报名路径
    }

    public static class CustomerLogBean {
        SignUpBean signupBean;
        InterviewBean interviewBean;
        ShopDataBean shopData;
        List<ResourceDataBean> resourceData;

        @Nullable
        public SignUpBean getSignUpBean() {
            return signupBean;
        }

        @Nullable
        public InterviewBean getInterviewBean() {
            return interviewBean;
        }

        @Nullable
        public ShopDataBean getShopData() {
            return shopData;
        }

        public List<ResourceDataBean> getResourceData() {
            return resourceData == null ? new ArrayList<>() : resourceData;
        }
    }

    /*客户信息*/
    public static class SignUpBean {
        public String customerName; //客户姓名
        public String customerPhone; //客户手机
        public String province;// 省份
        public String city; //市
        public String district; //区
        public String tmallId; // 京东/旺旺id
        public String orderNumber; //订单编号
        public String orderAmount;//订单金额
        public String gift; // 赠品/优惠券
        public String waitShopDatetime; //下发客户时间
        public String reutrnDateTime; //无效驳回时间
        public String signupRemark; //客服跟进留言
        public String followUpRecord; //聊天记录
        public String followCode; //预约类型
    }

    /*房屋信息*/
    public static class InterviewBean {
        public String buildingInformation; //楼盘信息
        public String address;  //详细地址
        public String houseStatus;  //房屋状态
        public String houseArea;  //房屋面积
        public String houseType;  //房屋户型
        public String checkbulidingCode;  //是否收楼(01为已收楼，02为未收楼)
        String checkbulidingTime;  //收楼时间
        public String decorationProgress;  //装修情况 (01-已装修/装修中，02-未装修)
        String decorationStartDatetime;  //装修日期
        public String customClass;  //定制品类
        public String customSpace;  //定制空间
        public String furnitureDemand;  //家具需求
        public String boardType;  //板材喜好
        public String decorationStyle;  //风格喜好
        public String decorationBudget;  //定制预算
        String expectMeasureDatetime; //预约量尺时间

        public String getCheckbulidingTime() {
            return TimeUtil.timeMillsFormat(checkbulidingTime);
        }

        public String getDecorationStartDatetime() {
            return TimeUtil.timeMillsFormat(decorationStartDatetime);
        }

        public String getExpectMeasureDatetime() {
            return TimeUtil.timeMillsFormat(expectMeasureDatetime, "yyyy.MM.dd HH:mm");
        }
    }

    public static class ShopDataBean {
        //"shopData":{
        //"recordValidDatetime":null,
        //"dealerName":null,
        //"province":null,
        //"partnerBigRegion":null,
        //"city":null,
        //"dealerId":null,
        //"contact":null,
        //"oneReturnDatetime":null,
        //"inValidDatetime":null,
        //"contactPhone":null,
        //"twoReturnDatetime":null,
        //"partnerCentre":null
        //},
        public String recordValidDatetime; //确认有效时间
    }

    public static class ResourceDataBean {
        public String filePath;
    }
}
