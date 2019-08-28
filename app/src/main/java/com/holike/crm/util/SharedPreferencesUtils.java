package com.holike.crm.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.holike.crm.base.MyApplication;
import com.holike.crm.bean.LoginBean;

/**
 * create by wqj on 2018-10-30 09:14:42
 * <p>
 * sp工具类
 */
public class SharedPreferencesUtils {
    /*本地数据保存的文件名*/
//    public static final String SP_NAME = "holikePreference";
    private static final String SP_NAME = "localSourceV2"; //v2.0以后版本 更改文件名 强制用户重新登录
    private static final String STAFF_ID = "staffId";
    private static final String DEALER_ID = "dealerId";//经销商id

    public static void saveInt(String key, int value) {
        SharedPreferences sp = MyApplication.getInstance().getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        if (sp != null) {
            Editor editor = sp.edit();
            editor.putInt(key, value);
            editor.apply();
        }
    }

    public static void saveString(String key, String value) {
        SharedPreferences sp = MyApplication.getInstance().getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        if (sp != null) {
            Editor editor = sp.edit();
            editor.putString(key, value);
            editor.apply();
        }
    }

    public static void saveLong(String key, Long value) {
        SharedPreferences sp = MyApplication.getInstance().getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        if (sp != null) {
            Editor editor = sp.edit();
            editor.putLong(key, value);
            editor.apply();
        }
    }

    public static void saveBoolean(String key, Boolean value) {
        SharedPreferences sp = MyApplication.getInstance().getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        if (sp != null) {
            Editor editor = sp.edit();
            editor.putBoolean(key, value);
            editor.apply();
        }
    }

    public static Boolean getBoolean(String key, Boolean defaultValue) {
        Boolean result = defaultValue;
        SharedPreferences sp = MyApplication.getInstance().getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        if (sp != null) {
            result = sp.getBoolean(key, defaultValue);
        }
        return result;
    }

    public static SharedPreferences get() {
        return MyApplication.getInstance().getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
    }

    public static String getString(String key, String defaultValue) {
        return get().getString(key, defaultValue);
    }

    public static int getInt(String key, int defaultValue) {
        int result = defaultValue;
        SharedPreferences sp = MyApplication.getInstance().getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        if (sp != null) {
            result = sp.getInt(key, defaultValue);
        }
        return result;
    }

    public static long getLong(String key, long defaultValue) {
        long result = defaultValue;
        SharedPreferences sp = MyApplication.getInstance().getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        if (sp != null) {
            result = sp.getLong(key, defaultValue);
        }
        return result;
    }

    public static long getLong(String key) {
        return getLong(key, 0);
    }

    public static String getString(String key) {
        return getString(key, "");
    }

    public static void saveUserInfo(LoginBean bean) {
        saveString(Constants.CLI_ID, bean.cliId);
        saveString(Constants.USER_ID, bean.userId);
        saveString(Constants.USER_PSW, bean.psw);
        saveString(Constants.COOKIE, bean.Cookie);
        saveString(Constants.COOKIE2, bean.Cookie2);
        saveString(STAFF_ID, bean.staffId);
        saveString(DEALER_ID, bean.dealerId);
    }

    /*登录成功的cliId*/
    public static String getCliId() {
        return getString(Constants.CLI_ID);
    }

    /*保存经销商id*/
    public static void saveDealerId(String dealerId) {
        saveString(DEALER_ID, dealerId);
    }

    /*获取登录后返回的dealerId*/
    public static String getDealerId() {
        return getString(DEALER_ID);
    }

    /*当前登录的userId*/
    public static String getUserId() {
        return getString(Constants.USER_ID);
    }

    /*当前登录的password*/
    public static String getPassword() {
        return getString(Constants.USER_PSW);
    }

    /*登录之后的cookie*/
    public static String getCookie() {
        return getString(Constants.COOKIE);
    }

    /*登录之后的cookie2*/
    public static String getCookie2() {
        return getString(Constants.COOKIE2);
    }

    public static int getInt(String key) {
        return getInt(key, 0);
    }

    public static boolean getBoolean(String key) {
        return getBoolean(key, false);
    }

    /*退出登录时调用*/
    public static void clear() {
        get().edit().clear().apply();
    }
}
