package com.holike.crm.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by wqj on 2018/3/5.
 * 消息列表bean
 */

public class MessageBean implements Serializable {
    /**
     * announcementRead : 19
     * messageList : [{"date":"2018-02-28","describe":"crmadmin","isRead":"0","messageId":"ddc0cd5a3bb545ec9a931963052a870f","orderId":"","time":"15:14","title":"好莱客"},{"date":"2018-02-05","describe":"王芳莉","isRead":"1","messageId":"98a7bcbb05e540f3a0d36f9c80fc238d","orderId":"","time":"36:38","title":"关于实木订单截止接单的通知"},{"date":"2018-02-05","describe":"王芳莉","isRead":"1","messageId":"1a64feff75d140198f757f93810ec3e5","orderId":"","time":"27:06","title":"关于卡普里系列DK床淘汰替换的通知"},{"date":"2018-01-23","describe":"王芳莉","isRead":"0","messageId":"748aa6fe05114910933e72d62adcbe0e","orderId":"","time":"23:11","title":"关于部分五金物料淘汰的通知"},{"date":"2018-01-23","describe":"王芳莉","isRead":"0","messageId":"55e5589632de4021a52ba486ea37ceef","orderId":"","time":"22:55","title":"关于多区门中格横装百叶的设计指引"},{"date":"2018-01-22","describe":"王芳莉","isRead":"0","messageId":"8f3a8c2c2c2947cf9fdcab63cf2f5ec6","orderId":"","time":"50:11","title":"关于2017年新门板及相关物料上市的通知"},{"date":"2018-01-11","describe":"crmadmin","isRead":"0","messageId":"01f485f2e08c410eadc654b8b24174d1","orderId":"","time":"38:32","title":"好莱客"},{"date":"2018-01-11","describe":"王芳莉","isRead":"0","messageId":"7686ea5d69be4dd99cbcf3286a0593d1","orderId":"","time":"10:14","title":"关于床头柜和床垫新品上市以及两款沙发单人位开放系统下单的通知"},{"date":"2018-01-11","describe":"王芳莉","isRead":"0","messageId":"2580d8effd44400a956a32f51a9f64b8","orderId":"","time":"03:40","title":"关于4.26世界无醛日促销活动获奖经销商奖励通告"},{"date":"2018-01-11","describe":"王芳莉","isRead":"0","messageId":"5f9a63ccce1f477bbd946bea9ec97eb9","orderId":"","time":"00:41","title":"关于终端预约量房物料的上市通知"}]
     * noticeRead : 6
     */

    private int announcementRead;
    private int noticeRead;
    private List<MessageListBean> messageList;

    public int getAnnouncementRead() {
        return announcementRead;
    }

    public void setAnnouncementRead(int announcementRead) {
        this.announcementRead = announcementRead;
    }

    public int getNoticeRead() {
        return noticeRead;
    }

    public void setNoticeRead(int noticeRead) {
        this.noticeRead = noticeRead;
    }

    public List<MessageListBean> getMessageList() {
        return messageList;
    }

    public void setMessageList(List<MessageListBean> messageList) {
        this.messageList = messageList;
    }

    public static class MessageListBean implements Serializable {
        /**
         * date : 2018-02-28
         * describe : crmadmin
         * isRead : 0
         * messageId : ddc0cd5a3bb545ec9a931963052a870f
         * orderId :
         * time : 15:14
         * title : 好莱客
         * personalId:
         * houseId:
         */

        private String date;
        private String describe;
        private String isRead;
        private String messageId;
        private String orderId;
        private String time;
        private String title;
        private String personalId;
        private String houseId;

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

        public void setMessageId(String messageId) {
            this.messageId = messageId;
        }

        public String getOrderId() {
            return orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }

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

        public String getPersonalId() {
            return personalId;
        }

        public void setPersonalId(String personalId) {
            this.personalId = personalId;
        }

        public String getHouseId() {
            return houseId;
        }

        public void setHouseId(String houseId) {
            this.houseId = houseId;
        }
    }
}
