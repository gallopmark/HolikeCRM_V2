package com.holike.crm.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by wqj on 2018/6/6.
 * 所属人
 */

public class AssociateBean implements Serializable {
    private List<AllotStyBean> allotSty;
    private List<GuideBean> guide;
    private List<ProfessionBean> profession;
    private List<Installer> installer;
    private List<AllUserBean> allUser;

    public List<AllotStyBean> getAllotSty() {
        return allotSty;
    }

    public void setAllotSty(List<AllotStyBean> allotSty) {
        this.allotSty = allotSty;
    }

    public List<GuideBean> getGuide() {
        return guide;
    }

    public void setGuide(List<GuideBean> guide) {
        this.guide = guide;
    }

    public List<ProfessionBean> getProfession() {
        return profession;
    }

    public void setProfession(List<ProfessionBean> profession) {
        this.profession = profession;
    }

    public List<Installer> getInstaller() {
        return installer;
    }

    public void setInstaller(List<Installer> installer) {
        this.installer = installer;
    }

    public List<AllUserBean> getAllUser() {
        return allUser;
    }

    public void setAllUser(List<AllUserBean> allUser) {
        this.allUser = allUser;
    }

    public static class AllotStyBean extends TypeIdBean.TypeIdItem {
        /**
         * userId : 20231007-0021
         * userName : 蔡齐任
         */

        private String userId;
        private String userName;

        public AllotStyBean(String userId, String userName) {
            super(userId, userName);
            this.userId = userId;
            this.userName = userName;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            setId(userId);
            this.userId = userId;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            setName(userName);
            this.userName = userName;
        }

        @Override
        public String getPickerViewText() {
            return userName;
        }
    }

    public static class GuideBean extends TypeIdBean.TypeIdItem {
        /**
         * userId : 20231007-0021
         * userName : 蔡齐任
         */

        private String userId;
        private String userName;

        public GuideBean(String userId, String userName) {
            super(userId, userName);
            this.userId = userId;
            this.userName = userName;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            setId(userId);
            this.userId = userId;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            setName(userName);
            this.userName = userName;
        }

        @Override
        public String getPickerViewText() {
            return userName;
        }
    }

    public static class ProfessionBean extends TypeIdBean.TypeIdItem {
        /**
         * userId : 20231007-0021
         * userName : 蔡齐任
         */

        private String userId;
        private String userName;

        public ProfessionBean(String userId, String userName) {
            super(userId, userName);
            this.userId = userId;
            this.userName = userName;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            setId(userId);
            this.userId = userId;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            setName(userName);
            this.userName = userName;
        }

        @Override
        public String getPickerViewText() {
            return userName;
        }
    }

    public static class Installer extends TypeIdBean.TypeIdItem {
        private String userId;
        private String userName;

        public Installer(String userId, String userName) {
            super(userId, userName);
            this.userId = userId;
            this.userName = userName;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            setId(userId);
            this.userId = userId;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            setName(userName);
            this.userName = userName;
        }

        @Override
        public String getPickerViewText() {
            return userName;
        }
    }

    public static class AllUserBean extends TypeIdBean.TypeIdItem {
        private String userId;
        private String userName;

        public AllUserBean(String userId, String userName) {
            this.userId = userId;
            this.userName = userName;
        }

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
