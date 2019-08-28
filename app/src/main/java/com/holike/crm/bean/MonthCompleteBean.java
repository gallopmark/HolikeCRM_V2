package com.holike.crm.bean;

import java.io.Serializable;

/**
 * Created by wqj on 2018/4/18.
 */

public class MonthCompleteBean implements Serializable {
    /**
     * area : 全国
     * day : 03/27-04/26
     * depositPercent :
     * month : 4月
     */

    private String area;
    private String day;
    private String depositPercent;
    private String month;

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getDepositPercent() {
        return depositPercent;
    }

    public void setDepositPercent(String depositPercent) {
        this.depositPercent = depositPercent;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }
}
