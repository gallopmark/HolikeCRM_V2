package com.holike.crm.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by wqj on 2018/8/17.
 * 客户详情bean
 */

public class CustomerDetailBean implements Serializable, MultiItem {
    /**
     * code : 0
     * customerDetailInfoList : [{"listDetails":{"name":"测试内容hz8v","number":"测试内容pt88"},"listHistory":[{"history":{"appointmentTime":"测试内容t81u","appointmentToInstallDate":"测试内容6dwv","bookOrderDate":"测试内容16es","depositAmount":"测试内容r48y","designAvailable":"测试内容5182","designerId":"测试内容33vy","endingInstallDate":"测试内容odsf","fileAddress":"测试内容3044","fileType":"测试内容kg4z","lastRemaining":"测试内容s34k","leaveReason":"测试内容112b","leaveToSeries":"测试内容3jnx","nextFollowUpDate":"测试内容4010","operateCode":"测试内容1s2g","remark":"测试内容rhkr","salesAmount":"测试内容7c77","toStatusCode":"测试内容15ak","userId":"测试内容1dy5"},"url":["string1","string2","string3","string4","string5"]}],"listHouseInfo":{"address":"拿粉底红烧鸡块200号","appointmentTime":"测试内容1327","appointmentToInstallDate":"测试内容8kj3","bookOrderDate":"测试内容cigr","endingInstallDate":"测试内容68s3","houseId":"3e0b9aa7bd8645bba8e1b0febe7323ae","lastRemaining":"测试内容2353","outTimeFlag":"测试内容72d5","shopId":1,"statusCode":"新增"},"listHouseSpace":[{"createDate":"测试内容hdkb","creater":"测试内容2bpy","orderId":"测试内容h6lo","statusCode":"测试内容62v1"}],"listStatus":[{"createDate":"测试内容770l","creater":"测试内容9vnn","customerStatusName":"测试内容34cv","img":"测试内容uz3r","isShow":"测试内容26s4","optCode":"测试内容u931","prepositionRuleStatus":"测试内容5xmc","customerStatus":"02","isAvailable":"1"}]}]
     * personal : {"createDate":"测试内容o39q","personalId":"测试内容d142","phoneNumber":"测试内容i4m6","source":"测试内容8340","userName":"测试内容onx7"}
     * url : http://120.79.76.243:80/customermgr/
     */

    @Override
    public int getItemType() {
        return 1;
    }

    private String code;
    private PersonalBean personal;
    private String url;
    private String appurl;

    public String getAppurl() {
        return appurl;
    }

    public void setAppurl(String appurl) {
        this.appurl = appurl;
    }

    private List<CustomerDetailInfoListBean> customerDetailInfoList;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public PersonalBean getPersonal() {
        return personal;
    }

