package com.holike.crm.bean;

import java.util.List;

public class ReceiptDeliveryManifestBean {


    /**
     * outStorageOperator : 测试内容yjk9
     * packingList : [{"packingData":[{"ply":"测试内容763k","quantity":"测试内容7vw7","unit":"测试内容251b","width":"测试内容wbsw","assortment":"密苏里胡桃","length":2370,"materialsDescription":"超高垭口套板","rowNumber":"000010"}],"packingBarCode":"101305","packingSerialNumber":"0001"}]
     * createTime : 2018.10.11
     * inStatus : 全部入库
     * incomingOperator : 刘贞雄-80000006
     * incomingTime : 2018.10.24 15:28
     * largeSizeComponentsIdentify : 门套板
     * outStatus : 全部出库
     * outStorageTime : 2018.10.25 16:27
     * receivePackages : 0
     * sendPackages : 4
     * soOrderId : 0340013563
     * totalPackage : 4
     */

    private String outStorageOperator;
    private String createTime;
    private String inStatus;
    private String incomingOperator;
    private String incomingTime;
    private String largeSizeComponentsIdentify;
    private String outStatus;
    private String outStorageTime;
    private String receivePackages;
    private String sendPackages;
    private String soOrderId;
    private String totalPackage;
    private List<PackingListBean> packingList;

    public String getOutStorageOperator() {
        return outStorageOperator;
    }

    public void setOutStorageOperator(String outStorageOperator) {
        this.outStorageOperator = outStorageOperator;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getInStatus() {
        return inStatus;
    }

    public void setInStatus(String inStatus) {
        this.inStatus = inStatus;
    }

    public String getIncomingOperator() {
        return incomingOperator;
    }

    public void setIncomingOperator(String incomingOperator) {
        this.incomingOperator = incomingOperator;
    }

    public String getIncomingTime() {
        return incomingTime;
    }

    public void setIncomingTime(String incomingTime) {
        this.incomingTime = incomingTime;
    }

    public String getLargeSizeComponentsIdentify() {
        return largeSizeComponentsIdentify;
    }

    public void setLargeSizeComponentsIdentify(String largeSizeComponentsIdentify) {
        this.largeSizeComponentsIdentify = largeSizeComponentsIdentify;
    }

    public String getOutStatus() {
        return outStatus;
    }

    public void setOutStatus(String outStatus) {
        this.outStatus = outStatus;
    }

    public String getOutStorageTime() {
        return outStorageTime;
    }

    public void setOutStorageTime(String outStorageTime) {
        this.outStorageTime = outStorageTime;
    }

    public String getReceivePackages() {
        return receivePackages;
    }

    public void setReceivePackages(String receivePackages) {
        this.receivePackages = receivePackages;
    }

    public String getSendPackages() {
        return sendPackages;
    }

    public void setSendPackages(String sendPackages) {
        this.sendPackages = sendPackages;
    }

    public String getSoOrderId() {
        return soOrderId;
    }

    public void setSoOrderId(String soOrderId) {
        this.soOrderId = soOrderId;
    }

    public String getTotalPackage() {
        return totalPackage;
    }

    public void setTotalPackage(String totalPackage) {
        this.totalPackage = totalPackage;
    }

    public List<PackingListBean> getPackingList() {
        return packingList;
    }

    public void setPackingList(List<PackingListBean> packingList) {
        this.packingList = packingList;
    }

    public static class PackingListBean {
        /**
         * packingData : [{"ply":"测试内容763k","quantity":"测试内容7vw7","unit":"测试内容251b","width":"测试内容wbsw","assortment":"密苏里胡桃","length":2370,"materialsDescription":"超高垭口套板","rowNumber":"000010"}]
         * packingBarCode : 101305
         * packingSerialNumber : 0001
         */

        private String packingBarCode;
        private String packingSerialNumber;
        private List<PackingDataBean> packingData;

        public String getPackingBarCode() {
            return packingBarCode;
        }

        public void setPackingBarCode(String packingBarCode) {
            this.packingBarCode = packingBarCode;
        }

        public String getPackingSerialNumber() {
            return packingSerialNumber;
        }

        public void setPackingSerialNumber(String packingSerialNumber) {
            this.packingSerialNumber = packingSerialNumber;
        }

        public List<PackingDataBean> getPackingData() {
            return packingData;
        }

        public void setPackingData(List<PackingDataBean> packingData) {
            this.packingData = packingData;
        }

        public static class PackingDataBean {
            /**
             * ply : 测试内容763k
             * quantity : 测试内容7vw7
             * unit : 测试内容251b
             * width : 测试内容wbsw
             * assortment : 密苏里胡桃
             * length : 2370
             * materialsDescription : 超高垭口套板
             * rowNumber : 000010
             */

            private String ply;
            private String quantity;
            private String unit;
            private String width;
            private String assortment;
            private String length;
            private String materialsDescription;
            private String rowNumber;

            public String getPly() {
                return ply;
            }

            public void setPly(String ply) {
                this.ply = ply;
            }

            public String getQuantity() {
                return quantity;
            }

            public void setQuantity(String quantity) {
                this.quantity = quantity;
            }

            public String getUnit() {
                return unit;
            }

            public void setUnit(String unit) {
                this.unit = unit;
            }

            public String getWidth() {
                return width;
            }

            public void setWidth(String width) {
                this.width = width;
            }

            public String getAssortment() {
                return assortment;
            }

            public void setAssortment(String assortment) {
                this.assortment = assortment;
            }

            public String getLength() {
                return length;
            }

            public void setLength(String length) {
                this.length = length;
            }

            public String getMaterialsDescription() {
                return materialsDescription;
            }

            public void setMaterialsDescription(String materialsDescription) {
                this.materialsDescription = materialsDescription;
            }

            public String getRowNumber() {
                return rowNumber;
            }

            public void setRowNumber(String rowNumber) {
                this.rowNumber = rowNumber;
            }
        }
    }
}
