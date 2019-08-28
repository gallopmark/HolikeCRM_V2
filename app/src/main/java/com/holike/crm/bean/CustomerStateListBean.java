package com.holike.crm.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wqj on 2018/8/22.
 * 客户状态列表bean
 */

public class CustomerStateListBean implements Serializable {
    /**
     * date : [{"adress":"测试内容fxs0","appointmentTime":"02-08","appointmentToInstallDate":"测试内容8u8f","bookOrderDate":"测试内容824v","createDate":"2018-08-20 14:17","depositAmount":"测试内容e734","designerId":"测试内容1639","endingInstallDate":"测试内容r9ow","houseInfoBean":"413cfca3257a41478fa2ee08d1f0f07a","isShow":"测试内容ad24","lastRarin":"测试内容4wnv","leaveReason":"测试内容65q6","measureTime":"测试内容f41v","nextDate":"测试内容54c1","optTime":"02-08","personalId":"C2018080062178","phone":"13655532221","saleId":"测试内容kba5","salesAmount":"测试内容ux59","sourceName":"网络资源","statuMove":"测试内容5h13","updateDate":"2018-08-21 00:00","userId":"测试内容y1u4","userName":"还是覅和"}]
     * size : 1
     * yesterdayAdd : 0
     */

    private long size;
    private long yesterdayAdd;
    private List<DateBean> date;

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public long getYesterdayAdd() {
        return yesterdayAdd;
    }

    public void setYesterdayAdd(long yesterdayAdd) {
        this.yesterdayAdd = yesterdayAdd;
    }

    public List<DateBean> getDate() {
        if (date == null) return new ArrayList<>();
        return date;
    }

    public void setDate(List<DateBean> date) {
        this.date = date;
    }

    public static class DateBean implements Serializable, MultiItem {
        /**
         * adress : 测试内容fxs0
         * appointmentTime : 02-08
         * appointmentToInstallDate : 测试内容8u8f
         * bookOrderDate : 测试内容824v
         * createDate : 2018-08-20 14:17
         * depositAmount : 测试内容e734
         * designerId : 测试内容1639
         * endingInstallDate : 测试内容r9ow
         * houseInfoBean : 413cfca3257a41478fa2ee08d1f0f07a
         * isShow : 测试内容ad24
         * lastRarin : 测试内容4wnv
         * leaveReason : 测试内容65q6
         * measureTime : 测试内容f41v
         * nextDate : 测试内容54c1
         * optTime : 02-08
         * personalId : C2018080062178
         * phone : 13655532221
         * saleId : 测试内容kba5
         * salesAmount : 测试内容ux59
         * sourceName : 网络资源
         * statuMove : 测试内容5h13
         * updateDate : 2018-08-21 00:00
         * userId : 测试内容y1u4
         * userName : 还是覅和
         */

        private String adress;
        private String appointmentTime;
        private String appointmentToInstallDate;
        private String bookOrderDate;
        private String createDate;
        private String depositAmount;
        private String designerId;
        private String endingInstallDate;
        private String houseId;
        private String isShow;
        private String lastRarin;
        private String leaveReason;
        private String measureTime;
        private String nextDate;
        private String optTime;
        private String personalId;
        private String phone;
        private String saleId;
        private String salesAmount;
        private String sourceName;
        private String statuMove;
        private String updateDate;
        private String userId;
        private String userName;
        private String orderDate;
        private String houseSaleId;
        private String definiteTime;
        private String install;
        private String nextTime;
        private String name;
        private String isRed;
        private String houseSalesAmount;
        private String show;

        public String countdown;        //保护倒计时

        public String getShow() {
            return show;
        }

        public void setShow(String show) {
            this.show = show;
        }

        public String getHouseSaleAmount() {
            return houseSalesAmount;
        }

        public void setHouseSaleAmount(String houseSaleAmount) {
            this.houseSalesAmount = houseSaleAmount;
        }

        public String getIsRed() {
            return isRed;
        }

        public void setIsRed(String isRed) {
            this.isRed = isRed;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getNextTime() {
            return nextTime;
        }

        public void setNextTime(String nextTime) {
            this.nextTime = nextTime;
        }

        public String getInstall() {
            return install;
        }

        public void setInstall(String install) {
            this.install = install;
        }

        public String getDefiniteTime() {
            return definiteTime;
        }

        public void setDefiniteTime(String definiteTime) {
            this.definiteTime = definiteTime;
        }

        public String getHouseSaleId() {
            return houseSaleId;
        }

        public void setHouseSaleId(String houseSaleId) {
            this.houseSaleId = houseSaleId;
        }

        public String getAdress() {
            return adress;
        }

        public void setAdress(String adress) {
            this.adress = adress;
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

        public String getCreateDate() {
            return createDate;
        }

        public void setCreateDate(String createDate) {
            this.createDate = createDate;
        }

        public String getDepositAmount() {
            return depositAmount;
        }

        public void setDepositAmount(String depositAmount) {
            this.depositAmount = depositAmount;
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

        public String getHouseId() {
            return houseId;
        }

        public void setHouseId(String houseId) {
            this.houseId = houseId;
        }

        public String getIsShow() {
            return isShow;
        }

        public void setIsShow(String isShow) {
            this.isShow = isShow;
        }

        public String getLastRarin() {
            return lastRarin;
        }

        public void setLastRarin(String lastRarin) {
            this.lastRarin = lastRarin;
        }

        public String getLeaveReason() {
            return leaveReason;
        }

        public void setLeaveReason(String leaveReason) {
            this.leaveReason = leaveReason;
        }

        public String getMeasureTime() {
            return measureTime;
        }

        public void setMeasureTime(String measureTime) {
            this.measureTime = measureTime;
        }

        public String getNextDate() {
            return nextDate;
        }

        public void setNextDate(String nextDate) {
            this.nextDate = nextDate;
        }

        public String getOptTime() {
            return optTime;
        }

        public void setOptTime(String optTime) {
            this.optTime = optTime;
        }

        public String getPersonalId() {
            return personalId;
        }

        public void setPersonalId(String personalId) {
            this.personalId = personalId;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getSaleId() {
            return saleId;
        }

        public void setSaleId(String saleId) {
            this.saleId = saleId;
        }

        public String getSalesAmount() {
            return salesAmount;
        }

        public void setSalesAmount(String salesAmount) {
            this.salesAmount = salesAmount;
        }

        public String getSourceName() {
            return sourceName;
        }

        public void setSourceName(String sourceName) {
            this.sourceName = sourceName;
        }

        public String getStatuMove() {
            return statuMove;
        }

        public void setStatuMove(String statuMove) {
            this.statuMove = statuMove;
        }

        public String getUpdateDate() {
            return updateDate;
        }

        public void setUpdateDate(String updateDate) {
            this.updateDate = updateDate;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getOrderDate() {
            return orderDate;
        }

        public void setOrderDate(String orderDate) {
            this.orderDate = orderDate;
        }

        @Override
        public int getItemType() {
            return 1;
        }
    }
}
