package com.bwton.agg.gateway.service.impl;

import com.bwton.agg.api.MerchantRest;
import com.bwton.agg.gateway.service.MerchantService;
import com.bwton.lang.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author DengQiongChuan
 * @date 2019-12-09 22:59
 */
@Service
public class MerchantServiceImpl implements MerchantService {
    @Autowired
    private MerchantRest merchantRest;

    public String getMerchantSecureKey(String merchantId) {
        Result<String> result = merchantRest.getSecureKey(merchantId);
        if (result.isSuccess()) {
            return result.getResult();
        } else {
            return null;
        }
    }
}