    public void setPersonal(PersonalBean personal) {
        this.personal = personal;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<CustomerDetailInfoListBean> getCustomerDetailInfoList() {
        return customerDetailInfoList;
    }

    public void setCustomerDetailInfoList(List<CustomerDetailInfoListBean> customerDetailInfoList) {
        this.customerDetailInfoList = customerDetailInfoList;
    }

    public static class PersonalBean implements Serializable {
        /**
         * createDate : 测试内容o39q
         * personalId : 测试内容d142
         * phoneNumber : 测试内容i4m6
         * source : 测试内容8340
         * userName : 测试内容onx7
         * "genderName": "女",
         * "associatesName": "21040老板",
         * "specialCustomersName": "",
         * "shopName": "阳泉经济技术开发区天野装饰材料经销部",
         * "sourceName": "电话资源"
         */

        private String address;
        private String associates;
        private String gender;
        private String shopId;
        private String specialCustomers;
        private String createDate;
        private String personalId;
        private String phoneNumber;
        private String source;
        private String userName;
        private String genderName;
        private String associatesName;
        private String specialCustomersName;
        private String shopName;
        private String sourceName;

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getAssociates() {
            return associates;
        }

        public void setAssociates(String associates) {
            this.associates = associates;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public String getShopId() {
            return shopId;
        }

        public void setShopId(String shopId) {
            this.shopId = shopId;
        }

        public String getSpecialCustomers() {
            return specialCustomers;
        }

        public void setSpecialCustomers(String specialCustomers) {
            this.specialCustomers = specialCustomers;
        }

        public String getCreateDate() {
            return createDate;
        }

        public void setCreateDate(String createDate) {
            this.createDate = createDate;
        }

        public String getPersonalId() {
            return personalId;
        }

        public void setPersonalId(String personalId) {
            this.personalId = personalId;
        }

        public String getPhoneNumber() {
            return phoneNumber;
        }

        public void setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
        }

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getGenderName() {
            return genderName;
        }

        public void setGenderName(String genderName) {
            this.genderName = genderName;
        }

        public String getAssociatesName() {
            return associatesName;
        }

        public void setAssociatesName(String associatesName) {
            this.associatesName = associatesName;
        }

        public String getSpecialCustomersName() {
            return specialCustomersName;
        }

        public void setSpecialCustomersName(String specialCustomersName) {
            this.specialCustomersName = specialCustomersName;
        }

        public String getShopName() {
            return shopName;
        }

        public void setShopName(String shopName) {
            this.shopName = shopName;
        }

        public String getSourceName() {
            return sourceName;
        }

        public void setSourceName(String sourceName) {
            this.sourceName = sourceName;
        }


        public PersonalBean(String associates, String gender, String shopId, String specialCustomers, String personalId, String phoneNumber, String source, String userName) {
            this.associates = associates;
            this.gender = gender;
            this.shopId = shopId;
            this.specialCustomers = specialCustomers;
            this.personalId = personalId;
            this.phoneNumber = phoneNumber;
            this.source = source;
            this.userName = userName;
        }

    }

    public static class CustomerDetailInfoListBean implements Serializable {
        /**
         * listDetails : {"name":"测试内容hz8v","number":"测试内容pt88"}
         * listHistory : [{"history":{"appointmentTime":"测试内容t81u","appointmentToInstallDate":"测试内容6dwv","bookOrderDate":"测试内容16es","depositAmount":"测试内容r48y","designAvailable":"测试内容5182","designerId":"测试内容33vy","endingInstallDate":"测试内容odsf","fileAddress":"测试内容3044","fileType":"测试内容kg4z","lastRemaining":"测试内容s34k","leaveReason":"测试内容112b","leaveToSeries":"测试内容3jnx","nextFollowUpDate":"测试内容4010","operateCode":"测试内容1s2g","remark":"测试内容rhkr","salesAmount":"测试内容7c77","toStatusCode":"测试内容15ak","userId":"测试内容1dy5"},"url":["string1","string2","string3","string4","string5"]}]
         * listHouseInfo : {"address":"拿粉底红烧鸡块200号","appointmentTime":"测试内容1327","appointmentToInstallDate":"测试内容8kj3","bookOrderDate":"测试内容cigr","endingInstallDate":"测试内容68s3","houseId":"3e0b9aa7bd8645bba8e1b0febe7323ae","lastRemaining":"测试内容2353","outTimeFlag":"测试内容72d5","shopId":1,"statusCode":"新增"}
         * listHouseSpace : [{"createDate":"测试内容hdkb","creater":"测试内容2bpy","orderId":"测试内容h6lo","statusCode":"测试内容62v1"}]
         * listStatus : [{"createDate":"测试内容770l","creater":"测试内容9vnn","customerStatusName":"测试内容34cv","img":"测试内容uz3r","isShow":"测试内容26s4","optCode":"测试内容u931","prepositionRuleStatus":"测试内容5xmc","customerStatus":"02","isAvailable":"1"}]
         */

        private boolean isSelect;
        private List<ListDetailsBean> listDetails;
        private ListHouseInfoBean listHouseInfo;
        private List<ListHistoryBean> listHistory;
        private List<ListHouseSpaceBean> listHouseSpace;
        private List<ListStatusBean> listStatus;

        public boolean isSelect() {
            return isSelect;
        }

        public void setSelect(boolean select) {
            isSelect = select;
        }

        public List<ListDetailsBean> getListDetails() {
            return listDetails;
        }

        public void setListDetails(List<ListDetailsBean> listDetails) {
            this.listDetails = listDetails;
        }

        public ListHouseInfoBean getListHouseInfo() {
            return listHouseInfo;
        }

        public void setListHouseInfo(ListHouseInfoBean listHouseInfo) {
            this.listHouseInfo = listHouseInfo;
        }

        public List<ListHistoryBean> getListHistory() {
            return listHistory;
        }

        public void setListHistory(List<ListHistoryBean> listHistory) {
            this.listHistory = listHistory;
        }

        public List<ListHouseSpaceBean> getListHouseSpace() {
            return listHouseSpace;
        }

        public void setListHouseSpace(List<ListHouseSpaceBean> listHouseSpace) {
            this.listHouseSpace = listHouseSpace;
        }

        public List<ListStatusBean> getListStatus() {
            return listStatus;
        }

        public void setListStatus(List<ListStatusBean> listStatus) {
            this.listStatus = listStatus;
        }

        public static class ListDetailsBean implements Serializable {
            /**
             * name : 测试内容hz8v
             * number : 测试内容pt88
             */

            private String name;
            private String number;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getNumber() {
                return number;
            }

            public void setNumber(String number) {
                this.number = number;
            }
        }

        public static class ListHouseInfoBean implements Serializable {
            /**
             * address : 拿粉底红烧鸡块200号
             * appointmentTime : 测试内容1327
             * appointmentToInstallDate : 测试内容8kj3
             * bookOrderDate : 测试内容cigr
             * endingInstallDate : 测试内容68s3
             * houseId : 3e0b9aa7bd8645bba8e1b0febe7323ae
             * lastRemaining : 测试内容2353
             * outTimeFlag : 测试内容72d5
             * shopId : 1
             * statusCode : 新增
             * "favTextureCodeName": null,
             * "favColorCodeName": null,
             * "houseTypeName": "单间",
             * "decoratePropertiesName": null,
             * "decorateTypeName": "新交房"
             */

            private String area;
            private String budget;
            private String buildingName;
            private String buildingNumber;
            private String decorateType;
            private String favColorCode;
            private String favTextureCode;
            private String decorateProperties;
            private String houseType;
            private String remark;
            private String address;
            private String appointmentTime;
            private String appointmentToInstallDate;
            private String bookOrderDate;
            private String endingInstallDate;
            private String houseId;
            private String lastRemaining;
            private String outTimeFlag;
            private String shopId;
            private String shopName;
            private String statusCode;
            private String favTextureCodeName;
            private String favColorCodeName;
            private String preferenceStyle;
            private String preferenceStyleName;
            private String houseTypeName;
            private String decoratePropertiesName;
            private String decorateTypeName;
            private String isAfter;
            private String statusMove;
            private String amountOfDate;
            private String digitalRemark;
            private String earnestHouseType;
            private String earnestHouseTypeName;
            private String checkbulidingCode;
            private String checkbulidingCodeName;

            private String customizeTheSpace;
            private String decorationProgress;
            private String decorationProgressName;

            private String furnitureDemand;
            private String plannedBaseDecorateDate;
            private String plannedHouseDeliveryDate;
            private String followCodeName;
            private String followCode;
            private String deliveryChannel;
            private String adName;
            private String salesId;
            private String associatesName;

            public String getPreferenceStyleName() {
                return preferenceStyleName;
            }

            public void setPreferenceStyleName(String preferenceStyleName) {
                this.preferenceStyleName = preferenceStyleName;
            }

            public String getPreferenceStyle() {
                return preferenceStyle;
            }

            public void setPreferenceStyle(String preferenceStyle) {
                this.preferenceStyle = preferenceStyle;
            }

            public String getCheckbulidingCodeName() {
                return checkbulidingCodeName;
            }

            public void setCheckbulidingCodeName(String checkbulidingCodeName) {
                this.checkbulidingCodeName = checkbulidingCodeName;
            }

            public String getDecorationProgressName() {
                return decorationProgressName;
            }

            public void setDecorationProgressName(String decorationProgressName) {
                this.decorationProgressName = decorationProgressName;
            }

            public String getAssociatesName() {
                return associatesName;
            }

            public void setAssociatesName(String associatesName) {
                this.associatesName = associatesName;
            }

            public String getShopName() {
                return shopName;
            }

            public void setShopName(String shopName) {
                this.shopName = shopName;
            }


            public String getSalesId() {
                return salesId;
            }

            public void setSalesId(String salesId) {
                this.salesId = salesId;
            }

            public String getFollowCodeName() {
                return followCodeName;
            }

            public void setFollowCodeName(String followCodeName) {
                this.followCodeName = followCodeName;
            }

            public String getFollowCode() {
                return followCode;
            }

            public void setFollowCode(String followCode) {
                this.followCode = followCode;
            }

            public String getDeliveryChannel() {
                return deliveryChannel;
            }

            public void setDeliveryChannel(String deliveryChannel) {
                this.deliveryChannel = deliveryChannel;
            }

            public String getAdName() {
                return adName;
            }

            public void setAdName(String adName) {
                this.adName = adName;
            }

            public String getCheckbulidingCode() {
                return checkbulidingCode;
            }

            public void setCheckbulidingCode(String checkbulidingCode) {
                this.checkbulidingCode = checkbulidingCode;
            }

            public String getCustomizeTheSpace() {
                return customizeTheSpace;
            }

            public void setCustomizeTheSpace(String customizeTheSpace) {
                this.customizeTheSpace = customizeTheSpace;
            }

            public String getDecorationProgress() {
                return decorationProgress;
            }

            public void setDecorationProgress(String decorationProgress) {
                this.decorationProgress = decorationProgress;
            }


            public String getFurnitureDemand() {
                return furnitureDemand;
            }

            public void setFurnitureDemand(String furnitureDemand) {
                this.furnitureDemand = furnitureDemand;
            }

            public String getPlannedBaseDecorateDate() {
                return plannedBaseDecorateDate;
            }

            public void setPlannedBaseDecorateDate(String plannedBaseDecorateDate) {
                this.plannedBaseDecorateDate = plannedBaseDecorateDate;
            }

            public String getPlannedHouseDeliveryDate() {
                return plannedHouseDeliveryDate;
            }

            public void setPlannedHouseDeliveryDate(String plannedHouseDeliveryDate) {
                this.plannedHouseDeliveryDate = plannedHouseDeliveryDate;
            }

            public String getEarnestHouseType() {
                return earnestHouseType;
            }

            public void setEarnestHouseType(String earnestHouseType) {
                this.earnestHouseType = earnestHouseType;
            }

            public String getEarnestHouseTypeName() {
                return earnestHouseTypeName;
            }

            public void setEarnestHouseTypeName(String earnestHouseTypeName) {
                this.earnestHouseTypeName = earnestHouseTypeName;
            }

            public String getDigitalRemark() {
                return digitalRemark;
            }

            public void setDigitalRemark(String digitalRemark) {
                this.digitalRemark = digitalRemark;
            }

            public String getArea() {
                return area;
            }

            public void setArea(String area) {
                this.area = area;
            }

            public String getBudget() {
                return budget;
            }

            public void setBudget(String budget) {
                this.budget = budget;
            }

            public String getBuildingName() {
                return buildingName;
            }

            public void setBuildingName(String buildingName) {
                this.buildingName = buildingName;
            }

            public String getBuildingNumber() {
                return buildingNumber;
            }

            public void setBuildingNumber(String buildingNumber) {
                this.buildingNumber = buildingNumber;
            }

            public String getDecorateType() {
                return decorateType;
            }

            public void setDecorateType(String decorateType) {
                this.decorateType = decorateType;
            }

            public String getFavColorCode() {
                return favColorCode;
            }

            public void setFavColorCode(String favColorCode) {
                this.favColorCode = favColorCode;
            }

            public String getFavTextureCode() {
                return favTextureCode;
            }

            public void setFavTextureCode(String favTextureCode) {
                this.favTextureCode = favTextureCode;
            }

            public String getDecorateProperties() {
                return decorateProperties;
            }

            public void setDecorateProperties(String decorateProperties) {
                this.decorateProperties = decorateProperties;
            }

            public String getHouseType() {
                return houseType;
            }

            public void setHouseType(String houseType) {
                this.houseType = houseType;
            }

            public String getRemark() {
                return remark;
            }

            public void setRemark(String remark) {
                this.remark = remark;
            }

            public String getAddress() {
                return address;
            }

            public void setAddress(String address) {
                this.address = address;
            }

            public String getAppointmentTime() {
                return appointmentTime;
            }

            public void setAppointmentTime(String appointmentTime) {
                this.appointmentTime = appointmentTime;
            }

            public String getAppointmentToInstallDate() {
                return appointmentToInstallDate;
            }

            public void setAppointmentToInstallDate(String appointmentToInstallDate) {
                this.appointmentToInstallDate = appointmentToInstallDate;
            }

            public String getBookOrderDate() {
                return bookOrderDate;
            }

            public void setBookOrderDate(String bookOrderDate) {
                this.bookOrderDate = bookOrderDate;
            }

            public String getEndingInstallDate() {
                return endingInstallDate;
            }

            public void setEndingInstallDate(String endingInstallDate) {
                this.endingInstallDate = endingInstallDate;
            }

            public String getHouseId() {
                return houseId;
            }

            public void setHouseId(String houseId) {
                this.houseId = houseId;
            }

            public String getLastRemaining() {
                return lastRemaining;
            }

            public void setLastRemaining(String lastRemaining) {
                this.lastRemaining = lastRemaining;
            }

            public String getOutTimeFlag() {
                return outTimeFlag;
            }

            public void setOutTimeFlag(String outTimeFlag) {
                this.outTimeFlag = outTimeFlag;
            }

            public String getShopId() {
                return shopId;
            }

            public void setShopId(String shopId) {
                this.shopId = shopId;
            }

            public String getStatusCode() {
                return statusCode;
            }

            public void setStatusCode(String statusCode) {
                this.statusCode = statusCode;
            }

            public String getFavTextureCodeName() {
                return favTextureCodeName;
            }

            public void setFavTextureCodeName(String favTextureCodeName) {
                this.favTextureCodeName = favTextureCodeName;
            }

            public String getFavColorCodeName() {
                return favColorCodeName;
            }

            public void setFavColorCodeName(String favColorCodeName) {
                this.favColorCodeName = favColorCodeName;
            }

            public String getHouseTypeName() {
                return houseTypeName;
            }

            public void setHouseTypeName(String houseTypeName) {
                this.houseTypeName = houseTypeName;
            }

            public String getDecoratePropertiesName() {
                return decoratePropertiesName;
            }

            public void setDecoratePropertiesName(String decoratePropertiesName) {
                this.decoratePropertiesName = decoratePropertiesName;
            }

            public String getDecorateTypeName() {
                return decorateTypeName;
            }

            public void setDecorateTypeName(String decorateTypeName) {
                this.decorateTypeName = decorateTypeName;
            }

            public String getIsAfter() {
                return isAfter;
            }

            public void setIsAfter(String isAfter) {
                this.isAfter = isAfter;
            }

            public String getStatusMove() {
                return statusMove;
            }

            public void setStatusMove(String statusMove) {
                this.statusMove = statusMove;
            }

            public String getAmountOfDate() {
                return amountOfDate;
            }

            public void setAmountOfDate(String amountOfDate) {
                this.amountOfDate = amountOfDate;
            }

            /*

                  private String followCodeName;
            private String followCode;
            private String deliveryChannel;
            private String adName;
             */
            public ListHouseInfoBean(String area, String budget, String buildingName, String buildingNumber,
                                     String decorateType, String favColorCode, String favTextureCode,
                                     String decorateProperties, String houseType, String remark, String houseId,
                                     String checkbulidingCode, String customizeTheSpace, String decorationProgress,
                                     String furnitureDemand, String plannedBaseDecorateDate,
                                     String plannedHouseDeliveryDate, String shopId, String salesId, String followCodeName, String deliveryChannel, String adName

            ) {
                this.area = area;
                this.budget = budget;
                this.buildingName = buildingName;
                this.buildingNumber = buildingNumber;
                this.decorateType = decorateType;
                this.favColorCode = favColorCode;
                this.favTextureCode = favTextureCode;
                this.decorateProperties = decorateProperties;
                this.houseType = houseType;
                this.remark = remark;
                this.houseId = houseId;
                this.checkbulidingCode = checkbulidingCode;
                this.customizeTheSpace = customizeTheSpace;
                this.decorationProgress = decorationProgress;
                this.furnitureDemand = furnitureDemand;
                this.plannedBaseDecorateDate = plannedBaseDecorateDate;
                this.plannedHouseDeliveryDate = plannedHouseDeliveryDate;
                this.followCodeName = followCodeName;
                this.deliveryChannel = deliveryChannel;
                this.adName = adName;
                this.salesId = salesId;
                this.shopId = shopId;
            }

        }

        public static class ListHistoryBean implements Serializable, MultiItem {
            @Override
            public int getItemType() {
                return 4;
            }

            /**
             * history : {"appointmentTime":"测试内容t81u","appointmentToInstallDate":"测试内容6dwv","bookOrderDate":"测试内容16es","depositAmount":"测试内容r48y","designAvailable":"测试内容5182","designerId":"测试内容33vy","endingInstallDate":"测试内容odsf","fileAddress":"测试内容3044","fileType":"测试内容kg4z","lastRemaining":"测试内容s34k","leaveReason":"测试内容112b","leaveToSeries":"测试内容3jnx","nextFollowUpDate":"测试内容4010","operateCode":"测试内容1s2g","remark":"测试内容rhkr","salesAmount":"测试内容7c77","toStatusCode":"测试内容15ak","userId":"测试内容1dy5"}
             * url : ["string1","string2","string3","string4","string5"]
             */


            private HistoryBean history;
            private List<String> url;

            public HistoryBean getHistory() {
                return history;
            }

            public void setHistory(HistoryBean history) {
                this.history = history;
            }

            public List<String> getUrl() {
                return url;
            }

            public void setUrl(List<String> url) {
                this.url = url;
            }

            public static class HistoryBean implements Serializable {
                /**
                 * appointmentTime : 测试内容t81u
                 * appointmentToInstallDate : 测试内容6dwv
                 * bookOrderDate : 测试内容16es
                 * depositAmount : 测试内容r48y
                 * designAvailable : 测试内容5182
                 * designerId : 测试内容33vy
                 * endingInstallDate : 测试内容odsf
                 * fileAddress : 测试内容3044
                 * fileType : 测试内容kg4z
                 * lastRemaining : 测试内容s34k
                 * leaveReason : 测试内容112b
                 * leaveToSeries : 测试内容3jnx
                 * nextFollowUpDate : 测试内容4010
                 * operateCode : 测试内容1s2g
                 * remark : 测试内容rhkr
                 * salesAmount : 测试内容7c77
                 * toStatusCode : 测试内容15ak
                 * userId : 测试内容1dy5
                 * canceled : 1
                 */

                private String appointmentTime;
                private String appointmentToInstallDate;
                private String bookOrderDate;
                private String depositAmount;
                private String designAvailable;
                private String designerId;
                private String endingInstallDate;
                private String fileAddress;
                private String fileType;
                private String lastRemaining;
                private String leaveReason;
                private String leaveToSeries;
                private String nextFollowUpDate;
                private String operateCode;
                private String remark;
                private String salesAmount;
                private String toStatusCode;
                private String userId;
                private String salesId;
                private String fromStatusCode;
                private String measureTime;
                private String canceled;

                public String getAppointmentTime() {
                    return appointmentTime;
                }

                public void setAppointmentTime(String appointmentTime) {
                    this.appointmentTime = appointmentTime;
                }

                public String getAppointmentToInstallDate() {
                    return appointmentToInstallDate;
                }

                public void setAppointmentToInstallDate(String appointmentToInstallDate) {
                    this.appointmentToInstallDate = appointmentToInstallDate;
                }

                public String getBookOrderDate() {
                    return bookOrderDate;
                }

                public void setBookOrderDate(String bookOrderDate) {
                    this.bookOrderDate = bookOrderDate;
                }

                public String getDepositAmount() {
                    return depositAmount;
                }

                public void setDepositAmount(String depositAmount) {
                    this.depositAmount = depositAmount;
                }

                public String getDesignAvailable() {
                    return designAvailable;
                }

                public void setDesignAvailable(String designAvailable) {
                    this.designAvailable = designAvailable;
                }

                public String getDesignerId() {
                    return designerId;
                }

                public void setDesignerId(String designerId) {
                    this.designerId = designerId;
                }

                public String getEndingInstallDate() {
                    return endingInstallDate;
                }

                public void setEndingInstallDate(String endingInstallDate) {
                    this.endingInstallDate = endingInstallDate;
                }

                public String getFileAddress() {
                    return fileAddress;
                }

                public void setFileAddress(String fileAddress) {
                    this.fileAddress = fileAddress;
                }

                public String getFileType() {
                    return fileType;
                }

                public void setFileType(String fileType) {
                    this.fileType = fileType;
                }

                public String getLastRemaining() {
                    return lastRemaining;
                }

                public void setLastRemaining(String lastRemaining) {
                    this.lastRemaining = lastRemaining;
                }

                public String getLeaveReason() {
                    return leaveReason;
                }

                public void setLeaveReason(String leaveReason) {
                    this.leaveReason = leaveReason;
                }

                public String getLeaveToSeries() {
                    return leaveToSeries;
                }

                public void setLeaveToSeries(String leaveToSeries) {
                    this.leaveToSeries = leaveToSeries;
                }

                public String getNextFollowUpDate() {
                    return nextFollowUpDate;
                }

                public void setNextFollowUpDate(String nextFollowUpDate) {
                    this.nextFollowUpDate = nextFollowUpDate;
                }

                public String getOperateCode() {
                    return operateCode;
                }

                public void setOperateCode(String operateCode) {
                    this.operateCode = operateCode;
                }

                public String getRemark() {
                    return remark;
                }

                public void setRemark(String remark) {
                    this.remark = remark;
                }

                public String getSalesAmount() {
                    return salesAmount;
                }

                public void setSalesAmount(String salesAmount) {
                    this.salesAmount = salesAmount;
                }

                public String getToStatusCode() {
                    return toStatusCode;
                }

                public void setToStatusCode(String toStatusCode) {
                    this.toStatusCode = toStatusCode;
                }

                public String getUserId() {
                    return userId;
                }

                public void setUserId(String userId) {
                    this.userId = userId;
                }

                public String getSalesId() {
                    return salesId;
                }

                public void setSalesId(String salesId) {
                    this.salesId = salesId;
                }

                public String getFromStatusCode() {
                    return fromStatusCode;
                }

                public void setFromStatusCode(String fromStatusCode) {
                    this.fromStatusCode = fromStatusCode;
                }

                public String getMeasureTime() {
                    return measureTime;
                }

                public void setMeasureTime(String measureTime) {
                    this.measureTime = measureTime;
                }

                public String getCanceled() {
                    return canceled;
                }

                public void setCanceled(String canceled) {
                    this.canceled = canceled;
                }
            }
        }

        public static class ListHouseSpaceBean implements Serializable, MultiItem {
            @Override
            public int getItemType() {
                return 1;
            }

            /**
             * createDate : 测试内容hdkb
             * creater : 测试内容2bpy
             * orderId : 测试内容h6lo
             * statusCode : 测试内容62v1
             */

            private String createDate;
            private String creater;
            private String orderId;
            private String houseName;

            public String getCreateDate() {
                return createDate;
            }

            public void setCreateDate(String createDate) {
                this.createDate = createDate;
            }

            public String getCreater() {
                return creater;
            }

            public void setCreater(String creater) {
                this.creater = creater;
            }

            public String getOrderId() {
                return orderId;
            }

            public void setOrderId(String orderId) {
                this.orderId = orderId;
            }

            public String getHouseName() {
                return houseName;
            }

            public void setHouseName(String houseName) {
                this.houseName = houseName;
            }
        }

        public static class ListStatusBean implements Serializable, MultiItem {
            /**
             * createDate : 测试内容770l
             * creater : 测试内容9vnn
             * customerStatusName : 测试内容34cv
             * img : 测试内容uz3r
             * isShow : 测试内容26s4
             * optCode : 测试内容u931
             * prepositionRuleStatus : 测试内容5xmc
             * customerStatus : 02
             * isAvailable : 1
             */

            private String createDate;
            private String creater;
            private String customerStatusName;
            private String img;
            private String isShow;
            private String optCode;
            private String prepositionRuleStatus;
            private String customerStatus;
            private String isAvailable;

            public String getCreateDate() {
                return createDate;
            }

            public void setCreateDate(String createDate) {
                this.createDate = createDate;
            }

            public String getCreater() {
                return creater;
            }

            public void setCreater(String creater) {
                this.creater = creater;
            }

            public String getCustomerStatusName() {
                return customerStatusName;
            }

            public void setCustomerStatusName(String customerStatusName) {
                this.customerStatusName = customerStatusName;
            }

            public String getImg() {
                return img;
            }

            public void setImg(String img) {
                this.img = img;
            }

            public String getIsShow() {
                return isShow;
            }

            public void setIsShow(String isShow) {
                this.isShow = isShow;
            }

            public String getOptCode() {
                return optCode;
            }

            public void setOptCode(String optCode) {
                this.optCode = optCode;
            }

            public String getPrepositionRuleStatus() {
                return prepositionRuleStatus;
            }

            public void setPrepositionRuleStatus(String prepositionRuleStatus) {
                this.prepositionRuleStatus = prepositionRuleStatus;
            }

            public String getCustomerStatus() {
                return customerStatus;
            }

            public void setCustomerStatus(String customerStatus) {
                this.customerStatus = customerStatus;
            }

            public String getIsAvailable() {
                return isAvailable;
            }

            public void setIsAvailable(String isAvailable) {
                this.isAvailable = isAvailable;
            }

            @Override
            public int getItemType() {
                return 1;
            }
        }
    }
}
