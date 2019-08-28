package com.holike.crm.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by wqj on 2018/5/14.
 * 建店报表
 */

public class BuildStoreBean implements Serializable {
    /**
     * percentData : [{"area":"销售一部","cityCode":"023","countsComplete":"23","countsTarget":"50","isClick":0,"name":"舒文进","percent":"46%","rank":"-","type":2},{"area":"晋蒙","cityCode":"017","countsComplete":"21","countsTarget":"51","isClick":0,"name":"李东升","percent":"41.2%","rank":"2","type":2},{"area":"陕宁","cityCode":"018","countsComplete":"","countsTarget":"","isClick":0,"name":"潘东升","percent":"","rank":"-","type":2},{"area":"贵州","cityCode":"019","countsComplete":"","countsTarget":"","isClick":0,"name":"鲁喜平","percent":"","rank":"-","type":2},{"area":"重庆","cityCode":"020","countsComplete":"2","countsTarget":"3","isClick":0,"name":"陶成汉","percent":"66.7%","rank":"1","type":2},{"area":"豫南","cityCode":"021","countsComplete":"","countsTarget":"","isClick":0,"name":"李泽鸿","percent":"","rank":"-","type":2},{"area":"豫北","cityCode":"022","countsComplete":"","countsTarget":"","isClick":0,"name":"杨志坤","percent":"","rank":"-","type":2},{"area":"甘青新藏","cityCode":"023","countsComplete":"","countsTarget":"","isClick":0,"name":"张立仁","percent":"","rank":"-","type":2}]
     * quarter : 4-6月目标
     * timeData : 数据更新时间：2018-05-14 09:26
     */

    private String quarter;
    private String timeData;
    private List<PercentDataBean> percentData;

    public String getQuarter() {
        return quarter;
    }

    public void setQuarter(String quarter) {
        this.quarter = quarter;
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

    public static class PercentDataBean implements Serializable {
        /**
         * area : 销售一部
         * cityCode : 023
         * countsComplete : 23
         * countsTarget : 50
         * isClick : 0
         * name : 舒文进
         * percent : 46%
         * rank : -
         * type : 2
         */

        private String area;
        private String cityCode;
        private String countsComplete;
        private String countsTarget;
        private int isClick;
        private String name;
        private String percent;
        private String rank;
        private int type;

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

        public String getPercent() {
            return percent;
        }

        public void setPercent(String percent) {
            this.percent = percent;
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
    }
}
