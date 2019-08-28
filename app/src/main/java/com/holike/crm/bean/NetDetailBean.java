package com.holike.crm.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by wqj on 2018/6/6.
 * 拉网明细
 */

public class NetDetailBean implements Serializable {
    /**
     * dataList : [{"area":"晋蒙","dealerName":"交城","level":"C4","name":"李东升","sales":"销售一部","status":"已优化"},{"area":"晋蒙","dealerName":"曲沃","level":"C4","name":"李东升","sales":"销售一部","status":""}]
     * date : 数据更新时间：2018-06-06 11:23
     */

    private String date;
    private List<DataListBean> dataList;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<DataListBean> getDataList() {
        return dataList;
    }

    public void setDataList(List<DataListBean> dataList) {
        this.dataList = dataList;
    }

    public static class DataListBean implements Serializable {
        /**
         * area : 晋蒙
         * dealerName : 交城
         * level : C4
         * name : 李东升
         * sales : 销售一部
         * status : 已优化
         */

        private String area;
        private String dealerName;
        private String level;
        private String name;
        private String sales;
        private String status;

        public String getArea() {
            return area;
        }

        public void setArea(String area) {
            this.area = area;
        }

        public String getDealerName() {
            return dealerName;
        }

        public void setDealerName(String dealerName) {
            this.dealerName = dealerName;
        }

        public String getLevel() {
            return level;
        }

        public void setLevel(String level) {
            this.level = level;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getSales() {
            return sales;
        }

        public void setSales(String sales) {
            this.sales = sales;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
}
