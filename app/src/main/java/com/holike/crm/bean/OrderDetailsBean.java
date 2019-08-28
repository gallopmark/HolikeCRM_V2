package com.holike.crm.bean;

import java.io.Serializable;

/**
 * Created by wqj on 2018/3/1.
 * 订单详情bean
 */

public class OrderDetailsBean implements Serializable {

    /**
     * accountCoefficient : 测试内容k60g
     * buyer : 卓伟
     * createDate : 2018-01-21
     * deliveryDate : 2018-02-28
     * disAmount : 测试内容hf48
     * districtName : 广东-广州-荔湾
     * factoryPriceAfDis : 测试内容2b4j
     * factoryPriceBeDis : 测试内容ao6h
     * isPackage : 0
     * isUrgent : 0
     * name : 罗女士
     * orderId : BZ13000400-1801210006
     * orderStatusName : 待销售审价
     * orderTypeName : 标准订单
     * phone : 18970917926
     * price : 4527.43
     * roomNumber : 测试内容n133
     * salePrice : 测试内容2oc9
     * totalArea : 15.8922
     * village : 万达旅游城D-10-2-2001
     */

    private String accountCoefficient;
    private String buyer;
    private String createDate;
    private String deliveryDate;
    private String disAmount;
    private String districtName;
    private String factoryPriceAfDis;
    private String factoryPriceBeDis;
    private String isPackage;
    private String isUrgent;
    private String name;
    private String orderId;
    private String orderStatusName;
    private String orderTypeName;
    private String phone;
    private String price;
    private String roomNumber;
    private String salePrice;
    private String totalArea;
    private String village;

    private String isFactoryPrice;
    private String isSalePrice;

    public String getIsFactoryPrice() {
        return isFactoryPrice;
    }

    public void setIsFactoryPrice(String isFactoryPrice) {
        this.isFactoryPrice = isFactoryPrice;
    }

    public String getIsSalePrice() {
        return isSalePrice;
    }

    public void setIsSalePrice(String isSalePrice) {
        this.isSalePrice = isSalePrice;
    }

    public String getAccountCoefficient() {
        return accountCoefficient;
    }

    public void setAccountCoefficient(String accountCoefficient) {
        this.accountCoefficient = accountCoefficient;
    }

    public String getBuyer() {
        return buyer;
    }

    public void setBuyer(String buyer) {
        this.buyer = buyer;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public String getDisAmount() {
        return disAmount;
    }

    public void setDisAmount(String disAmount) {
        this.disAmount = disAmount;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public String getFactoryPriceAfDis() {
        return factoryPriceAfDis;
    }

    public void setFactoryPriceAfDis(String factoryPriceAfDis) {
        this.factoryPriceAfDis = factoryPriceAfDis;
    }

    public String getFactoryPriceBeDis() {
        return factoryPriceBeDis;
    }

    public void setFactoryPriceBeDis(String factoryPriceBeDis) {
        this.factoryPriceBeDis = factoryPriceBeDis;
    }

    public String getIsPackage() {
        return isPackage;
    }

    public void setIsPackage(String isPackage) {
        this.isPackage = isPackage;
    }

    public String getIsUrgent() {
        return isUrgent;
    }

    public void setIsUrgent(String isUrgent) {
        this.isUrgent = isUrgent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderStatusName() {
        return orderStatusName;
    }

    public void setOrderStatusName(String orderStatusName) {
        this.orderStatusName = orderStatusName;
    }

    public String getOrderTypeName() {
        return orderTypeName;
    }

    public void setOrderTypeName(String orderTypeName) {
        this.orderTypeName = orderTypeName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public String getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(String salePrice) {
        this.salePrice = salePrice;
    }

    public String getTotalArea() {
        return totalArea;
    }

    public void setTotalArea(String totalArea) {
        this.totalArea = totalArea;
    }

    public String getVillage() {
        return village;
    }

    public void setVillage(String village) {
        this.village = village;
    }
}
