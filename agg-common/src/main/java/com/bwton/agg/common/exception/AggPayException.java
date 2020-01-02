package com.bwton.agg.common.exception;

import com.bwton.agg.common.enums.AggPayExceptionEnum;
import lombok.Data;

/**
 * @author DengQiongChuan
 * @date 2019-12-17 10:15
 */
@Data
public class AggPayException extends RuntimeException {
    private static final long serialVersionUID = -5577082408188133534L;

    /** 错误码类型。{@link com.bwton.agg.common.enums.AggPayExceptionEnum} */
    private String errType;

    /** 错误码*/
    private String errCode;
    /** 错误消息*/
    private String errMsg;

    public AggPayException() {
        super();
    }

    public AggPayException(AggPayExceptionEnum err) {
        this(null, err.getCode(), err.getMsg(), null);
    }

    public AggPayException(String errType, AggPayExceptionEnum err) {
        this(null, err.getCode(), err.getMsg(), null);
    }

    public AggPayException(AggPayExceptionEnum err, Throwable throwable) {
        this(null, err.getCode(), err.getMsg(), throwable);
    }

    public AggPayException(String errType, AggPayExceptionEnum err, Throwable throwable) {
        this(null, err.getCode(), err.getMsg(), throwable);
    }

    public AggPayException(String errCode, String errMsg) {
        this(null, errCode, errMsg, null);
    }

    public AggPayException(String errType, String errCode, String errMsg) {
        this(null, errCode, errMsg, null);
    }

    public AggPayException(String errType, String errCode, String errMsg, Throwable throwable) {
        super(errMsg, throwable);
        this.errType = errType;
        this.errCode = errCode;
        this.errMsg = errMsg;
    }
}