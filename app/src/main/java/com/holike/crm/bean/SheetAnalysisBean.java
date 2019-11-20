package com.holike.crm.bean;

import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pony on 2019/11/2.
 * Version v3.0 app报表
 * 板材分析数据
 */
public class SheetAnalysisBean {
    public String dateToDate;
    @SuppressWarnings("WeakerAccess")
    List<DataBean> resultList;

    public List<DataBean> getDatas() {
        return resultList == null ? new ArrayList<>() : resultList;
    }

    public static class DataBean {
        public String division; //划分
        public String principal; //负责人
        public String granule; //颗粒板
        public String total; //
        public String original; //原态板
        @SerializedName("original_k")
        public String originalK; //原态板K
        public String type;
        public String cityCode;
        String isClick;

        public boolean isClickable() {
            return TextUtils.equals(isClick, "1");
        }

        public String getShowOriginal() {
            return getShowText(original);
        }

        public String getShowOriginalK() {
            return getShowText(originalK);
        }

        private String getShowText(String source) {
            if (TextUtils.isEmpty(source)) return "";
            String text = source;
            if (source.contains("(")) {
                int index = source.indexOf("(");
                text = source.substring(0, index) + "\n";
                text += source.substring(index);
            } else if (source.contains("（")) {
                int index = source.indexOf("（");
                text = source.substring(0, index) + "\n";
                text += source.substring(index);
            }
            return text;
        }
    }
}
