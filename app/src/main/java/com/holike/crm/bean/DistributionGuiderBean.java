package com.holike.crm.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by wqj on 2018/9/19.
 */

public class DistributionGuiderBean implements Serializable {


    private List<GuideBean> guide;

    public List<GuideBean> getGuide() {
        return guide;
    }

    public void setGuide(List<GuideBean> guide) {
        this.guide = guide;
    }

    public static class GuideBean {
        /**
         * userId : 13109100-0002
         * available : 1
         * city : null
         * createBy : 13109100
         * createTime : 1539160654000
         * email : null
         * fileId :
         * gender : 3
         * partnerId : 13109100
         * password : 34F85CA80EC353D3052B8A2D3973A0C5
         * serialNumber : null
         * shopId : null
         * staffId :
         * telphone :
         * updateBy : null
         * updateTime : 1539160858000
         * userCode : null
         * userName : 测试
         * userType : 02
         * virtualUserId : null
         */

        private String userId;
        private String available;
        private Object city;
        private String createBy;
        private long createTime;
        private Object email;
        private String fileId;
        private String gender;
        private String partnerId;
        private String password;
        private Object serialNumber;
        private Object shopId;
        private String staffId;
        private String telphone;
        private Object updateBy;
        private long updateTime;
        private Object userCode;
        private String userName;
        private String userType;
        private Object virtualUserId;

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getAvailable() {
            return available;
        }

        public void setAvailable(String available) {
            this.available = available;
        }

        public Object getCity() {
            return city;
        }

        public void setCity(Object city) {
            this.city = city;
        }

        public String getCreateBy() {
            return createBy;
        }

        public void setCreateBy(String createBy) {
            this.createBy = createBy;
        }

        public long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(long createTime) {
            this.createTime = createTime;
        }

        public Object getEmail() {
            return email;
        }

        public void setEmail(Object email) {
            this.email = email;
        }

        public String getFileId() {
            return fileId;
        }

        public void setFileId(String fileId) {
            this.fileId = fileId;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public String getPartnerId() {
            return partnerId;
        }

        public void setPartnerId(String partnerId) {
            this.partnerId = partnerId;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public Object getSerialNumber() {
            return serialNumber;
        }

        public void setSerialNumber(Object serialNumber) {
            this.serialNumber = serialNumber;
        }

        public Object getShopId() {
            return shopId;
        }

        public void setShopId(Object shopId) {
            this.shopId = shopId;
        }

        public String getStaffId() {
            return staffId;
        }

        public void setStaffId(String staffId) {
            this.staffId = staffId;
        }

        public String getTelphone() {
            return telphone;
        }

        public void setTelphone(String telphone) {
            this.telphone = telphone;
        }

        public Object getUpdateBy() {
            return updateBy;
        }

        public void setUpdateBy(Object updateBy) {
            this.updateBy = updateBy;
        }

        public long getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(long updateTime) {
            this.updateTime = updateTime;
        }

        public Object getUserCode() {
            return userCode;
        }

        public void setUserCode(Object userCode) {
            this.userCode = userCode;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getUserType() {
            return userType;
        }

        public void setUserType(String userType) {
            this.userType = userType;
        }

        public Object getVirtualUserId() {
            return virtualUserId;
        }

        public void setVirtualUserId(Object virtualUserId) {
            this.virtualUserId = virtualUserId;
        }
    }
}
