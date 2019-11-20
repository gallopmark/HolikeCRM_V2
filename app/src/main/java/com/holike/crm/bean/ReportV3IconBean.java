package com.holike.crm.bean;

import com.holike.crm.util.ParseUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pony on 2019/10/31.
 * Version v3.0 app报表
 */
public class ReportV3IconBean {

    public String imageUrl;
    public String title;
    public String type;

    @SuppressWarnings("WeakerAccess")
    List<IconBean> itemMap;
    @SuppressWarnings("WeakerAccess")
    List<ArrIconBigBean> arrIconBig;

    public int getType() {
        return ParseUtils.parseInt(type);
    }

    public List<IconBean> getItemMap() {
        return itemMap == null ? new ArrayList<>() : itemMap;
    }

    public List<ArrIconBigBean> getArrIconBig() {
        return arrIconBig == null ? new ArrayList<>() : arrIconBig;
    }

    public static class IconBean {
        public String imageUrl;
        public String title;
        public String type;

        public int getType() {
            return ParseUtils.parseInt(type);
        }
    }

    public static class ArrIconBigBean {
        public String title;

        List<IconBean> arrIcon;

        public List<IconBean> getArrIcon() {
            return arrIcon == null ? new ArrayList<>() : arrIcon;
        }
    }
}
