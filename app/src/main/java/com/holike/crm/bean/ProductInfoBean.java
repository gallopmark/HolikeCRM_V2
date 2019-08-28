package com.holike.crm.bean;

import org.parceler.Parcel;

import java.util.List;

@Parcel
public class ProductInfoBean {

    /**
     * dataList : [{"colorName":"杏白色","cuttingLong":558,"cuttingWidth":305.5,"depth":307,"edgeBanding":"2111","face":"","grainDirection":"H","height":18,"length":559,"matName":"颗粒板","materialName":"顶板","qty":1,"remark":"","tankGroupName":"书房开放柜","unit":"块"}]
     * name : 柜体
     * type : 1
     */

    public String name;
    public List<DataListBean> dataList;
    public String type;

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

    public List<DataListBean> getDataList() {
        return dataList;
    }

    public void setDataList(List<DataListBean> dataList) {
        this.dataList = dataList;
    }

    @Parcel
    public static class DataListBean {
        /**
         * colorName : 杏白色
         * cuttingLong : 558
         * cuttingWidth : 305.5
         * depth : 307
         * edgeBanding : 2111
         * face :
         * grainDirection : H
         * height : 18
         * length : 559
         * matName : 颗粒板
         * materialName : 顶板
         * qty : 1
         * remark :
         * tankGroupName : 书房开放柜
         * unit : 块
         * **************************
         * colorName	花色
         * cuttingLong	开料长
         * cuttingWidth	开料宽
         * depth	宽
         * edgeBanding	封边信息
         * face	贴面工艺
         * grainDirection	纹路
         * height	厚
         * length	长
         * matName	材质
         * materialName	物料名称
         * qty	数量
         * remark	备注
         * tankGroupName	柜分组名
         * unit	单位
         */

        public String colorName;
        public String cuttingLong;
        public String cuttingWidth;
        public String depth;
        public String edgeBanding;
        public String face;
        public String grainDirection;
        public String height;
        public String length;
        public String matName;
        public String materialName;
        public String qty;
        public String remark;
        public String tankGroupName;
        public String unit;

        public String getColorName() {
            return colorName;
        }

        public void setColorName(String colorName) {
            this.colorName = colorName;
        }

        public String getCuttingLong() {
            return cuttingLong;
        }

        public void setCuttingLong(String cuttingLong) {
            this.cuttingLong = cuttingLong;
        }

        public String getCuttingWidth() {
            return cuttingWidth;
        }

        public void setCuttingWidth(String cuttingWidth) {
            this.cuttingWidth = cuttingWidth;
        }

        public String getDepth() {
            return depth;
        }

        public void setDepth(String depth) {
            this.depth = depth;
        }

        public String getEdgeBanding() {
            return edgeBanding;
        }

        public void setEdgeBanding(String edgeBanding) {
            this.edgeBanding = edgeBanding;
        }

        public String getFace() {
            return face;
        }

        public void setFace(String face) {
            this.face = face;
        }

        public String getGrainDirection() {
            return grainDirection;
        }

        public void setGrainDirection(String grainDirection) {
            this.grainDirection = grainDirection;
        }

        public String getHeight() {
            return height;
        }

        public void setHeight(String height) {
            this.height = height;
        }

        public String getLength() {
            return length;
        }

        public void setLength(String length) {
            this.length = length;
        }

        public String getMatName() {
            return matName;
        }

        public void setMatName(String matName) {
            this.matName = matName;
        }

        public String getMaterialName() {
            return materialName;
        }

        public void setMaterialName(String materialName) {
            this.materialName = materialName;
        }

        public String getQty() {
            return qty;
        }

        public void setQty(String qty) {
            this.qty = qty;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public String getTankGroupName() {
            return tankGroupName;
        }

        public void setTankGroupName(String tankGroupName) {
            this.tankGroupName = tankGroupName;
        }

        public String getUnit() {
            return unit;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }
    }
}
