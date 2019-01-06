package com.monjya.android.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by xmx on 2016/12/2.
 */

public class DateUtils {

    public static String beautifyDate(Date date) {
        return beautifyDate(date, Format.YearMonthDay);
    }

    public static String beautifyDate(Date date, Format format) {
        if (date == null) {
            return "";
        }

        Calendar calendar = Calendar.getInstance();
        Date now = calendar.getTime();
        long deltaS = (now.getTime() - date.getTime()) / 1000;
        if (deltaS < 60) {
            return "刚刚";
        }

        if (deltaS < 3600) {
            return (deltaS / 60) + "分钟前";
        }

        if (deltaS < 3600 * 24) {
            return (deltaS / 3600) + "小时前";
        }

        SimpleDateFormat sdf = null;
        int thisYear = calendar.get(Calendar.YEAR);
        calendar.setTime(date);
        if (format == Format.YearMonthDayHourMinuteSecond) {
            if (thisYear == calendar.get(Calendar.YEAR)) {
                sdf = new SimpleDateFormat("MM-dd HH:mm:ss");
            } else {
                sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            }
        } else if (format == Format.YearMonthDay) {
            if (thisYear == calendar.get(Calendar.YEAR)) {
                sdf = new SimpleDateFormat("MM-dd");
            } else {
                sdf = new SimpleDateFormat("yyyy-MM-dd");
            }
        }

        if (sdf != null) {
            return sdf.format(date);
        }

        return "";
    }
    
    public enum Format {
        YearMonthDayHourMinuteSecond, YearMonthDay
    }
}
