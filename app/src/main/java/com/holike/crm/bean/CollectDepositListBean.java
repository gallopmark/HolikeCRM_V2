package com.holike.crm.bean;

import java.io.Serializable;

/**
 * Created by wqj on 2018/8/20.
 * 查询未交定金客户信息列表
 */

public class CollectDepositListBean implements Serializable,MultiItem {
    /**
     * address : 江苏省盐城市阜宁县人民路001
     * createDate : 2018-08-13 14:58
     * houseInfoBean : 3e0b9aa7bd8645bba8e1b0febe7323ae
     * personalId : C2018080057856
     * phoneNumber : 15022200001
     * userName : 测试呢
     */

    private String address;
    private String createDate;
    private String houseId;
    private String personalId;
    private String phoneNumber;
    private String userName;

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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public int getItemType() {
        return 0;
    }
}
