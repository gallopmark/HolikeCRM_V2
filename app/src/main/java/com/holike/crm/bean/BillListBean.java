package com.holike.crm.bean;


import org.parceler.Parcel;

import java.io.Serializable;
import java.util.List;

@Parcel
public class BillListBean implements Serializable {

    /**
     * pageData : [{"orderId":"","bstkd":"ZB13109100-1902120001","zysqc":-183916.94,"shopName":"","zkyje":53417.52,"zhuok":"0.00","type":-1,"zdate":"2019-02-12","zysfs":"0.00","dqrso":"0.00","ccjzh":"150.00","hdzkje":"0.00","zflje":"0.00","zfladd":"0.00","zysqm":-183916.94,"createDate":1111111111111,"ccjzq":"150.00","amount":-1,"dealerName":"","koufei":"0.00","zfldow":"0.00","zqcye":53567.52,"money":-150,"cztxt":"下单","zflqc":"0.00","zflqm":"0.00","statusCode":""},{"orderId":"","bstkd":"ZB13109100-1902120002","zysqc":-183916.94,"shopName":"","zkyje":53416.72,"zhuok":"0.00","type":-1,"zdate":"2019-02-12","zysfs":"0.00","dqrso":"0.00","ccjzh":"0.80","hdzkje":"0.00","zflje":"0.00","zfladd":"0.00","zysqm":-183916.94,"createDate":1111111111111,"ccjzq":"0.80","amount":-1,"dealerName":"","koufei":"0.00","zfldow":"0.00","zqcye":53417.52,"money":-0.7999999999956344,"cztxt":"下单","zflqc":"0.00","zflqm":"0.00","statusCode":""}]
     * surplusAmount : -1
     * creditAmount : -1
     * plantTotPriAf : -1
     * headData : {"ccjzq":150.8,"zysqc":-183916.94,"zkyje":53416.72,"zhuok":"0.00","zysfs":"0.00","koufei":"0.00","kscsoje":-152661.2,"zfldow":"0.00","zqcye":53567.52,"zxsrw":"0.00","zflqc":"0.00","dqrso":206077.92,"limitLeft":53416.72,"limit":"0.00","ccjzh":150.8,"hdzkje":"0.00","zflqm":"0.00","zflje":"0.00","zfladd":"0.00","zysqm":-183916.94}
     */
    String endTime;
    String startTime;
    String surplusAmount;
    String creditAmount;
    String plantTotPriAf;
    HeadDataBean headData;
    List<PageDataBean> pageData;


    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getSurplusAmount() {
        return surplusAmount;
    }

    public void setSurplusAmount(String surplusAmount) {
        this.surplusAmount = surplusAmount;
    }

    public String getCreditAmount() {
        return creditAmount;
    }

    public void setCreditAmount(String creditAmount) {
        this.creditAmount = creditAmount;
    }

    public String getPlantTotPriAf() {
        return plantTotPriAf;
    }

    public void setPlantTotPriAf(String plantTotPriAf) {
        this.plantTotPriAf = plantTotPriAf;
    }

    public HeadDataBean getHeadData() {
        return headData;
    }

    public void setHeadData(HeadDataBean headData) {
        this.headData = headData;
    }

    public List<PageDataBean> getPageData() {
        return pageData;
    }

    public void setPageData(List<PageDataBean> pageData) {
        this.pageData = pageData;
    }

    @Parcel
    public static class HeadDataBean implements Serializable{
        /**
         * ccjzq : 150.8
         * zysqc : -183916.94
         * zkyje : 53416.72
         * zhuok : 0.00
         * zysfs : 0.00
         * koufei : 0.00
         * kscsoje : -152661.2
         * zfldow : 0.00
         * zqcye : 53567.52
         * zxsrw : 0.00
         * zflqc : 0.00
         * dqrso : 206077.92
         * limitLeft : 53416.72
         * limit : 0.00
         * ccjzh : 150.8
         * hdzkje : 0.00
         * zflqm : 0.00
         * zflje : 0.00
         * zfladd : 0.00
         * zysqm : -183916.94
         */

