package com.bwton.agg.merchant.service;

import com.bwton.agg.bean.vo.ChannelMerchantVO;
import com.bwton.agg.bean.vo.MerchantVO;
import com.bwton.agg.bean.vo.TradeRouteVO;

/**
 * @author DengQiongChuan
 * @date 2019-12-09 22:07
 */
public interface MerchantService {
    String getMerchantSecureKey(String merchantId);

    TradeRouteVO getTradeRoute(String merchantId);

    ChannelMerchantVO getChannelMerchant(Integer channelId, String channelMerchantId);

    MerchantVO getChannelMerchantById(String merchantId);
}
