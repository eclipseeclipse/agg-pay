package com.bwton.agg.gateway.filter.pre;

import com.bwton.agg.common.configuration.CommonConfig;
import com.bwton.agg.common.constant.CommonConstants;
import com.bwton.agg.common.util.SignUtils;
import com.bwton.agg.common.util.TraceIDUtils;
import com.bwton.agg.common.util.UuidUtils;
import com.bwton.agg.common.util.XmlUtils;
import com.bwton.agg.gateway.service.MerchantService;
import com.bwton.agg.gateway.util.RequestContextUtil;
import com.bwton.agg.gateway.util.RequestUtil;
import com.bwton.agg.gateway.util.ResponseUtil;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.TreeMap;

import static com.bwton.agg.common.enums.AggPayExceptionEnum.GET_SECURE_KEY_FAIL;
import static com.bwton.agg.common.enums.AggPayExceptionEnum.MERCHANT_ID_IS_EMPTY;
import static com.bwton.agg.common.enums.AggPayExceptionEnum.SIGN_VERIFY_FAIL;
import static com.bwton.agg.common.enums.AggPayExceptionEnum.SYS_ERROR;

/**
 * 验签过滤器。
 *
 * @author DengQiongChuan
 * @date 2019-12-09 22:49
 */
@Slf4j
@Component
public class CheckSignFilter extends ZuulFilter {
    @Autowired
    private MerchantService merchantService;

    @Autowired
    private CommonConfig commonConfig;

    @Override
    public String filterType() {
        return FilterConstants.PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        RequestContext ctx = RequestContext.getCurrentContext();
        if (ctx.getBoolean(CommonConstants.ZUUL_FILTER_TERMINATE)) {
            return false;
        }
        // 渠道异步通知不验签
        HttpServletRequest request = ctx.getRequest();
        String reqUri = request.getRequestURI();
        if (StringUtils.isNotBlank(reqUri) && reqUri.startsWith(commonConfig.getTradeNotifyUri())) {
            return false;
        }
        return true;
    }

    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        try {
            String reqBody = RequestUtil.getRequestBody(request);
            TreeMap<String, String> reqXmlMap = XmlUtils.toMap(reqBody);
            String reqSign = reqXmlMap.get(CommonConstants.SIGN);
            if (StringUtils.isBlank(reqSign)) {
                RequestContextUtil.setResponseTerminateAttribute(ctx, SIGN_VERIFY_FAIL);
                return null;
            }

            String merchantId = reqXmlMap.get(CommonConstants.MERCHANT_ID);
            if (StringUtils.isBlank(merchantId)) {
                RequestContextUtil.setResponseTerminateAttribute(ctx, MERCHANT_ID_IS_EMPTY);
                return null;
            }else {
                // 平台商户号放入请求头
                ctx.addZuulRequestHeader(CommonConstants.HTTP_HEADER_MERCHANT_ID, merchantId);
            }

            // 从商户服务获取密钥
            String secureKey;
            try {
                secureKey = merchantService.getMerchantSecureKey(merchantId);
            } catch (Exception e) {
                RequestContextUtil.setResponseTerminateAttribute(ctx, GET_SECURE_KEY_FAIL);
                return null;
            }
            if (StringUtils.isBlank(secureKey)) {
                RequestContextUtil.setResponseTerminateAttribute(ctx, GET_SECURE_KEY_FAIL);
                return null;
            }
            // 验签
            String sign = SignUtils.sign4Map(secureKey, reqXmlMap);
            if (!reqSign.equals(sign)) {
                RequestContextUtil.setResponseTerminateAttribute(ctx, SIGN_VERIFY_FAIL);
                return null;
            }

            String deviceInfo = reqXmlMap.get(CommonConstants.MERCHANT_DEVICE_INFO);
            if (StringUtils.isNotBlank(deviceInfo)) {
                // 商户设备号放入请求头
                ctx.addZuulRequestHeader(CommonConstants.HTTP_HEADER_DEVICE_INFO, deviceInfo);
            }

            ctx.setSendZuulResponse(true);
        } catch (Exception e) {
            log.info("验签过滤器中发生异常", e);
            RequestContextUtil.setResponseTerminateAttribute(ctx, SYS_ERROR);
            return null;
        }
        ctx.set(CommonConstants.ZUUL_FILTER_TERMINATE, false);
        return null;
    }
}
