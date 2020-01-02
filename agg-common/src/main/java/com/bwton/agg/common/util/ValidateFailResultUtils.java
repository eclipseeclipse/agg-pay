package com.bwton.agg.common.util;

import javax.validation.ConstraintViolation;
import java.util.Set;

/**
 * {@link javax.validation.Validator} 校验失败结果工具
 *
 * @author DengQiongChuan
 * @date 2019-05-09 22:15
 */
public class ValidateFailResultUtils {

    /**
     * 包装 {@link javax.validation.Validator} 校验失败结果包装
     * @param result 校验失败结果
     * @param <T>
     * @return 校验失败结果字符串
     */
    public static <T> String buildMsg(Set<ConstraintViolation<T>> result) {
        StringBuilder msg = new StringBuilder();
        for (ConstraintViolation<T> validateResult : result) {
            msg.append(validateResult.getPropertyPath().toString());
            msg.append(validateResult.getMessage());
            msg.append(",");
        }
        msg = msg.deleteCharAt(msg.length() - 1);
        return msg.toString();
    }

}
