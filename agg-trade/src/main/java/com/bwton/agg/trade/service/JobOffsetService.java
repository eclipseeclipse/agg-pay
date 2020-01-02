package com.bwton.agg.trade.service;

/**
 * 补偿服务。到渠道查询交易结果
 *
 * @author DengQiongChuan
 * @date 2019-12-12 20:43
 */
public interface JobOffsetService {

    boolean txChannelPayOffset();

    boolean txChannelRefundOffset();
}
