package com.bwton.agg.common.configuration;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author DengQiongChuan
 * @date 2019-12-05 19:38
 */
@Component
@Data
public class CommonConfig {
    /** Redis获取锁等待时间。单位秒 */
    @Value("${common.lock.wait.time}")
    private Integer lockWaitTime;

    /** Redis锁租赁时间。单位秒 */
    @Value("${common.lock.lease.time}")
    private Integer lockLeaseTime;

    /** 渠道回调聚合支付地址 */
    @Value("${common.notify.url}")
    private String notifyUrl;

    /** 交易网关URL */
    @Value("${trade.gataway.uri}")
    private String tradeGatewayUri;

    /** 统一下单URL */
    @Value("${trade.place.order.uri}")
    private String tradePlaceOrderUri;

    /** 支付查询URL */
    @Value("${trade.pay.query.uri}")
    private String tradePayQueryUri;

    /** 交易关闭URL */
    @Value("${trade.close.uri}")
    private String tradeCloseUri;

    /** 退款URL */
    @Value("${trade.refund.uri}")
    private String tradeRefundUri;

    /** 退款查询URL */
    @Value("${trade.refund.query.uri}")
    private String tradeRefundQueryUri;

    /** 支付回调地址 */
    @Value("${trade.notify.uri}")
    private String tradeNotifyUri;
}
