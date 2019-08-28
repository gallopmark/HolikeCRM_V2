package com.holike.crm.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by wqj on 2018/5/31.
 * 新零售
 */

public class NewRetailBean implements Serializable {
    /**
     * depositList : [{"area":"四大区","cityCode":"","countsComplete":"0","isChange":0,"isClick":0,"name":"那崇奇","percent":"-","rank":-1,"target":"","type":-1},{"area":"销售一部","cityCode":"1000","countsComplete":"0","isChange":0,"isClick":1,"name":"舒文进","percent":"-","rank":-1,"target":"","type":1},{"area":"销售二部","cityCode":"2000","countsComplete":"0","isChange":0,"isClick":1,"name":"周家红","percent":"-","rank":-1,"target":"","type":1},{"area":"销售三部","cityCode":"3000","countsComplete":"0","isChange":0,"isClick":1,"name":"章峰海","percent":"-","rank":-1,"target":"","type":1},{"area":"销售四部","cityCode":"4000","countsComplete":"0","isChange":0,"isClick":1,"name":"陈少华","percent":"-","rank":-1,"target":"","type":1},{"area":"浙江","cityCode":"001","countsComplete":"0","isChange":0,"isClick":1,"name":"罗绍舜","percent":"-","rank":-1,"target":"","type":2},{"area":"湖南","cityCode":"002","countsComplete":"0","isChange":0,"isClick":1,"name":"罗绍舜","percent":"-","rank":-1,"target":"","type":2},{"area":"江西","cityCode":"003","countsComplete":"0","isChange":0,"isClick":1,"name":"张振有","percent":"-","rank":-1,"target":"","type":2},{"area":"江苏","cityCode":"004","countsComplete":"0","isChange":0,"isClick":1,"name":"石小明","percent":"-","rank":-1,"target":"","type":2},{"area":"东三省","cityCode":"005","countsComplete":"0","isChange":0,"isClick":1,"name":"孙华龙","percent":"-","rank":-1,"target":"","type":2},{"area":"河北","cityCode":"006","countsComplete":"0","isChange":0,"isClick":1,"name":"刘谊","percent":"-","rank":-1,"target":"","type":2},{"area":"鲁东","cityCode":"007","countsComplete":"0","isChange":0,"isClick":1,"name":"练有为","percent":"-","rank":-1,"target":"","type":2},{"area":"鲁北","cityCode":"008","countsComplete":"12","isChange":0,"isClick":1,"name":"肖翔","percent":"50%","rank":1,"target":"24","type":2},{"area":"天津北京","cityCode":"009","countsComplete":"0","isChange":0,"isClick":1,"name":"张君","percent":"-","rank":-1,"target":"","type":2},{"area":"安徽","cityCode":"010","countsComplete":"0","isChange":0,"isClick":1,"name":"杨鹏程（暂代）","percent":"-","rank":-1,"target":"","type":2},{"area":"云南","cityCode":"011","countsComplete":"0","isChange":0,"isClick":1,"name":"黄为明","percent":"-","rank":-1,"target":"","type":2},{"area":"广西","cityCode":"012","countsComplete":"0","isChange":0,"isClick":1,"name":"叶文发","percent":"-","rank":-1,"target":"","type":2},{"area":"四川","cityCode":"013","countsComplete":"0","isChange":0,"isClick":1,"name":"上官显儒","percent":"-","rank":-1,"target":"","type":2},{"area":"湖北","cityCode":"014","countsComplete":"0","isChange":0,"isClick":1,"name":"陈迎","percent":"-","rank":-1,"target":"","type":2},{"area":"广东海南","cityCode":"015","countsComplete":"0","isChange":0,"isClick":1,"name":"刘辉顺","percent":"-","rank":-1,"target":"","type":2},{"area":"福建","cityCode":"016","countsComplete":"0","isChange":0,"isClick":1,"name":"胡成","percent":"-","rank":-1,"target":"","type":2},{"area":"晋蒙","cityCode":"017","countsComplete":"0","isChange":0,"isClick":1,"name":"李东升","percent":"-","rank":-1,"target":"","type":2},{"area":"陕宁","cityCode":"018","countsComplete":"0","isChange":0,"isClick":1,"name":"潘东升","percent":"-","rank":-1,"target":"","type":2},{"area":"贵州","cityCode":"019","countsComplete":"0","isChange":0,"isClick":1,"name":"鲁喜平","percent":"-","rank":-1,"target":"","type":2},{"area":"重庆","cityCode":"020","countsComplete":"0","isChange":0,"isClick":1,"name":"陶成汉","percent":"-","rank":-1,"target":"","type":2},{"area":"豫南","cityCode":"021","countsComplete":"0","isChange":0,"isClick":1,"name":"李泽鸿","percent":"-","rank":-1,"target":"","type":2},{"area":"豫北","cityCode":"022","countsComplete":"0","isChange":0,"isClick":1,"name":"杨志坤","percent":"-","rank":-1,"target":"","type":2},{"area":"甘青新藏","cityCode":"023","countsComplete":"0","isChange":0,"isClick":1,"name":"张立仁","percent":"-","rank":-1,"target":"","type":2}]
     * selectData : [{"name":"6月","time":1},{"name":"全年","time":2}]
     * timeData : 2018年05月27日-2018年06月26日
     * date:测试内容64td
     */

    private String timeData;
    private String data;
    private List<DepositListBean> depositList;
    private List<SelectDataBean> selectData;

    public String getTimeData() {
        return timeData;
    }

    public void setTimeData(String timeData) {
        this.timeData = timeData;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
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

    public static class DepositListBean implements Serializable {
        /**
         * area : 四大区
         * cityCode :
         * countsComplete : 0
         * isChange : 0
         * isClick : 0
         * name : 那崇奇
         * percent : -
         * rank : -1
         * target :
         * type : -1
         */

        private String area;
        private String cityCode;
        private String countsComplete;
        private int isChange;
        private int isClick;
        private String name;
        private String percent;
        private String rank;
        private String target;
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

        public int getIsChange() {
            return isChange;
        }

        public void setIsChange(int isChange) {
            this.isChange = isChange;
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

        public String getTarget() {
            return target;
        }

        public void setTarget(String target) {
            this.target = target;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }
    }

    public static class SelectDataBean implements Serializable {
        /**
         * name : 6月
         * time : 1
         */

        private String name;
        private int time;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getTime() {
            return time;
        }

        public void setTime(int time) {
            this.time = time;
        }
    }
}
