package com.holike.crm.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by wqj on 2018/5/31.
 * 终端大检查
 */

public class TerminalCheckBean implements Serializable {
    /**
     * percentData : [{"area":"四大区","cityCode":"","countsComplete":"150","countsNotComplete":"150","countsTarget":"300","date":1527669763000,"isClick":0,"name":"那崇奇","percent":"50%","rank":"-","type":1},{"area":"销售一部","cityCode":"1000","countsComplete":"-","countsNotComplete":"0","countsTarget":"-","isClick":1,"name":"舒文进","percent":"-","rank":"-","type":1},{"area":"销售二部","cityCode":"2000","countsComplete":"-","countsNotComplete":"0","countsTarget":"-","isClick":1,"name":"周家红","percent":"-","rank":"-","type":1},{"area":"销售三部","cityCode":"3000","countsComplete":"-","countsNotComplete":"0","countsTarget":"-","isClick":1,"name":"章峰海","percent":"-","rank":"-","type":1},{"area":"销售四部","cityCode":"4000","countsComplete":"-","countsNotComplete":"0","countsTarget":"-","isClick":1,"name":"陈少华","percent":"-","rank":"-","type":1},{"area":"浙江","cityCode":"001","countsComplete":"-","countsNotComplete":"0","countsTarget":"-","isClick":0,"name":"罗绍舜","percent":"-","rank":"-","type":1},{"area":"湖南","cityCode":"002","countsComplete":"-","countsNotComplete":"0","countsTarget":"-","isClick":0,"name":"罗绍舜","percent":"-","rank":"-","type":1},{"area":"江西","cityCode":"003","countsComplete":"-","countsNotComplete":"0","countsTarget":"-","isClick":0,"name":"张振有","percent":"-","rank":"-","type":1},{"area":"江苏","cityCode":"004","countsComplete":"-","countsNotComplete":"0","countsTarget":"-","isClick":0,"name":"石小明","percent":"-","rank":"-","type":1},{"area":"东三省","cityCode":"005","countsComplete":"-","countsNotComplete":"0","countsTarget":"-","isClick":0,"name":"孙华龙","percent":"-","rank":"-","type":1},{"area":"河北","cityCode":"006","countsComplete":"-","countsNotComplete":"0","countsTarget":"-","isClick":0,"name":"刘谊","percent":"-","rank":"-","type":1},{"area":"鲁东","cityCode":"007","countsComplete":"-","countsNotComplete":"0","countsTarget":"-","isClick":0,"name":"练有为","percent":"-","rank":"-","type":1},{"area":"鲁北","cityCode":"008","countsComplete":"-","countsNotComplete":"0","countsTarget":"-","isClick":0,"name":"肖翔","percent":"-","rank":"-","type":1},{"area":"天津北京","cityCode":"009","countsComplete":"-","countsNotComplete":"0","countsTarget":"-","isClick":0,"name":"张君","percent":"-","rank":"-","type":1},{"area":"安徽","cityCode":"010","countsComplete":"-","countsNotComplete":"0","countsTarget":"-","isClick":0,"name":"杨鹏程（暂代）","percent":"-","rank":"-","type":1},{"area":"云南","cityCode":"011","countsComplete":"-","countsNotComplete":"0","countsTarget":"-","isClick":0,"name":"黄为明","percent":"-","rank":"-","type":1},{"area":"广西","cityCode":"012","countsComplete":"-","countsNotComplete":"0","countsTarget":"-","isClick":0,"name":"叶文发","percent":"-","rank":"-","type":1},{"area":"四川","cityCode":"013","countsComplete":"-","countsNotComplete":"0","countsTarget":"-","isClick":0,"name":"上官显儒","percent":"-","rank":"-","type":1},{"area":"湖北","cityCode":"014","countsComplete":"-","countsNotComplete":"0","countsTarget":"-","isClick":0,"name":"陈迎","percent":"-","rank":"-","type":1},{"area":"广东海南","cityCode":"015","countsComplete":"-","countsNotComplete":"0","countsTarget":"-","isClick":0,"name":"刘辉顺","percent":"-","rank":"-","type":1},{"area":"福建","cityCode":"016","countsComplete":"-","countsNotComplete":"0","countsTarget":"-","isClick":0,"name":"胡成","percent":"-","rank":"-","type":1},{"area":"晋蒙","cityCode":"017","countsComplete":"-","countsNotComplete":"0","countsTarget":"-","isClick":0,"name":"李东升","percent":"-","rank":"-","type":1},{"area":"陕宁","cityCode":"018","countsComplete":"-","countsNotComplete":"0","countsTarget":"-","isClick":0,"name":"潘东升","percent":"-","rank":"-","type":1},{"area":"贵州","cityCode":"019","countsComplete":"-","countsNotComplete":"0","countsTarget":"-","isClick":0,"name":"鲁喜平","percent":"-","rank":"-","type":1},{"area":"重庆","cityCode":"020","countsComplete":"-","countsNotComplete":"0","countsTarget":"-","isClick":0,"name":"陶成汉","percent":"-","rank":"-","type":1},{"area":"豫南","cityCode":"021","countsComplete":"-","countsNotComplete":"0","countsTarget":"-","isClick":0,"name":"李泽鸿","percent":"-","rank":"-","type":1},{"area":"豫北","cityCode":"022","countsComplete":"-","countsNotComplete":"0","countsTarget":"-","isClick":0,"name":"杨志坤","percent":"-","rank":"-","type":1},{"area":"甘青新藏","cityCode":"023","countsComplete":"-","countsNotComplete":"0","countsTarget":"-","date":1527669763000,"isClick":0,"name":"张立仁","percent":"-","rank":"-","type":1}]
     * timeData : 数据更新时间：2018-05-30 16:42
     */

    private String timeData;
    private List<PercentDataBean> percentData;

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
         * area : 四大区
         * cityCode :
         * countsComplete : 150
         * countsNotComplete : 150
         * countsTarget : 300
         * date : 1527669763000
         * isClick : 0
         * name : 那崇奇
         * percent : 50%
         * rank : -
         * type : 1
         */

        private String area;
        private String cityCode;
        private String countsComplete;
        private String countsNotComplete;
        private String countsTarget;
        private long date;
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

        public String getCountsNotComplete() {
            return countsNotComplete;
        }

        public void setCountsNotComplete(String countsNotComplete) {
            this.countsNotComplete = countsNotComplete;
        }

        public String getCountsTarget() {
            return countsTarget;
        }

        public void setCountsTarget(String countsTarget) {
            this.countsTarget = countsTarget;
        }

        public long getDate() {
            return date;
        }

        public void setDate(long date) {
            this.date = date;
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
