package com.bwton.agg.trade.dao;

import com.bwton.agg.trade.data.entity.TxChannelEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * @author DengQiongChuan
 * @date 2019-12-03 20:25
 */
@Mapper
public interface TxChannelDao {
    TxChannelEntity getByTxSerialNo(String txSerialNo);

    List<TxChannelEntity> listByTxSerialNo(@Param("txSerialNos") List<String> txSerialNos);

    int insert(TxChannelEntity txChannelEntity);

    int update(TxChannelEntity txChannelEntity);

    TxChannelEntity getByTxTradeNo(String txTradeNo);

    List<TxChannelEntity> listRefundByTxChannelTimeAndTxStatus(@Param("startTime") Date startTime,
                                                               @Param("txStatus") Integer txStatus,
                                                               @Param("pageSize") Integer pageSize);

    List<TxChannelEntity> listByTxStatusAndTime(@Param("startTime") Date startTime,
                                                @Param("txStatus") Integer txStatus,
                                                @Param("tradeType") Integer tradeType);
}
