package com.holike.crm.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by wqj on 2018/3/7.
 * 订金交易情况bean
 */

public class FastLiveBean implements Serializable {

    /**
     * percentData : [{"area":"王求对","cityCode":"12603300","isChange":0,"isClick":0,"name":"王求对","select1":1518,"select2":0,"select3":1600.24,"select4":0,"totalMoney":"0","type":4},{"area":"上虞","cityCode":"12600900","isChange":0,"isClick":0,"name":"吴一鸣","select1":0,"select2":0,"select3":0,"select4":0,"totalMoney":"0","type":4},{"area":"绍兴","cityCode":"12601001","isChange":0,"isClick":0,"name":"谢海华","select1":0,"select2":0,"select3":0,"select4":0,"totalMoney":"0","type":4},{"area":"绍兴","cityCode":"12601002","isChange":0,"isClick":0,"name":"吕立锋","select1":0,"select2":0,"select3":0,"select4":0,"totalMoney":"0","type":4},{"area":"金华","cityCode":"12602101","isChange":0,"isClick":0,"name":"徐华","select1":0,"select2":0,"select3":0,"select4":0,"totalMoney":"0","type":4},{"area":"浦江","cityCode":"12602200","isChange":0,"isClick":0,"name":"张国生","select1":0,"select2":0,"select3":0,"select4":0,"totalMoney":"0","type":4},{"area":"永康","cityCode":"12602300","isChange":0,"isClick":0,"name":"黄耀辉","select1":0,"select2":0,"select3":0,"select4":0,"totalMoney":"0","type":4},{"area":"义乌","cityCode":"12602400","isChange":0,"isClick":0,"name":"赵品成","select1":0,"select2":0,"select3":0,"select4":0,"totalMoney":"0","type":4},{"area":"东阳","cityCode":"12602701","isChange":0,"isClick":0,"name":"陈信远","select1":0,"select2":0,"select3":0,"select4":0,"totalMoney":"0","type":4},{"area":"诸暨","cityCode":"12602901","isChange":0,"isClick":0,"name":"顾海霞","select1":0,"select2":0,"select3":0,"select4":0,"totalMoney":"0","type":4},{"area":"新昌","cityCode":"12603101","isChange":0,"isClick":0,"name":"吕楠","select1":1518,"select2":0,"select3":1600.24,"select4":0,"totalMoney":"0","type":4},{"area":"兰溪","cityCode":"12603300","isChange":0,"isClick":0,"name":"范韵月","select1":0,"select2":0,"select3":0,"select4":0,"totalMoney":"0","type":4}]
     * selectData : [{"name":"本季度","selectTime":"1"},{"name":"全年","selectTime":"-1"},{"name":"查询日期","selectTime":"2"}]
     * timeData : 2018.01.01-2019.03.31
     * title : 绍兴市,金华市
     */

    private String timeData;
    private String title;
    private List<PercentDataBean> percentData;
    private List<SelectDataBean> selectData;

    public String getTimeData() {
        return timeData;
    }

    public void setTimeData(String timeData) {
        this.timeData = timeData;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public static class PercentDataBean {
        /**
         * area : 王求对
         * cityCode : 12603300
         * isChange : 0
         * isClick : 0
         * name : 王求对
         * select1 : 1518
         * select2 : 0
         * select3 : 1600.24
         * select4 : 0
         * totalMoney : 0
         * type : 4
         */

        private String area;
        private String cityCode;
        private String isChange;
        private String isClick;
        private String name;
        private String select1;
        private String select2;
        private String select3;
        private String select4;
        private String totalMoney;
        private String type;

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

        public String getIsChange() {
            return isChange;
        }

        public void setIsChange(String isChange) {
            this.isChange = isChange;
        }

        public String getIsClick() {
            return isClick;
        }

        public void setIsClick(String isClick) {
            this.isClick = isClick;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getSelect1() {
            return select1;
        }

        public void setSelect1(String select1) {
            this.select1 = select1;
        }

        public String getSelect2() {
            return select2;
        }

        public void setSelect2(String select2) {
            this.select2 = select2;
        }

        public String getSelect3() {
            return select3;
        }

        public void setSelect3(String select3) {
            this.select3 = select3;
        }

        public String getSelect4() {
            return select4;
        }

        public void setSelect4(String select4) {
            this.select4 = select4;
        }

        public String getTotalMoney() {
            return totalMoney;
        }

        public void setTotalMoney(String totalMoney) {
            this.totalMoney = totalMoney;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }

    public static class SelectDataBean {
        /**
         * name : 本季度
         * selectTime : 1
         */

        private String name;
        private String selectTime;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getSelectTime() {
            return selectTime;
        }

        public void setSelectTime(String selectTime) {
            this.selectTime = selectTime;
        }
    }
}
