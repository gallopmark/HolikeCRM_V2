package com.holike.crm.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by wqj on 2018/5/25.
 * 原态板占比bean
 */

public class OriginalBoardBean implements Serializable {
    /**
     * percentData : [{"area":"四大区","cityCode":"","isChange":0,"isClick":0,"name":"那崇奇","percent":"30.9%","type":2},{"area":"销售一部","cityCode":"1000","isChange":0,"isClick":1,"name":"舒文进","percent":"27%","type":1},{"area":"销售二部","cityCode":"2000","isChange":0,"isClick":1,"name":"周家红","percent":"39.7%","type":1},{"area":"销售三部","cityCode":"3000","isChange":0,"isClick":1,"name":"章峰海","percent":"33%","type":1},{"area":"销售四部","cityCode":"4000","isChange":0,"isClick":1,"name":"陈少华","percent":"27.1%","type":1},{"area":"浙江","cityCode":"001","isChange":0,"isClick":1,"name":"罗绍舜","percent":"33.9%","type":2},{"area":"湖南","cityCode":"002","isChange":0,"isClick":1,"name":"周志华","percent":"24.1%","type":2},{"area":"江西","cityCode":"003","isChange":0,"isClick":1,"name":"张振有","percent":"19.4%","type":2},{"area":"江苏","cityCode":"004","isChange":0,"isClick":1,"name":"杨俊杰","percent":"42.2%","type":2},{"area":"东三省","cityCode":"005","isChange":0,"isClick":1,"name":"孙华龙","percent":"14.3%","type":2},{"area":"河北","cityCode":"006","isChange":0,"isClick":1,"name":"刘谊","percent":"28.7%","type":2},{"area":"鲁东","cityCode":"007","isChange":0,"isClick":1,"name":"练有为","percent":"58.1%","type":2},{"area":"鲁北","cityCode":"008","isChange":0,"isClick":1,"name":"肖翔","percent":"41%","type":2},{"area":"天津北京","cityCode":"009","isChange":0,"isClick":1,"name":"张君","percent":"63.1%","type":2},{"area":"安徽","cityCode":"010","isChange":0,"isClick":1,"name":"杨鹏程（暂代）","percent":"31.9%","type":2},{"area":"云南","cityCode":"011","isChange":0,"isClick":1,"name":"黄为明","percent":"1%","type":2},{"area":"广西","cityCode":"012","isChange":0,"isClick":1,"name":"叶文发","percent":"22.8%","type":2},{"area":"四川","cityCode":"013","isChange":0,"isClick":1,"name":"上官显儒","percent":"7.5%","type":2},{"area":"湖北","cityCode":"014","isChange":0,"isClick":1,"name":"陈迎","percent":"15.2%","type":2},{"area":"广东海南","cityCode":"015","isChange":0,"isClick":1,"name":"刘辉顺","percent":"54.5%","type":2},{"area":"福建","cityCode":"016","isChange":0,"isClick":1,"name":"胡成","percent":"14%","type":2},{"area":"晋蒙","cityCode":"017","isChange":0,"isClick":1,"name":"李东升","percent":"20.8%","type":2},{"area":"陕宁","cityCode":"018","isChange":0,"isClick":1,"name":"潘东升","percent":"10.4%","type":2},{"area":"贵州","cityCode":"019","isChange":0,"isClick":1,"name":"鲁喜平","percent":"15.6%","type":2},{"area":"重庆","cityCode":"020","isChange":0,"isClick":1,"name":"陶成汉","percent":"13.1%","type":2},{"area":"豫南","cityCode":"021","isChange":0,"isClick":1,"name":"李泽鸿","percent":"29%","type":2},{"area":"豫北","cityCode":"022","isChange":0,"isClick":1,"name":"杨志坤","percent":"27.8%","type":2},{"area":"甘青新藏","cityCode":"023","isChange":0,"isClick":1,"name":"张立仁","percent":"44.1%","type":2}]
     * time : 五月
     * timeData : 2018年01月01日-2018年05月25日
     * dealerData : [{"countsComplete":"测试内容e35c","month":"测试内容kg3k"}]
     * isDealer : 测试内容72v6
     */

    private String time;
    private String timeData;
    private List<PercentDataBean> percentData;
    private String isDealer;
    private List<DealerDataBean> dealerData;

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

    public String getIsDealer() {
        return isDealer;
    }

    public void setIsDealer(String isDealer) {
        this.isDealer = isDealer;
    }

    public List<DealerDataBean> getDealerData() {
        return dealerData;
    }

    public void setDealerData(List<DealerDataBean> dealerData) {
        this.dealerData = dealerData;
    }

    public static class PercentDataBean implements Serializable {
        /**
         * area : 四大区
         * cityCode :
         * isChange : 0
         * isClick : 0
         * name : 那崇奇
         * percent : 30.9%
         * type : 2
         */

        private String area;
        private String cityCode;
        private int isChange;
        private int isClick;
        private String name;
        private String percent;
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

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }
    }

    public static class DealerDataBean implements Serializable {
        public DealerDataBean() {
        }

        public DealerDataBean(String month, String countsComplete) {
            this.countsComplete = countsComplete;
            this.month = month;
        }

        private String countsComplete;
        private String month;

        public String getCountsComplete() {
            return countsComplete;
        }

        public void setCountsComplete(String countsComplete) {
            this.countsComplete = countsComplete;
        }

        public String getMonth() {
            return month;
        }

        public void setMonth(String month) {
            this.month = month;
        }
    }
}
