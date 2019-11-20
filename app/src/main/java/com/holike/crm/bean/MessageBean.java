package com.holike.crm.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by wqj on 2018/3/5.
 * 消息列表bean
 */

public class MessageBean implements Serializable {
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
