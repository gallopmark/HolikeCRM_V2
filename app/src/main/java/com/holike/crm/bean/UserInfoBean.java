package com.holike.crm.bean;

import java.io.Serializable;

/**
 * Created by wqj on 2018/3/5.
 * 个人信息bean
 */

public class UserInfoBean implements Serializable {
    /**
     * createDate : 2017-12-26
     * headImgUrl : https://file.holike.com/b368d62d19edc831ef08f064c062cf68.jpg
     * name : 宗芳
     * phone : 13155889339
     * customerTypeName : 测试内容2o5z
     * userId : 测试内容rlca
     */

    private String createDate;
    private String headImgUrl;
    private String name;
    private String phone;
    private String customerTypeName;

    private String userId;

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getHeadImgUrl() {
        return headImgUrl;
    }

    public void setHeadImgUrl(String headImgUrl) {
        this.headImgUrl = headImgUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCustomerTypeName() {
        return customerTypeName;
    }

    public void setCustomerTypeName(String customerTypeName) {
        this.customerTypeName = customerTypeName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
