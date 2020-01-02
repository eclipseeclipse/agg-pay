package com.bwton.agg.common.util;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;

/**
 * @author DengQiongChuan
 * @date 2019-04-22 17:41
 */
public class DateUtils {

    public static final String FINAL_DATE = "29991231235959";
    private static final DateTimeFormatter YYYY_MM_DD_HH_MM_SS = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final DateTimeFormatter YYYYMMDDHHMMSS = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    /**
     * 获取指定时间当月的第一天（零点）
     * @param date
     * @return
     */
    public static Date firstDayOfMonth(Date date) {
        if(date == null) {
            return null;
        }
        LocalDateTime dateTime = dateToLocalDateTime(date);
        LocalDateTime firstDayOfMonth = dateTime.with(TemporalAdjusters.firstDayOfMonth()).with(LocalTime.MIN);
        return localDateTimeToDate(firstDayOfMonth);
    }

    /**
     * 获取指定时间当月的最后一天。23:59:59.999999999
     * @param date
     * @return
     */
    public static Date lastDayOfMonth(Date date) {
        if(date == null){
            return null;
        }
        LocalDateTime dateTime = dateToLocalDateTime(date);
        LocalDateTime lastDayOfMonth = dateTime.with(TemporalAdjusters.lastDayOfMonth()).with(LocalTime.MAX);
        return localDateTimeToDate(lastDayOfMonth);
    }

    /**
     * 获取指定时间下个月的第一天（零点）
     * @param date
     * @return
     */
    public static Date firstDayOfNextMonth(Date date) {
        if(date == null){
            return null;
        }
        LocalDateTime dateTime = dateToLocalDateTime(date);
        LocalDateTime firstDayOfNextMonth = dateTime.with(TemporalAdjusters.firstDayOfNextMonth()).with(LocalTime.MIN);
        return localDateTimeToDate(firstDayOfNextMonth);
    }

    /**
     * 获取指定时间本周的开始时间（零点）。周一为一周的第一天，周日为一周的最后一天。
     * 例如，2019-04-17 12:29:00是星期三，那么该日期的本周开始时间是2019-04-15 00:00:00。
     * @param date 时间
     * @return 本周开始的零点时间。
     */
    public static Date firstDayOfWeek(Date date) {
        if(date == null){
            return null;
        }
        LocalDateTime dateTime = dateToLocalDateTime(date);
        int dayOfWeek = dateTime.getDayOfWeek().getValue();
        LocalDateTime result = dateTime.plusDays(1 - dayOfWeek);
        LocalDateTime firstDayOfWeek = LocalDateTime.of(
                result.getYear(), result.getMonth(), result.getDayOfMonth(), 0, 0, 0);
        return localDateTimeToDate(firstDayOfWeek);
    }

    /**
     * 获取指定时间下周的开始时间（零点）。周一为一周的第一天，周日为一周的最后一天。
     * 例如，2019-04-17 12:29:00是星期三，那么该日期的下周开始时间是2019-04-22 00:00:00。
     * @param date 时间
     * @return 下周开始的零点时间
     */
    public static Date firstDayOfNextWeek(Date date) {
        if(date == null){
            return null;
        }
        LocalDateTime dateTime = dateToLocalDateTime(date);
        int dayOfWeek = dateTime.getDayOfWeek().getValue();
        LocalDateTime result = dateTime.plusDays(1 - dayOfWeek).plusWeeks(1);
        LocalDateTime firstDayOfNextWeek = LocalDateTime.of(
                result.getYear(), result.getMonth(), result.getDayOfMonth(), 0, 0, 0);
        return localDateTimeToDate(firstDayOfNextWeek);
    }

    /**
     * 获取指定时间本周的结束时间（23:59:59）。周一为一周的第一天，周日为一周的最后一天。
     * 例如，2019-04-17 12:29:00是星期三，那么该日期的本周结束时间是2019-04-21 23:59:59.999999999。
     * @param date 时间
     * @return 本周结束的零点时间。
     */
    public static Date lastDayOfWeek(Date date) {
        if(date == null){
            return null;
        }
        LocalDateTime dateTime = dateToLocalDateTime(date);
        int dayOfWeek = dateTime.getDayOfWeek().getValue();
        LocalDateTime result = dateTime.plusDays(7 - dayOfWeek);
        LocalDateTime lastDayOfWeek = LocalDateTime.of(
                LocalDate.of(result.getYear(), result.getMonth(), result.getDayOfMonth()), LocalTime.MAX);
        return localDateTimeToDate(lastDayOfWeek);
    }

