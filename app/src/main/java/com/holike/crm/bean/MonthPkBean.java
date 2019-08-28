package com.holike.crm.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by wqj on 2018/6/7.
 * 月度pk
 */

public class MonthPkBean implements Serializable {
    /**
     * isProvince : 1
     * timeData:数据更新时间：2018-06-06 16:25
     * percentData : [{"area":"贵州","countsMonthComplete":"16%","countsYearComplete":"5%","name":"鲁喜平","percent":300,"rank":"1","sales":"销售一部"}]
     * provinceData : {"firstThree":[{"money":300,"province":"贵州"},{"money":255,"province":"晋蒙"}],"group":"A","monthComplete":"2","monthPercent":"5%","monthTarget":"3","percent":300,"preCity":"","rankAft":"2","rankPre":"1","yearComplete":"4","yearPercent":"16%","yearTarget":"5"}
     */

    private int isProvince;
    private String timeData;
    private ProvinceDataBean provinceData;
    private List<PercentDataBean> percentData;

    /**
     * provinceData : {"yearPercent":"","yearTarget":"","yearComplete":"","monthTarget":"","monthComplete":"","monthPercent":"","percent":"","group":"","rankPre":"","rankAft":"","preCity":"","firstThree":[]}
     * timeData : 数据更新时间：2018-06-06 16:25
     * percentData : [{"area":"晋蒙","date":1528273549000,"countsYearComplete":"10%","name":"李东升","rank":"2","percent":255,"sales":"销售一部","countsMonthComplete":"15%"},{"area":"贵州","date":1528342874000,"countsYearComplete":"5%","name":"鲁喜平","rank":"1","percent":300,"sales":"销售一部","countsMonthComplete":"16%"}]
     */


    public int getIsProvince() {
        return isProvince;
    }

    public void setIsProvince(int isProvince) {
        this.isProvince = isProvince;
    }

    public String getTimeData() {
        return timeData;
    }

    public void setTimeData(String timeData) {
        this.timeData = timeData;
    }

    public ProvinceDataBean getProvinceData() {
        return provinceData;
    }

    public void setProvinceData(ProvinceDataBean provinceData) {
        this.provinceData = provinceData;
    }

    public List<PercentDataBean> getPercentData() {
        return percentData;
    }

    public void setPercentData(List<PercentDataBean> percentData) {
        this.percentData = percentData;
    }

    public static class ProvinceDataBean {
        /**
         * firstThree : [{"money":300,"province":"贵州"},{"money":255,"province":"晋蒙"}]
         * city:广州
         * group : A
         * monthComplete : 2
         * monthPercent : 5%
         * monthTarget : 3
         * percent : 300
         * preCity :
         * rankAft : 2
         * rankPre : 1
         * yearComplete : 4
         * yearPercent : 16%
         * yearTarget : 5
         */

        private String city;
        private String group;
        private String monthComplete;
        private String monthPercent;
        private String monthTarget;
        private String percent;
        private String preCity;
        private String rankAft;
        private String rankPre;
        private String yearComplete;
        private String yearPercent;
        private String yearTarget;
        private List<FirstThreeBean> firstThree;

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getGroup() {
            return group;
        }

        public void setGroup(String group) {
            this.group = group;
        }

        public String getMonthComplete() {
            return monthComplete;
        }

        public void setMonthComplete(String monthComplete) {
            this.monthComplete = monthComplete;
        }

        public String getMonthPercent() {
            return monthPercent;
        }

        public void setMonthPercent(String monthPercent) {
            this.monthPercent = monthPercent;
        }

        public String getMonthTarget() {
            return monthTarget;
        }

        public void setMonthTarget(String monthTarget) {
            this.monthTarget = monthTarget;
        }

        public String getPercent() {
            return percent;
        }

        public void setPercent(String percent) {
            this.percent = percent;
        }

        public String getPreCity() {
            return preCity;
        }

        public void setPreCity(String preCity) {
            this.preCity = preCity;
        }

        public String getRankAft() {
            return rankAft;
        }

        public void setRankAft(String rankAft) {
            this.rankAft = rankAft;
        }

        public String getRankPre() {
            return rankPre;
        }

        public void setRankPre(String rankPre) {
            this.rankPre = rankPre;
        }

        public String getYearComplete() {
            return yearComplete;
        }

        public void setYearComplete(String yearComplete) {
            this.yearComplete = yearComplete;
        }

        public String getYearPercent() {
            return yearPercent;
        }

        public void setYearPercent(String yearPercent) {
            this.yearPercent = yearPercent;
        }

        public String getYearTarget() {
            return yearTarget;
        }

        public void setYearTarget(String yearTarget) {
            this.yearTarget = yearTarget;
        }

        public List<FirstThreeBean> getFirstThree() {
            return firstThree;
        }

        public void setFirstThree(List<FirstThreeBean> firstThree) {
            this.firstThree = firstThree;
        }

        public static class FirstThreeBean implements Serializable {
            /**
             * money : 300
             * province : 贵州
             */

            private int money;
            private String province;

            public int getMoney() {
                return money;
            }

            public void setMoney(int money) {
                this.money = money;
            }

            public String getProvince() {
                return province;
            }

            public void setProvince(String province) {
                this.province = province;
            }
        }
    }

    public static class PercentDataBean implements Serializable {
        /**
         * area : 贵州
         * countsMonthComplete : 16%
         * countsYearComplete : 5%
         * name : 鲁喜平
         * percent : 300
         * rank : 1
         * sales : 销售一部
         */

        private String area;
        private String countsMonthComplete;
        private String countsYearComplete;
        private String name;
        private String percent;
        private String rank;
        private String sales;

        public String getArea() {
            return area;
        }

        public void setArea(String area) {
            this.area = area;
        }

        public String getCountsMonthComplete() {
            return countsMonthComplete;
        }

        public void setCountsMonthComplete(String countsMonthComplete) {
            this.countsMonthComplete = countsMonthComplete;
        }

        public String getCountsYearComplete() {
            return countsYearComplete;
        }

        public void setCountsYearComplete(String countsYearComplete) {
            this.countsYearComplete = countsYearComplete;
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

        public String getSales() {
            return sales;
        }

        public void setSales(String sales) {
            this.sales = sales;
        }
    }
}
