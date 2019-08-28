package com.holike.crm.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by wqj on 2018/6/25.
 * 主动营销
 */

public class ActiveMarketBean implements Serializable {
    /**
     * countTotal : 21
     * dataList : [{"area":"晋蒙","countsComplete":"14","day":"8","dealerName":"晋中","name":"梁富杰","time":"6.28-6.29"},{"area":"晋蒙","countsComplete":"6","day":"4","dealerName":"介休","name":"梁富杰","time":"5.27-6.27"},{"area":"晋蒙","countsComplete":"1","day":"5","dealerName":"交城","name":"梁富杰","time":"5.27-6.27"}]
     * dayTotal : 17
     * isActive : 0
     * isSelect : 1
     * selectData : [{"cityCode":1000,"name":"销售一部"},{"cityCode":2000,"name":"销售二部"},{"cityCode":3000,"name":"销售三部"},{"cityCode":4000,"name":"销售四部"}]
     * selectName : 销售一部
     * timeData : [{"name":"六月","time":"06"},{"name":"查询日期","time":"-1"}]
     */

    private int countTotal;
    private int dayTotal;
    private int isActive;
    private int isSelect;
    private String selectName;
    private List<DataListBean> dataList;
    private List<OrderRankingBean.SelectDataBean> selectData;
    private List<TimeDataBean> timeData;

    public int getCountTotal() {
        return countTotal;
    }

    public void setCountTotal(int countTotal) {
        this.countTotal = countTotal;
    }

    public int getDayTotal() {
        return dayTotal;
    }

    public void setDayTotal(int dayTotal) {
        this.dayTotal = dayTotal;
    }

    public int getIsActive() {
        return isActive;
    }

    public void setIsActive(int isActive) {
        this.isActive = isActive;
    }

    public int getIsSelect() {
        return isSelect;
    }

    public void setIsSelect(int isSelect) {
        this.isSelect = isSelect;
    }

    public String getSelectName() {
        return selectName;
    }

    public void setSelectName(String selectName) {
        this.selectName = selectName;
    }

    public List<DataListBean> getDataList() {
        return dataList;
    }

    public void setDataList(List<DataListBean> dataList) {
        this.dataList = dataList;
    }

    public List<OrderRankingBean.SelectDataBean> getSelectData() {
        return selectData;
    }

    public void setSelectData(List<OrderRankingBean.SelectDataBean> selectData) {
        this.selectData = selectData;
    }

    public List<TimeDataBean> getTimeData() {
        return timeData;
    }

    public void setTimeData(List<TimeDataBean> timeData) {
        this.timeData = timeData;
    }

    public static class DataListBean implements Serializable {
        /**
         * area : 晋蒙
         * countsComplete : 14
         * day : 8
         * dealerName : 晋中
         * name : 梁富杰
         * time : 6.28-6.29
         */

        private String area;
        private String countsComplete;
        private String day;
        private String dealerName;
        private String name;
        private String time;

        public String getArea() {
            return area;
        }

        public void setArea(String area) {
            this.area = area;
        }

        public String getCountsComplete() {
            return countsComplete;
        }

        public void setCountsComplete(String countsComplete) {
            this.countsComplete = countsComplete;
        }

        public String getDay() {
            return day;
        }

        public void setDay(String day) {
            this.day = day;
        }

        public String getDealerName() {
            return dealerName;
        }

        public void setDealerName(String dealerName) {
            this.dealerName = dealerName;
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

    public static class TimeDataBean implements Serializable {
        /**
         * name : 六月
         * time : 06
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
