package com.bwton.agg.trade.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author DengQiongChuan
 * @date 2019-12-12 22:40
 */
@Component
@Data
public class TxTradeConfig {

    // 查渠道交易补偿表当前时间往后 N 分钟内的交易记录
    @Value("${txchannel.offset.time}")
    public Integer txChannelOffsetTime;

    // 每次从渠道交易表中查出的交易记录数
    @Value("${txchannel.page.size}")
    public Integer txChannelPageSize;

    @Value("${txchannel.refund.hours}")
    public Integer txchannelRefundHours;
}
