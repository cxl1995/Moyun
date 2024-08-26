package com.mywl.app.platform.utils;

import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * 时间处理工具类
 */
public class DateUtil {
    /**
     * 时间戳转时间
     *
     * @param timestamp
     * @return
     */
    public static LocalDateTime timestampToDate(String timestamp) {
        if (timestamp == null) {
            return null;
        }
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(Long.valueOf(timestamp)), ZoneId.systemDefault());
    }

    /**
     * 时间戳转换成日期格式字符串
     *
     * @param seconds 精确到毫秒的字符串
     * @param format
     * @return
     */
    public static String timeStamp2Date(String seconds, String format) {
        if (seconds == null || seconds.isEmpty() || seconds.equals("null")) {
            return "";
        }
        if (format == null || format.isEmpty()) {
            format = "yyyy-MM-dd HH:mm:ss";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date(Long.valueOf(seconds)));
    }

    /**
     * 时间转时间戳
     *
     * @param localDateTime
     * @return
     */
    public static Long dateToTimestamp(LocalDateTime localDateTime) {
        if (localDateTime == null) {
            return null;
        }
        return localDateTime.toInstant(ZoneOffset.of("+8")).toEpochMilli();
    }

    /**
     * 时间转时间戳
     *
     * @param localDateTime
     * @return
     */
    public static String dateToTimestampByString(LocalDateTime localDateTime) {
        Long dateTime = dateToTimestamp(localDateTime);
        if (dateTime == null) {
            return null;
        }
        return dateTime.toString();
    }

    /**
     * 时间格式化
     *
     * @param localDateTime
     * @return
     */
    public static String dateToStringFormatByString(LocalDateTime localDateTime) {
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return df.format(localDateTime);
    }

    /**
     * 时间格式化
     *
     * @param localDateTime
     * @return
     */
    public static String dateToTimestampStringFormat(LocalDateTime localDateTime) {
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return df.format(localDateTime);
    }

    /**
     * 时间戳转时间字符串,格式是 年-月-日 时:分:秒
     *
     * @param timestamp
     * @return
     */
    public static String timestampToString(String timestamp) {
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return df.format(timestampToDate(timestamp));
    }

    /**
     * 时间戳转时间字符串
     *
     * @param timestamp * @param timestamp
     * @return
     */
    public static String timestampToStringAbbreviations(String timestamp, String dateFormat) {
        DateTimeFormatter df = DateTimeFormatter.ofPattern(dateFormat);
        return df.format(timestampToDate(timestamp));
    }

    /**
     * 取日期的时间部分
     *
     * @param localDateTime
     * @return 1970年1月1号的时间戳
     */
    public static Long getTimeByDate(LocalDateTime localDateTime) {
        Duration duration = Duration.between(localDateTime, LocalDateTime.of(1970, 1, 1, 0, 0));
        return duration.getSeconds();
    }

    /**
     * 取当前时间的日期部分
     *
     * @param localDateTime
     * @return
     */
    public static String getLocalDateStartToString(LocalDateTime localDateTime) {
        return dateToTimestampByString(localDateTime.toLocalDate().atTime(0, 0, 0));
    }

    /**
     * 取当前时间的日期部分，加上23点59分59秒
     *
     * @param localDateTime
     * @return
     */
    public static String getLocalDateEndToString(LocalDateTime localDateTime) {
        return dateToTimestampByString(localDateTime.toLocalDate().atTime(23, 59, 59));
    }

    /**
     * 今天加上指定天数
     *
     * @param dayInterval
     * @return
     */
    public static LocalDateTime addDay(long dayInterval) {
        return addDay(LocalDateTime.now(), dayInterval);
    }

    /**
     * 指定日期加上指定天数
     *
     * @param localDateTime
     * @param dayInterval   单位是天数
     * @return
     */
    public static LocalDateTime addDay(LocalDateTime localDateTime, long dayInterval) {
        if (localDateTime == null) {
            return null;
        }
        return localDateTime.plusDays(dayInterval);
    }
}
