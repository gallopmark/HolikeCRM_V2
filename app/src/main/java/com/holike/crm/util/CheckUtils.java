package com.holike.crm.util;

import android.text.TextUtils;

public class CheckUtils {
    public static boolean isMobile(String phone) {
        if (TextUtils.isEmpty(phone)) return false;
        String regex = "[1]\\d{10}";
        return phone.matches(regex);
    }

    /*是否是微信号*/
    public static boolean isWxNumber(String source) {
        if (TextUtils.isEmpty(source)) return false;
        String regex = "^[a-zA-Z][-_a-zA-Z0-9]{5,19}+$";
        return source.matches(regex);
    }

    /*正则表达式：要求6位以上，只能有大小写字母和数字，并且大小写字母和数字都要有。*/
    public static boolean isPassword(String source) {
        if (TextUtils.isEmpty(source)) return false;
//        String regex = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,500}$";
        String regex = "^(?=.*[0-9].*)(?=.*[A-Z].*)(?=.*[a-z].*).{6,500}$";
        return source.matches(regex);
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
