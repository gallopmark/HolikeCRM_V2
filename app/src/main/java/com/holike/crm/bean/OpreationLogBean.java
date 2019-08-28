package com.holike.crm.bean;

import org.parceler.Parcel;

@Parcel
public class OpreationLogBean {


    /**
     * hour : 17:40
     * operationLogContent : 系统炸单成功
     * spaceName : 玄关306328
     * userName : system
     * year : 2018-11-29
     */

    public String hour;
    public String operationLogContent;
    public String spaceName;
    public String userName;
    public String year;

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public String getOperationLogContent() {
        return operationLogContent;
    }

    public void setOperationLogContent(String operationLogContent) {
        this.operationLogContent = operationLogContent;
    }

    public String getSpaceName() {
        return spaceName;
    }

    public void setSpaceName(String spaceName) {
        this.spaceName = spaceName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }
}
