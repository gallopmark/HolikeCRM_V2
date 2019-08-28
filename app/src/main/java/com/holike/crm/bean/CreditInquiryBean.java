package com.holike.crm.bean;

import org.parceler.Parcel;

import java.util.List;

@Parcel
public class CreditInquiryBean {


    /**
     * creditAmount : 54317.52
     * pageData : [{"orderId":"BZ13109100-1812040001","plantPriceAf":232.68,"plantPriceBe":232.68},{"orderId":"BZ13109100-1812100002","plantPriceAf":5333.03,"plantPriceBe":5333.03},{"orderId":"BZ13109100-1812100003","plantPriceAf":4260.93,"plantPriceBe":4260.93},{"orderId":"BZ13109100-1812100004","plantPriceAf":5641.86,"plantPriceBe":5641.86},{"orderId":"BZ13109100-1812100005","plantPriceAf":3029.55,"plantPriceBe":3029.55},{"orderId":"BZ13109100-1812100006","plantPriceAf":716.78,"plantPriceBe":716.78},{"orderId":"BZ13109100-1812100007","plantPriceAf":3314.74,"plantPriceBe":3314.74},{"orderId":"BZ13109100-1812100008","plantPriceAf":1597.56,"plantPriceBe":1597.56},{"orderId":"BZ13109100-1812100009","plantPriceAf":615.41,"plantPriceBe":615.41},{"orderId":"BZ13109100-1812100010","plantPriceAf":762.13,"plantPriceBe":762.13}]
     * plantTotPriAf : 156715.16
     * surplusAmount : -102397.64
     */

    String creditAmount;
    String plantTotPriAf;
    String plantTotPriBe;
    String surplusAmount;
    String orderTot;
    List<PageDataBean> pageData;

    public String getPlantTotPriBe() {
        return plantTotPriBe;
    }

    public void setPlantTotPriBe(String plantTotPriBe) {
        this.plantTotPriBe = plantTotPriBe;
    }

    public String getOrderTot() {
        return orderTot;
    }

    public void setOrderTot(String orderTot) {
        this.orderTot = orderTot;
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

    public String getSurplusAmount() {
        return surplusAmount;
    }

    public void setSurplusAmount(String surplusAmount) {
        this.surplusAmount = surplusAmount;
    }

    public List<PageDataBean> getPageData() {
        return pageData;
    }

    public void setPageData(List<PageDataBean> pageData) {
        this.pageData = pageData;
    }

    @Parcel
    public static class PageDataBean implements MultiItem{
        /**
         * orderId : BZ13109100-1812040001
         * plantPriceAf : 232.68
         * plantPriceBe : 232.68
         */

        String orderId;
        String plantPriceAf;
        String plantPriceBe;

        public String getOrderId() {
            return orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }

        public String getPlantPriceAf() {
            return plantPriceAf;
        }

        public void setPlantPriceAf(String plantPriceAf) {
            this.plantPriceAf = plantPriceAf;
        }

        public String getPlantPriceBe() {
            return plantPriceBe;
        }

        public void setPlantPriceBe(String plantPriceBe) {
            this.plantPriceBe = plantPriceBe;
        }

        @Override
        public int getItemType() {
            return 0;
        }
    }
}
