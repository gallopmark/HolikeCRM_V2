package com.holike.crm.bean;

import android.text.TextUtils;

import androidx.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/*员工排行榜*/
public class EmployeeRankingBean {
    @SuppressWarnings("WeakerAccess")
    MyRankingBean myRank;
    public String startTime;
    public String endTime;
    public String title;
    @SuppressWarnings("WeakerAccess")
    String isDealer; //是否是经销商
    @SuppressWarnings("WeakerAccess")
    List<RankingDataBean> rankData;

    public boolean isDealer() {
        return TextUtils.equals(isDealer, "1");
    }

    @Nullable
    public MyRankingBean getMyRank() {
        return myRank;
    }

    public List<RankingDataBean> getRankData() {
        return rankData == null ? new ArrayList<>() : rankData;
    }

    public static class MyRankingBean {
        public String myselfCounts; //我的签单数
        public String myselfSum; //我的成交金额
        public String myselfRank; //我的排名
        public String myselfReceiver; //我的回款金额
        public String myselfOrder; //我的下单金额
    }

    public static class RankingDataBean {
        @SerializedName("user_name")
        public String userName; //名称
        public String Counts; //签单数
        @SerializedName("dealer_name")
        public String dealerName; //经销商
        public String rank; //排名
        @SerializedName("shop_name")
        public String shopName; //门店名称
        public String Sum; //成交金额
        public String Receiver; //回款金额
        public String Order; //下单金额
    }
}
