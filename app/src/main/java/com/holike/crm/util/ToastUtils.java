package com.holike.crm.util;


import android.view.Gravity;
import android.widget.Toast;

import com.holike.crm.base.MyApplication;


/**
 * Created by wqj on 2017/12/28.
 * Toast（防止重复显示）
 */
@Deprecated
public class ToastUtils {
    /**
     * 之前显示的内容
     */
    private static String oldMsg;
    /**
     * Toast对象
     */
    private static Toast toast = null;
    /**
     * 第一次时间
     */
    private static long oneTime = 0;
    /**
     * 第二次时间
     */
    private static long twoTime = 0;


    public static class ToastGravity {
        public final static int LOW = 0x00;
        public final static int CENTER = 0x01;
        public final static int HIGH = 0x02;
    }

    /**
     * 显示Toast
     *
     * @param message
     */
    public static void showToast(String message) {
        if (toast == null) {
            toast = Toast.makeText(MyApplication.getInstance(), message, Toast.LENGTH_SHORT);
            toast.show();
            oneTime = System.currentTimeMillis();
        } else {
            twoTime = System.currentTimeMillis();
            if (message.equals(oldMsg)) {
                if (twoTime - oneTime > Toast.LENGTH_SHORT) {
                    toast.show();
                }
            } else {
                oldMsg = message;
                toast.setText(message);
                toast.show();
            }
        }
        oneTime = twoTime;

    }


    /**
     * 显示Toast
     *
     * @param gravity toast位置
     * @param message 内容
     */
    public static void showToast(String message, int gravity) {
        if (toast == null) {
            toast = Toast.makeText(MyApplication.getInstance(), message, Toast.LENGTH_SHORT);
            switch (gravity) {
                case ToastGravity.LOW:
                    toast.setGravity(Gravity.BOTTOM, 0, DensityUtil.dp2px(200));
                    break;
                case ToastGravity.HIGH:
                    toast.setGravity(Gravity.TOP, 0, 0);
                    break;
                case ToastGravity.CENTER:
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    break;
            }
            toast.show();
            oneTime = System.currentTimeMillis();
        } else {
            twoTime = System.currentTimeMillis();
            if (message.equals(oldMsg)) {
                if (twoTime - oneTime > Toast.LENGTH_SHORT) {
                    toast.show();
                }
            } else {
                oldMsg = message;
                toast.setText(message);
                toast.show();
            }
        }
        oneTime = twoTime;

    }
}
