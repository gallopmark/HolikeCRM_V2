package com.holike.crm.bean;

import java.io.Serializable;

/**
 * Created by wqj on 2018/2/28.
 * 客户列表bean
 */

public class CustomerListBean implements Serializable, MultiItem {
    /**
     * antecedentId : 测试内容6331
     * buildingName : 1
     * source : 1
     * sourceName : 1
     * statusCodeName : 测试内容0lb8
     * address : 广东省广州市南沙区护发素几十块
     * createDate : 2018-08-08 15:39
     * houseInfoBean : 533aec7a9cde4865bead20aac2b2c125
     * personalId : C2018080010796
     * phoneNumber : 17088885555
     * updateTime : 2018-08-09
     * userName : 烧烤好的返款
     * StatusMoveName:
     */

    private String antecedentId;
    private String buildingName;
    private String source;
    private String sourceName;
    private String statusCodeName;
    private String address;
    private String createDate;
    private String houseId;
    private String personalId;
    private String phoneNumber;
    private String updateTime;
    private String userName;
    private String statusMoveName;
    private String antecedentPrice;
    private String shopId;
    private String digitalRemark;
    private String site;
    private String dealerId;

    public String getDealerId() {
        return dealerId;
    }

    public void setDealerId(String dealerId) {
        this.dealerId = dealerId;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getDigitalRemark() {
        return digitalRemark;
    }

    public void setDigitalRemark(String digitalRemark) {
        this.digitalRemark = digitalRemark;
    }

    public String getAntecedentId() {
        return antecedentId;
    }

    public void setAntecedentId(String antecedentId) {
        this.antecedentId = antecedentId;
    }

    public String getBuildingName() {
        return buildingName;
    }

    public void setBuildingName(String buildingName) {
        this.buildingName = buildingName;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public String getStatusCodeName() {
        return statusCodeName;
    }

    public void setStatusCodeName(String statusCodeName) {
        this.statusCodeName = statusCodeName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getHouseId() {
        return houseId;
    }

    public void setHouseId(String houseId) {
        this.houseId = houseId;
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

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getStatusMoveName() {
        return statusMoveName;
    }

    public void setStatusMoveName(String statusMoveName) {
        this.statusMoveName = statusMoveName;
    }

    public String getAntecedentPrice() {
        return antecedentPrice;
    }

    public void setAntecedentPrice(String antecedentPrice) {
        this.antecedentPrice = antecedentPrice;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    @Override
    public int getItemType() {
        return 1;
    }
}
