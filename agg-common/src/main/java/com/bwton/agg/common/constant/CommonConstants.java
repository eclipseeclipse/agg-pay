package com.bwton.agg.common.constant;

/**
 * @author yangqing
 * @description 普通常量
 * @create 2019/4/18 2:14 PM
 */

public interface CommonConstants {
    String VERSION = "2.0";

    String HTTP_BODY = "body";
    String HTTP_HEADERS = "headers";
    String UTF_8 = "UTF-8";

    /** 后续的 ZUUL Filter 是否执行。true不执行，false执行 */
    String ZUUL_FILTER_TERMINATE = "zuul-filter-terminate";

    /** 请求头全局跟踪号 */
    String HTTP_HEADER_GLOBAL_SEQ = "sequence";
    /** 请求头平台商户号 */
    String HTTP_HEADER_MERCHANT_ID = "bwt-mch-id";
    /** 请求头商户设备号 */
    String HTTP_HEADER_DEVICE_INFO = "bwt-device-info";

    String VERSION_NAME = "version";
    String CHARSET_NAME = "charset";
    String SIGN_TYPE_NAME = "sign_type";
    String SERVICE_NAME = "service";
    String STATUS = "status";
    String MESSAGE = "message";
    String NONCE_STR = "nonce_str";
    String SIGN = "sign";
    String MERCHANT_ID = "mch_id";
    String MERCHANT_DEVICE_INFO = "device_info";

    String DEFAULT_SIGN_TYPE = "MD5";

    String UNION_SUCCESS_CODE = "0";
    String UNION_SUCCESS_MESSAGE = "成功";
    String UNION_VERSION = "2.0";

    //00 成功
    String UNION_ERR_CODE_00 = "00";

    //04 交易状态未明
    String UNION_ERR_CODE_04 = "04";

    //06 系统繁忙，请稍后再试
    String UNION_ERR_CODE_06 = "06";

    //04106_1211006 订单号不存在
    String UNION_ERR_CODE_ORDER_NOT_EXISTS="Order not exists" ;

    //04103_1210001 订单不存在
    String UNION_ERR_CODE_04103_1210001="04103_1210001" ;


    String HSLOGPRE ="发生熔断，原因是：";

    /**最大分页数*/
    Integer MAX_PAGE_SIZE = 20;

    /**
     * 用户抵扣相关操作 Redisson 锁的租赁时间。单位秒。
     */
    Integer USER_DEDUCTION_LOCK_LEASE_TIME = 30;

    /**
     * 告诉调用者消息消费结果
     */
    String MSG_CONSUME_SUCESS = "success";

    /**
     * 告诉调用者消息消费结果
     */
    String MSG_CONSUM_ERROR = "error";

    /**
     * LinkedBlockingQueue线程池的容量
     */
    Integer QUEUE_SIZE = 10240;

    /**
     * SUCCESS—退款成功
     * FAIL—退款失败
     * PROCESSING—退款处理中
     * NOTSURE—未确定，需要商户原退款单号重新发起
     * CHANGE—转入代发
     */
    String SUCCESS = "SUCCESS";
    String FAIL = "FAIL";
    String PROCESSING = "PROCESSING";
    String NOTSURE = "NOTSURE";
    String CHANGE = "CHANGE";

    /**
     * 退款笔数，需要根据该字段做数据处理
     */
    String REFUND_COUNT="refund_count";
}