        String ccjzq;
        String zysqc;
        String zkyje;
        String zhuok;
        String zysfs;
        String koufei;
        String kscsoje;
        String zfldow;
        String zqcye;
        String zxsrw;
        String zflqc;
        String dqrso;
        String limitLeft;
        String limit;
        String ccjzh;
        String hdzkje;
        String zflqm;
        String zflje;
        String zfladd;
        String zysqm;

        public String getCcjzq() {
            return ccjzq;
        }

        public void setCcjzq(String ccjzq) {
            this.ccjzq = ccjzq;
        }

        public String getZysqc() {
            return zysqc;
        }

        public void setZysqc(String zysqc) {
            this.zysqc = zysqc;
        }

        public String getZkyje() {
            return zkyje;
        }

        public void setZkyje(String zkyje) {
            this.zkyje = zkyje;
        }

        public String getZhuok() {
            return zhuok;
        }

        public void setZhuok(String zhuok) {
            this.zhuok = zhuok;
        }

        public String getZysfs() {
            return zysfs;
        }

        public void setZysfs(String zysfs) {
            this.zysfs = zysfs;
        }

        public String getKoufei() {
            return koufei;
        }

        public void setKoufei(String koufei) {
            this.koufei = koufei;
        }

        public String getKscsoje() {
            return kscsoje;
        }

        public void setKscsoje(String kscsoje) {
            this.kscsoje = kscsoje;
        }

        public String getZfldow() {
            return zfldow;
        }

        public void setZfldow(String zfldow) {
            this.zfldow = zfldow;
        }

        public String getZqcye() {
            return zqcye;
        }

        public void setZqcye(String zqcye) {
            this.zqcye = zqcye;
        }

        public String getZxsrw() {
            return zxsrw;
        }

        public void setZxsrw(String zxsrw) {
            this.zxsrw = zxsrw;
        }

        public String getZflqc() {
            return zflqc;
        }

        public void setZflqc(String zflqc) {
            this.zflqc = zflqc;
        }

        public String getDqrso() {
            return dqrso;
        }

        public void setDqrso(String dqrso) {
            this.dqrso = dqrso;
        }

        public String getLimitLeft() {
            return limitLeft;
        }

        public void setLimitLeft(String limitLeft) {
            this.limitLeft = limitLeft;
        }

        public String getLimit() {
            return limit;
        }

        public void setLimit(String limit) {
            this.limit = limit;
        }

        public String getCcjzh() {
            return ccjzh;
        }

        public void setCcjzh(String ccjzh) {
            this.ccjzh = ccjzh;
        }

        public String getHdzkje() {
            return hdzkje;
        }

        public void setHdzkje(String hdzkje) {
            this.hdzkje = hdzkje;
        }

        public String getZflqm() {
            return zflqm;
        }

        public void setZflqm(String zflqm) {
            this.zflqm = zflqm;
        }

        public String getZflje() {
            return zflje;
        }

        public void setZflje(String zflje) {
            this.zflje = zflje;
        }

        public String getZfladd() {
            return zfladd;
        }

        public void setZfladd(String zfladd) {
            this.zfladd = zfladd;
        }

        public String getZysqm() {
            return zysqm;
        }

        public void setZysqm(String zysqm) {
            this.zysqm = zysqm;
        }
    }

    @Parcel
    public static class PageDataBean implements Serializable,MultiItem {
        /**
         * orderId :
         * bstkd : ZB13109100-1902120001
         * zysqc : -183916.94
         * shopName :
         * zkyje : 53417.52
         * zhuok : 0.00
         * type : -1
         * zdate : 2019-02-12
         * zysfs : 0.00
         * dqrso : 0.00
         * ccjzh : 150.00
         * hdzkje : 0.00
         * zflje : 0.00
         * zfladd : 0.00
         * zysqm : -183916.94
         * createDate : 1111111111111
         * ccjzq : 150.00
         * amount : -1
         * dealerName :
         * koufei : 0.00
         * zfldow : 0.00
         * zqcye : 53567.52
         * money : -150
         * cztxt : 下单
         * zflqc : 0.00
         * zflqm : 0.00
         * statusCode :
         */

