package com.chenframework.common.utils;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 日期时间操作类
 */
public final class DateUtil extends DateUtils {

    /**
     * yyyy年MM月dd日
     */
    public final static SimpleDateFormat DATEFORMAT_CHINA = new SimpleDateFormat("yyyy年MM月dd日");

    public final static String PATTERN_DATE = "yyyy-MM-dd";
    public final static String PATTERN_DATETIME = "yyyy-MM-dd HH:mm:ss";

    private final static String[] WEEK_CHINA = { "星期一", "星期二", "星期三", "星期四", "星期五", "星期六", "星期日" };

    private DateUtil() {

    }

    public static String formatDate() {
        return formatDate(new Date());
    }

    public static String formatDate(Date date) {
        return formatDatetime(date, PATTERN_DATE);
    }

    public static String formatDatetime() {
        return formatDatetime(new Date());
    }

    public static String formatDatetime(Date date) {
        return formatDatetime(date, PATTERN_DATETIME);
    }

    public static String formatDatetime(Date date, String pattern) {
        return new SimpleDateFormat(pattern).format(date);
    }

    public static String formatDatetime(long mils) {
        return new SimpleDateFormat(PATTERN_DATETIME).format(new Date(mils));
    }

    public static int getDayOfMonth() {
        return LocalDate.now().getDayOfMonth();
    }

    public static String getDayOfMonthStr() {
        int a = getDayOfMonth();
        return (a < 10 ? "0" : "") + a;
    }

    /**
     * 格式为ISO8601字符串
     */
    public static String getISO8601(Date date) {
        String pattern = "yyyy-MM-dd'T'HH:mm:ss:SSSZZ";
        return DateFormatUtils.format(date, pattern);
    }

    public static int getMonth() {
        return LocalDate.now().getMonthValue();
    }

    public static Date getMonthFirstDay() {
        LocalDateTime first = LocalDateTime.of(getYear(), getMonth(), 1, 0, 0, 0);
        Instant instant = first.atZone(ZoneId.systemDefault()).toInstant();
        return Date.from(instant);
    }

    public static Date getMonthLastDay() {
        Calendar ca = Calendar.getInstance();
        ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH));
        return ca.getTime();
    }

    public static String getMonthStr() {
        int a = getMonth();
        return (a < 10 ? "0" : "") + a;
    }

    public static int getSeason() {
        int month = getMonth();
        if (month <= 3) {
            return 1;
        }
        if (month <= 6) {
            return 2;
        }
        if (month <= 9) {
            return 3;
        }
        return 4;
    }

    public static int getWeek() {
        return getWeek(new Date());
    }

    public static int getWeek(Date date) {
        LocalDateTime localDateTime = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
        LocalDate localDate = localDateTime.toLocalDate();
        return localDate.getDayOfWeek().getValue();
    }

    public static Date getWeekFirstDay() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        return cal.getTime();
    }

    public static String getWeekStr() {
        return WEEK_CHINA[getWeek() - 1];
    }

    public static int getYear() {
        return LocalDate.now().getYear();
    }

    public static List<Date> getBetweenDates(Date startDate, Date endDate) {
        List<Date> dates = new ArrayList<>();
        for (long longs = startDate.getTime(); longs <= endDate.getTime(); longs += MILLIS_PER_DAY) {
            dates.add(new Date(longs));
        }
        return dates;
    }

    public static String nowDate() {
        return LocalDate.now().format(DateTimeFormatter.ofPattern(PATTERN_DATE));
    }

    public static String nowDateTime() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern(PATTERN_DATE));
    }

    public static Date parseDate(String date) {
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return f.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Date parseDatetime(String datetime) {
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            return f.parse(datetime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Date toDate(LocalDateTime localDateTime) {
        Instant instant = localDateTime.atZone(ZoneId.systemDefault()).toInstant();
        return Date.from(instant);
    }

    public static LocalDateTime toLocalDateTime(Date date) {
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }

    public static Date plusDays(Date date, long days) {
        if (date == null) {
            return null;
        }
        LocalDateTime localDateTime = toLocalDateTime(date);
        return toDate(localDateTime.plusDays(days));
    }

    public static Date minusDays(Date date, long days) {
        if (date == null) {
            return null;
        }
        LocalDateTime localDateTime = toLocalDateTime(date);
        return toDate(localDateTime.minusDays(days));

    }
}
