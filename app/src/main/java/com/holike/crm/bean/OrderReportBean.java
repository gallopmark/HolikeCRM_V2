package com.holike.crm.bean;

import android.text.TextUtils;

import java.io.Serializable;
import java.util.List;

/**
 * Created by wqj on 2018/3/7.
 * 订金交易情况bean
 */

public class OrderReportBean implements Serializable {

    /**
     * depositList : [{"isChange":37511,"area":"全国","cityCode":"","complete":"","countstoday":1,"countstotal":19,"isClick":0,"moneytoday":10000,"moneytotal":136337,"name":"冯良波","rank":0,"target":"","type":1},{"isChange":37511,"area":"销售一部","cityCode":"1000","complete":"1000","countstoday":0,"countstotal":1,"isClick":1,"moneytoday":0,"moneytotal":20000,"name":"舒文进","rank":1,"target":"1000","type":1},{"isChange":37511,"area":"销售二部","cityCode":"2000","complete":"2000","countstoday":0,"countstotal":4,"isClick":1,"moneytoday":0,"moneytotal":58171,"name":"周家红","rank":1,"target":"2000","type":1},{"isChange":37511,"area":"销售三部","cityCode":"3000","complete":"3000","countstoday":1,"countstotal":10,"isClick":1,"moneytoday":10000,"moneytotal":42666,"name":"章峰海","rank":1,"target":"3000","type":1},{"isChange":37511,"area":"销售四部","cityCode":"4000","complete":"4000","countstoday":0,"countstotal":3,"isClick":1,"moneytoday":0,"moneytotal":15000,"name":"陈少华","rank":1,"target":"4000","type":1},{"isChange":37511,"area":"广州市场","cityCode":"5000","complete":"5000","countstoday":0,"countstotal":1,"isClick":1,"moneytoday":0,"moneytotal":500,"name":"罗绍舜","rank":1,"target":"5000","type":1},{"isChange":37511,"area":"上海市场","cityCode":"6000","complete":"6000","countstoday":0,"countstotal":0,"isClick":1,"moneytoday":0,"moneytotal":0,"name":"李阳","rank":1,"target":"6000","type":1}]
     * isEdit : 测试内容o467
     * selectData : [{"name":1,"time":1}]
     * timeData : {"day":25,"describe":"426活动定金交易报表","endTime":"1524672000","startTime":"1522598400"}
     * time:
     */

    private String isEdit;
    private String time;
    private TimeDataBean timeData;
    private List<DepositListBean> depositList;
    private List<SelectDataBean> selectData;

    public String getIsEdit() {
        return isEdit;
    }

    public void setIsEdit(String isEdit) {
        this.isEdit = isEdit;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public TimeDataBean getTimeData() {
        return timeData;
    }

    public void setTimeData(TimeDataBean timeData) {
        this.timeData = timeData;
    }

    public List<DepositListBean> getDepositList() {
        return depositList;
    }

    public void setDepositList(List<DepositListBean> depositList) {
        this.depositList = depositList;
    }

    public List<SelectDataBean> getSelectData() {
        return selectData;
    }

    public void setSelectData(List<SelectDataBean> selectData) {
        this.selectData = selectData;
    }

    public static class TimeDataBean implements Serializable {
        /**
         * day : 25
         * describe : 426活动定金交易报表
         * endTime : 1524672000
         * startTime : 1522598400
         */

        private int day;
        private String describe;
        private String endTime;
        private String startTime;

        public int getDay() {
            return day;
        }

        public void setDay(int day) {
            this.day = day;
        }

        public String getDescribe() {
            return describe;
        }

        public void setDescribe(String describe) {
            this.describe = describe;
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

    public static class DepositListBean implements Serializable {
        /**
         * isChange : 37511
         * area : 全国
         * cityCode :
         * complete :
         * countstoday : 1
         * countstotal : 19
         * isClick : 0
         * moneytoday : 10000
         * moneytotal : 136337
         * name : 冯良波
         * rank : 0
         * target :
         * type : 1
         */

        private String area;
        private String cityCode;
        private String complete;
        private int countstoday;
        private int countstotal;
        private int isClick;
        private float moneytoday;
        private float moneytotal;
        private String name;
        private String target;
        private int rank;
        private int type;
        private int isChange;

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

        public String getComplete() {
            return TextUtils.isEmpty(complete) ? "-" : complete;
        }

        public void setComplete(String complete) {
            this.complete = complete;
        }

        public int getCountstoday() {
            return countstoday;
        }

        public void setCountstoday(int countstoday) {
            this.countstoday = countstoday;
        }

        public int getCountstotal() {
            return countstotal;
        }

        public void setCountstotal(int countstotal) {
            this.countstotal = countstotal;
        }

        public int getIsClick() {
            return isClick;
        }

        public void setIsClick(int isClick) {
            this.isClick = isClick;
        }

        public float getMoneytoday() {
            return moneytoday;
        }

        public void setMoneytoday(float moneytoday) {
            this.moneytoday = moneytoday;
        }

        public float getMoneytotal() {
            return moneytotal;
        }

        public void setMoneytotal(float moneytotal) {
            this.moneytotal = moneytotal;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getTarget() {
            return TextUtils.isEmpty(target) ? "-" : target;
        }

        public void setTarget(String target) {
            this.target = target;
        }

        public int getRank() {
            return rank;
        }

        public void setRank(int rank) {
            this.rank = rank;
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

    public static class SelectDataBean implements Serializable {
        /**
         * name : 1
         * time : 1
         */

        private String name;
        private String time;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }
    }
}
