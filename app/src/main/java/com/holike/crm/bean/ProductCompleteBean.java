package com.holike.crm.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by wqj on 2018/5/16.
 */

public class ProductCompleteBean implements Serializable {
    /**
     * percentList : [{"area":"重庆","day":"04/27-05/26","month":"5月","percent":"19.61%"},{"area":"重庆","day":"03/27-04/26","month":"4月","percent":"135.54%"},{"area":"重庆","day":"02/26-03/27","month":"3月","percent":"547.12%"},{"area":"重庆","day":"01/26-02/27","month":"2月","percent":"8%"},{"area":"重庆","day":"12/26-01/27","month":"1月","percent":"56.76%"}]
     * select : {"cityCode":"020","name":"重庆","type":"2"}
     * selectData : [{"cityCode":"-1","name":"四大区","type":"-1"},{"cityCode":"1000","name":"销售一部","type":"1"},{"cityCode":"2000","name":"销售二部","type":"1"},{"cityCode":"3000","name":"销售三部","type":"1"},{"cityCode":"4000","name":"销售四部","type":"1"},{"cityCode":"001","name":"浙江","type":"2"},{"cityCode":"002","name":"湖南","type":"2"},{"cityCode":"003","name":"江西","type":"2"},{"cityCode":"004","name":"江苏","type":"2"},{"cityCode":"005","name":"东三省","type":"2"},{"cityCode":"006","name":"河北","type":"2"},{"cityCode":"007","name":"鲁东","type":"2"},{"cityCode":"008","name":"鲁北","type":"2"},{"cityCode":"009","name":"天津北京","type":"2"},{"cityCode":"010","name":"安徽","type":"2"},{"cityCode":"011","name":"云南","type":"2"},{"cityCode":"012","name":"广西","type":"2"},{"cityCode":"013","name":"四川","type":"2"},{"cityCode":"014","name":"湖北","type":"2"},{"cityCode":"015","name":"广东海南","type":"2"},{"cityCode":"016","name":"福建","type":"2"},{"cityCode":"017","name":"晋蒙","type":"2"},{"cityCode":"018","name":"陕宁","type":"2"},{"cityCode":"019","name":"贵州","type":"2"},{"cityCode":"020","name":"重庆","type":"2"},{"cityCode":"021","name":"豫南","type":"2"},{"cityCode":"022","name":"豫北","type":"2"},{"cityCode":"023","name":"甘青新藏","type":"2"}]
     */

    private SelectBean select;
    private List<PercentListBean> percentList;
    private List<OrderRankingBean.SelectDataBean> selectData;

    public SelectBean getSelect() {
        return select;
    }

    public void setSelect(SelectBean select) {
        this.select = select;
    }

    public List<PercentListBean> getPercentList() {
        return percentList;
    }

    public void setPercentList(List<PercentListBean> percentList) {
        this.percentList = percentList;
    }

    public List<OrderRankingBean.SelectDataBean> getSelectData() {
        return selectData;
    }

    public void setSelectData(List<OrderRankingBean.SelectDataBean> selectData) {
        this.selectData = selectData;
    }

    public static class SelectBean implements Serializable {
        /**
         * cityCode : 020
         * name : 重庆
         * type : 2
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

    public static class PercentListBean extends MonthCompleteBean {
        /**
         * area : 重庆
         * day : 04/27-05/26
         * month : 5月
         * percent : 19.61%
         * start: 1527350401,
         * end: 1530025201,
         */

        private String start;
        private String end;
        private String percent;

        public String getStart() {
            return start;
        }

        public void setStart(String start) {
            this.start = start;
        }

        public String getEnd() {
            return end;
        }

        public void setEnd(String end) {
            this.end = end;
        }

        public String getPercent() {
            return percent;
        }

        public void setPercent(String percent) {
            this.percent = percent;
        }

        @Override
        public String getDepositPercent() {
            return percent;
        }
    }

}
