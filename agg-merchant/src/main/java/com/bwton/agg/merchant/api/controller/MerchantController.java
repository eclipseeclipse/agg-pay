package com.bwton.agg.merchant.api.controller;

import com.bwton.agg.bean.bo.ChannelMerchantQueryBO;
import com.bwton.agg.bean.vo.ChannelMerchantVO;
import com.bwton.agg.bean.vo.MerchantVO;
import com.bwton.agg.bean.vo.TradeRouteVO;
import com.bwton.agg.common.BaseController;
import com.bwton.agg.merchant.service.MerchantService;
import com.bwton.core.web.CommonResult;
import com.bwton.lang.Result;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import static com.bwton.agg.common.enums.AggPayExceptionEnum.CHANNEL_MERCHANT_NO_NOT_EXISTS;
import static com.bwton.agg.common.enums.AggPayExceptionEnum.MERCHANT_SECURE_KEY_NOT_EXISTS;

/**
 * @author DengQiongChuan
 * @date 2019-12-09 22:03
 */
@RestController
public class MerchantController extends BaseController {

    @Autowired
    private MerchantService merchantService;

    @RequestMapping(value = "/merchant/securekey/get/{merchantId}", method = RequestMethod.GET)
    public Result<String> getSecureKey(@PathVariable String merchantId){
        String secureKey = merchantService.getMerchantSecureKey(merchantId);
        if (StringUtils.isBlank(secureKey)) {
            return CommonResult.failure(MERCHANT_SECURE_KEY_NOT_EXISTS);
        }
        return CommonResult.success(secureKey);
    }

    @RequestMapping(value = "/merchant/channel_merchant/get/{merchantId}", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Result<MerchantVO> getChannelMerchantById(@PathVariable String merchantId) {
        MerchantVO merchantVO = merchantService.getChannelMerchantById(merchantId);
        return CommonResult.success(merchantVO);
    }

    @RequestMapping(value = "/merchant/trade_route/get/{merchantId}", method = RequestMethod.GET)
    public Result<TradeRouteVO> getTradeRoute(@PathVariable String merchantId) {
        TradeRouteVO tradeRouteVO = merchantService.getTradeRoute(merchantId);
        return CommonResult.success(tradeRouteVO);
    }

    @RequestMapping(value = "/merchant/channelmerchant/get", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Result<ChannelMerchantVO> getChannelMerchant(@RequestBody ChannelMerchantQueryBO channelMerchantQueryBO) {
        ChannelMerchantVO channelMerchantVO = merchantService.getChannelMerchant(
                channelMerchantQueryBO.getChannelId(), channelMerchantQueryBO.getChannelMerchantId());
        if (channelMerchantVO == null) {
            return CommonResult.failure(CHANNEL_MERCHANT_NO_NOT_EXISTS);
        } else {
            return CommonResult.success(channelMerchantVO);
        }
    }
}
