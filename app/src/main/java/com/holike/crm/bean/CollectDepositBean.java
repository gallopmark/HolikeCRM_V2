package com.holike.crm.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by wqj on 2018/8/20.
 * 查询未交定金客户信息列表
 */

public class CollectDepositBean implements Serializable {

    /**
     * code : 0
     * reason : 成功
     * data : [{"earnest":null,"isEarnest":"0","earnestType":"","url":null,"digitalRemark":"","site":"山西省阳泉市","personalId":"VA201810000051","userName":"门店-样品","phoneNumber":"13934030856","source":null,"sourceName":null,"plannedHouseDeliveryDate":null,"plannedBaseDecorateDate":null,"customerTypeCode":null,"customerTypeCodeName":null,"dealerId":null,"furnitureDemand":null,"customizeTheSpace":null,"gender":null,"genderName":null,"houseId":"cc369049a7004297b5bee8f284916f18","buildingName":null,"buildingNumber":null,"address":"null","statusCode":null,"statusCodeName":null,"associates":null,"associatesName":null,"shopId":null,"shopIdName":null,"area":null,"content":null,"houseType":null,"decorateType":null,"decorateProperties":null,"createDate":"null","houseCreateDate":null,"updateTime":"","specialCustomers":"","antecedentId":"","antecedentPrice":null,"favTextureCode":"","favColorCode":"","number":"","name":"","remark":"","statusMove":null,"statusMoveName":null,"budget":"","listStatus":null,"listHistory":null,"listExchangeInfo":null},{"earnest":null,"isEarnest":"0","earnestType":"","url":null,"digitalRemark":"","site":"山西省阳泉市","personalId":"C201806151609773","userName":"张美玲","phoneNumber":"13994519336","source":null,"sourceName":null,"plannedHouseDeliveryDate":null,"plannedBaseDecorateDate":null,"customerTypeCode":null,"customerTypeCodeName":null,"dealerId":null,"furnitureDemand":null,"customizeTheSpace":null,"gender":null,"genderName":null,"houseId":"ffbe78c1465b97b2398f2e9d327c738d","buildingName":null,"buildingNumber":null,"address":"香榭","statusCode":null,"statusCodeName":null,"associates":null,"associatesName":null,"shopId":null,"shopIdName":null,"area":null,"content":null,"houseType":null,"decorateType":null,"decorateProperties":null,"createDate":"null","houseCreateDate":null,"updateTime":"","specialCustomers":"","antecedentId":"","antecedentPrice":null,"favTextureCode":"","favColorCode":"","number":"","name":"","remark":"","statusMove":null,"statusMoveName":null,"budget":"","listStatus":null,"listHistory":null,"listExchangeInfo":null},{"earnest":null,"isEarnest":"0","earnestType":"","url":null,"digitalRemark":"","site":"山西省阳泉市","personalId":"C201609306936","userName":"杨","phoneNumber":"13994491607","source":null,"sourceName":null,"plannedHouseDeliveryDate":null,"plannedBaseDecorateDate":null,"customerTypeCode":null,"customerTypeCodeName":null,"dealerId":null,"furnitureDemand":null,"customizeTheSpace":null,"gender":null,"genderName":null,"houseId":"ff2a671b12054194ae516f5c87e01d42","buildingName":null,"buildingNumber":null,"address":"恒大新城3-1-902","statusCode":null,"statusCodeName":null,"associates":null,"associatesName":null,"shopId":null,"shopIdName":null,"area":null,"content":null,"houseType":null,"decorateType":null,"decorateProperties":null,"createDate":"null","houseCreateDate":null,"updateTime":"","specialCustomers":"","antecedentId":"","antecedentPrice":null,"favTextureCode":"","favColorCode":"","number":"","name":"","remark":"","statusMove":null,"statusMoveName":null,"budget":"","listStatus":null,"listHistory":null,"listExchangeInfo":null},{"earnest":null,"isEarnest":"0","earnestType":"","url":null,"digitalRemark":"","site":"山西省阳泉市","personalId":"C201609304826","userName":"张","phoneNumber":"13934281095","source":null,"sourceName":null,"plannedHouseDeliveryDate":null,"plannedBaseDecorateDate":null,"customerTypeCode":null,"customerTypeCodeName":null,"dealerId":null,"furnitureDemand":null,"customizeTheSpace":null,"gender":null,"genderName":null,"houseId":"fea9dd40fb6446d6ad2580adc5aa86dc","buildingName":null,"buildingNumber":null,"address":"半山6-1-2001","statusCode":null,"statusCodeName":null,"associates":null,"associatesName":null,"shopId":null,"shopIdName":null,"area":null,"content":null,"houseType":null,"decorateType":null,"decorateProperties":null,"createDate":"null","houseCreateDate":null,"updateTime":"","specialCustomers":"","antecedentId":"","antecedentPrice":null,"favTextureCode":"","favColorCode":"","number":"","name":"","remark":"","statusMove":null,"statusMoveName":null,"budget":"","listStatus":null,"listHistory":null,"listExchangeInfo":null},{"earnest":null,"isEarnest":"0","earnestType":"","url":null,"digitalRemark":"","site":"山西省阳泉市","personalId":"C201605375717","userName":"倪","phoneNumber":"13935311772","source":null,"sourceName":null,"plannedHouseDeliveryDate":null,"plannedBaseDecorateDate":null,"customerTypeCode":null,"customerTypeCodeName":null,"dealerId":null,"furnitureDemand":null,"customizeTheSpace":null,"gender":null,"genderName":null,"houseId":"fc5c5107848644fd9a368345691ee509","buildingName":null,"buildingNumber":null,"address":"水电处3-2-2602","statusCode":null,"statusCodeName":null,"associates":null,"associatesName":null,"shopId":null,"shopIdName":null,"area":null,"content":null,"houseType":null,"decorateType":null,"decorateProperties":null,"createDate":"null","houseCreateDate":null,"updateTime":"","specialCustomers":"","antecedentId":"","antecedentPrice":null,"favTextureCode":"","favColorCode":"","number":"","name":"","remark":"","statusMove":null,"statusMoveName":null,"budget":"","listStatus":null,"listHistory":null,"listExchangeInfo":null},{"earnest":null,"isEarnest":"0","earnestType":"","url":null,"digitalRemark":"","site":"山西省阳泉市","personalId":"C201810011031954","userName":"王","phoneNumber":"13935324363","source":null,"sourceName":null,"plannedHouseDeliveryDate":null,"plannedBaseDecorateDate":null,"customerTypeCode":null,"customerTypeCodeName":null,"dealerId":null,"furnitureDemand":null,"customizeTheSpace":null,"gender":null,"genderName":null,"houseId":"fb55f5e3c0ffec4f5d332cfb1f532ae8","buildingName":null,"buildingNumber":null,"address":"御康","statusCode":null,"statusCodeName":null,"associates":null,"associatesName":null,"shopId":null,"shopIdName":null,"area":null,"content":null,"houseType":null,"decorateType":null,"decorateProperties":null,"createDate":"null","houseCreateDate":null,"updateTime":"","specialCustomers":"","antecedentId":"","antecedentPrice":null,"favTextureCode":"","favColorCode":"","number":"","name":"","remark":"","statusMove":null,"statusMoveName":null,"budget":"","listStatus":null,"listHistory":null,"listExchangeInfo":null},{"earnest":null,"isEarnest":"0","earnestType":"","url":null,"digitalRemark":"","site":"山西省阳泉市","personalId":"C201805211552360","userName":"胡","phoneNumber":"13935363022","source":null,"sourceName":null,"plannedHouseDeliveryDate":null,"plannedBaseDecorateDate":null,"customerTypeCode":null,"customerTypeCodeName":null,"dealerId":null,"furnitureDemand":null,"customizeTheSpace":null,"gender":null,"genderName":null,"houseId":"fb32c735410d5e5ec157014070fd92ce","buildingName":null,"buildingNumber":null,"address":"漾泉小区","statusCode":null,"statusCodeName":null,"associates":null,"associatesName":null,"shopId":null,"shopIdName":null,"area":null,"content":null,"houseType":null,"decorateType":null,"decorateProperties":null,"createDate":"null","houseCreateDate":null,"updateTime":"","specialCustomers":"","antecedentId":"","antecedentPrice":null,"favTextureCode":"","favColorCode":"","number":"","name":"","remark":"","statusMove":null,"statusMoveName":null,"budget":"","listStatus":null,"listHistory":null,"listExchangeInfo":null},{"earnest":null,"isEarnest":"0","earnestType":"","url":null,"digitalRemark":"","site":"山西省阳泉市","personalId":"C2018040008825","userName":"候璇","phoneNumber":"13934033978","source":null,"sourceName":null,"plannedHouseDeliveryDate":null,"plannedBaseDecorateDate":null,"customerTypeCode":null,"customerTypeCodeName":null,"dealerId":null,"furnitureDemand":null,"customizeTheSpace":null,"gender":null,"genderName":null,"houseId":"f8fdcb6aa28943c4bd62fb9feb916620","buildingName":null,"buildingNumber":null,"address":"燕竹花园","statusCode":null,"statusCodeName":null,"associates":null,"associatesName":null,"shopId":null,"shopIdName":null,"area":null,"content":null,"houseType":null,"decorateType":null,"decorateProperties":null,"createDate":"null","houseCreateDate":null,"updateTime":"","specialCustomers":"","antecedentId":"","antecedentPrice":null,"favTextureCode":"","favColorCode":"","number":"","name":"","remark":"","statusMove":null,"statusMoveName":null,"budget":"","listStatus":null,"listHistory":null,"listExchangeInfo":null},{"earnest":null,"isEarnest":"0","earnestType":"","url":null,"digitalRemark":"","site":"山西省阳泉市","personalId":"C20170994975","userName":"王海林","phoneNumber":"13934288868","source":null,"sourceName":null,"plannedHouseDeliveryDate":null,"plannedBaseDecorateDate":null,"customerTypeCode":null,"customerTypeCodeName":null,"dealerId":null,"furnitureDemand":null,"customizeTheSpace":null,"gender":null,"genderName":null,"houseId":"f87c435f200a42859f1b2ba071303bbf","buildingName":null,"buildingNumber":null,"address":"怡园小区","statusCode":null,"statusCodeName":null,"associates":null,"associatesName":null,"shopId":null,"shopIdName":null,"area":null,"content":null,"houseType":null,"decorateType":null,"decorateProperties":null,"createDate":"null","houseCreateDate":null,"updateTime":"","specialCustomers":"","antecedentId":"","antecedentPrice":null,"favTextureCode":"","favColorCode":"","number":"","name":"","remark":"","statusMove":null,"statusMoveName":null,"budget":"","listStatus":null,"listHistory":null,"listExchangeInfo":null},{"earnest":null,"isEarnest":"0","earnestType":"","url":null,"digitalRemark":"","site":"山西省阳泉市","personalId":"C201707120705","userName":"先生","phoneNumber":"13935363322","source":null,"sourceName":null,"plannedHouseDeliveryDate":null,"plannedBaseDecorateDate":null,"customerTypeCode":null,"customerTypeCodeName":null,"dealerId":null,"furnitureDemand":null,"customizeTheSpace":null,"gender":null,"genderName":null,"houseId":"f776a7c18709458ebe1470837c17c0d9","buildingName":null,"buildingNumber":null,"address":"恒大新城10-1-16","statusCode":null,"statusCodeName":null,"associates":null,"associatesName":null,"shopId":null,"shopIdName":null,"area":null,"content":null,"houseType":null,"decorateType":null,"decorateProperties":null,"createDate":"null","houseCreateDate":null,"updateTime":"","specialCustomers":"","antecedentId":"","antecedentPrice":null,"favTextureCode":"","favColorCode":"","number":"","name":"","remark":"","statusMove":null,"statusMoveName":null,"budget":"","listStatus":null,"listHistory":null,"listExchangeInfo":null}]
     * att : {"totalRows":280,"index":0,"totalPage":28,"rowsInPage":10,"offset":0,"indexDisplay":1,"nearPages":[0,1,2,3,4]}
     */

