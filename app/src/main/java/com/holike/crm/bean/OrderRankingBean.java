package com.holike.crm.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by wqj on 2018/4/27.
 */

public class OrderRankingBean implements Serializable {

    /**
     * endTime : 测试内容487c
     * myRank : {"myselfCounts":"1","myselfRank":"1"}
     * rankData : [{"area":"广东海南","city":"佛山市","counts":75,"rank":"1","userName":"test002"},{"area":"鲁北","city":"泰安市","counts":72,"rank":"2","userName":"张天成"},{"area":"江西","city":"抚州市","counts":50,"rank":"3","userName":"宗芳"},{"area":"晋蒙","city":"呼和浩特市","counts":41,"rank":"4","userName":"王晶"},{"area":"江苏","city":"盐城市","counts":33,"rank":"5","userName":"吴林林"},{"area":"江苏","city":"盐城市","counts":21,"rank":"6","userName":"万雅"},{"area":"江苏","city":"宿迁市","counts":18,"rank":"7","userName":"杨威"},{"area":"江苏","city":"盐城市","counts":16,"rank":"8","userName":"李鹏程"},{"area":"河北","city":"石家庄市","counts":13,"rank":"9","userName":"赵梦婵"},{"area":"鲁东","city":"青岛市","counts":12,"rank":"10","userName":"王波"},{"area":"湖南","city":"长沙市","counts":11,"rank":"11","userName":"29030老板"},{"area":"四川","city":"乐山市","counts":10,"rank":"12","userName":"36221老板"},{"area":"豫北","city":"郑州市","counts":10,"rank":"13","userName":"韩晓梅"},{"area":"甘青新藏","city":"兰州市","counts":10,"rank":"14","userName":"张锦祯"},{"area":"四川","city":"成都市","counts":9,"rank":"15","userName":"蒋燕"},{"area":"贵州","city":"毕节市","counts":9,"rank":"16","userName":"35420老板"},{"area":"鲁东","city":"青岛市","counts":9,"rank":"17","userName":"朱晓琳"},{"area":"湖北","city":"十堰市","counts":7,"rank":"18","userName":"吕露"},{"area":"江苏","city":"盐城市","counts":7,"rank":"19","userName":"朱为君"},{"area":"豫南","city":"平顶山市","counts":6,"rank":"20","userName":"18060老板"},{"area":"豫北","city":"郑州市","counts":6,"rank":"21","userName":"钱冬燕"},{"area":"陕宁","city":"宝鸡市","counts":6,"rank":"22","userName":"小会"},{"area":"河北","city":"衡水市","counts":5,"rank":"23","userName":"11112老板"},{"area":"江苏","city":"盐城市","counts":5,"rank":"24","userName":"陈月梅"},{"area":"浙江","city":"衢州市","counts":5,"rank":"25","userName":"徐光施"},{"area":"四川","city":"成都市","counts":5,"rank":"26","userName":"杨威"},{"area":"东三省","city":"丹东市","counts":5,"rank":"27","userName":"14201老板"},{"area":"湖南","city":"郴州市","counts":4,"rank":"28","userName":"29270老板"},{"area":"天津北京","city":"市辖区","counts":3,"rank":"29","userName":"徐灵霞"},{"area":"江苏","city":"盐城市","counts":3,"rank":"30","userName":"吴海燕"},{"area":"东三省","city":"丹东市","counts":3,"rank":"31","userName":"14021老板"},{"area":"重庆","city":"市辖区","counts":2,"rank":"32","userName":"徐燕"},{"area":"豫南","city":"信阳市","counts":2,"rank":"33","userName":"李娜"},{"area":"河北","city":"保定市","counts":2,"rank":"34","userName":"张晓娜"},{"area":"广东海南","city":"惠州市","counts":2,"rank":"35","userName":"殷函光"},{"area":"豫北","city":"许昌市","counts":2,"rank":"36","userName":"yanli"},{"area":"湖北","city":"省直辖县级行政区划","counts":2,"rank":"37","userName":"袁万全"},{"area":"安徽","city":"亳州市","counts":2,"rank":"38","userName":"孟飞"},{"area":"甘青新藏","city":"张掖市","counts":1,"rank":"39","userName":"梁光峰"},{"area":"豫北","city":"郑州市","counts":1,"rank":"40","userName":"曾子芹"},{"area":"重庆","city":"市辖区","counts":1,"rank":"41","userName":"周宗美"},{"area":"安徽","city":"亳州市","counts":1,"rank":"42","userName":"何海玲"},{"area":"广东海南","city":"韶关市","counts":1,"rank":"43","userName":"41020老板"},{"area":"江苏","city":"连云港市","counts":1,"rank":"44","userName":"22660老板"},{"area":"甘青新藏","city":"昌吉回族自治州","counts":1,"rank":"45","userName":"38150老板"},{"area":"江苏","city":"连云港市","counts":1,"rank":"46","userName":"27280老板"},{"area":"安徽","city":"亳州市","counts":1,"rank":"47","userName":"吴俊华"},{"area":"鲁东","city":"青岛市","counts":1,"rank":"48","userName":"翟如娟"},{"area":"江西","city":"新余市","counts":1,"rank":"49","userName":"汤桂云"},{"area":"广东海南","city":"佛山市","counts":1,"rank":"50","userName":"test001"},{"area":"鲁东","city":"威海市","counts":1,"rank":"51","userName":"宋美娟"},{"area":"安徽","city":"亳州市","counts":1,"rank":"52","userName":"王兵"},{"area":"安徽","city":"亳州市","counts":1,"rank":"53","userName":"魏攀攀"}]
     * select : {"cityCode":"-1","name":"四大区","type":"-1"}
     * selectData : [{"cityCode":"-1","name":"四大区","type":"-1"},{"cityCode":"1000","name":"销售一部","type":"-1"},{"cityCode":"2000","name":"销售二部","type":"-1"},{"cityCode":"3000","name":"销售三部","type":"-1"},{"cityCode":"4000","name":"销售四部","type":"-1"},{"cityCode":"001","name":"浙江","type":"1"},{"cityCode":"002","name":"湖南","type":"1"},{"cityCode":"003","name":"江西","type":"1"},{"cityCode":"004","name":"江苏","type":"1"},{"cityCode":"005","name":"东三省","type":"1"},{"cityCode":"006","name":"河北","type":"1"},{"cityCode":"007","name":"鲁东","type":"1"},{"cityCode":"008","name":"鲁北","type":"1"},{"cityCode":"009","name":"天津北京","type":"1"},{"cityCode":"010","name":"安徽","type":"1"},{"cityCode":"011","name":"云南","type":"1"},{"cityCode":"012","name":"广西","type":"1"},{"cityCode":"013","name":"四川","type":"1"},{"cityCode":"014","name":"湖北","type":"1"},{"cityCode":"015","name":"广东海南","type":"1"},{"cityCode":"016","name":"福建","type":"1"},{"cityCode":"017","name":"晋蒙","type":"1"},{"cityCode":"018","name":"陕宁","type":"1"},{"cityCode":"019","name":"贵州","type":"1"},{"cityCode":"020","name":"重庆","type":"1"},{"cityCode":"021","name":"豫南","type":"1"},{"cityCode":"022","name":"豫北","type":"1"},{"cityCode":"023","name":"甘青新藏","type":"1"}]
     * startTime : 测试内容5xtd
     * title : 01.01-05.26四大区排行
     */

