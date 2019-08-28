package com.holike.crm.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by wqj on 2018/3/6.
 * 订单交易对趋势
 */

public class WeekDepositBean implements Serializable {

    /**
     * moneyData : [{"counts":"3","endTime":"3.7","month":"2月","startTime":"3.1","timeStamp":"测试内容jn88","money":0},{"counts":"3","endTime":"3.7","month":"2月","startTime":"3.1","timeStamp":"测试内容jn88","money":466135},{"counts":"3","endTime":"3.7","month":"2月","startTime":"3.1","timeStamp":"测试内容jn88","money":41119.7},{"counts":"3","endTime":"3.7","month":"2月","startTime":"3.1","timeStamp":"测试内容jn88","money":8099.9},{"counts":"3","endTime":"3.7","month":"2月","startTime":"3.1","timeStamp":"测试内容jn88","money":1000},{"counts":"3","endTime":"3.7","month":"2月","startTime":"3.1","timeStamp":"测试内容jn88","money":8000},{"counts":"3","endTime":"3.7","month":"2月","startTime":"3.1","timeStamp":"测试内容jn88","money":14000}]
     * totalCount : 3
     * totalMoney : 538354.60
     */

    private String totalCount;
    private String totalMoney;
    private List<MoneyDataBean> moneyData;

    public String getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(String totalCount) {
        this.totalCount = totalCount;
    }

    public String getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(String totalMoney) {
        this.totalMoney = totalMoney;
    }

    public List<MoneyDataBean> getMoneyData() {
        return moneyData;
    }

    public void setMoneyData(List<MoneyDataBean> moneyData) {
        this.moneyData = moneyData;
    }

    public static class MoneyDataBean implements Serializable {
        /**
         * counts : 3
         * endTime : 3.7
         * month : 2月
         * startTime : 3.1
         * timeStamp : 测试内容jn88
         * money : 0
         */

        private int counts;
        private String endTime;
        private String month;
        private String startTime;
        private String timeStamp;
        private float money;

        public int getCounts() {
            return counts;
        }

        public void setCounts(int counts) {
            this.counts = counts;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public String getMonth() {
            return month;
        }

        public void setMonth(String month) {
            this.month = month;
        }

        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public String getTimeStamp() {
            return timeStamp;
        }

        public void setTimeStamp(String timeStamp) {
            this.timeStamp = timeStamp;
        }

        public float getMoney() {
            return money;
        }

        public void setMoney(float money) {
            this.money = money;
        }
    }
}
