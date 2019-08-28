package com.holike.crm.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by wqj on 2018/6/26.
 * 主动营销-填写城市
 */

public class WriteCityBean implements Serializable {
    private List<ActiveRecordBean> activeRecord;
    private List<SelectDataBean> selectData;

    public List<ActiveRecordBean> getActiveRecord() {
        return activeRecord;
    }

    public void setActiveRecord(List<ActiveRecordBean> activeRecord) {
        this.activeRecord = activeRecord;
    }

    public List<SelectDataBean> getSelectData() {
        return selectData;
    }

    public void setSelectData(List<SelectDataBean> selectData) {
        this.selectData = selectData;
    }

    public static class ActiveRecordBean implements Serializable {
        /**
         * dealerId : 21098
         * name : 梁富杰
         * time : 6.28-6.29
         */

        private String dealerId;
        private String name;
        private String time;

        public String getDealerId() {
            return dealerId;
        }

        public void setDealerId(String dealerId) {
            this.dealerId = dealerId;
        }

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

        public ActiveRecordBean(String dealerId, String name, String time) {
            this.dealerId = dealerId;
            this.name = name;
            this.time = time;
        }
    }

    public static class SelectDataBean implements Serializable {
        /**
         * dealerId : 11500000
         * dealerName : 呼和浩特
         */

        private String dealerId;
        private String dealerName;

        public String getDealerId() {
            return dealerId;
        }

        public void setDealerId(String dealerId) {
            this.dealerId = dealerId;
        }

        public String getDealerName() {
            return dealerName;
        }

        public void setDealerName(String dealerName) {
            this.dealerName = dealerName;
        }
    }
}
