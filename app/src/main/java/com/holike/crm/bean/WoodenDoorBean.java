package com.holike.crm.bean;

import android.text.TextUtils;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.annotations.Nullable;

/**
 * Created by gallop on 2019/8/28.
 * Copyright holike possess 2019.
 */
public class WoodenDoorBean implements Serializable {

    public String timeData;
    public String time;
    private String isDealer;
    public String selected;
    private DealerDataBean dealerData;
    private List<SelectDataBean> selectData;
    private List<PercentDataBean> percentData;

    public boolean isDealer() {
        return TextUtils.equals(isDealer, "1");
    }

    @Nullable
    public DealerDataBean getDealerData() {
        return dealerData;
    }

    @NonNull
    public List<SelectDataBean> getSelectData() {
        return selectData == null ? new ArrayList<>() : selectData;
    }

    @NonNull
    public List<PercentDataBean> getPercentData() {
        return percentData == null ? new ArrayList<>() : percentData;
    }

    public static class DealerDataBean implements Serializable {
        public String totalComplete;
        public String dealerTime;
        List<DealerListBean> dealerList;

        @NonNull
        public List<DealerListBean> getDealerList() {
            return dealerList == null ? new ArrayList<>() : dealerList;
        }
    }

    public static class DealerListBean implements Serializable {
        public String month; //时间
        public String achievement; //业绩

        public DealerListBean(String month, String achievement) {
            this.month = month;
            this.achievement = achievement;
        }
    }


    public static class SelectDataBean implements Serializable {
        public String name;
        public String selectTime;
    }

    public static class PercentDataBean implements Serializable {
        public String area;
        public String zhongxin;
        public String old;
        public String cityCode;
        public String percentComplete;
        public String type;
        public String isClick;
        public String countsComplete;
        public String name;
        public String countsTarget;
        public String rank;
        public String isChange;
        public String countsTodayComplete;

        public boolean isClick() {
            return TextUtils.equals(isClick, "1");
        }
    }
}