    /**
     * 今天开始时间（零点）
     * @return
     */
    public static Date beginOfToday() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime beginOfToday = LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(), 0, 0, 0);
        return localDateTimeToDate(beginOfToday);
    }

    /**
     * 今天结束时间（23:59:59.999999999）
     * @return
     */
    public static Date endOfToday() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime beginOfToday = LocalDateTime.of(
                LocalDate.of(now.getYear(), now.getMonth(), now.getDayOfMonth()), LocalTime.MAX);
        return localDateTimeToDate(beginOfToday);
    }

    /**
     * 明天开始时间（零点）
     * @return
     */
    public static Date beginOfTomorrow() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime beginOfToday = LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(), 0, 0, 0);
        return localDateTimeToDate(beginOfToday.plusDays(1));
    }

    /**
     * 传入的时间是否是 1970-01-01 00:00:00
     * @param date
     * @return true是，false不是
     */
    public static boolean isDefault1970(Date date) {
        if(date == null){
            return false;
        }
        LocalDateTime utc1970 = LocalDateTime.of(1970, 1, 1, 0, 0, 0);
        return utc1970.isEqual(dateToLocalDateTime(date));
    }

    /**
     * 获得 2099-01-01 00:00:00
     * @return
     */
    public static Date year20990101() {
        LocalDateTime year2099 = LocalDateTime.of(2099, 1, 1, 0, 0, 0);
        return localDateTimeToDate(year2099);
    }

    /**
     * Date转换为LocalDateTime
     * @param date 时间
     * @return
     */
    public static LocalDateTime dateToLocalDateTime(Date date) {
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }

    /**
     * LocalDateTime转换为Date
     * @param localDateTime 时间
     * @return
     */
    public static Date localDateTimeToDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * 获得当前时间，精度到秒。当前时间的毫秒数为0。
     * @return
     */
    public static Date nowWithoutNano() {
        LocalDateTime dateTime = LocalDateTime.now().withNano(0);
        return localDateTimeToDate(dateTime);
    }

    /**
     * 指定时间与当前时间相差的秒数。
     * @param dateTime 时间
     * @return 返回正整数或者负数
     */
    public static long betweenNowSeconds(LocalDateTime dateTime) {
        return Duration.between(dateTime, LocalDateTime.now()).getSeconds();
    }

    /**
     * 指定时间与当前时间相差的秒数。
     * @param date 时间
     * @return 返回正整数或者负数
     */
    public static long betweenNowSeconds(Date date) {
        return Duration.between(dateToLocalDateTime(date), LocalDateTime.now()).getSeconds();
    }

    /**
     * 日期格式化。格式 yyyy-MM-dd HH:mm:ss
     * @param date
     * @return 返回格式化后的日期
     */
    public static String YYYY_MM_DD_HH_MM_SS(Date date) {
        LocalDateTime dateTime = dateToLocalDateTime(date);
        return dateTime.format(YYYY_MM_DD_HH_MM_SS);
    }

    /**
     * 日期字符串转为日期。格式 yyyy-MM-dd HH:mm:ss
     * @param dateString
     * @return 返回日期
     */
    public static Date YYYY_MM_DD_HH_MM_SS(String dateString) {
        LocalDateTime dateTime = LocalDateTime.parse(dateString, YYYY_MM_DD_HH_MM_SS);
        return localDateTimeToDate(dateTime);
    }

    /**
     * 日期格式化。格式 yyyyMMddHHmmss
     * @param date
     * @return 返回格式化后的日期
     */
    public static String YYYYMMDDHHMMSS(Date date) {
        LocalDateTime dateTime = dateToLocalDateTime(date);
        return dateTime.format(YYYYMMDDHHMMSS);
    }

    /**
     * 日期字符串转为日期。格式 yyyyMMddHHmmss
     * @param dateString
     * @return 返回日期
     */
    public static Date YYYYMMDDHHMMSS(String dateString) {
        LocalDateTime dateTime = LocalDateTime.parse(dateString, YYYYMMDDHHMMSS);
        return localDateTimeToDate(dateTime);
    }
}
