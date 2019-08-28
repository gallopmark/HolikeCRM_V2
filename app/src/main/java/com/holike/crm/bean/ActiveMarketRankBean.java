package com.holike.crm.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by wqj on 2018/6/27.
 * 主动营销排行榜
 */

public class ActiveMarketRankBean implements Serializable {
    /**
     * timeData : 2018.06.01-2018.06.30
     * dataList : [{"area":"福建","achievement":"425.83","name":"王博琴","rank":1,"sales":"销售四部"},{"area":"晋蒙","achievement":"","name":"李杨","rank":"-","sales":"销售一部"},{"area":"四川","achievement":"","name":"张华","rank":"-","sales":"销售四部"},{"area":"广西","achievement":"","name":"林进强","rank":"-","sales":"销售四部"},{"area":"广东海南","achievement":"","name":"秦忠雄","rank":"-","sales":"销售四部"},{"area":"湖北","achievement":"","name":"彭祖顺","rank":"-","sales":"销售四部"},{"area":"云南","achievement":"","name":"杨冬娃","rank":"-","sales":"销售四部"}]
     * isActive : 1
     * activeData : {"firstThree":[{"area":"福建","achievement":"425.83","name":"王博琴","rank":1,"sales":"销售四部"}],"city":"晋蒙","achievement":"","totalRank":7,"name":"李杨","preName":"王博琴","rank":""}
     * selectName : 晋蒙
     */

    private String timeData;
    private int isActive;
    private ActiveDataBean activeData;
    private String selectName;
    private List<DataListBean> dataList;

    public String getTimeData() {
        return timeData;
    }

    public void setTimeData(String timeData) {
        this.timeData = timeData;
    }

    public int getIsActive() {
        return isActive;
    }

    public void setIsActive(int isActive) {
        this.isActive = isActive;
    }

    public ActiveDataBean getActiveData() {
        return activeData;
    }

    public void setActiveData(ActiveDataBean activeData) {
        this.activeData = activeData;
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

    public static class ActiveDataBean {
        /**
         * firstThree : [{"area":"福建","achievement":"425.83","name":"王博琴","rank":1,"sales":"销售四部"}]
         * city : 晋蒙
         * achievement :
         * totalRank : 7
         * name : 李杨
         * preName : 王博琴
         * rank :
         * time:
         */

        private String city;
        private String achievement;
        private int totalRank;
        private String name;
        private String preName;
        private String rank;
        private String time;
        private List<FirstThreeBean> firstThree;

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getAchievement() {
            return achievement;
        }

        public void setAchievement(String achievement) {
            this.achievement = achievement;
        }

        public int getTotalRank() {
            return totalRank;
        }

        public void setTotalRank(int totalRank) {
            this.totalRank = totalRank;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPreName() {
            return preName;
        }

        public void setPreName(String preName) {
            this.preName = preName;
        }

        public String getRank() {
            return rank;
        }

        public void setRank(String rank) {
            this.rank = rank;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public List<FirstThreeBean> getFirstThree() {
            return firstThree;
        }

        public void setFirstThree(List<FirstThreeBean> firstThree) {
            this.firstThree = firstThree;
        }

        public static class FirstThreeBean {
            /**
             * area : 福建
             * achievement : 425.83
             * name : 王博琴
             * rank : 1
             * sales : 销售四部
             */

            private String area;
            private String achievement;
            private String name;
            private int rank;
            private String sales;

            public String getArea() {
                return area;
            }

            public void setArea(String area) {
                this.area = area;
            }

            public String getAchievement() {
                return achievement;
            }

            public void setAchievement(String achievement) {
                this.achievement = achievement;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public int getRank() {
                return rank;
            }

            public void setRank(int rank) {
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

    public static class DataListBean implements Serializable {
        /**
         * area : 福建
         * achievement : 425.83
         * name : 王博琴
         * rank : 1
         * sales : 销售四部
         */

        private String area;
        private String achievement;
        private String name;
        private String rank;
        private String sales;

        public String getArea() {
            return area;
        }

        public void setArea(String area) {
            this.area = area;
        }

        public String getAchievement() {
            return achievement;
        }

        public void setAchievement(String achievement) {
            this.achievement = achievement;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
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
