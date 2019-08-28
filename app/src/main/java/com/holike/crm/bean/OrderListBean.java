package com.holike.crm.bean;

import java.io.Serializable;

/**
 * Created by wqj on 2018/3/1.
 * 订单列表bean
 */

public class OrderListBean implements Serializable, MultiItem {
    /**
     * createDate : 2018-01-22
     * name : 杨华
     * orderId : BZ13004201-1801220009
     * orderStatusName : 待设计审核
     * orderTypeName : 标准订单
     * phone : 13870442098
     * roomNumber :
     * village : 书香琴苑35-2-302
     */

    private String createDate;
    private String name;
    private String orderId;
    private String orderStatusName;
    private String orderTypeName;
    private String phone;
    private String roomNumber;
    private String village;
    private String spaceRoom;

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
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

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public String getVillage() {
        return village;
    }

    public void setVillage(String village) {
        this.village = village;
    }

    public String getSpaceRoom() {
        return spaceRoom;
    }

    @Override
    public int getItemType() {
        return 1;
    }
}
