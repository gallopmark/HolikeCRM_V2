package com.holike.crm.bean;

import com.google.gson.Gson;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by wqj on 2018/2/28.
 * 新增客户bean
 */

public class CustomerBean implements Serializable {

    private String area;
    private String associates;
    private String budget;
    private String buildingName;
    private String buildingNumber;
    private String decorateProperties;
    private String decorateType;
    private String earnest;
    private String favColorCode;
    private String favTextureCode;
    private String gender;
    private String houseType;
    private String isEarnest;
    private String name;
    private String number;
    private String personalId;
    private String phoneNumber;
    private String remark;
    private String shopId;
    private String source;
    private String specialCustomers;
    private String userName;
    private String antecedentPrice;
    private String earnestType;
    private String decorateDate;
    private String floorDate;
    private String checkbulidingCode;
    private String decorationProgress;
    private String customizeTheSpace;
    private String furnitureDemand;

    public String getFurnitureDemand() {
        return furnitureDemand;
    }

    public void setFurnitureDemand(String furnitureDemand) {
        this.furnitureDemand = furnitureDemand;
    }

    public String getCustomizeTheSpace() {
        return customizeTheSpace;
    }

    public void setCustomizeTheSpace(String customizeTheSpace) {
        this.customizeTheSpace = customizeTheSpace;
    }

    private Map<String, String> url;

    public String getCheckbulidingCode() {
        return checkbulidingCode;
    }

    public void setCheckbulidingCode(String checkbulidingCode) {
        this.checkbulidingCode = checkbulidingCode;
    }

    public String getDecorationProgress() {
        return decorationProgress;
    }

    public void setDecorationProgress(String decorationProgress) {
        this.decorationProgress = decorationProgress;
    }

    public String getDecorateDate() {
        return decorateDate;
    }

    public void setDecorateDate(String decorateDate) {
        this.decorateDate = decorateDate;
    }

    public String getFloorDate() {
        return floorDate;
    }

    public void setFloorDate(String floorDate) {
        this.floorDate = floorDate;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getAssociates() {
        return associates;
    }

    public void setAssociates(String associates) {
        this.associates = associates;
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


    public String getDecorateProperties() {
        return decorateProperties;
    }

    public void setDecorateProperties(String decorateProperties) {
        this.decorateProperties = decorateProperties;
    }

    public String getDecorateType() {
        return decorateType;
    }

    public void setDecorateType(String decorateType) {
        this.decorateType = decorateType;
    }

    public String getEarnest() {
        return earnest;
    }

    public void setEarnest(String earnest) {
        this.earnest = earnest;
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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getHouseType() {
        return houseType;
    }

    public void setHouseType(String houseType) {
        this.houseType = houseType;
    }

    public String getIsEarnest() {
        return isEarnest;
    }

    public void setIsEarnest(String isEarnest) {
        this.isEarnest = isEarnest;
    }

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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getSpecialCustomers() {
        return specialCustomers;
    }

    public void setSpecialCustomers(String specialCustomers) {
        this.specialCustomers = specialCustomers;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAntecedentPrice() {
        return antecedentPrice;
    }

    public void setAntecedentPrice(String antecedentPrice) {
        this.antecedentPrice = antecedentPrice;
    }

    public Map<String, String> getUrl() {
        return url;
    }

    public void setUrl(Map<String, String> url) {
        this.url = url;
    }

    public String getEarnestType() {
        return earnestType;
    }

    public void setEarnestType(String earnestType) {
        this.earnestType = earnestType;
    }

    public CustomerBean() {
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("\"area\":\"").append(area).append('\"');
        sb.append(",\"associates\":\"").append(associates).append('\"');
        sb.append(",\"budget\":\"").append(budget).append('\"');
        sb.append(",\"buildingName\":\"").append(buildingName).append('\"');
        sb.append(",\"buildingNumber\":\"").append(buildingNumber).append('\"');
        sb.append(",\"decorateProperties\":\"").append(decorateProperties).append('\"');
        sb.append(",\"decorateType\":\"").append(decorateType).append('\"');
        sb.append(",\"antecedentPrice\":\"").append(antecedentPrice).append('\"');
        sb.append(",\"favColorCode\":\"").append(favColorCode).append('\"');
        sb.append(",\"favTextureCode\":\"").append(favTextureCode).append('\"');
        sb.append(",\"gender\":\"").append(gender).append('\"');
        sb.append(",\"houseType\":\"").append(houseType).append('\"');
        sb.append(",\"isEarnest\":\"").append(isEarnest).append('\"');
        sb.append(",\"name\":\"").append(name).append('\"');
        sb.append(",\"number\":\"").append(number).append('\"');
        sb.append(",\"personalId\":\"").append(personalId).append('\"');
        sb.append(",\"phoneNumber\":\"").append(phoneNumber).append('\"');
        sb.append(",\"remark\":\"").append(remark).append('\"');
        sb.append(",\"shopId\":\"").append(shopId).append('\"');
        sb.append(",\"source\":\"").append(source).append('\"');
        sb.append(",\"specialCustomers\":\"").append(specialCustomers).append('\"');
        sb.append(",\"userName\":\"").append(userName).append('\"');
        sb.append(",\"earnestType\":\"").append(earnestType).append('\"');
//        sb.append(",\"decorateDate\":\"").append(decorateDate).append('\"');
//        sb.append(",\"floorDate\":\"").append(floorDate).append('\"');
//        sb.append(",\"checkbulidingCode\":\"").append(checkbulidingCode).append('\"');
//        sb.append(",\"decorationProgress\":\"").append(decorationProgress).append('\"');
        sb.append(",\"customizeTheSpace\":\"").append(customizeTheSpace).append('\"');
        sb.append(",\"furnitureDemand\":\"").append(furnitureDemand).append('\"');
        sb.append(",\"url\":").append(new Gson().toJson(url, Map.class));
        sb.append('}');
        return sb.toString();
    }

    public CustomerBean(String area, String associates, String budget, String buildingName, String buildingNumber, String decorateProperties, String decorateType, String antecedentPrice, String favColorCode, String favTextureCode, String gender, String houseType, String isEarnest, String name, String number, String personalId, String phoneNumber, String remark, String shopId, String source, String specialCustomers, String userName,String earnestType,
                        String decorateDate,String floorDate,String decorationProgress,String checkbulidingCode,
                        String customizeTheSpace,String furnitureDemand,
                        Map<String, String> url) {
        this.area = area;
        this.associates = associates;
        this.budget = budget;
        this.buildingName = buildingName;
        this.buildingNumber = buildingNumber;
        this.decorateProperties = decorateProperties;
        this.decorateType = decorateType;
        this.antecedentPrice = antecedentPrice;
        this.favColorCode = favColorCode;
        this.favTextureCode = favTextureCode;
        this.gender = gender;
        this.houseType = houseType;
        this.isEarnest = isEarnest;
        this.name = name;
        this.number = number;
        this.personalId = personalId;
        this.phoneNumber = phoneNumber;
        this.remark = remark;
        this.shopId = shopId;
        this.source = source;
        this.specialCustomers = specialCustomers;
        this.userName = userName;
        this.url = url;
        this.decorateDate = decorateDate;
        this.floorDate = floorDate;
        this.earnestType = earnestType;
        this.decorationProgress = decorationProgress;
        this.checkbulidingCode = checkbulidingCode;
        this.customizeTheSpace = customizeTheSpace;
        this.furnitureDemand = furnitureDemand;
    }
}
