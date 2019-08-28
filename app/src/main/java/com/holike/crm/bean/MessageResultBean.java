package com.holike.crm.bean;

import android.text.SpannableString;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.annotations.NonNull;

/**
 * Created by gallop on 2019/8/21.
 * Copyright holike possess 2019.
 */
public class MessageResultBean {
    public int noticeRead; //通知未读数
    public int announcementRead; // @mock=19, 公告未读数
    private List<MessageBean> messageList;

    @NonNull
    public List<MessageBean> getMessageList() {
        return messageList == null ? new ArrayList<>() : messageList;
    }

    public static class MessageBean {
        public String messageId; //消息Id
        public String personalId; //, 顾客id 和订单id两者其中会有一个为空字符串
        public String houseId; //房屋id
        public String orderId;  //订单id
        public String title; //标题
        public String describe; //通知类型或者发布人
        String isRead; //是否读了 0未读 1已读
        public String date; //年月日
        public String time; //时分
        public String name;
        String isShow; //1展示重新分配 0不展示重新分配
        String isClick; //1重新分配可点 0重新分配不可点

        public String getType() {
            if (TextUtils.isEmpty(title)) return "";
            if (title.contains("【") && title.contains("】")) {
                int start = title.indexOf("【");
                int end = title.indexOf("】") + 1;
                return title.substring(start, end);
            }
            return "";
        }

        public String wrapTitle(){
            if (TextUtils.isEmpty(title)) return "";
            if (title.contains("【") && title.contains("】")) {
                try {
                    return title.substring(title.lastIndexOf("】") + 1);
                } catch (Exception e) {
                    return title;
                }
            }
            return title;
        }

        /*设置已读*/
        public void setRead() {
            this.isRead = "1";
        }

        /*设置未读*/
        public void setUnread() {
            this.isRead = "0";
        }

        public boolean isRead() {
            return TextUtils.equals(isRead, "1");
        }

        public boolean isShow() {
            return TextUtils.equals(isShow, "1");
        }

        public boolean isClick() {
            return TextUtils.equals(isClick, "1");
        }

        public void setUnClickable(){
            isClick = "0";
        }
    }
}
