package com.bwton.agg.common.util;

import com.bwton.agg.common.constant.DateConstants;
import com.bwton.core.util.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author 李智鹏
 * @Description 时间工具类
 * @date 2018年2月5日 上午9:51:08
 */
@Slf4j
public class TimeUtils {
    private static SimpleDateFormat SDF = new SimpleDateFormat(DateConstants.YYYYMMDDHHMMSS);
    private static SimpleDateFormat SDF2 = new SimpleDateFormat(DateConstants.YYMMDDHHMMSS);
    private static SimpleDateFormat SDF3 = new SimpleDateFormat(DateConstants.MMDD);
    private static SimpleDateFormat SDF4 = new SimpleDateFormat(DateConstants.YYMMDDHHMM);

    public static synchronized String dateToStr(Date date, String format) {
        if (date == null || StringUtils.isBlank(format)) {
            return null;
        }
        try {
            if (DateConstants.YYYYMMDDHHMMSS.equals(format)) {
                return SDF.format(date);
            } else if (DateConstants.YYMMDDHHMMSS.equals(format)) {
                return SDF2.format(date);
            } else if (DateConstants.MMDD.equals(format)) {
                return SDF3.format(date);
            } else if (DateConstants.YYMMDDHHMM.equals(format)) {
                return SDF4.format(date);
            } else {
                return new SimpleDateFormat(format).format(date);
            }
        } catch (Exception e) {
            log.error("dateToStr异常", e);
            // 不要返回空
            return null;
        }
    }

    /**
     * 线程不安全，存在风险
     *
     * @param date
     * @param format
     * @return
     */
    @Deprecated
    public static Date strToDate(String date, String format) {
        if (StringUtils.isAnyBlank(date, format)) {
            return null;
        }
        try {
            if (DateConstants.YYYYMMDDHHMMSS.equals(format)) {
                return SDF.parse(date);
            } else {
                return new SimpleDateFormat(format).parse(date);
            }
        } catch (Exception e) {
            log.error("strToDate异常", e);
            return null;
        }
    }

    public static String dateToStr(Date date) {
        return dateToStr(date, DateConstants.YYYYMMDDHHMMSS);
    }

    /**
     * date时间延迟amount分钟
     *
     * @param date
     * @param amount
     * @return
     */
    public static String addMinutes(String date, final int amount, String format) {
        try {
            Date d = DateUtils.parseDate(date);
            d = DateUtils.addMinutes(d, amount);
            return dateToStr(d, format);
        } catch (final Exception e) {
            log.error("addMinutes异常", e);
            return "";
        }
    }

    public static String addMinutes(Date date, final int amount, String format) {
        return DateFormatUtils.format(DateUtils.addMinutes(date, amount), format);
    }

    public static String addMinutes(int amount, String format) {
        return addMinutes(new Date(), amount, format);
    }

    public static String addMinutes(String date, final int amount) {
        return addMinutes(date, amount, DateConstants.YYYYMMDDHHMMSS);
    }

    public static String addMinutes(Date date, final int amount) {
        return addMinutes(date, amount, DateConstants.YYYYMMDDHHMMSS);
    }

    public static String getCurrent(String format) {
        return dateToStr(new Date(), format);
    }

    public static String getCurrent() {
        return dateToStr(new Date());
    }

    public static String getCurrentSeq() {
        return dateToStr(new Date(), DateConstants.YYYYMMDDHHMMSSSSS);
    }

    /**
     * 收集起始时间到结束时间之间所有的时间并以字符串集合方式返回
     *
     * @param start
     * @param end
     * @return
     */
    public static List<String> getBetweenDate(String start, String end) {
        Long oneDay = 1000 * 60 * 60 * 24L;
        Long startTime = DateUtils.parseDate(start).getTime();
        Long endTime = DateUtils.parseDate(end).getTime();
        List<String> list = new ArrayList<>();
        while (startTime <= endTime) {
            Date d = new Date(startTime);
            list.add(TimeUtils.dateToStr(d, DateConstants.YYYYMMDD));
            startTime += oneDay;
        }
        return list;
    }

    public static boolean isExpire(Date date, int expireTime) {
        Date d = DateUtils.addMinutes(date, expireTime);
        return new Date().compareTo(d) >= 0;
    }
}