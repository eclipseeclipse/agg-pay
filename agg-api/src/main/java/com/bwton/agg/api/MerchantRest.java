package com.bwton.agg.api;

import com.bwton.agg.bean.bo.ChannelMerchantQueryBO;
import com.bwton.agg.bean.vo.ChannelMerchantVO;
import com.bwton.agg.bean.vo.MerchantVO;
import com.bwton.agg.bean.vo.TradeRouteVO;
import com.bwton.lang.Result;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient("agg-merchant")
public interface MerchantRest {

    /**
     * 获取平台商户密钥
     * @param merchantId 平台商户号
     * @return
     */
    @RequestMapping(value = "/merchant/securekey/get/{merchantId}", method = RequestMethod.GET)
    Result<String> getSecureKey(@PathVariable("merchantId") String merchantId);

    @RequestMapping(value = "/merchant/channel_merchant/get/{merchantId}", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    Result<MerchantVO> getChannelMerchantById(@PathVariable("merchantId") String merchantId);

    /**
     * 获取平台商户路由
     * @param merchantId 平台商户号
     * @return
     */
    @RequestMapping(value = "/merchant/trade_route/get/{merchantId}", method = RequestMethod.GET)
    Result<TradeRouteVO> getTradeRoute(@PathVariable("merchantId") String merchantId);

    /**
     * 获取渠道商户信息
     */
    @RequestMapping(value = "/merchant/channelmerchant/get", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    Result<ChannelMerchantVO> getChannelMerchant(@RequestBody  ChannelMerchantQueryBO channelMerchantQueryBO);
}
