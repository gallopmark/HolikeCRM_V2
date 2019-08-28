package com.holike.crm.util;

import android.text.TextUtils;

public class CheckUtils {
    public static boolean isMobile(String phone) {
        if (TextUtils.isEmpty(phone)) return false;
        String regex = "[1]\\d{10}";
        return phone.matches(regex);
    }

    /**
     * 判断是否是快速点击
     */
    private static long lastClickTime;

    public static boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (0 < timeD && timeD < 500) {
            return true;
        }
        lastClickTime = time;
        return false;
    }
}
