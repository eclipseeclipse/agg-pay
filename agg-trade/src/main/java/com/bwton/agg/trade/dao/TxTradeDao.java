package com.bwton.agg.trade.dao;

import com.bwton.agg.trade.data.entity.TxTradeEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author DengQiongChuan
 * @date 2019-12-03 20:19
 */
@Mapper
public interface TxTradeDao {
    TxTradeEntity getByTxTradeNo(String txTradeNo);

    TxTradeEntity getByMerOrderNoAndMerchantId(TxTradeEntity queryEntity);

    TxTradeEntity getByTxTradeNoAndMerchantId(TxTradeEntity queryEntity);

    int insert(TxTradeEntity txTradeEntity);

    int update(TxTradeEntity txTradeEntity);

    String accountRefundTotalByOrgSerialNo(@Param("txTradeNo") String txTradeNo,@Param("txStatus") Integer txStatus,@Param("tradeType") Integer tradeType);
}
