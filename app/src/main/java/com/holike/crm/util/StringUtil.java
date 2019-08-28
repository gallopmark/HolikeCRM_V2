package com.holike.crm.util;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by wqj on 2017/11/1.
 * string 帮助类
 */

public class StringUtil {

    /**
     * List<String>转换成List<Integer>
     *
     * @param stringList
     * @return
     */
    public static List<Integer> strListToIntList(List<String> stringList) {
        List<Integer> integerList = null;
        if (stringList != null && stringList.size() > 0) {
            integerList = new ArrayList<>();
            for (String str : stringList) {
                integerList.add(Integer.valueOf(str));
            }
        }
        return integerList;
    }


    /**
     * 将map转换成ABCD选项
     *
     * @param optionsMap
     * @param list
     * @return
     */
    public static String mapToString(Map<String, String> optionsMap, List<String> list) {
        StringBuffer options = new StringBuffer();
        for (String key : list) {
            if (optionsMap.get(key) != null) {
                options.append(optionsMap.get(key));
            }
        }
        return options.toString();
    }

    /**
     * 将数字转中文
     *
     * @param number
     * @return
     */
    public static String numToStr(int number) {
        //数字对应的汉字
        String[] num = {"零", "一", "二", "三", "四", "五", "六", "七", "八", "九"};
        //单位
        String[] unit = {"", "十", "百", "千", "万", "十", "百", "千", "亿", "十", "百", "千", "万亿"};
        //将输入数字转换为字符串
        String result = String.valueOf(number);
        //将该字符串分割为数组存放
        char[] ch = result.toCharArray();
        //结果 字符串
        StringBuilder str = new StringBuilder();
        int length = ch.length;
        for (int i = 0; i < length; i++) {
            int c = (int) ch[i] - 48;
            if (c != 0) {
                str.append(num[c]).append(unit[length - i - 1]);
            } else {
                str.append(num[c]);
            }
        }
        return str.toString();
    }

    /**
     * 将字符串分割成int数组
     *
     * @param str
     * @return
     */
    public static int[] getIntFromString(String str, String regex) {
        String[] strs = str.split(regex);
        int[] ints = new int[strs.length];
        for (int i = 0, length = strs.length; i < length; i++) {
            ints[i] = Integer.parseInt(strs[i]);
        }
        return ints;
    }

    /**
     * 获取url地址文件的后缀名
     *
     * @param url
     * @return
     */
    public static String getFileType(String url) {
        if (!TextUtils.isEmpty(url)) {
            String[] names = url.split("\\.");
            if (names.length > 0) {
                return "." + names[names.length - 1];
            } else {
                return "";
            }
        } else return "";
    }

    /**
     * 将秒转换成时长
     *
     * @param timeMs
     * @return
     */
    public static String stringForTime(int timeMs) {
        if (timeMs <= 0 || timeMs >= 24 * 60 * 60 * 1000) {
            return "00:00";
        }
        int totalSeconds = timeMs / 1000;
        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;
        StringBuilder stringBuilder = new StringBuilder();
        Formatter mFormatter = new Formatter(stringBuilder, Locale.getDefault());
        if (hours > 0) {
            return mFormatter.format("%d:%02d:%02d", hours, minutes, seconds).toString();
        } else {
            return mFormatter.format("%02d:%02d", minutes, seconds).toString();
        }
    }
}
