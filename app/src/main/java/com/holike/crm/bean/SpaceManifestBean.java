package com.holike.crm.bean;

import org.parceler.Parcel;

import java.util.List;

@Parcel
public class SpaceManifestBean {


    /**
     * dataList : [{"productType":"测试内容o2t9","color":"杏白色","grainDirection":"","isBack":"","joinActivityName":"不参与活动","orUrgent":"否","or_free":"否","patching":"否","planDeliveryDate":"2018-12-04","productName":"右圆弧矮柜","product_Qty":1,"remark":"","style":"其他","texture":"颗粒板","totalArea":1.0409,"unifiedDelivery":"是","urgentPrice":8.77}]
     * houseName : 主卧
     */

    public String houseName;
    public List<DataListBean> dataList;

    public String getHouseName() {
        return houseName;
    }

    public void setHouseName(String houseName) {
        this.houseName = houseName;
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
         * productType : 测试内容o2t9
         * color : 杏白色
         * grainDirection :
         * isBack :
         * joinActivityName : 不参与活动
         * orUrgent : 否
         * or_free : 否
         * patching : 否
         * planDeliveryDate : 2018-12-04
         * productName : 右圆弧矮柜
         * product_Qty : 1
         * remark :
         * style : 其他
         * texture : 颗粒板
         * totalArea : 1.0409
         * unifiedDelivery : 是
         * urgentPrice : 8.77
         */

        public String productType;
        public String product_Qty;
        public String color;
        public String grainDirection;
        public String isBack;
        public String joinActivityName;
        public String orUrgent;
        public String or_free;
        public String patching;
        public String planDeliveryDate;
        public String productName;
        public String remark;
        public String style;
        public String texture;
        public String totalArea;
        public String unifiedDelivery;
        public String urgentPrice;

        public String getProductType() {
            return productType;
        }

        public void setProductType(String productType) {
            this.productType = productType;
        }

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
        }

        public String getGrainDirection() {
            return grainDirection;
        }

        public void setGrainDirection(String grainDirection) {
            this.grainDirection = grainDirection;
        }

        public String getIsBack() {
            return isBack;
        }

        public void setIsBack(String isBack) {
            this.isBack = isBack;
        }

        public String getJoinActivityName() {
            return joinActivityName;
        }

        public void setJoinActivityName(String joinActivityName) {
            this.joinActivityName = joinActivityName;
        }

        public String getOrUrgent() {
            return orUrgent;
        }

        public void setOrUrgent(String orUrgent) {
            this.orUrgent = orUrgent;
        }

        public String getOr_free() {
            return or_free;
        }

        public void setOr_free(String or_free) {
            this.or_free = or_free;
        }

        public String getPatching() {
            return patching;
        }

        public void setPatching(String patching) {
            this.patching = patching;
        }

        public String getPlanDeliveryDate() {
            return planDeliveryDate;
        }

        public void setPlanDeliveryDate(String planDeliveryDate) {
            this.planDeliveryDate = planDeliveryDate;
        }

        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }

        public String getProduct_Qty() {
            return product_Qty;
        }

        public void setProduct_Qty(String product_Qty) {
            this.product_Qty = product_Qty;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public String getStyle() {
            return style;
        }

        public void setStyle(String style) {
            this.style = style;
        }

        public String getTexture() {
            return texture;
        }

        public void setTexture(String texture) {
            this.texture = texture;
        }

        public String getTotalArea() {
            return totalArea;
        }

        public void setTotalArea(String totalArea) {
            this.totalArea = totalArea;
        }

        public String getUnifiedDelivery() {
            return unifiedDelivery;
        }

        public void setUnifiedDelivery(String unifiedDelivery) {
            this.unifiedDelivery = unifiedDelivery;
        }

        public String getUrgentPrice() {
            return urgentPrice;
        }

        public void setUrgentPrice(String urgentPrice) {
            this.urgentPrice = urgentPrice;
        }
    }
}
