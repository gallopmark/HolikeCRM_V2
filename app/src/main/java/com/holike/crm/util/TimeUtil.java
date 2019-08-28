package com.holike.crm.util;

import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by wqj on 2017/10/9.
 */

public class TimeUtil {
    /**
     * timestamp转string
     *
     * @param stamp
     * @param type"yyyy-MM-dd"
     * @return
     */
    public static String stampToString(String stamp, String type) {
        try {
            //时间戳转字符串要13位
            switch (stamp.length()) {
                case 10:
                    stamp = stamp + "000";
                    break;
                case 11:
                    stamp = stamp + "00";
                    break;
                case 12:
                    stamp = stamp + "0";
                    break;
            }
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(type);
            Date date = new Date(Long.parseLong(stamp));
            return simpleDateFormat.format(date);
        } catch (Exception e) {
            return "无法显示时间";
        }

    }


    public static String transDateFormat(String time) {
        return time.replace("-", ".");
    }

    public static String transDateFormatCommit(String time) {
        return time.replace(".", "-");
    }

    /**
     * string转timeStamp
     *
     * @param s
     * @param type "yyyy-MM-dd"
     * @return
     */
    public static String stringToStamp(String s, String type) {
        Date date = stringToDate(s, type);
        if (date == null) {
            return "";
        }
        long stamp = date.getTime();
        return String.valueOf(stamp > 10000000000L ? stamp / 1000 : stamp);//字符串转时间戳要10位

    }

    /**
     * string转date
     *
     * @param str
     * @param type
     * @return
     */
    public static Date stringToDate(String str, String type) {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(type, Locale.getDefault());
            return simpleDateFormat.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * date转string
     *
     * @param date
     * @param type
     * @return
     */
    public static String dateToString(Date date, String type) {
        SimpleDateFormat format = new SimpleDateFormat(type, Locale.getDefault());
        return format.format(date);
    }

    /**
     * stamp转date
     *
     * @param stamp
     * @return
     */
    public static Date stampToData(String stamp, String type) {
        return stringToDate(stampToString(stamp, type), type);
    }

    /**
     * date转stamp
     *
     * @param date
     * @param type
     * @return
     */
    public static String dataToStamp(Date date, String type) {
        return stringToStamp(dateToString(date, type), type);
    }

    /**
     * String 转化Calendar
     *
     * @param str
     * @param type "yyyy-MM-dd"
     * @return
     */
    public static Calendar stringToCalendar(String str, String type) {
        SimpleDateFormat sdf = new SimpleDateFormat(type);
        Date date = null;
        try {
            date = sdf.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }

    /**
     * stamp 转化Calendar
     *
     * @param stamp
     * @param type
     * @return
     */
    public static Calendar stampToCalendar(String stamp, String type) {
        return stringToCalendar(stampToString(stamp, type), type);
    }

    /**
     * "yyyy-MM-dd" 转换 "yyyy-MM-dd hh:MM:mm"
     *
     * @param time
     * @param type
     * @param toType
     * @return
     */
    public static String stringToString(String time, String type, String toType) {
        return stampToString(stringToStamp(time, type), toType);
    }

    public static String dateToStamp(Date date, boolean isEndDate) {
        long time;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        if (isEndDate) {
            calendar.set(Calendar.HOUR, 23);
            calendar.set(Calendar.MINUTE, 59);
            calendar.set(Calendar.SECOND, 59);
        } else {
            calendar.set(Calendar.HOUR, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
        }
        time = calendar.getTimeInMillis();
        return String.valueOf(time > 10000000000L ? time / 1000 : time);
    }

    public static String timeMillsFormat(String timeMills) {
        return timeMillsFormat(timeMills, "yyyy.MM.dd");
    }

    public static String timeMillsFormat(String timeMills, String pattern) {
        if (TextUtils.isEmpty(timeMills)) return "";
        try {
            long time = Long.parseLong(timeMills);
            return timeMillsFormat(time, pattern);
        } catch (Exception e) {
            return "";
        }
    }

    public static String timeMillsFormat(long time) {
        return timeMillsFormat(time, "yyyy.MM.dd");
    }

    public static String timeMillsFormat(long time, String pattern) {
        Date date = new Date(time);
        SimpleDateFormat format = new SimpleDateFormat(pattern, Locale.getDefault());
        return format.format(date);
    }

    public static String timeMillsFormat(Date date) {
        return timeMillsFormat(date, "yyyy.MM.dd");
    }

    public static String timeMillsFormat(Date date, String pattern) {
        SimpleDateFormat format = new SimpleDateFormat(pattern, Locale.getDefault());
        return format.format(date);
    }

    /**
     * @param source     时间字符串
     * @param patternIn  以此格式得到date
     * @param patternOut 以此格式输出转换之后的字符串
     */
    public static String parse(String source, String patternIn, String patternOut) {
        if (TextUtils.isEmpty(source)) return "";
        try {
            SimpleDateFormat format = new SimpleDateFormat(patternIn, Locale.getDefault());
            Date date = format.parse(source);
            return TimeUtil.timeMillsFormat(date, patternOut);
        } catch (Exception e) {
            return source;
        }
    }

    /*秒数 转时分秒输出*/
    public static String getTime(int second) {
        if (second < 10) {
            return "00:00:0" + second;
        }
        if (second < 60) {
            return "00:00:" + second;
        }
        if (second < 3600) {
            int minute = second / 60;
            second = second - minute * 60;
            if (minute < 10) {
                if (second < 10) {
                    return "00:" + "0" + minute + ":0" + second;
                }
                return "00:" + "0" + minute + ":" + second;
            }
            if (second < 10) {
                return "00:" + minute + ":0" + second;
            }
            return "00:" + minute + ":" + second;
        }
        int hour = second / 3600;
        int minute = (second - hour * 3600) / 60;
        second = second - hour * 3600 - minute * 60;
        if (hour < 10) {
            if (minute < 10) {
                if (second < 10) {
                    return "0" + hour + ":0" + minute + ":0" + second;
                }
                return "0" + hour + ":0" + minute + ":" + second;
            }
            if (second < 10) {
                return "0" + hour + minute + ":0" + second;
            }
            return "0" + hour + minute + ":" + second;
        }
        if (minute < 10) {
            if (second < 10) {
                return hour + ":0" + minute + ":0" + second;
            }
            return hour + ":0" + minute + ":" + second;
        }
        if (second < 10) {
            return hour + minute + ":0" + second;
        }
        return hour + minute + ":" + second;
    }
}
