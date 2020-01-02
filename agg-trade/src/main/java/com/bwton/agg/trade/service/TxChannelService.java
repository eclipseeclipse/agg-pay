package com.bwton.agg.trade.service;

import com.bwton.agg.bean.vo.ChannelPayVO;
import com.bwton.agg.bean.vo.MerchantVO;
import com.bwton.agg.bean.vo.TradeRouteVO;
import com.bwton.agg.trade.data.entity.TxChannelEntity;
import com.bwton.agg.trade.data.entity.TxTradeEntity;

/**
 * @ClassName
 * @Description TODO
 * @Author wuhaonan@bwton.com
 * @Date
 */
public interface TxChannelService {
    /**
     * 统一下单（主扫）
     * @param txTradeEntity
     * @param tradeRouteVO
     * @return
     */
    TxChannelEntity createPlaceOrder(TxTradeEntity txTradeEntity, TradeRouteVO tradeRouteVO);

    /**
     * 下单后更新订单
     * @param txChannelEntity
     * @param channelPayVO
     * @return
     */
    TxChannelEntity updatePlaceOrder(TxChannelEntity txChannelEntity, ChannelPayVO channelPayVO);

    /**
     * 更新渠道交易记录
     */
    int updateTxChannelEntity(TxChannelEntity txChannelEntity);

    TxChannelEntity createProcessingTxChannel(TxTradeEntity txTradeEntity, MerchantVO merchantVO);

    /**
     * 获取渠道交易记录
     * @param txSerialNo 交易流水号
     * @return
     */
    TxChannelEntity getByTxSerialNo(String txSerialNo);

    /**
     * 获取渠道交易记录
     * @param txTradeNo 平台流水号
     * @return
     */
    TxChannelEntity getByTxTradeNo(String txTradeNo);
}
