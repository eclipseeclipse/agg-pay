package com.bwton.agg.trade.service;

import com.bwton.agg.bean.bo.OrderCloseBO;
import com.bwton.agg.bean.bo.OrderPayBO;
import com.bwton.agg.bean.bo.OrderRefundBO;
import com.bwton.agg.bean.vo.ChannelPayVO;
import com.bwton.agg.bean.vo.MerchantVO;
import com.bwton.agg.bean.vo.TradeRouteVO;
import com.bwton.agg.trade.data.entity.TxChannelEntity;
import com.bwton.agg.trade.data.entity.TxTradeEntity;

public interface TxTradeService {
    /**
     * 统一下单（主扫）
     * @param orderPayBO
     * @param tradeRouteVO
     * @return
     */
    TxTradeEntity createPlaceOrder(OrderPayBO orderPayBO, TradeRouteVO tradeRouteVO);

    /**
     * 下单后更新订单
     *
     * @param txTradeEntity
     * @param channelPayBO
     * @return
     */
    TxTradeEntity updatePlaceOrder(TxTradeEntity txTradeEntity, ChannelPayVO channelPayBO);

    /**
     * 更新平台交易记录
     * @param txTradeEntity
     * @return
     */
    int updateTxTradeEntity(TxTradeEntity txTradeEntity);

    TxTradeEntity createCloseTxTrade(OrderCloseBO orderCloseBO, TxTradeEntity orgTxTrade, MerchantVO merchantVO);

    TxTradeEntity getByMerOrderNoAndMerchantId(String outTradeNo, String merchantId);

    TxTradeEntity getByTxTradeNoAndMerchantId(String txTradeNo, String merchantId);

    TxChannelEntity createRefundTxTrade(OrderRefundBO orderRefundDTO, TxTradeEntity orgTxTrade, MerchantVO merchantVO);

    Integer accountRefundTotalByOrgSerialNo(String txTradeNo);
}
