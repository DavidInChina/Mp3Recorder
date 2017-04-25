/*
 * Copyright (c) 2015.
 * All Rights Reserved.
 */

package com.czt.mp3recorder.util;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期处理工具类
 * Created by macchen on 15/3/26.
 */
public class TimeUtils {
    /**
     * 默认的时间格式
     */
    public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    /**
     * 默认的日期格式
     */
    public static final String DATE_FORMAT_DATE = "yyyy-MM-dd";
    /**
     * 默认的日期格式2
     */
    public static final String DATE_FORMAT_DATE2 = "yyyyMMdd";
    /**
     * 默认的日期格式3
     */
    public static final String DATE_FORMAT_DATE3 = "yyyy";

    /**
     * 禁止方法构造
     */
    private TimeUtils() {
        throw new AssertionError();
    }

    /**
     * 按指定格式去格式化毫秒数
     *
     * @param timeInMillis 毫秒数
     * @param format       转换格式
     * @return 返回转换后的字符串
     */
    public static String formatMillisTo(long timeInMillis, String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(new Date(timeInMillis));
    }

    /**
     * 按固定格式去格式化毫秒数
     *
     * @param timeInMillis 毫秒数
     * @return 返回转换后的字符串
     */
    public static String formatMillisToTime(long timeInMillis) {
        return formatMillisTo(timeInMillis, DEFAULT_DATE_FORMAT);
    }

    /**
     * 按固定格式去格式化毫秒数
     *
     * @param timeInMillis 毫秒数
     * @return 返回转换后的字符串
     */
    public static String formatMillisToDate(long timeInMillis) {
        return formatMillisTo(timeInMillis, DATE_FORMAT_DATE);
    }

    /**
     * @return 返回当前毫秒数
     */
    public static long getCurrentMillis() {
        return System.currentTimeMillis();
    }

    /**
     * @return 返回当前毫秒数转换后的字符串
     */
    public static String getCurrentTimeInString() {
        return getCurrentTimeInString(DATE_FORMAT_DATE2);
    }

    /**
     * @return 返回当前毫秒数转换后的字符串
     */
    public static String getCurrentTimeInStringYear() {
        return getCurrentTimeInString(DATE_FORMAT_DATE3);
    }

    /**
     * 把秒数格式化为00:00:00格式
     *
     * @param seconds
     * @return
     */
    public static String formatSeconds(int seconds) {
        return getTwoDecimalsValue(seconds / 3600) + ":"
                + getTwoDecimalsValue(seconds / 60) + ":"
                + getTwoDecimalsValue(seconds % 60);
    }

    private static String getTwoDecimalsValue(int value) {
        if (value >= 0 && value <= 9) {
            return "0" + value;
        } else {
            return value + "";
        }
    }

    /**
     * @return 返回当前毫秒数转换后的字符串
     */
    public static String getCurrentDateInString() {
        return getCurrentTimeInString(DATE_FORMAT_DATE);
    }

    /**
     * 返回当前毫秒数转换后的字符串
     *
     * @param format 转换格式
     * @return
     */
    public static String getCurrentTimeInString(String format) {
        return formatMillisTo(getCurrentMillis(), format);
    }

    public static String getBeforeDayTimeInString(String format) {
        return formatMillisTo(getCurrentMillis() - 24 * 60 * 60 * 1000, format);//获取前一天
    }

    public static String getNextDayTimeInString(String format) {
        return formatMillisTo(getCurrentMillis() + 24 * 60 * 60 * 1000, format);//获取后一天
    }

    public static String getBeforeMonthTimeInString(String format) {
        long month = 30L * 24 * 60 * 60 * 1000;//定义长整形变量
        return formatMillisTo(getCurrentMillis() - month, format);//获取前一个月，30天前
    }

    /**
     * 字符串指定格式转换成Date对象
     *
     * @param strDate 需要转换的字符串对象
     * @param format  转换格式
     * @return
     */
    public static Date stringToDate(String strDate, String format) {
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        ParsePosition pos = new ParsePosition(0);
        Date strToDate = formatter.parse(strDate, pos);
        return strToDate;
    }

    /**
     * 字符串固定格式转换成Date对象
     *
     * @param strDate 需要转换的字符串对象
     * @return
     */
    public static Date stringToDate(String strDate) {
        return stringToDate(strDate, DEFAULT_DATE_FORMAT);
    }

    public static boolean comapreNow(String strDate) {
        if (null == strDate || "".equals(strDate)) {
            return true;
        }
        return getCurrentMillis() >= getMillisFromDate(strDate) + 24 * 60 * 60 * 1000;//如果当前时间大于传入日期后一天,则表示已过期
    }

    public static long getMillisFromDate(String strDate) {
        return stringToDate(strDate, DATE_FORMAT_DATE).getTime();
    }

    public static String secToTime(long time) {
        String timeStr = null;
        int hour = 0;
        int minute = 0;
        int second = 0;
        if (time <= 0)
            return "00:00:00";
        else {
            hour = (int) (time / 3600000);
            minute = (int) (time / 60000 - 60 * hour);
            second = (int) ((time / 1000) % 60);
            timeStr = unitFormat(hour) + ":" + unitFormat(minute) + ":" + unitFormat(second);
        }
        return timeStr;
    }

    public static String unitFormat(int i) {
        String retStr = null;
        if (i >= 0 && i < 10)
            retStr = "0" + Integer.toString(i);
        else
            retStr = "" + i;
        return retStr;
    }

    public static String getWeekDay(String date) {
        String weekDay = "星期";
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(format.parse(date));
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 1) {
            weekDay += "天";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 2) {
            weekDay += "一";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 3) {
            weekDay += "二";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 4) {
            weekDay += "三";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 5) {
            weekDay += "四";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 6) {
            weekDay += "五";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 7) {
            weekDay += "六";
        }
        return weekDay;
    }
}
