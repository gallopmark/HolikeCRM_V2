package com.holike.crm.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import androidx.core.content.pm.PackageInfoCompat;

import com.holike.crm.base.MyApplication;


/**
 * Created by wqj on 2017/10/12.
 */

public class PackageUtil {

    /**
     * 获取本应用版本名
     */
    public static String getVersionName() {
        return getPackageInfo(MyApplication.getInstance()).versionName;
    }

    /**
     * 获取本应用版本号
     */
    public static long getVersionCode() {
        return PackageInfoCompat.getLongVersionCode(getPackageInfo(MyApplication.getInstance()));
    }

    /**
     * 获取应用信息
     *
     * @param context
     * @param apkPath 安装包路劲
     * @return
     */
    private static PackageInfo getPackageInfo(Context context, String apkPath) {
        PackageInfo pi = null;
        try {
            PackageManager pm = context.getPackageManager();
            if (apkPath == null) {
                pi = pm.getPackageInfo(context.getPackageName(), PackageManager.GET_CONFIGURATIONS);
            } else {
                pi = pm.getPackageArchiveInfo(apkPath, PackageManager.GET_ACTIVITIES);
            }
            return pi;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pi;
    }

    private static PackageInfo getPackageInfo(Context context) {
        return getPackageInfo(context, null);
    }

    /**
     * 获取安装包版本号
     *
     * @param apkPath 安装包路径
     * @return
     */
    public static int getVersionCode(String apkPath) {
        int versionCode = 0;
        if (IOUtil.isExists(apkPath)) {
            try {
                versionCode = getPackageInfo(MyApplication.getInstance(), apkPath).versionCode;
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }
        }
        return versionCode;
    }

    /**
     * 获取手机型号
     */
    public static String getPhoneModel() {
        return android.os.Build.MODEL;
    }

    /**
     * 获取手机系统版本
     */
    public static String getSystemVersion() {
        return android.os.Build.VERSION.RELEASE;
    }

    /**
     * 获取渠道号
     *
     * @return
     */
    public static String getChannel() {
        MyApplication myApplication = MyApplication.getInstance();
        return getMetaData(myApplication, "UMENG_CHANNEL");
    }

    public static String getMetaData(Context context, String metaData) {
        if (context == null) {
            return null;
        }
        String Name = "";
        try {
            PackageManager packageManager = context.getPackageManager();
            if (packageManager != null) {
                //注意此处为ApplicationInfo 而不是 ActivityInfo,因为友盟设置的meta-data是在application标签中，而不是某activity标签中，所以用ApplicationInfo
                ApplicationInfo applicationInfo = packageManager.getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
                if (applicationInfo != null) {
                    if (applicationInfo.metaData != null) {
                        Name = applicationInfo.metaData.getString(metaData);
                    }
                }
            }
        } catch (Exception e) {
        }
        if (Name == null) {
            return "";
        }
        return Name;
    }
}
