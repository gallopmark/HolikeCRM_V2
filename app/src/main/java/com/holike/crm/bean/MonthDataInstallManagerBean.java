package com.holike.crm.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gallop on 2019/8/9.
 * Copyright holike possess 2019.
 */
public class MonthDataInstallManagerBean {
    public String timeData;
    List<ArrBean> arr;

    public List<ArrBean> getArr() {
        return arr == null ? new ArrayList<>() : arr;
    }

    public static class ArrBean {
        public String area;
        public String installed;
        public String Satisfied;
        public String name;
        public String firstSuccess;
    }
}
