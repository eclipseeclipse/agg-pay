package com.bwton.agg.gateway.util;

import com.bwton.agg.common.constant.CommonConstants;
import com.bwton.agg.common.enums.AggPayExceptionEnum;
import com.netflix.zuul.context.RequestContext;

/**
 * @author DengQiongChuan
 * @date 2019-12-26 15:56
 */
public class RequestContextUtil {

    /**
     * 后续ZUUL过滤器不执行，返回响应时RequestContext需要设置的属性。
     */
    public static void setResponseTerminateAttribute(RequestContext ctx, AggPayExceptionEnum exceptionEnum) {
        ctx.set(CommonConstants.ZUUL_FILTER_TERMINATE, true);
        ctx.setSendZuulResponse(false);
        ctx.setResponseBody(ResponseUtil.buildResponseXml(exceptionEnum));
    }

    /**
     * 返回响应时RequestContext需要设置的属性。
     */
    public static void setResponseAttribute(RequestContext ctx, AggPayExceptionEnum exceptionEnum) {
        ctx.setSendZuulResponse(false);
        ctx.setResponseBody(ResponseUtil.buildResponseXml(exceptionEnum));
    }

}
