package com.holike.crm.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by wqj on 2018/4/12.
 * 订金转化报表bean
 */

public class TranslateReportBean implements Serializable {


    /**
     * percentData : [{"rank":"1","area":"福建","cityCode":"016","counts":2,"isClick":0,"money":2,"name":"胡成","percent":"100%","type":3},{"rank":"1","area":"福州市","cityCode":"350100","counts":0,"isClick":1,"money":0,"name":"赵峡梁","percent":"0%","type":3},{"rank":"1","area":"厦门市","cityCode":"350200","counts":0,"isClick":1,"money":0,"name":"刘振鹏","percent":"0%","type":3},{"rank":"1","area":"莆田市","cityCode":"350300","counts":0,"isClick":1,"money":0,"name":"刘振鹏","percent":"0%","type":3},{"rank":"1","area":"三明市","cityCode":"350400","counts":0,"isClick":1,"money":0,"name":"赵峡梁","percent":"0%","type":3},{"rank":"1","area":"泉州市","cityCode":"350500","counts":2,"isClick":1,"money":2,"name":"刘振鹏","percent":"100%","type":3},{"rank":"1","area":"漳州市","cityCode":"350600","counts":0,"isClick":1,"money":0,"name":"赵峡梁","percent":"0%","type":3},{"rank":"1","area":"南平市","cityCode":"350700","counts":0,"isClick":1,"money":0,"name":"赵峡梁","percent":"0%","type":3},{"rank":"1","area":"龙岩市","cityCode":"350800","counts":0,"isClick":1,"money":0,"name":"刘振鹏","percent":"0%","type":3},{"rank":"1","area":"宁德市","cityCode":"350900","counts":0,"isClick":1,"money":0,"name":"赵峡梁","percent":"0%","type":3}]
     * timeData : {"day":51,"endTime":"1523501885","startTime":"1519059661"}
     */

    private TimeDataBean timeData;
    private List<PercentDataBean> percentData;

    public TimeDataBean getTimeData() {
        return timeData;
    }

    public void setTimeData(TimeDataBean timeData) {
        this.timeData = timeData;
    }

    public List<PercentDataBean> getPercentData() {
        return percentData;
    }

    public void setPercentData(List<PercentDataBean> percentData) {
        this.percentData = percentData;
    }

    public static class TimeDataBean implements Serializable {
        /**
         * day : 51
         * endTime : 1523501885
         * startTime : 1519059661
         */

        private int day;
        private String endTime;
        private String startTime;

        public int getDay() {
            return day;
        }

        public void setDay(int day) {
            this.day = day;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }
    }

    public static class PercentDataBean implements Serializable {
        /**
         * rank : 1
         * area : 福建
         * cityCode : 016
         * counts : 2
         * isClick : 0
         * money : 2
         * name : 胡成
         * percent : 100%
         * notPercent:100
         * type : 3
         * isChange : 1
         */

        private String rank;
        private String area;
        private String cityCode;
        private int counts;
        private int isClick;
        private int money;
        private String name;
        private String percent;
        private String notPercent;
        private int type;
        private int isChange;

        public String getRank() {
            return rank;
        }

        public void setRank(String rank) {
            this.rank = rank;
        }

        public String getArea() {
            return area;
        }

        public void setArea(String area) {
            this.area = area;
        }

        public String getCityCode() {
            return cityCode;
        }

        public void setCityCode(String cityCode) {
            this.cityCode = cityCode;
        }

        public int getCounts() {
            return counts;
        }

        public void setCounts(int counts) {
            this.counts = counts;
        }

        public int getIsClick() {
            return isClick;
        }

        public void setIsClick(int isClick) {
            this.isClick = isClick;
        }

        public int getMoney() {
            return money;
        }

        public void setMoney(int money) {
            this.money = money;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPercent() {
            return percent;
        }

        public void setPercent(String percent) {
            this.percent = percent;
        }

        public String getNotPercent() {
            return notPercent;
        }

        public void setNotPercent(String notPercent) {
            this.notPercent = notPercent;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getIsChange() {
            return isChange;
        }

        public void setIsChange(int isChange) {
            this.isChange = isChange;
        }
    }
}
