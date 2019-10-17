package com.holike.crm.bean;

import android.text.TextUtils;

import androidx.annotation.NonNull;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wqj on 2018/3/1.
 * 首页数据
 */

public class HomepageBean implements Serializable {

    /**
     * expenseTypeData : [{"expenseId":"测试内容936u","expenseName":"测试内容3ctw"}]
     * mainPicture : ["https://file.holike.com/9585f743-4cd9-48c5-b218-d8b79169037a.jpg","https://file.holike.com/aacf0257-898d-4080-9039-6b12bd275021.jpg"]
     * messageContent : 第一条消息
     * messageId : 12
     * messageList : [{"date":"测试内容83nl","describe":"测试内容f92k","isRead":"测试内容f7h7","messageId":"测试内容q787","orderId":"测试内容80zf","time":"测试内容7eyc","title":"测试内容8588","type":21023}]
     * newData : {"amount":"测试内容6m45","area":"测试内容mbxk","contractCount":"测试内容ldbn","getMoney":"测试内容xuar","installed":"测试内容qd24","itemList":[{"count":"测试内容i7pu","icon":"测试内容3tk9","name":"测试内容j1df"}],"newsCount":"测试内容tg2o","orderCount":"测试内容c13v","picCount":"测试内容k1a3","scaleCount":"测试内容3ye6","time":"测试内容fcog","usrType":"测试内容i6tm"}
     * orderData : {"area":"广州市","counts":"50","countsOrder":"50","depositMoney":"50","depositPercent":"50.5%","isShow":1,"orderMoney":"50","orderPercent":"50.5%","targetOrder":"500","targetOrderMoney":"50","targetOrderMoneyPercent":"50.5%","targetOrderPercent":"50%","time":"03/27-04/26","timeDescribe":"426"}
     * phone : 测试内容4p6m
     * questionTypeData : [{"itemList":[{"itemId":"测试内容5wc5","itemName":"测试内容tx23"}],"questionId":"测试内容12qo","questionName":"测试内容3n2u"}]
     * ranking : https://file.holike.com/9a34423d-602d-43e5-b75f-5e235128d104.jpg
     * type : 测试内容86o6
     * typeList : {"associatesData":[{"associatesId":"测试内容4573","associatesName":"测试内容hl1n"}],"customerTypeData":[{"customerTypeId":"0","customerTypeName":"虚拟用户"},{"customerTypeId":"1","customerTypeName":"潜在客户"},{"customerTypeId":"2","customerTypeName":"虚拟客户"},{"customerTypeId":"3","customerTypeName":"正式客户"},{"customerTypeId":"4","customerTypeName":"流失客户"}],"depositData":[{"depositId":"1","depositName":"1万以下"},{"depositId":"2","depositName":"10000-19999"},{"depositId":"3","depositName":"20000-29999"},{"depositId":"4","depositName":"30000-39999"},{"depositId":"5","depositName":"40000-49999"},{"depositId":"6","depositName":"5万以上"}],"followData":[{"followId":"01","followName":"客户未分发"},{"followId":"02","followName":"客户未回访"},{"followId":"03","followName":"客户跟进中"},{"followId":"04","followName":"客户结束跟进"},{"followId":"05","followName":"客户已指定经销商"},{"followId":"06","followName":"经销商已接收"}],"orderStatusData":[{"orderStatusId":"101","orderStatusName":"取消订单"},{"orderStatusId":"102","orderStatusName":"待设计审核"},{"orderStatusId":"103","orderStatusName":"整单撤回"},{"orderStatusId":"104","orderStatusName":"部分空间撤回"},{"orderStatusId":"105","orderStatusName":"审单审图"},{"orderStatusId":"107","orderStatusName":"设计退单"},{"orderStatusId":"108","orderStatusName":"部分空间设计退单"},{"orderStatusId":"109","orderStatusName":"待销售审价"},{"orderStatusId":"110","orderStatusName":"销售审价通过"},{"orderStatusId":"111","orderStatusName":"信贷不通过"},{"orderStatusId":"112","orderStatusName":"财务确认"},{"orderStatusId":"113","orderStatusName":"经销商确认"},{"orderStatusId":"114","orderStatusName":"经销商删除"},{"orderStatusId":"201","orderStatusName":"未排产"},{"orderStatusId":"202","orderStatusName":"已排产"},{"orderStatusId":"203","orderStatusName":"生产中"},{"orderStatusId":"204","orderStatusName":"部分入库"},{"orderStatusId":"205","orderStatusName":"全部入库"},{"orderStatusId":"206","orderStatusName":"部分出库"},{"orderStatusId":"207","orderStatusName":"全部出库"},{"orderStatusId":"208","orderStatusName":"部分开票"},{"orderStatusId":"209","orderStatusName":"已开票"}],"orderTypeData":[{"orderTypeId":"ZRE","orderTypeName":"售后退货单"},{"orderTypeId":"ZOR1","orderTypeName":"标准订单"},{"orderTypeId":"ZOR2","orderTypeName":"样品订单"},{"orderTypeId":"ZOR3","orderTypeName":"自营工程订单"},{"orderTypeId":"ZOR4","orderTypeName":"经销商工程订单"},{"orderTypeId":"ZOR5","orderTypeName":"展会样柜订单"},{"orderTypeId":"ZOR6","orderTypeName":"公司自用订单"},{"orderTypeId":"ZOR7","orderTypeName":"标准补单"},{"orderTypeId":"ZOR9","orderTypeName":"研发打样订单"},{"orderTypeId":"ZFE","orderTypeName":"售后补单"}],"questionTypeData":[{"questionTypeId":"测试内容w3xb","questionTypeName":"测试内容lj6t"}],"renovationData":[{"renovationId":"01","renovationName":"新交房"},{"renovationId":"02","renovationName":"旧房重装"}],"renovationNatureData":[{"renovationNatureId":"01","renovationNatureName":"精装房"},{"renovationNatureId":"02","renovationNatureName":"毛坯房"}],"roomData":[{"roomId":"01","roomName":"单间"},{"roomId":"02","roomName":"一房一厅"},{"roomId":"03","roomName":"二房一厅"},{"roomId":"04","roomName":"二房二厅"},{"roomId":"05","roomName":"三房一厅"},{"roomId":"06","roomName":"三房二厅"},{"roomId":"07","roomName":"四房二厅"},{"roomId":"08","roomName":"别墅"}],"shopData":[{"shopId":"测试内容k2o3","shopName":"测试内容92sd"}],"sourceData":[{"sourceId":"01","sourceName":"网络资源"},{"sourceId":"02","sourceName":"电话资源"},{"sourceId":"03","sourceName":"老客户介绍"},{"sourceId":"04","sourceName":"专业卖场自然人流"},{"sourceId":"05","sourceName":"商超自然人流"},{"sourceId":"06","sourceName":"老客户二次消费"},{"sourceId":"07","sourceName":"活动订单"},{"sourceId":"08","sourceName":"业务员自联"}]}
     */

