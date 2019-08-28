package com.holike.crm.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by wqj on 2018/5/30.
 * 订单交易报表-填写目标
 */

public class OrderReportTargetBean implements Serializable {
    /**
     * depositList : [{"area":"桐梓县","cityCode":"13501501","isChange":0,"name":"35171老板","target":""},{"area":"桐梓县","cityCode":"13501501-0001","isChange":0,"name":"伏小燕","target":""},{"area":"桐梓县","cityCode":"13501501-0002","isChange":0,"name":"刘华杰","target":""},{"area":"桐梓县","cityCode":"13501501-0004","isChange":0,"name":"杨修霞","target":""},{"area":"桐梓县","cityCode":"13501501-0005","isChange":0,"name":"祝明权","target":""},{"area":"桐梓县","cityCode":"13501501-0011","isChange":0,"name":"伏小燕","target":""}]
     * selectData : [{"name":"616","time":3},{"name":"6月","time":1},{"name":"全年","time":2}]
     * timeData : {"endTime":1530374399,"startTime":1526140800}
     * title : 测试内容41dz
     */

    private TimeDataBean timeData;
    private List<DepositListBean> depositList;
    private List<OrderReportBean.SelectDataBean> selectData;
    private String title;
    private String time;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public TimeDataBean getTimeData() {
        return timeData;
    }

    public void setTimeData(TimeDataBean timeData) {
        this.timeData = timeData;
    }

    public List<DepositListBean> getDepositList() {
        return depositList;
    }

    public void setDepositList(List<DepositListBean> depositList) {
        this.depositList = depositList;
    }

    public List<OrderReportBean.SelectDataBean> getSelectData() {
        return selectData;
    }

    public void setSelectData(List<OrderReportBean.SelectDataBean> selectData) {
        this.selectData = selectData;
    }

    public static class TimeDataBean {
        /**
         * endTime : 1530374399
         * startTime : 1526140800
         */

        private int endTime;
        private int startTime;

        public int getEndTime() {
            return endTime;
        }

        public void setEndTime(int endTime) {
            this.endTime = endTime;
        }

        public int getStartTime() {
            return startTime;
        }

        public void setStartTime(int startTime) {
            this.startTime = startTime;
        }
    }

    public static class DepositListBean implements Serializable {
        /**
         * area : 桐梓县
         * cityCode : 13501501
         * isChange : 0
         * name : 35171老板
         * target :
         */

        private String area;
        private String cityCode;
        private int isChange;
        private String name;
        private String target;

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

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getTarget() {
            return target;
        }

        public void setTarget(String target) {
            this.target = target;
        }
    }

}
