package com.bwton.agg.api;

import com.bwton.lang.Result;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
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
    }
