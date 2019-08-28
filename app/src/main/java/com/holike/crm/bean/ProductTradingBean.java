package com.holike.crm.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by wqj on 2018/5/15.
 * 成品交易报表bean
 */

public class ProductTradingBean implements Serializable {
    /**
     * percentData : [{"area":"四大区","cityCode":"","countsComplete":"242.4","countsTarget":"1152","isClick":0,"name":"那崇奇","percent":"21.04%","rank":"-","type":1},{"area":"销售一部","cityCode":"1000","countsComplete":"65.39","countsTarget":"287.84","isClick":1,"name":"舒文进","percent":"22.72%","rank":"-","type":1},{"area":"销售二部","cityCode":"2000","countsComplete":"66.08","countsTarget":"292.32","isClick":1,"name":"周家红","percent":"22.61%","rank":"-","type":1},{"area":"销售三部","cityCode":"3000","countsComplete":"51.98","countsTarget":"293.49","isClick":1,"name":"章峰海","percent":"17.71%","rank":"-","type":1},{"area":"销售四部","cityCode":"4000","countsComplete":"58.95","countsTarget":"278.35","isClick":1,"name":"陈少华","percent":"21.18%","rank":"-","type":1},{"area":"浙江","cityCode":"001","countsComplete":"7.48","countsTarget":"63.8","isClick":0,"name":"罗绍舜","percent":"11.73%","rank":"22","type":1},{"area":"湖南","cityCode":"002","countsComplete":"8.06","countsTarget":"70.71","isClick":0,"name":"周志华","percent":"11.4%","rank":"23","type":1},{"area":"江西","cityCode":"003","countsComplete":"14.87","countsTarget":"63.13","isClick":0,"name":"张振有","percent":"23.56%","rank":"6","type":1},{"area":"江苏","cityCode":"004","countsComplete":"21.56","countsTarget":"95.85","isClick":0,"name":"杨俊杰","percent":"22.5%","rank":"10","type":1},{"area":"东三省","cityCode":"005","countsComplete":"9.53","countsTarget":"46.88","isClick":0,"name":"孙华龙","percent":"20.32%","rank":"14","type":1},{"area":"河北","cityCode":"006","countsComplete":"16.65","countsTarget":"74.14","isClick":0,"name":"刘谊","percent":"22.45%","rank":"11","type":1},{"area":"鲁东","cityCode":"007","countsComplete":"10.49","countsTarget":"44.67","isClick":0,"name":"练有为","percent":"23.48%","rank":"7","type":1},{"area":"鲁北","cityCode":"008","countsComplete":"9.18","countsTarget":"39.98","isClick":0,"name":"肖翔","percent":"22.95%","rank":"9","type":1},{"area":"天津北京","cityCode":"009","countsComplete":"3.18","countsTarget":"26.15","isClick":0,"name":"张君","percent":"12.17%","rank":"20","type":1},{"area":"安徽","cityCode":"010","countsComplete":"17.06","countsTarget":"60.5","isClick":0,"name":"杨鹏程（暂代）","percent":"28.19%","rank":"3","type":1},{"area":"云南","cityCode":"011","countsComplete":"6.28","countsTarget":"22.68","isClick":0,"name":"黄为明","percent":"27.68%","rank":"4","type":1},{"area":"广西","cityCode":"012","countsComplete":"5.08","countsTarget":"17.8","isClick":0,"name":"叶文发","percent":"28.54%","rank":"2","type":1},{"area":"四川","cityCode":"013","countsComplete":"16.09","countsTarget":"59.33","isClick":0,"name":"上官显儒","percent":"27.13%","rank":"5","type":1},{"area":"湖北","cityCode":"014","countsComplete":"7.08","countsTarget":"59.22","isClick":0,"name":"陈迎","percent":"11.95%","rank":"21","type":1},{"area":"广东海南","cityCode":"015","countsComplete":"19.34","countsTarget":"82.52","isClick":0,"name":"刘辉顺","percent":"23.44%","rank":"8","type":1},{"area":"福建","cityCode":"016","countsComplete":"5.07","countsTarget":"36.8","isClick":0,"name":"胡成","percent":"13.79%","rank":"18","type":1},{"area":"晋蒙","cityCode":"017","countsComplete":"13.94","countsTarget":"67.51","isClick":0,"name":"李东升","percent":"20.64%","rank":"13","type":1},{"area":"陕宁","cityCode":"018","countsComplete":"7.7","countsTarget":"38.18","isClick":0,"name":"潘东升","percent":"20.17%","rank":"15","type":1},{"area":"贵州","cityCode":"019","countsComplete":"4.24","countsTarget":"33.29","isClick":0,"name":"鲁喜平","percent":"12.74%","rank":"19","type":1},{"area":"重庆","cityCode":"020","countsComplete":"5.68","countsTarget":"28.94","isClick":0,"name":"陶成汉","percent":"19.61%","rank":"16","type":1},{"area":"豫南","cityCode":"021","countsComplete":"6.47","countsTarget":"36.89","isClick":0,"name":"李泽鸿","percent":"17.53%","rank":"17","type":1},{"area":"豫北","cityCode":"022","countsComplete":"18.13","countsTarget":"40.89","isClick":0,"name":"杨志坤","percent":"44.33%","rank":"1","type":1},{"area":"甘青新藏","cityCode":"023","countsComplete":"9.24","countsTarget":"42.14","isClick":0,"name":"张立仁","percent":"21.94%","rank":"12","type":1}]
     * time : 五月
     * timeData : 2018年04月27日-2018年05月26日
     * describe : 测试内容yh22
     * dealerList : [{"countsComplete":"测试内容cx5u","month":"测试内容4ve6"}]
     * isDealer : 测试内容2gc1
     * total : 1
     * activity:
     */

    private String timeData;
    private List<PercentDataBean> percentData;
    private String describe;
    private String activity;
    private List<SelectDataBean> selectData;
    private String isDealer;
    private String time;
    private String total;
    private List<OriginalBoardBean.DealerDataBean> dealerList;


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

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public List<SelectDataBean> getSelectData() {
        return selectData;
    }

    public void setSelectData(List<SelectDataBean> selectData) {
        this.selectData = selectData;
    }

    public String getIsDealer() {
        return isDealer;
    }

    public void setIsDealer(String isDealer) {
        this.isDealer = isDealer;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public List<OriginalBoardBean.DealerDataBean> getDealerList() {
        return dealerList;
    }

    public void setDealerList(List<OriginalBoardBean.DealerDataBean> dealerList) {
        this.dealerList = dealerList;
    }

    public static class PercentDataBean implements Serializable {
        /**
         * area : 四大区
         * cityCode :
         * countsComplete : 242.4
         * countsTarget : 1152
         * isClick : 0
         * name : 那崇奇
         * percent : 21.04%
         * rank : -
         * type : 1
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

    public static class SelectDataBean implements Serializable {
        /**
         * name : 测试内容7in7
         * time : 测试内容ox83
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
