package com.bwton.agg.gateway.filter.post;

import com.bwton.agg.common.configuration.CommonConfig;
import com.bwton.agg.common.constant.CommonConstants;
import com.bwton.agg.common.util.SignUtils;
import com.bwton.agg.common.util.XmlUtils;
import com.bwton.agg.gateway.service.MerchantService;
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
import static com.bwton.agg.common.enums.AggPayExceptionEnum.RESPONSE_BODY_IS_EMPTY;
import static com.bwton.agg.common.enums.AggPayExceptionEnum.SYS_ERROR;

/**
 * 加签过滤器
 *
 * @author DengQiongChuan
 * @date 2019-12-17 19:13
 */
@Slf4j
@Component
public class SignFilter extends ZuulFilter {
    @Autowired
    private MerchantService merchantService;

    @Autowired
    private CommonConfig commonConfig;

    @Override
    public String filterType() {
        //后置过滤器
        return FilterConstants.POST_TYPE;
    }

    @Override
    public int filterOrder() {
        //优先级，数字越大，优先级越低
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        //是否执行该过滤器，true代表需要过滤
        RequestContext ctx = RequestContext.getCurrentContext();
        if (ctx.getBoolean(CommonConstants.ZUUL_FILTER_TERMINATE)) {
            return false;
        }

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
        try {
            InputStream stream = ctx.getResponseDataStream();
            String respBody = StreamUtils.copyToString(stream, StandardCharsets.UTF_8);
            if (StringUtils.isBlank(respBody)) {
                ctx.setResponseBody(ResponseUtil.buildResponseXml(RESPONSE_BODY_IS_EMPTY));
                return null;
            }
            TreeMap<String, String > xmlMap = XmlUtils.toMap(respBody);
            xmlMap.put(CommonConstants.VERSION_NAME, CommonConstants.VERSION);
            xmlMap.put(CommonConstants.CHARSET_NAME, CommonConstants.UTF_8);

            String merchantId = xmlMap.get(CommonConstants.MERCHANT_ID);
            // 返回的信息中没有平台商户号，则不加签
            if (StringUtils.isBlank(merchantId)) {
                respBody = XmlUtils.toXml(xmlMap);
                ctx.setResponseBody(respBody);
                return null;
            }

            xmlMap.put(CommonConstants.SIGN_TYPE_NAME, CommonConstants.DEFAULT_SIGN_TYPE);
            xmlMap.put(CommonConstants.NONCE_STR, String.valueOf(System.currentTimeMillis()));
            // 从商户服务获取密钥
            String secureKey;
            try {
                secureKey = merchantService.getMerchantSecureKey(merchantId);
            } catch (Exception e) {
                ctx.setResponseBody(ResponseUtil.buildResponseXml(GET_SECURE_KEY_FAIL));
                return null;
            }
            String sign = SignUtils.sign4Map(secureKey, xmlMap);
            xmlMap.put(CommonConstants.SIGN, sign);
            respBody = XmlUtils.toXml(xmlMap);
            ctx.setResponseBody(respBody);
        } catch (Exception e) {
            log.info("加签过滤器中发生异常", e);
            ctx.setResponseBody(ResponseUtil.buildResponseXml(SYS_ERROR));
        }
        return null;
    }

}
