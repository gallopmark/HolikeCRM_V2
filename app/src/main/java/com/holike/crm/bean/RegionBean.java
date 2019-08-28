package com.holike.crm.bean;

import android.text.TextUtils;

/**
 * Created by gallop on 2019/8/2.
 * Copyright holike possess 2019.
 */
public class RegionBean {
    public String regionCode;
    public String parentRegionCode;
    public String regionLevel;
    public String regionName;

    public RegionBean(String regionCode) {
        this.regionCode = regionCode;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (obj instanceof RegionBean) {
            return TextUtils.equals(regionCode, ((RegionBean) obj).regionCode);
        } else {
            return false;
        }
    }
}
