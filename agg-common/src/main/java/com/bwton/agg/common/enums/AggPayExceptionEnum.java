package com.bwton.agg.common.enums;

import com.bwton.lang.Err;

/**
 * 错误代码定义规则：4位数字
 * 前2位的定义：
 * 00 基础模块
 * 01 交易服务
 * 02 商户服务
 * 03 渠道前置
 * 09 熔断返回错误码6位长度，09+模块错误头+错误码，枚举以HS开头定义
 *
 * @author yangqing
 * @description
 * @create 2019/4/17 3:20 PM
 */
public enum AggPayExceptionEnum implements Err {
    SYS_SUCCESS("0", "成功"),
    FAIL("1", "失败"),
    PROCESSING("2", "处理中"),

    SYS_ERROR("0001", "系统错误"),
    SERVICE_ERROR("0002", "服务异常"),
    PARAM_ERROR("0003", "参数错误"),

    // 基础模块
    SIGN_VERIFY_FAIL("0011", "签名为空或验证失败"),
    LOCK_FAIL("0012", "获取Redis锁失败"),
    GET_SECURE_KEY_FAIL("0013", "获取密钥失败"),
    LOCK_EXCEPTION("0014", "获取Redis锁异常"),
    MQ_SEND_EXCEPTION("0015", "发送mq消息异常"),
    MQ_PARAMS_FAIL("0016", "发送mq消息，参数不合法"),
    MERCHANT_ID_IS_EMPTY("0017", "商户号不能为空"),
    REQUEST_BODY_IS_EMPTY("0018", "HTTP Request Body为空"),
    BEAN_TO_MAP_EXCEPTION("0019", "bean转map发生异常"),
    XML_TO_MAP_EXCEPTION("0020", "xml转map发生异常"),
    RESPONSE_BODY_IS_EMPTY("0021", "HTTP Response Body为空"),
    UNSUPPORT_INTERFACE_METHOD("0022", "不支持的接口"),


    // 交易服务 0101
    ORDER_IS_EXIST("0101", "订单已存在"),
    MERCHANT_ROUTE_FAIL("0102", "商户路由失败"),
    INVOKE_CHANNEL_FAIL("0103", "调用渠道前置失败"),
    ORDER_IS_NOT_EXIST("0104", "订单不存在"),
    CHANNEL_ORDER_IS_NOT_EXIST("0105", "渠道订单不存在"),
    ORG_ORDER_STATUS_NOT_CORRECT("0106", "原订单状态不正确"),
    REFUND_FEE_OVERSTEP("0107","退款金额超出原订单实际支付金额"),
    REFUND_FEE_ALREADY_EXISTED("0108","申请退款订单给的商户订单号和平台商户号查询已存在，创建退款订单失败"),
    ORG_ORDER_STATUS_NOT_SUCCESS("0109", "原订单状态不是支付成功状态，不允许申请退款"),
    SINGLE_LIMIT_NOT_PERMIT("0110", "交易金额不能大于单笔限额"),
    ORDER_NOT_ALLOW_CLOSE("0111", "订单不允许关闭"),


    HS_XXX01("090101", "XXX，熔断处理"),

    // 商户服务 0201
    MERCHANT_ID_NOT_EXISTS("0201", "商户号不存在"),
    MERCHANT_NOT_CONFIG_ROUTE("0202", "该商户号没有配置路由"),
    CHANNEL_MERCHANT_NO_NOT_EXISTS("0203", "渠道商户号不存在"),
    MERCHANT_SECURE_KEY_NOT_EXISTS("0203", "商户号或商户密钥不存在"),

    HS_XXX02("090201", "XXX，熔断处理"),

    // 渠道前置 0301
    CHANNEL_PRE_JAXB_EXCEPTION("0301", "JAXB异常"),
    CHANNEL_PRE_RETURN_OBJ_IS_EMPTY("0302", "渠道返回空值对象"),
    CHANNEL_PRE_SIGN_FAIL("0303", "渠道返回结果验签失败"),
    CHANNEL_PRE_HTTP_EXCEPTION("0304", "渠道HTTP调用异常"),
    CHANNEL_PRE_HTTP_BODY_EMPTY("0305", "渠道HTTP返回结果body为空"),
    CHANNEL_PRE_CHANNEL_RETURN_ERROR("0306", "渠道请求结果失败"),
    CHANNEL_PRE_UNKNOW_EXCEPTION("0307", "请求渠道发生未知异常"),
    CHANNEL_PRE_PROCESS_FAIL("0308", "渠道处理失败"),
    CHANNEL_PRE_GATAWAY_CODE_NOT_RETURN("0309", "渠道通信标识状态未返回"),
    CHANNEL_PRE_BUSINESS_RESULT_CODE_NOT_RETURN("0310", "渠道业务结果状态未返回"),


    HS_XXX03("090301", "XXX，熔断处理"),

    ;

    /** 错误代码 */
    private String code;
    /** 错误信息 */
    private String msg;

    /** 错误类型 */
    private AggPayExceptionEnum errType;

    AggPayExceptionEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    AggPayExceptionEnum(String code, String msg, AggPayExceptionEnum errType) {
        this.code = code;
        this.msg = msg;
        this.errType = errType;
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public AggPayExceptionEnum getErrType() {
        return errType;
    }
}