        String orderId;
        String bstkd;
        String zysqc;
        String shopName;
        String zkyje;
        String zhuok;
        String type;
        String zdate;
        String zysfs;
        String dqrso;
        String ccjzh;
        String hdzkje;
        String zflje;
        String zfladd;
        String zysqm;
        String createDate;
        String ccjzq;
        String amount;
        String dealerName;
        String koufei;
        String zfldow;
        String zqcye;
        String money;
        String cztxt;
        String zflqc;
        String zflqm;
        String statusCode;

        public String getOrderId() {
            return orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }

        public String getBstkd() {
            return bstkd;
        }

        public void setBstkd(String bstkd) {
            this.bstkd = bstkd;
        }

        public String getZysqc() {
            return zysqc;
        }

        public void setZysqc(String zysqc) {
            this.zysqc = zysqc;
        }

        public String getShopName() {
            return shopName;
        }

        public void setShopName(String shopName) {
            this.shopName = shopName;
        }

        public String getZkyje() {
            return zkyje;
        }

        public void setZkyje(String zkyje) {
            this.zkyje = zkyje;
        }

        public String getZhuok() {
            return zhuok;
        }

        public void setZhuok(String zhuok) {
            this.zhuok = zhuok;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getZdate() {
            return zdate;
        }

        public void setZdate(String zdate) {
            this.zdate = zdate;
        }

        public String getZysfs() {
            return zysfs;
        }

        public void setZysfs(String zysfs) {
            this.zysfs = zysfs;
        }

        public String getDqrso() {
            return dqrso;
        }

        public void setDqrso(String dqrso) {
            this.dqrso = dqrso;
        }

        public String getCcjzh() {
            return ccjzh;
        }

        public void setCcjzh(String ccjzh) {
            this.ccjzh = ccjzh;
        }

        public String getHdzkje() {
            return hdzkje;
        }

        public void setHdzkje(String hdzkje) {
            this.hdzkje = hdzkje;
        }

        public String getZflje() {
            return zflje;
        }

        public void setZflje(String zflje) {
            this.zflje = zflje;
        }

        public String getZfladd() {
            return zfladd;
        }

        public void setZfladd(String zfladd) {
            this.zfladd = zfladd;
        }

        public String getZysqm() {
            return zysqm;
        }

        public void setZysqm(String zysqm) {
            this.zysqm = zysqm;
        }

        public String getCreateDate() {
            return createDate;
        }

        public void setCreateDate(String createDate) {
            this.createDate = createDate;
        }

        public String getCcjzq() {
            return ccjzq;
        }

        public void setCcjzq(String ccjzq) {
            this.ccjzq = ccjzq;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getDealerName() {
            return dealerName;
        }

        public void setDealerName(String dealerName) {
            this.dealerName = dealerName;
        }

        public String getKoufei() {
            return koufei;
        }

        public void setKoufei(String koufei) {
            this.koufei = koufei;
        }

        public String getZfldow() {
            return zfldow;
        }

        public void setZfldow(String zfldow) {
            this.zfldow = zfldow;
        }

        public String getZqcye() {
            return zqcye;
        }

        public void setZqcye(String zqcye) {
            this.zqcye = zqcye;
        }

        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
        }

        public String getCztxt() {
            return cztxt;
        }

        public void setCztxt(String cztxt) {
            this.cztxt = cztxt;
        }

        public String getZflqc() {
            return zflqc;
        }

        public void setZflqc(String zflqc) {
            this.zflqc = zflqc;
        }

        public String getZflqm() {
            return zflqm;
        }

        public void setZflqm(String zflqm) {
            this.zflqm = zflqm;
        }

        public String getStatusCode() {
            return statusCode;
        }

        public void setStatusCode(String statusCode) {
            this.statusCode = statusCode;
        }

        @Override
        public int getItemType() {
            return 0;
        }
    }
}