    private String messageContent;
    private String messageId;
    private NewDataBean newData;
    private OrderDataBean orderData;
    private String phone;
    private String ranking;
    private String type;
    private TypeListBean typeList;
    private List<ExpenseTypeDataBean> expenseTypeData;
    private List<String> mainPicture;
    private List<MessageListBean> messageList;
    private List<QuestionTypeDataBean> questionTypeData;
    private List<RoleDataBean.AuthInfoBean> authInfo;

    public TypeListBean getTypeList() {
        return typeList;
    }

    public void setTypeList(TypeListBean typeListBean) {
        this.typeList = typeListBean;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public NewDataBean getNewData() {
        return newData;
    }

    public void setNewData(NewDataBean newData) {
        this.newData = newData;
    }

    public OrderDataBean getOrderData() {
        return orderData;
    }

    public void setOrderData(OrderDataBean orderData) {
        this.orderData = orderData;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRanking() {
        return ranking;
    }

    public void setRanking(String ranking) {
        this.ranking = ranking;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<ExpenseTypeDataBean> getExpenseTypeData() {
        return expenseTypeData;
    }

    public void setExpenseTypeData(List<ExpenseTypeDataBean> expenseTypeData) {
        this.expenseTypeData = expenseTypeData;
    }

    public List<String> getMainPicture() {
        return mainPicture;
    }

    public void setMainPicture(List<String> mainPicture) {
        this.mainPicture = mainPicture;
    }

    public List<MessageListBean> getMessageList() {
        return messageList;
    }

    public void setMessageList(List<MessageListBean> messageList) {
        this.messageList = messageList;
    }

    public List<QuestionTypeDataBean> getQuestionTypeData() {
        return questionTypeData;
    }

    public void setQuestionTypeData(List<QuestionTypeDataBean> questionTypeData) {
        this.questionTypeData = questionTypeData;
    }

    public List<RoleDataBean.AuthInfoBean> getAuthInfo() {
        return authInfo;
    }

    public static class NewDataBean implements Serializable {
        /**
         * amount : 测试内容6m45
         * area : 测试内容mbxk
         * contractCount : 测试内容ldbn
         * getMoney : 测试内容xuar
         * installed : 测试内容qd24
         * itemList : [{"count":"测试内容i7pu","icon":"测试内容3tk9","name":"测试内容j1df"}]
         * newsCount : 测试内容tg2o
         * orderCount : 测试内容c13v
         * picCount : 测试内容k1a3
         * scaleCount : 测试内容3ye6
         * likeCount : 1
         * time : 测试内容fcog
         * usrType : 测试内容i6tm
         */
        String create;
        List<RoleBean> roleList;
        String amount; //安装订单金额
        String area; //安装平方数
        String contractCount; //签约数
        String getMoney; // 已收款（万为单位）
        String installed; //安装客户数
        String newsCount; //新建客户数
        String orderCount; //订金单数
        String picCount; //出图数
        String scaleCount; //量尺数
        String likeCount; //意向客户数
        String time; //时间描述
        public String usrType;

        public String prescaleCount; //预约量房数
        public String firstSuccess; //一次完成安装率
        public String Satisfied; //客户满意度
        public String scan; //	已扫码入库件数 扫码数
        public String deposit; //订金
        public String contract; //合同款
        public String contractTotal; //成交总金额 合同总金额
        public String seaCustomer; //公海数
        public String scalePercent; //量尺成交率
        public String enterPercent; //进店成交率
        public String ordersCustomer; //订单客户数
        public String receivables; //已收款
        public String tail;//尾款
        public String orders; //定金单数

        private List<ItemListBean> itemList;
        private List<CreditItem> creditItem;

        public List<CreditItem> getCreditItem() {
            return creditItem;
        }

        public String getAmount() {
            return amount;
        }

        public String getArea() {
            return area;
        }

        public void setArea(String area) {
            this.area = area;
        }

        public String getContractCount() {
            return contractCount;
        }

        public String getGetMoney() {
            return getMoney;
        }

        public void setGetMoney(String getMoney) {
            this.getMoney = getMoney;
        }

        public String getInstalled() {
            return installed;
        }

        public void setInstalled(String installed) {
            this.installed = installed;
        }

        public String getNewsCount() {
            return newsCount;
        }

        public String getOrderCount() {
            return orderCount;
        }

        public String getPicCount() {
            return picCount;
        }

        public String getScaleCount() {
            return scaleCount;
        }

        public String getLikeCount() {
            return likeCount;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getUsrType() {
            return usrType;
        }

        public List<ItemListBean> getItemList() {
            return itemList;
        }

        public String getCreate() {
            return create;
        }

        public List<RoleBean> getRoleList() {
            if (roleList == null) return new ArrayList<>();
            return roleList;
        }

        public static class RoleBean implements Serializable {
            public String roleName; //角色名
            public String amount; //安装订单金额
            public String area; //安装平方数
            public String contractCount; //签约数
            public String getMoney; // 已收款（万为单位）
            public String installed; //安装客户数
            public String newsCount; //新建客户数
            public String orderCount; //订金单数
            public String picCount; //出图数
            public String scaleCount; //量尺数
            public String likeCount; //意向客户数
            public String time; //时间描述
            public String usrType;
            @SerializedName("isClike")
            String isClick;

            public String prescaleCount; //预约量房数
            public String firstSuccess; //一次完成安装率
            public String Satisfied; //客户满意度
            public String scan; //	已扫码入库件数 扫码数
            public String deposit; //订金
            public String contract; //合同款
            public String contractTotal; //成交总金额 合同总金额
            public String seaCustomer; //公海数
            public String scalePercent; //量尺成交率
            public String enterPercent; //进店成交率
            public String ordersCustomer; //订单客户数
            public String receivables; //已收款
            public String tail;//尾款
            public String orders; //定金单数

            private List<ItemListBean> itemList;

            @NonNull
            public List<ItemListBean> getItemList() {
                if (itemList == null) return new ArrayList<>();
                return itemList;
            }

            /*主要针对老板本月数据是否可点击*/
            public boolean isClick() {
                return TextUtils.equals(isClick, "1");
            }
        }

        public static class ItemListBean implements Serializable {
            /**
             * count : 测试内容i7pu
             * icon : 测试内容3tk9
             * name : 测试内容j1df
             * color:1
             */

            private String count;
            private String icon;
            private String name;
            private String color;

            public String getCount() {
                return count;
            }

            public void setCount(String count) {
                this.count = count;
            }

            public String getIcon() {
                return icon;
            }

            public void setIcon(String icon) {
                this.icon = icon;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getColor() {
                return color;
            }

            public void setColor(String color) {
                this.color = color;
            }
        }

        public static class CreditItem implements Serializable {
            private String icon;
            private String name;
            private String type;

            public String getIcon() {
                return icon;
            }

            public void setIcon(String icon) {
                this.icon = icon;
            }

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
        }
    }

    public static class OrderDataBean implements Serializable {
        /**
         * area : 广州市
         * counts : 50
         * countsOrder : 50
         * depositMoney : 50
         * depositPercent : 50.5%
         * isShow : 1
         * orderMoney : 50
         * orderPercent : 50.5%
         * targetOrder : 500
         * targetOrderMoney : 50
         * targetOrderMoneyPercent : 50.5%
         * targetOrderPercent : 50%
         * time : 03/27-04/26
         * timeDescribe : 426
         */

        private String area;
        private String counts;
        private String countsOrder;
        private String depositMoney;
        private String depositPercent;
        private int isShow;
        private String orderMoney;
        private String orderPercent;
        private String targetOrder;
        private String targetOrderMoney;
        private String targetOrderMoneyPercent;
        private String targetOrderPercent;
        private String time;
        private String timeDescribe;
        private String Cookie;
        public String Cookie2;

        public String getCookie() {
            return Cookie;
        }

        public void setCookie(String cookie) {
            Cookie = cookie;
        }

        public String getArea() {
            return area;
        }

        public void setArea(String area) {
            this.area = area;
        }

        public String getCounts() {
            return counts;
        }

        public void setCounts(String counts) {
            this.counts = counts;
        }

        public String getCountsOrder() {
            return countsOrder;
        }

        public void setCountsOrder(String countsOrder) {
            this.countsOrder = countsOrder;
        }

        public String getDepositMoney() {
            return depositMoney;
        }

        public void setDepositMoney(String depositMoney) {
            this.depositMoney = depositMoney;
        }

        public String getDepositPercent() {
            return depositPercent;
        }

        public void setDepositPercent(String depositPercent) {
            this.depositPercent = depositPercent;
        }

        public int getIsShow() {
            return isShow;
        }

        public void setIsShow(int isShow) {
            this.isShow = isShow;
        }

        public String getOrderMoney() {
            return orderMoney;
        }

        public void setOrderMoney(String orderMoney) {
            this.orderMoney = orderMoney;
        }

        public String getOrderPercent() {
            return orderPercent;
        }

        public void setOrderPercent(String orderPercent) {
            this.orderPercent = orderPercent;
        }

        public String getTargetOrder() {
            return targetOrder;
        }

        public void setTargetOrder(String targetOrder) {
            this.targetOrder = targetOrder;
        }

        public String getTargetOrderMoney() {
            return targetOrderMoney;
        }

        public void setTargetOrderMoney(String targetOrderMoney) {
            this.targetOrderMoney = targetOrderMoney;
        }

        public String getTargetOrderMoneyPercent() {
            return targetOrderMoneyPercent;
        }

        public void setTargetOrderMoneyPercent(String targetOrderMoneyPercent) {
            this.targetOrderMoneyPercent = targetOrderMoneyPercent;
        }

        public String getTargetOrderPercent() {
            return targetOrderPercent;
        }

        public void setTargetOrderPercent(String targetOrderPercent) {
            this.targetOrderPercent = targetOrderPercent;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getTimeDescribe() {
            return timeDescribe;
        }

        public void setTimeDescribe(String timeDescribe) {
            this.timeDescribe = timeDescribe;
        }
    }


    public static class ExpenseTypeDataBean implements Serializable {
        /**
         * expenseId : 测试内容936u
         * expenseName : 测试内容3ctw
         */

        private String expenseId;
        private String expenseName;

        public String getExpenseId() {
            return expenseId;
        }

        public void setExpenseId(String expenseId) {
            this.expenseId = expenseId;
        }

        public String getExpenseName() {
            return expenseName;
        }

        public void setExpenseName(String expenseName) {
            this.expenseName = expenseName;
        }
    }

    public static class MessageListBean implements Serializable {
        /**
         * date : 测试内容83nl
         * describe : 测试内容f92k
         * isRead : 测试内容f7h7
         * messageId : 测试内容q787
         * orderId : 测试内容80zf
         * time : 测试内容7eyc
         * title : 测试内容8588
         * type : 21023
         */

        private String date;
        private String describe;
        private String isRead;
        private String messageId;
        private String orderId;
        private String time;
        private String title;
        private int type;
        public String personalId;
        public String houseId;
        @SerializedName("high_seas_house_flag")
        String highSeasHouseFlag;

        public String getPersonalId() {
            return personalId;
        }

        public void setPersonalId(String personalId) {
            this.personalId = personalId;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getDescribe() {
            return describe;
        }

        public void setDescribe(String describe) {
            this.describe = describe;
        }

        public String getIsRead() {
            return isRead;
        }

        public void setIsRead(String isRead) {
            this.isRead = isRead;
        }

        public String getMessageId() {
            return messageId;
        }

        public String getOrderId() {
            return orderId;
        }

        public String getTime() {
            return time;
        }

        public String getTitle() {
            return title;
        }

        public int getType() {
            return type;
        }

        public boolean isHighSeasHouse() {
            return TextUtils.equals(highSeasHouseFlag, "Y") || TextUtils.equals(highSeasHouseFlag, "y");
        }
    }

    public static class QuestionTypeDataBean implements Serializable {
        /**
         * itemList : [{"itemId":"测试内容5wc5","itemName":"测试内容tx23"}]
         * questionId : 测试内容12qo
         * questionName : 测试内容3n2u
         */

        private String questionId;
        private String questionName;
        private List<ItemListBean> itemList;

        public String getQuestionId() {
            return questionId;
        }

        public void setQuestionId(String questionId) {
            this.questionId = questionId;
        }

        public String getQuestionName() {
            return questionName;
        }

        public void setQuestionName(String questionName) {
            this.questionName = questionName;
        }

        public List<ItemListBean> getItemList() {
            return itemList;
        }

        public void setItemList(List<ItemListBean> itemList) {
            this.itemList = itemList;
        }

        public static class ItemListBean implements Serializable {
            /**
             * itemId : 测试内容5wc5
             * itemName : 测试内容tx23
             */

            private String itemId;
            private String itemName;
            private boolean isSelect;

            public String getItemId() {
                return itemId;
            }

            public void setItemId(String itemId) {
                this.itemId = itemId;
            }

            public String getItemName() {
                return itemName;
            }

            public void setItemName(String itemName) {
                this.itemName = itemName;
            }

            public boolean isSelect() {
                return isSelect;
            }

            public void setSelect(boolean select) {
                isSelect = select;
            }
        }
    }


    public static class TypeListBean implements Serializable {
        private List<BrankDataBean> brankData;
        List<DistributionStoreBean> shopData;
        public List<SourceBean> sourceData;
        public List<DepositBean> depositData;
        List<RoleDataBean> roleData;

        public List<BrankDataBean> getBrankData() {
            return brankData;
        }

        public void setBrankData(List<BrankDataBean> brankData) {
            this.brankData = brankData;
        }

        @NonNull
        public List<DistributionStoreBean> getShopData() {
            return shopData == null ? new ArrayList<>() : shopData;
        }

        @NonNull
        public List<RoleDataBean> getRoleData() {
            return roleData == null ? new ArrayList<>() : roleData;
        }

        public static class BrankDataBean implements Serializable {
            /**
             * barkId : 测试内容kz5n
             * barkName : 测试内容13k3
             */

            private String barkId;
            private String barkName;

            public BrankDataBean(String barkId, String barkName) {
                this.barkId = barkId;
                this.barkName = barkName;
            }

            public String getBarkId() {
                return barkId;
            }

            public void setBarkId(String barkId) {
                this.barkId = barkId;
            }

            public String getBarkName() {
                return barkName;
            }

            public void setBarkName(String barkName) {
                this.barkName = barkName;
            }
        }
    }
}
