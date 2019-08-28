package com.holike.crm.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by wqj on 2018/8/23.
 * 分配导购bean
 */

public class DivideGuideBean implements Serializable {
    /**
     * emptyIdentifier : 1
     * flag : true
     * list : [{"userId":"11000002-0001","userName":"付东旭"}]
     * name : 测试内容c86e
     * time : 测试内容elw3
     */

    private boolean flag;
    private String name;
    private String time;
    private List<ListBean> list;

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean extends TypeIdBean.TypeIdItem {
        /**
         * userId : 11000002-0001
         * userName : 付东旭
         */

        private String userId;
        private String userName;

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        @Override
        public String getPickerViewText() {
            return userName;
        }
    }
}
