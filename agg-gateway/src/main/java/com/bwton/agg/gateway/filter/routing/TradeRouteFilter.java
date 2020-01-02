package com.bwton.agg.gateway.filter.routing;

import com.bwton.agg.common.configuration.CommonConfig;
import com.bwton.agg.common.constant.CommonConstants;
import com.bwton.agg.common.util.XmlUtils;
import com.bwton.agg.enums.InterfaceTypeEnum;
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

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.TreeMap;

import static com.bwton.agg.common.enums.AggPayExceptionEnum.SYS_ERROR;
import static com.bwton.agg.common.enums.AggPayExceptionEnum.UNSUPPORT_INTERFACE_METHOD;

/**
 * TODO 增加注释说明
 * @author DengQiongChuan
 * @date 2019-12-15 14:07
 */
@Slf4j
@Component
public class TradeRouteFilter extends ZuulFilter {

    @Autowired
    private CommonConfig commonConfig;

    @Override
    public String filterType() {
        return FilterConstants.ROUTE_TYPE;
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        RequestContext ctx = RequestContext.getCurrentContext();
        return !ctx.getBoolean(CommonConstants.ZUUL_FILTER_TERMINATE);
    }

    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        try {
            String reqUri = request.getRequestURI();
            if (StringUtils.isBlank(reqUri) || !reqUri.equalsIgnoreCase(commonConfig.getTradeGatewayUri())) {
                return null;
            }

            String reqBody = RequestUtil.getRequestBody(request);
            //todo 日志输出按照公司规范调整
            log.info("request method:{}, url:{}, body: {}",
                    request.getMethod(), request.getRequestURL().toString(), reqBody);

            // 当请求URI是支付网关时，根据接口类型进行路由
            TreeMap<String, String> reqXmlMap = XmlUtils.toMap(reqBody);
            String interfaceType = reqXmlMap.get(CommonConstants.SERVICE_NAME);
            InterfaceTypeEnum interfaceTypeEnum = InterfaceTypeEnum.getEnum(interfaceType);
            String tradeUri;
            //todo 如果interfaceTypeEnum为空会出问题。增加一个default
            switch (interfaceTypeEnum) {
                case PLACE_ORDER:
                    tradeUri = commonConfig.getTradePlaceOrderUri();
                    break;
                case QUERY:
                    tradeUri = commonConfig.getTradePayQueryUri();
                    break;
                case CLOSE:
                    tradeUri = commonConfig.getTradeCloseUri();
                    break;
                case REFUND:
                    tradeUri = commonConfig.getTradeRefundUri();
                    break;
                case REFUND_QUERY:
                    tradeUri = commonConfig.getTradeRefundQueryUri();
                    break;
                case UNDEFINED:
                default:
                    tradeUri = "";
            }
            if (StringUtils.isBlank(tradeUri)) {
                ctx.setResponseBody(ResponseUtil.buildResponseXml(UNSUPPORT_INTERFACE_METHOD));
                return null;
            }
            ctx.put(FilterConstants.REQUEST_URI_KEY, tradeUri);
        } catch (IOException e) {
            log.info("交易路由过滤器中发生异常", e);
            RequestContextUtil.setResponseTerminateAttribute(ctx, SYS_ERROR);
        }
        return null;
    }
}