    private AttBean att;
    private List<DataBean> data;

    public AttBean getAtt() {
        return att;
    }

    public void setAtt(AttBean att) {
        this.att = att;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class AttBean implements Serializable{
        /**
         * totalRows : 280
         * index : 0
         * totalPage : 28
         * rowsInPage : 10
         * offset : 0
         * indexDisplay : 1
         * nearPages : [0,1,2,3,4]
         */

        private int index;
        private int totalPage;

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        public int getTotalPage() {
            return totalPage;
        }

        public void setTotalPage(int totalPage) {
            this.totalPage = totalPage;
        }
    }

    public static class DataBean implements Serializable{
        /**
         * earnest : null
         * isEarnest : 0
         * earnestType :
         * url : null
         * digitalRemark :
         * site : 山西省阳泉市
         * personalId : VA201810000051
         * userName : 门店-样品
         * phoneNumber : 13934030856
         * source : null
         * sourceName : null
         * plannedHouseDeliveryDate : null
         * plannedBaseDecorateDate : null
         * customerTypeCode : null
         * customerTypeCodeName : null
         * dealerId : null
         * furnitureDemand : null
         * customizeTheSpace : null
         * gender : null
         * genderName : null
         * houseId : cc369049a7004297b5bee8f284916f18
         * buildingName : null
         * buildingNumber : null
         * address : null
         * statusCode : null
         * statusCodeName : null
         * associates : null
         * associatesName : null
         * shopId : null
         * shopIdName : null
         * area : null
         * content : null
         * houseType : null
         * decorateType : null
         * decorateProperties : null
         * createDate : null
         * houseCreateDate : null
         * updateTime :
         * specialCustomers :
         * antecedentId :
         * antecedentPrice : null
         * favTextureCode :
         * favColorCode :
         * number :
         * name :
         * remark :
         * statusMove : null
         * statusMoveName : null
         * budget :
         * listStatus : null
         * listHistory : null
         * listExchangeInfo : null
         */

        private Object earnest;
        private String isEarnest;
        private String earnestType;
        private Object url;
        private String digitalRemark;
        private String site;
        private String personalId;
        private String userName;
        private String phoneNumber;
        private Object source;
        private Object sourceName;
        private Object plannedHouseDeliveryDate;
        private Object plannedBaseDecorateDate;
        private Object customerTypeCode;
        private Object customerTypeCodeName;
        private Object dealerId;
        private Object furnitureDemand;
        private Object customizeTheSpace;
        private Object gender;
        private Object genderName;
        private String houseId;
        private Object buildingName;
        private Object buildingNumber;
        private String address;
        private Object statusCode;
        private Object statusCodeName;
        private Object associates;
        private Object associatesName;
        private Object shopId;
        private Object shopIdName;
        private Object area;
        private Object content;
        private Object houseType;
        private Object decorateType;
        private Object decorateProperties;
        private String createDate;
        private Object houseCreateDate;
        private String updateTime;
        private String specialCustomers;
        private String antecedentId;
        private Object antecedentPrice;
        private String favTextureCode;
        private String favColorCode;
        private String number;
        private String name;
        private String remark;
        private Object statusMove;
        private Object statusMoveName;
        private String budget;
        private Object listStatus;
        private Object listHistory;
        private Object listExchangeInfo;

        public Object getEarnest() {
            return earnest;
        }

        public void setEarnest(Object earnest) {
            this.earnest = earnest;
        }

        public String getIsEarnest() {
            return isEarnest;
        }

        public void setIsEarnest(String isEarnest) {
            this.isEarnest = isEarnest;
        }

        public String getEarnestType() {
            return earnestType;
        }

        public void setEarnestType(String earnestType) {
            this.earnestType = earnestType;
        }

        public Object getUrl() {
            return url;
        }

        public void setUrl(Object url) {
            this.url = url;
        }

        public String getDigitalRemark() {
            return digitalRemark;
        }

        public void setDigitalRemark(String digitalRemark) {
            this.digitalRemark = digitalRemark;
        }

        public String getSite() {
            return site;
        }

        public void setSite(String site) {
            this.site = site;
        }

        public String getPersonalId() {
            return personalId;
        }

        public void setPersonalId(String personalId) {
            this.personalId = personalId;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getPhoneNumber() {
            return phoneNumber;
        }

        public void setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
        }

        public Object getSource() {
            return source;
        }

        public void setSource(Object source) {
            this.source = source;
        }

        public Object getSourceName() {
            return sourceName;
        }

        public void setSourceName(Object sourceName) {
            this.sourceName = sourceName;
        }

        public Object getPlannedHouseDeliveryDate() {
            return plannedHouseDeliveryDate;
        }

        public void setPlannedHouseDeliveryDate(Object plannedHouseDeliveryDate) {
            this.plannedHouseDeliveryDate = plannedHouseDeliveryDate;
        }

        public Object getPlannedBaseDecorateDate() {
            return plannedBaseDecorateDate;
        }

        public void setPlannedBaseDecorateDate(Object plannedBaseDecorateDate) {
            this.plannedBaseDecorateDate = plannedBaseDecorateDate;
        }

        public Object getCustomerTypeCode() {
            return customerTypeCode;
        }

        public void setCustomerTypeCode(Object customerTypeCode) {
            this.customerTypeCode = customerTypeCode;
        }

        public Object getCustomerTypeCodeName() {
            return customerTypeCodeName;
        }

        public void setCustomerTypeCodeName(Object customerTypeCodeName) {
            this.customerTypeCodeName = customerTypeCodeName;
        }

        public Object getDealerId() {
            return dealerId;
        }

        public void setDealerId(Object dealerId) {
            this.dealerId = dealerId;
        }

        public Object getFurnitureDemand() {
            return furnitureDemand;
        }

        public void setFurnitureDemand(Object furnitureDemand) {
            this.furnitureDemand = furnitureDemand;
        }

        public Object getCustomizeTheSpace() {
            return customizeTheSpace;
        }

        public void setCustomizeTheSpace(Object customizeTheSpace) {
            this.customizeTheSpace = customizeTheSpace;
        }

        public Object getGender() {
            return gender;
        }

        public void setGender(Object gender) {
            this.gender = gender;
        }

        public Object getGenderName() {
            return genderName;
        }

        public void setGenderName(Object genderName) {
            this.genderName = genderName;
        }

        public String getHouseId() {
            return houseId;
        }

        public void setHouseId(String houseId) {
            this.houseId = houseId;
        }

        public Object getBuildingName() {
            return buildingName;
        }

        public void setBuildingName(Object buildingName) {
            this.buildingName = buildingName;
        }

        public Object getBuildingNumber() {
            return buildingNumber;
        }

        public void setBuildingNumber(Object buildingNumber) {
            this.buildingNumber = buildingNumber;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public Object getStatusCode() {
            return statusCode;
        }

        public void setStatusCode(Object statusCode) {
            this.statusCode = statusCode;
        }

        public Object getStatusCodeName() {
            return statusCodeName;
        }

        public void setStatusCodeName(Object statusCodeName) {
            this.statusCodeName = statusCodeName;
        }

        public Object getAssociates() {
            return associates;
        }

        public void setAssociates(Object associates) {
            this.associates = associates;
        }

        public Object getAssociatesName() {
            return associatesName;
        }

        public void setAssociatesName(Object associatesName) {
            this.associatesName = associatesName;
        }

        public Object getShopId() {
            return shopId;
        }

        public void setShopId(Object shopId) {
            this.shopId = shopId;
        }

        public Object getShopIdName() {
            return shopIdName;
        }

        public void setShopIdName(Object shopIdName) {
            this.shopIdName = shopIdName;
        }

        public Object getArea() {
            return area;
        }

        public void setArea(Object area) {
            this.area = area;
        }

        public Object getContent() {
            return content;
        }

        public void setContent(Object content) {
            this.content = content;
        }

        public Object getHouseType() {
            return houseType;
        }

        public void setHouseType(Object houseType) {
            this.houseType = houseType;
        }

        public Object getDecorateType() {
            return decorateType;
        }

        public void setDecorateType(Object decorateType) {
            this.decorateType = decorateType;
        }

        public Object getDecorateProperties() {
            return decorateProperties;
        }

        public void setDecorateProperties(Object decorateProperties) {
            this.decorateProperties = decorateProperties;
        }

        public String getCreateDate() {
            return createDate;
        }

        public void setCreateDate(String createDate) {
            this.createDate = createDate;
        }

        public Object getHouseCreateDate() {
            return houseCreateDate;
        }

        public void setHouseCreateDate(Object houseCreateDate) {
            this.houseCreateDate = houseCreateDate;
        }

        public String getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(String updateTime) {
            this.updateTime = updateTime;
        }

        public String getSpecialCustomers() {
            return specialCustomers;
        }

        public void setSpecialCustomers(String specialCustomers) {
            this.specialCustomers = specialCustomers;
        }

        public String getAntecedentId() {
            return antecedentId;
        }

        public void setAntecedentId(String antecedentId) {
            this.antecedentId = antecedentId;
        }

        public Object getAntecedentPrice() {
            return antecedentPrice;
        }

        public void setAntecedentPrice(Object antecedentPrice) {
            this.antecedentPrice = antecedentPrice;
        }

        public String getFavTextureCode() {
            return favTextureCode;
        }

        public void setFavTextureCode(String favTextureCode) {
            this.favTextureCode = favTextureCode;
        }

        public String getFavColorCode() {
            return favColorCode;
        }

        public void setFavColorCode(String favColorCode) {
            this.favColorCode = favColorCode;
        }

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public Object getStatusMove() {
            return statusMove;
        }

        public void setStatusMove(Object statusMove) {
            this.statusMove = statusMove;
        }

        public Object getStatusMoveName() {
            return statusMoveName;
        }

        public void setStatusMoveName(Object statusMoveName) {
            this.statusMoveName = statusMoveName;
        }

        public String getBudget() {
            return budget;
        }

        public void setBudget(String budget) {
            this.budget = budget;
        }

        public Object getListStatus() {
            return listStatus;
        }

        public void setListStatus(Object listStatus) {
            this.listStatus = listStatus;
        }

        public Object getListHistory() {
            return listHistory;
        }

        public void setListHistory(Object listHistory) {
            this.listHistory = listHistory;
        }

        public Object getListExchangeInfo() {
            return listExchangeInfo;
        }

        public void setListExchangeInfo(Object listExchangeInfo) {
            this.listExchangeInfo = listExchangeInfo;
        }
    }
}
