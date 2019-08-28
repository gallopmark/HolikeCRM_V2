package com.holike.crm.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by wqj on 2018/6/15.
 * 订单筛选条件id
 */

public class TypeListBean implements Serializable {
    private List<OrderStatusDataBean> orderStatusData;
    private List<OrderTypeDataBean> orderTypeData;


    public List<OrderStatusDataBean> getOrderStatusData() {
        return orderStatusData;
    }

    public void setOrderStatusData(List<OrderStatusDataBean> orderStatusData) {
        this.orderStatusData = orderStatusData;
    }

    public List<OrderTypeDataBean> getOrderTypeData() {
        return orderTypeData;
    }

    public void setOrderTypeData(List<OrderTypeDataBean> orderTypeData) {
        this.orderTypeData = orderTypeData;
    }


    public static class OrderStatusDataBean implements Serializable {
        /**
         * orderStatusId : 101
         * orderStatusName : 取消订单
         */

        private String orderStatusId;
        private String orderStatusName;

        public String getOrderStatusId() {
            return orderStatusId;
        }

        public void setOrderStatusId(String orderStatusId) {
            this.orderStatusId = orderStatusId;
        }

        public String getOrderStatusName() {
            return orderStatusName;
        }

        public void setOrderStatusName(String orderStatusName) {
            this.orderStatusName = orderStatusName;
        }

        public OrderStatusDataBean() {
        }

        public OrderStatusDataBean(String orderStatusId, String orderStatusName) {
            this.orderStatusId = orderStatusId;
            this.orderStatusName = orderStatusName;
        }
    }

    public static class OrderTypeDataBean implements Serializable {
        /**
         * orderTypeId : ZRE
         * orderTypeName : 售后退货单
         */

        private String orderTypeId;
        private String orderTypeName;

        public String getOrderTypeId() {
            return orderTypeId;
        }

        public void setOrderTypeId(String orderTypeId) {
            this.orderTypeId = orderTypeId;
        }

        public String getOrderTypeName() {
            return orderTypeName;
        }

        public void setOrderTypeName(String orderTypeName) {
            this.orderTypeName = orderTypeName;
        }

        public OrderTypeDataBean() {
        }

        public OrderTypeDataBean(String orderTypeId, String orderTypeName) {
            this.orderTypeId = orderTypeId;
            this.orderTypeName = orderTypeName;
        }
    }

}