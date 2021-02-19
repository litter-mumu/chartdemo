package com.cash.hunterchartdemo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Util {

    /**
     * 获取当前时间
     *
     * @param pattern
     * @return
     */
    public static String getCurDate(String pattern) {
        SimpleDateFormat sDateFormat = new SimpleDateFormat(pattern);
        return sDateFormat.format(new java.util.Date());
    }

    /**
     * 时间戳转换成字符串
     *
     * @param milSecond
     * @return
     */
    public static String getDateToString(long milSecond) {
        Date date = new Date(milSecond);
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        return format.format(date);
    }


    /**
     * 取两个double的平均值，并返回两位数的float
     */
    public static float getAverage(double d1, double d2) {
        return (float) (d1 + d2) / 2;
    }
}
