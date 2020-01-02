package com.bwton.agg.common.constant;

/**
 * @author 李智鹏
 * @Description mq消息常量
 * @date 2018年2月6日 下午7:34:33
 */
public interface MqConstants {

    /**
     * mq消息分隔符
     */
    String MSG_SEPARATOR = "===";

    /**
     * mq消息字段
     */

    // 异步计算
    String CALC_ASYNC_HEAD = "calc_async";

    // 计算结果处理
    String CALC_RESULT = "calc_result";

    // 结算结果通知
    String CALC_RESULT_NOTIFY = "calc_result_notify";

}
