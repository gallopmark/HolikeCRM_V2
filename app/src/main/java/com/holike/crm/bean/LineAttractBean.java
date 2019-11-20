package com.holike.crm.bean;

import android.text.TextUtils;

import org.parceler.Parcel;

import java.util.List;

@Parcel
public class LineAttractBean {

    /**
     * dealerData : [{"dataList":[{"customerAppointment":"测试内容1pq8","customerBill":"测试内容7348","customerCommunicate":"测试内容ubc4","customerDeposit":"测试内容7v56","customerLoss":"测试内容a672","customerMeasure":"测试内容v2r8","customerNo":"测试内容2azy","customerOrder":"测试内容g4kc","customerTotal":"测试内容ayq4","earnestMoney":"测试内容r3u0","name":"测试内容y66w","orderPercent":"测试内容252k","scalePercent":"测试内容t372","signingMoney":"测试内容k2j6"}],"shopName":"测试内容72m9"}]
     * isDealer : 0
     * percentData : [{"area":"四大区","cityCode":"","customerNo":13,"customerNoPercent":"10.6%","customerTotal":123,"customerYes":110,"earnestCount":0,"earnestMoney":0,"isChange":0,"isClick":0,"name":"那崇奇","orderPercent":"0%","scaleCount":0,"scaleCountPercent":"0%","signingCount":0,"signingMoney":0,"type":2},{"area":"销售一部","cityCode":"1000","customerNo":7,"customerNoPercent":"25.9%","customerTotal":27,"customerYes":20,"earnestCount":0,"earnestMoney":0,"isChange":0,"isClick":1,"name":"肖凌云","orderPercent":"0%","scaleCount":0,"scaleCountPercent":"0%","signingCount":0,"signingMoney":0,"type":1},{"area":"销售二部","cityCode":"2000","customerNo":0,"customerNoPercent":"-","customerTotal":0,"customerYes":0,"earnestCount":0,"earnestMoney":0,"isChange":0,"isClick":1,"name":"刘特科","orderPercent":"-","scaleCount":0,"scaleCountPercent":"-","signingCount":0,"signingMoney":0,"type":1},{"area":"销售三部","cityCode":"3000","customerNo":6,"customerNoPercent":"6.2%","customerTotal":96,"customerYes":90,"earnestCount":0,"earnestMoney":0,"isChange":0,"isClick":1,"name":"肖凌云","orderPercent":"0%","scaleCount":0,"scaleCountPercent":"0%","signingCount":0,"signingMoney":0,"type":1},{"area":"销售四部","cityCode":"4000","customerNo":0,"customerNoPercent":"-","customerTotal":0,"customerYes":0,"earnestCount":0,"earnestMoney":0,"isChange":0,"isClick":1,"name":"舒文进","orderPercent":"-","scaleCount":0,"scaleCountPercent":"-","signingCount":0,"signingMoney":0,"type":1},{"area":"粤东","cityCode":"015","customerNo":6,"customerNoPercent":"6.2%","customerTotal":96,"customerYes":90,"earnestCount":0,"earnestMoney":0,"isChange":0,"isClick":1,"name":"刘辉顺","orderPercent":"0%","scaleCount":0,"scaleCountPercent":"0%","signingCount":0,"signingMoney":0,"type":2},{"area":"四川","cityCode":"013","customerNo":7,"customerNoPercent":"25.9%","customerTotal":27,"customerYes":20,"earnestCount":0,"earnestMoney":0,"isChange":0,"isClick":1,"name":"李东升","orderPercent":"0%","scaleCount":0,"scaleCountPercent":"0%","signingCount":0,"signingMoney":0,"type":2},{"area":"浙江","cityCode":"001","customerNo":0,"customerNoPercent":"-","customerTotal":0,"customerYes":0,"earnestCount":0,"earnestMoney":0,"isChange":0,"isClick":1,"name":"李东升","orderPercent":"-","scaleCount":0,"scaleCountPercent":"-","signingCount":0,"signingMoney":0,"type":2},{"area":"湘南","cityCode":"002","customerNo":0,"customerNoPercent":"-","customerTotal":0,"customerYes":0,"earnestCount":0,"earnestMoney":0,"isChange":0,"isClick":1,"name":"徐克衡","orderPercent":"-","scaleCount":0,"scaleCountPercent":"-","signingCount":0,"signingMoney":0,"type":2},{"area":"江西","cityCode":"003","customerNo":0,"customerNoPercent":"-","customerTotal":0,"customerYes":0,"earnestCount":0,"earnestMoney":0,"isChange":0,"isClick":1,"name":"何金波","orderPercent":"-","scaleCount":0,"scaleCountPercent":"-","signingCount":0,"signingMoney":0,"type":2},{"area":"苏南","cityCode":"004","customerNo":0,"customerNoPercent":"-","customerTotal":0,"customerYes":0,"earnestCount":0,"earnestMoney":0,"isChange":0,"isClick":1,"name":"杨俊杰","orderPercent":"-","scaleCount":0,"scaleCountPercent":"-","signingCount":0,"signingMoney":0,"type":2},{"area":"黑吉","cityCode":"005","customerNo":0,"customerNoPercent":"-","customerTotal":0,"customerYes":0,"earnestCount":0,"earnestMoney":0,"isChange":0,"isClick":1,"name":"王庆亮","orderPercent":"-","scaleCount":0,"scaleCountPercent":"-","signingCount":0,"signingMoney":0,"type":2},{"area":"冀南","cityCode":"006","customerNo":0,"customerNoPercent":"-","customerTotal":0,"customerYes":0,"earnestCount":0,"earnestMoney":0,"isChange":0,"isClick":1,"name":"杨志坤","orderPercent":"-","scaleCount":0,"scaleCountPercent":"-","signingCount":0,"signingMoney":0,"type":2},{"area":"鲁东","cityCode":"007","customerNo":0,"customerNoPercent":"-","customerTotal":0,"customerYes":0,"earnestCount":0,"earnestMoney":0,"isChange":0,"isClick":1,"name":"石航","orderPercent":"-","scaleCount":0,"scaleCountPercent":"-","signingCount":0,"signingMoney":0,"type":2},{"area":"安徽","cityCode":"010","customerNo":0,"customerNoPercent":"-","customerTotal":0,"customerYes":0,"earnestCount":0,"earnestMoney":0,"isChange":0,"isClick":1,"name":"肖翔","orderPercent":"-","scaleCount":0,"scaleCountPercent":"-","signingCount":0,"signingMoney":0,"type":2},{"area":"云南","cityCode":"011","customerNo":0,"customerNoPercent":"-","customerTotal":0,"customerYes":0,"earnestCount":0,"earnestMoney":0,"isChange":0,"isClick":1,"name":"黄为明","orderPercent":"-","scaleCount":0,"scaleCountPercent":"-","signingCount":0,"signingMoney":0,"type":2},{"area":"广西","cityCode":"012","customerNo":0,"customerNoPercent":"-","customerTotal":0,"customerYes":0,"earnestCount":0,"earnestMoney":0,"isChange":0,"isClick":1,"name":"叶文发","orderPercent":"-","scaleCount":0,"scaleCountPercent":"-","signingCount":0,"signingMoney":0,"type":2},{"area":"湖北","cityCode":"014","customerNo":0,"customerNoPercent":"-","customerTotal":0,"customerYes":0,"earnestCount":0,"earnestMoney":0,"isChange":0,"isClick":1,"name":"邵斌","orderPercent":"-","scaleCount":0,"scaleCountPercent":"-","signingCount":0,"signingMoney":0,"type":2},{"area":"福建","cityCode":"016","customerNo":0,"customerNoPercent":"-","customerTotal":0,"customerYes":0,"earnestCount":0,"earnestMoney":0,"isChange":0,"isClick":1,"name":"廖城","orderPercent":"-","scaleCount":0,"scaleCountPercent":"-","signingCount":0,"signingMoney":0,"type":2},{"area":"陕宁","cityCode":"018","customerNo":0,"customerNoPercent":"-","customerTotal":0,"customerYes":0,"earnestCount":0,"earnestMoney":0,"isChange":0,"isClick":1,"name":"潘东升","orderPercent":"-","scaleCount":0,"scaleCountPercent":"-","signingCount":0,"signingMoney":0,"type":2},{"area":"贵州","cityCode":"019","customerNo":0,"customerNoPercent":"-","customerTotal":0,"customerYes":0,"earnestCount":0,"earnestMoney":0,"isChange":0,"isClick":1,"name":"鲁喜平","orderPercent":"-","scaleCount":0,"scaleCountPercent":"-","signingCount":0,"signingMoney":0,"type":2},{"area":"重庆","cityCode":"020","customerNo":0,"customerNoPercent":"-","customerTotal":0,"customerYes":0,"earnestCount":0,"earnestMoney":0,"isChange":0,"isClick":1,"name":"陶成汉","orderPercent":"-","scaleCount":0,"scaleCountPercent":"-","signingCount":0,"signingMoney":0,"type":2},{"area":"豫北","cityCode":"022","customerNo":0,"customerNoPercent":"-","customerTotal":0,"customerYes":0,"earnestCount":0,"earnestMoney":0,"isChange":0,"isClick":1,"name":"上官显儒","orderPercent":"-","scaleCount":0,"scaleCountPercent":"-","signingCount":0,"signingMoney":0,"type":2},{"area":"甘青","cityCode":"023","customerNo":0,"customerNoPercent":"-","customerTotal":0,"customerYes":0,"earnestCount":0,"earnestMoney":0,"isChange":0,"isClick":1,"name":"夏文杰","orderPercent":"-","scaleCount":0,"scaleCountPercent":"-","signingCount":0,"signingMoney":0,"type":2},{"area":"辽宁","cityCode":"031","customerNo":0,"customerNoPercent":"-","customerTotal":0,"customerYes":0,"earnestCount":0,"earnestMoney":0,"isChange":0,"isClick":1,"name":"刘谊","orderPercent":"-","scaleCount":0,"scaleCountPercent":"-","signingCount":0,"signingMoney":0,"type":2},{"area":"湘北","cityCode":"032","customerNo":0,"customerNoPercent":"-","customerTotal":0,"customerYes":0,"earnestCount":0,"earnestMoney":0,"isChange":0,"isClick":1,"name":"徐克衡","orderPercent":"-","scaleCount":0,"scaleCountPercent":"-","signingCount":0,"signingMoney":0,"type":2}]
     * selectData : [{"name":"全年","selectTime":"-1"},{"name":"本月","selectTime":"1"},{"name":"查询日期","selectTime":"2"}]
     * selected : 1
     * timeData : 2019.02.01-2019.02.28
     */

