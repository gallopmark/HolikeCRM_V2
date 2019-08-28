package com.holike.crm.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by wqj on 2018/5/18.
 * 业绩报表bean
 */

public class PerformanceBean implements Serializable {

    /**
     * percentData : [{"countsTodayComplete": "测试内容ske5","area":"晋蒙","cityCode":"017","countsComplete":"3514.09","countsTarget":"","isClick":0,"name":"李东升","percentComplete":"","percentThan":"21.39%","rank":"-","type":1}]
     * time : 五月
     * timeData : 2018年01月01日-2018年05月18日
     * dealerData : {"complete":"测试内容257x","dealerList":[{"achievement":"测试内容w8f5","month":"测试内容5r9w"}],"percent":"测试内容i786","percentComplete":"测试内容3w7s","target":"测试内容1276"}
     * isDealer : 测试内容l93v
     * selectData : [{"name":"测试内容4e06","selectTime":"测试内容2tpy"}]
     */

    private String time;
    private String timeData;
    private List<PercentDataBean> percentData;
    private List<SelectDataBean> selectData;
    private DealerDataBean dealerData;
    private String isDealer;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTimeData() {
        return timeData;
    }

    public void setTimeData(String timeData) {
        this.timeData = timeData;
    }

    public List<PercentDataBean> getPercentData() {
        return percentData;
    }

    public void setPercentData(List<PercentDataBean> percentData) {
        this.percentData = percentData;
    }

    public List<SelectDataBean> getSelectData() {
        return selectData;
    }

    public void setSelectData(List<SelectDataBean> selectData) {
        this.selectData = selectData;
    }

    public DealerDataBean getDealerData() {
        return dealerData;
    }

    public void setDealerData(DealerDataBean dealerData) {
        this.dealerData = dealerData;
    }

    public String getIsDealer() {
        return isDealer;
    }

    public void setIsDealer(String isDealer) {
        this.isDealer = isDealer;
    }

    public static class PercentDataBean implements Serializable {
        /**
         * countsTodayComplete: "测试内容ske5"
         * area : 晋蒙
         * cityCode : 017
         * countsComplete : 3514.09
         * countsTarget :
         * isClick : 0
         * name : 李东升
         * percentComplete :
         * percentThan : 21.39%
         * rank : -
         * type : 1
         * isChange : 1
         */

        private String countsTodayComplete;
        private String area;
        private String cityCode;
        private String countsComplete;
        private String countsTarget;
        private int isClick;
        private String name;
        private String percentComplete;
        private String percentThan;
        private String rank;
        private int type;
        private int isChange;

        public String getCountsTodayComplete() {
            return countsTodayComplete;
        }

        public void setCountsTodayComplete(String countsTodayComplete) {
            this.countsTodayComplete = countsTodayComplete;
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

        public String getCountsComplete() {
            return countsComplete;
        }

        public void setCountsComplete(String countsComplete) {
            this.countsComplete = countsComplete;
        }

        public String getCountsTarget() {
            return countsTarget;
        }

        public void setCountsTarget(String countsTarget) {
            this.countsTarget = countsTarget;
        }

        public int getIsClick() {
            return isClick;
        }

        public void setIsClick(int isClick) {
            this.isClick = isClick;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPercentComplete() {
            return percentComplete;
        }

        public void setPercentComplete(String percentComplete) {
            this.percentComplete = percentComplete;
        }

        public String getPercentThan() {
            return percentThan;
        }

        public void setPercentThan(String percentThan) {
            this.percentThan = percentThan;
        }

        public String getRank() {
            return rank;
        }

        public void setRank(String rank) {
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
         * selectTime : -1
         * name : 全年
         */

        private int selectTime;
        private String name;
        private String time;

        public int getSelectTime() {
            return selectTime;
        }

        public void setSelectTime(int selectTime) {
            this.selectTime = selectTime;
        }

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

    public static class DealerDataBean implements Serializable {
        /**
         * complete : 测试内容257x
         * dealerList : [{"achievement":"测试内容w8f5","month":"测试内容5r9w"}]
         * percent : 测试内容i786
         * percentComplete : 测试内容3w7s
         * target : 测试内容1276
         * dealerTime : 2018年
         */

        private String complete;
        private String percent;
        private String percentComplete;
        private String target;
        private String dealerTime;
        private List<DealerListBean> dealerList;

        public String getComplete() {
            return complete;
        }

        public void setComplete(String complete) {
            this.complete = complete;
        }

        public String getPercent() {
            return percent;
        }

        public void setPercent(String percent) {
            this.percent = percent;
        }

        public String getPercentComplete() {
            return percentComplete;
        }

        public void setPercentComplete(String percentComplete) {
            this.percentComplete = percentComplete;
        }

        public String getTarget() {
            return target;
        }

        public void setTarget(String target) {
            this.target = target;
        }

        public String getDealerTime() {
            return dealerTime;
        }

        public void setDealerTime(String dealerTime) {
            this.dealerTime = dealerTime;
        }

        public List<DealerListBean> getDealerList() {
            return dealerList;
        }

        public void setDealerList(List<DealerListBean> dealerList) {
            this.dealerList = dealerList;
        }

        public static class DealerListBean implements Serializable {
            /**
             * achievement : 测试内容w8f5
             * month : 测试内容5r9w
             */

            private String achievement;
            private String month;

            public String getAchievement() {
                return achievement;
            }

            public void setAchievement(String achievement) {
                this.achievement = achievement;
            }

            public String getMonth() {
                return month;
            }

            public void setMonth(String month) {
                this.month = month;
            }
        }
    }
}
