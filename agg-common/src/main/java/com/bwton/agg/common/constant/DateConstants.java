package com.bwton.agg.common.constant;

/**
 * @author 李智鹏
 * @Description: 时间常量
 * @date 2018年2月6日 下午7:33:56
 */
public interface DateConstants {
    String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";
    String YYYYMMDD_HHMMSS = "yyyyMMdd HHmmss";
    String YYYYMMDDHHMMSSSSS = "yyyyMMddHHmmssSSS";
    String YYMMDDHHMMSS = "yyMMddHHmmss";
    String YYMMDDHHMM = "yyMMddHHmm";
    String YYYYMMDD = "yyyyMMdd";
    String YYYY_MM_DD = "yyyy-MM-dd";
    String YYYY = "yyyy";
    String YYMMDD = "yyMMdd";
    String HHMMSS = "HHmmss";
    String HHMM = "HHmm";
    String MMDD = "MMdd";
    String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
    String YYYYMMDD_HH_MM_SS = "yyyyMMdd HH:mm:ss";
    String YYYY_MM_DD_HH_MM_SS_SSS = "yyyy-MM-dd HH:mm:ss.SSS";

    /**
     * 正则表达式
     */
    String YYYYMMDDHHMMSS_REGEX = "^((([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})(((0[13578]|1[02])(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)(0[1-9]|[12][0-9]|30))|(02(0[1-9]|[1][0-9]|2[0-8]))))|((([0-9]{2})(0[48]|[2468][048]|[13579][26])|((0[48]|[2468][048]|[3579][26])00))0229))([0-1]?[0-9]|2[0-3])([0-5][0-9])([0-5][0-9])$";
    String YYMM_REGEX = "^\\d\\d(0[1-9]|1[0-2])$";
    String YYYYMMDD_REGEX = "(([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})(((0[13578]|1[02])(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)(0[1-9]|[12][0-9]|30))|(02(0[1-9]|[1][0-9]|2[0-8]))))|((([0-9]{2})(0[48]|[2468][048]|[13579][26])|((0[48]|[2468][048]|[3579][26])00))0229)";

}
