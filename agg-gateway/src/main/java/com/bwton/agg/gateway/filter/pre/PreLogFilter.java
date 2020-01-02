package com.bwton.agg.gateway.filter.pre;

import com.bwton.agg.common.constant.CommonConstants;
import com.bwton.agg.common.util.TraceIDUtils;
import com.bwton.agg.common.util.UuidUtils;
import com.bwton.agg.gateway.util.RequestContextUtil;
import com.bwton.agg.gateway.util.RequestUtil;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

import static com.bwton.agg.common.enums.AggPayExceptionEnum.REQUEST_BODY_IS_EMPTY;
import static com.bwton.agg.common.enums.AggPayExceptionEnum.SYS_ERROR;

/**
 * 日志过滤器。打印请求日志，为第一个过滤器。
 *
 * @author DengQiongChuan
 * @date 2019-12-09 22:49
 */
@Slf4j
@Component
public class PreLogFilter extends ZuulFilter {

    @Override
    public String filterType() {
        return FilterConstants.PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        return -1;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {
        // 全局跟踪号放入请求头
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        String globalSeq = UuidUtils.generateUUID();
        ctx.addZuulRequestHeader(CommonConstants.HTTP_HEADER_GLOBAL_SEQ, globalSeq);
        TraceIDUtils.setMCData(globalSeq);

        // 打印请求日志，并判断请求体是否为空字符
        try {
            //todo 统一复制request的写法
            String reqBody = RequestUtil.getRequestBody(request);
            log.info("request method:{}, url:{}, body: {}",
                    request.getMethod(), request.getRequestURL().toString(), reqBody);
            if (StringUtils.isBlank(reqBody)) {
                RequestContextUtil.setResponseTerminateAttribute(ctx, REQUEST_BODY_IS_EMPTY);
                return null;
            }
        } catch (IOException e) {
            log.info("日志过滤器中发生异常", e);
            RequestContextUtil.setResponseTerminateAttribute(ctx, SYS_ERROR);
            return null;
        }

        ctx.set(CommonConstants.ZUUL_FILTER_TERMINATE, false);
        return null;
    }
}
