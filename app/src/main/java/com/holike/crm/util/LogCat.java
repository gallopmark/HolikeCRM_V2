package com.holike.crm.util;

import android.text.TextUtils;
import android.util.Log;

import com.holike.crm.BuildConfig;

import java.io.PrintWriter;
import java.io.StringWriter;


/**
 * Logcat日志工具类
 * Created by wqj on 2017/9/20.
 */

public class LogCat {
    //    private static boolean isShow = BuildConfig.LOG_DEBUG;
    private static String TAG = BuildConfig.VERSION_NAME;
    private final static int LOG_MAX_LENGTH = 2000;

    public static boolean isShow() {
        return BuildConfig.LOG_DEBUG;
    }

    public static void i(String msg) {
        if (isShow()) {
            LogCat.print("i", TAG, msg);
        }
    }

    public static void i(String TAG, String msg) {
        if (isShow()) {
            LogCat.print("i", TAG, msg);
        }
    }

    public static void e(String msg) {
        if (isShow()) {
            Log.e(TAG, msg);
        }
    }

    public static void e(Throwable e) {
        if (isShow()) {
            LogCat.e(TAG, e);
        }
    }


    public static void d(String msg) {
        if (isShow()) {
            LogCat.print("d", TAG, msg + "");
        }
    }

    public static void d(String TAG, String msg) {
        if (isShow()) {
            LogCat.print("d", TAG, msg);
        }
    }


    public static void v(String msg) {
        if (isShow()) {
            LogCat.print("v", TAG, msg);
        }
    }

    public static void v(String TAG, String msg) {
        if (isShow()) {
            LogCat.print("v", TAG, msg);
        }
    }

    public static void w(String msg) {
        if (isShow()) {
            LogCat.print("w", TAG, msg);
        }
    }

    public static void w(String TAG, String msg) {
        if (isShow()) {
            LogCat.print("w", TAG, msg);
        }
    }

    /*
    打印请求
     */
    public static void v_request(String msg) {
        if (isShow()) {
            LogCat.print("v", "Request:", msg);
        }
    }

    /*
    打印响应结果
     */
    public static void i_response(String msg) {
        if (isShow()) {
            LogCat.print("i", "Response:", msg);
        }
    }


    public static void e(String tag, Throwable e) {
        StringWriter sw = new StringWriter();
        e.printStackTrace(new PrintWriter(sw, true));
        LogCat.print("e", tag, sw.toString());
    }

    public static void e(String tag, String msg) {
        if (isShow()) {
            LogCat.print("e", tag, msg);
        }
    }


    /*
    日志长度超过限制时，采取分段形式打印日志
     */
    public static void print(String logType, String TAG, String msg) {
        if (!isShow() || TextUtils.isEmpty(msg)) return;
        int strLength = msg.length();
        int start = 0;
        int end = LOG_MAX_LENGTH;
        for (int i = 1; i < 100; i++) {
            if (strLength > end) {
                switch (logType) {
                    case "v":
                        Log.v(TAG + i, msg.substring(start, end));
                        break;
                    case "i":
                        Log.i(TAG + i, msg.substring(start, end));
                        break;
                    case "d":
                        Log.d(TAG + i, msg.substring(start, end));
                        break;
                    case "e":
                        Log.e(TAG + i, msg.substring(start, end));
                        break;
                    case "w":
                        Log.w(TAG + i, msg.substring(start, end));
                        break;

                }
                start = end;
                end += LOG_MAX_LENGTH;
            } else {
                switch (logType) {
                    case "v":
                        Log.v(TAG, msg.substring(start, strLength));
                        break;
                    case "i":
                        Log.i(TAG, msg.substring(start, strLength));
                        break;
                    case "d":
                        Log.d(TAG, msg.substring(start, strLength));
                        break;
                    case "e":
                        Log.e(TAG, msg.substring(start, strLength));
                        break;
                    case "w":
                        Log.w(TAG, msg.substring(start, strLength));
                        break;
                }
                break;
            }
        }
    }

}
