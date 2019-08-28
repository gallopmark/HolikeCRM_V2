package com.holike.crm.bean;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gallop on 2019/8/13.
 * Copyright holike possess 2019.
 * 当前登录用户信息
 */
public class CurrentUserBean {
    private InfoBean userInfo;
    private List<DealerInfo> dealerInfo;
    private List<ShopInfo> shopInfo;

    @Nullable
    public InfoBean getUserInfo() {
        return userInfo;
    }

    @NonNull
    public List<ShopInfo> getShopInfo() {
        return shopInfo == null ? new ArrayList<>() : shopInfo;
    }

    public List<DealerInfo> getDealerInfo() {
        return dealerInfo == null ? new ArrayList<>() : dealerInfo;
    }

    public static class InfoBean {
        public String available;
        public String city;
        public String createBy;
        public String createTime;
        public String email;
        public String fileId;
        public String gender;
        public String landline;
        public String partnerId;
        public String password;
        public String serialNumber;
        public String shopId;
        public String staffId;
        public String stationId;
        public String telphone;
        public String updateBy;
        public String updateTime;
        public String userCode;
        public String userId;
        public String userName;
        public String userType;
        public String virtualUserId;
    }

    public static class DealerInfo {
        public String areaId;
        public String areaManager;
        public String auditStatus;
        public String businessAssistant;
        public String businessAssistantCode;
        public String centerAreaManager;
        public String cityCode;
        public String companyCode;
        public String controlSubject;
        public String country;
        public String countyCity;
        public String countyCode;
        public String createBy;
        public String createDate;
        public String creditRange;
        public String creditSegment;
        public String dealerAddress;
        public String dealerContactPhone;
        public String dealerContactsName;
        public String dealerContactsPhone;
        public String dealerId;
        public String dealerName;
        public String dealerType;
        public String distributionChannel;
        public String frequentContactsEmail;
        public String frequentContactsName;
        public String frequentContactsPhone;
        public String frequentContactsQq;
        public String homeCity;
        public String joinReason;
        public String joinTime;
        public String language;
        public String legalRepresentative;
        public String limitAmount;
        public String limitRule;
        public String limitValidDate;
        public String marketLevel;
        public String materialType;
        public String oldDealerId;
        public String payGuarantee;
        public String paymentTerms;
        public String postcode;
        public String principalName;
        public String principalPhone;
        public String productGroup;
        public String provinceCode;
        public String regionManager;
        public String retiredTime;
        public String sales;
        public String salesOffice;
        public String salesOrganization;
        public String searchTerms;
        public String shipmentTerms;
        public String signingDate;
        public String signingPersonnel;
        public String signingPersonnelEmali;
        public String signingPersonnelPhone;
        public String statusCode;
        public String supportCenter;
        public String taxClassification;
        public String transferTime;
        public String updateBy;
        public String updateDate;
        public String updateReason;
    }

    public static class ShopInfo {
        public String auditStatus;
        public String buildShopOaNumber;
        public String businessLicense;
        public String businessLicenseAddress;
        public String centerAreaManager;
        public String cityCode;
        public String country;
        public String countyCity;
        public String countyCode;
        public String createBy;
        public String createDate;
        public String dealerId;
        public String dealerType;
        public String decorationDate;
        public String decorationInfo;
        public String designerName;
        public String designerPhone;
        public String designerQq;
        public String frequentContactsQq;
        public String language;
        public String largeHomeType;
        public String managerName;
        public String managerPhone;
        public String managerQq;
        public String materialType;
        public String oldShopId;
        public String openTime;
        public String phone;
        public String postcode;
        public String principalEmali;
        public String principalPhone;
        public String profitCenter;
        public String provinceCode;
        public String regionManager;
        public String searchTerms;
        public String shopAddress;
        public String shopArea;
        public String shopClassify;
        public String shopId;
        public String shopName;
        public String shopPrincipal;
        public String shopType;
        public String shopTypeCode;
        public String shopValue;
        public String shoppingGuideName;
        public String shoppingGuidePhone;
        public String shoppingGuideQq;
        public String shutTime;
        public String statusCode;
        public String transferDate;
        public String updateBy;
        public String updateDate;
        public String updateReason;
        public String virtualUserId;
        public String whetherReceive;
    }
}
