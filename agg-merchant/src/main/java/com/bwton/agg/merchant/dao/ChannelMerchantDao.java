package com.bwton.agg.merchant.dao;

import com.bwton.agg.merchant.data.entity.ChannelMerchantEntity;
import org.apache.ibatis.annotations.Param;

/**
 * @author DengQiongChuan
 * @date 2019-12-03 20:31
 */
public interface ChannelMerchantDao {
    ChannelMerchantEntity getById(Integer id);

    ChannelMerchantEntity getByChannelIdAndChannelMerId(
            @Param("channelId") Integer channelId, @Param("channelMerId") String channelMerId);

    int insert(ChannelMerchantEntity channelMerchantEntity);

    int update(ChannelMerchantEntity channelMerchantEntity);

    ChannelMerchantEntity getByMerchantId(String merchantId);
}