    private String endTime;
    private MyRankBean myRank;
    private SelectBean select;
    private String startTime;
    private String title;
    private List<RankDataBean> rankData;
    private List<SelectDataBean> selectData;

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public MyRankBean getMyRank() {
        return myRank;
    }

    public void setMyRank(MyRankBean myRank) {
        this.myRank = myRank;
    }

    public SelectBean getSelect() {
        return select;
    }

    public void setSelect(SelectBean select) {
        this.select = select;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<RankDataBean> getRankData() {
        return rankData;
    }

    public void setRankData(List<RankDataBean> rankData) {
        this.rankData = rankData;
    }

    public List<SelectDataBean> getSelectData() {
        return selectData;
    }

    public void setSelectData(List<SelectDataBean> selectData) {
        this.selectData = selectData;
    }

    public static class MyRankBean implements Serializable {
        /**
         * myselfCounts : 1
         * myselfRank : 1
         */

        private String myselfCounts;
        private String myselfRank;

        public String getMyselfCounts() {
            return myselfCounts;
        }

        public void setMyselfCounts(String myselfCounts) {
            this.myselfCounts = myselfCounts;
        }

        public String getMyselfRank() {
            return myselfRank;
        }

        public void setMyselfRank(String myselfRank) {
            this.myselfRank = myselfRank;
        }
    }

    public static class SelectBean implements Serializable {
        /**
         * cityCode : -1
         * name : 四大区
         * type : -1
         */

        private String cityCode;
        private String name;
        private String type;

        public String getCityCode() {
            return cityCode;
        }

        public void setCityCode(String cityCode) {
            this.cityCode = cityCode;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }

    public static class RankDataBean implements Serializable {
        /**
         * area : 广东海南
         * city : 佛山市
         * counts : 75
         * rank : 1
         * userName : test002
         */

        private String area;
        private String city;
        private int counts;
        private String rank;
        private String userName;

        public String getArea() {
            return area;
        }

        public void setArea(String area) {
            this.area = area;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public int getCounts() {
            return counts;
        }

        public void setCounts(int counts) {
            this.counts = counts;
        }

        public String getRank() {
            return rank;
        }

        public void setRank(String rank) {
            this.rank = rank;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }
    }

    public static class SelectDataBean implements Serializable {
        /**
         * cityCode : -1
         * name : 四大区
         * type : -1
         */

        private String cityCode;
        private String name;
        private String type;

        public String getCityCode() {
            return cityCode;
        }

        public void setCityCode(String cityCode) {
            this.cityCode = cityCode;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }
}