    String isDealer;
    String selected;
    String timeData;
    List<DealerDataBean> dealerData;
    List<PercentDataBean> percentData;
    List<SelectDataBean> selectData;
    String isShow;

    public String getIsDealer() {
        return isDealer;
    }

    public void setIsDealer(String isDealer) {
        this.isDealer = isDealer;
    }

    public boolean isDealer() {
        return TextUtils.equals(isDealer, "1");
    }

    public String getSelected() {
        return selected;
    }

    public void setSelected(String selected) {
        this.selected = selected;
    }

    public String getTimeData() {
        return timeData;
    }

    public void setTimeData(String timeData) {
        this.timeData = timeData;
    }

    public List<DealerDataBean> getDealerData() {
        return dealerData;
    }

    public void setDealerData(List<DealerDataBean> dealerData) {
        this.dealerData = dealerData;
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

    public boolean isShow() {
        return TextUtils.equals(isShow, "1");
    }

    @Parcel
    public static class DealerDataBean {
        /**
         * dataList : [{"customerAppointment":"测试内容1pq8","customerBill":"测试内容7348","customerCommunicate":"测试内容ubc4","customerDeposit":"测试内容7v56","customerLoss":"测试内容a672","customerMeasure":"测试内容v2r8","customerNo":"测试内容2azy","customerOrder":"测试内容g4kc","customerTotal":"测试内容ayq4","earnestMoney":"测试内容r3u0","name":"测试内容y66w","orderPercent":"测试内容252k","scalePercent":"测试内容t372","signingMoney":"测试内容k2j6"}]
         * shopName : 测试内容72m9
         */

        String shopName;
        List<DataListBean> dataList;

        public String getShopName() {
            return shopName;
        }

        public void setShopName(String shopName) {
            this.shopName = shopName;
        }

        public List<DataListBean> getDataList() {
            return dataList;
        }

        public void setDataList(List<DataListBean> dataList) {
            this.dataList = dataList;
        }

        @Parcel
        public static class DataListBean {
            /**
             * customerAppointment : 测试内容1pq8
             * customerBill : 测试内容7348
             * customerCommunicate : 测试内容ubc4
             * customerDeposit : 测试内容7v56
             * customerLoss : 测试内容a672
             * customerMeasure : 测试内容v2r8
             * customerNo : 测试内容2azy
             * customerOrder : 测试内容g4kc
             * customerTotal : 测试内容ayq4
             * earnestMoney : 测试内容r3u0
             * name : 测试内容y66w
             * orderPercent : 测试内容252k
             * scalePercent : 测试内容t372
             * signingMoney : 测试内容k2j6
             */

            String customerAppointment; //预约量尺数
            String customerBill;  //已签合同数
            String customerCommunicate;  //已沟通数
            String customerDeposit; //已收定金数
            String customerDepositMoney; //已收定金金额
            String customerLoss;  //已流失数
            String customerMeasure; //量尺数
            String customerNo;  //无效退回数
            String customerOrder;   //下单客户
            String customerTotal;   //分配数
            String earnestMoney;    //下单金额
            String name;    //导购
            String orderPercent;    //定金转化率
            String scalePercent;    //量尺转化率
            String signingMoney;    //已签合同金额

            public String getCustomerAppointment() {
                return customerAppointment;
            }

            public void setCustomerAppointment(String customerAppointment) {
                this.customerAppointment = customerAppointment;
            }

            public String getCustomerBill() {
                return customerBill;
            }

            public void setCustomerBill(String customerBill) {
                this.customerBill = customerBill;
            }

            public String getCustomerCommunicate() {
                return customerCommunicate;
            }

            public void setCustomerCommunicate(String customerCommunicate) {
                this.customerCommunicate = customerCommunicate;
            }

            public String getCustomerDeposit() {
                return customerDeposit;
            }

            public void setCustomerDeposit(String customerDeposit) {
                this.customerDeposit = customerDeposit;
            }

            public String getCustomerDepositMoney() {
                return customerDepositMoney;
            }

            public void setCustomerDepositMoney(String customerDepositMoney) {
                this.customerDepositMoney = customerDepositMoney;
            }

            public String getCustomerLoss() {
                return customerLoss;
            }

            public void setCustomerLoss(String customerLoss) {
                this.customerLoss = customerLoss;
            }

            public String getCustomerMeasure() {
                return customerMeasure;
            }

            public void setCustomerMeasure(String customerMeasure) {
                this.customerMeasure = customerMeasure;
            }

            public String getCustomerNo() {
                return customerNo;
            }

            public void setCustomerNo(String customerNo) {
                this.customerNo = customerNo;
            }

            public String getCustomerOrder() {
                return customerOrder;
            }

            public void setCustomerOrder(String customerOrder) {
                this.customerOrder = customerOrder;
            }

            public String getCustomerTotal() {
                return customerTotal;
            }

            public void setCustomerTotal(String customerTotal) {
                this.customerTotal = customerTotal;
            }

            public String getEarnestMoney() {
                return earnestMoney;
            }

            public void setEarnestMoney(String earnestMoney) {
                this.earnestMoney = earnestMoney;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getOrderPercent() {
                return orderPercent;
            }

            public void setOrderPercent(String orderPercent) {
                this.orderPercent = orderPercent;
            }

            public String getScalePercent() {
                return scalePercent;
            }

            public void setScalePercent(String scalePercent) {
                this.scalePercent = scalePercent;
            }

            public String getSigningMoney() {
                return signingMoney;
            }

            public void setSigningMoney(String signingMoney) {
                this.signingMoney = signingMoney;
            }
        }
    }

    @Parcel
    public static class PercentDataBean {
        /**
         * area : 四大区
         * cityCode :
         * customerNo : 13
         * customerNoPercent : 10.6%
         * customerTotal : 123
         * customerYes : 110
         * earnestCount : 0
         * earnestMoney : 0
         * isChange : 0
         * isClick : 0
         * name : 那崇奇
         * orderPercent : 0%
         * scaleCount : 0
         * scaleCountPercent : 0%
         * signingCount : 0
         * signingMoney : 0
         * type : 2
         */

        String area;
        String cityCode;
        String customerNo;
        String customerNoPercent;
        String customerTotal;
        String customerYes;
        int earnestCount;
        String earnestMoney;
        String isChange;
        String isClick;
        String name;
        String orderPercent;
        int scaleCount;
        String scaleCountPercent;
        int signingCount;
        String signingMoney;
        String type;

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

        public String getCustomerNo() {
            return customerNo;
        }

        public void setCustomerNo(String customerNo) {
            this.customerNo = customerNo;
        }

        public String getCustomerNoPercent() {
            return customerNoPercent;
        }

        public void setCustomerNoPercent(String customerNoPercent) {
            this.customerNoPercent = customerNoPercent;
        }

        public String getCustomerTotal() {
            return customerTotal;
        }

        public void setCustomerTotal(String customerTotal) {
            this.customerTotal = customerTotal;
        }

        public String getCustomerYes() {
            return customerYes;
        }

        public void setCustomerYes(String customerYes) {
            this.customerYes = customerYes;
        }

        public String getEarnestCount() {
            return String.valueOf(earnestCount);
        }

        public void setEarnestCount(int earnestCount) {
            this.earnestCount = earnestCount;
        }

        public String getEarnestMoney() {
            return earnestMoney;
        }

        public void setEarnestMoney(String earnestMoney) {
            this.earnestMoney = earnestMoney;
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

        public String getOrderPercent() {
            return orderPercent;
        }

        public void setOrderPercent(String orderPercent) {
            this.orderPercent = orderPercent;
        }

        public String getScaleCount() {
            return String.valueOf(scaleCount);
        }

        public void setScaleCount(int scaleCount) {
            this.scaleCount = scaleCount;
        }

        public String getScaleCountPercent() {
            return scaleCountPercent;
        }

        public void setScaleCountPercent(String scaleCountPercent) {
            this.scaleCountPercent = scaleCountPercent;
        }

        public String getSigningCount() {
            return String.valueOf(signingCount);
        }

        public void setSigningCount(int signingCount) {
            this.signingCount = signingCount;
        }

        public String getSigningMoney() {
            return signingMoney;
        }

        public void setSigningMoney(String signingMoney) {
            this.signingMoney = signingMoney;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }

    @Parcel
    public static class SelectDataBean {
        /**
         * name : 全年
         * selectTime : -1
         */

        String name;
        String selectTime;

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
