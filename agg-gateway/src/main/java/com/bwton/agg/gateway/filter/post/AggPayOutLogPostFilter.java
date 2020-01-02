package com.bwton.agg.gateway.filter.post;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;

/**
 * @author yangqing
 * @description 打印与外部系统通讯请求与返回日志
 * @create 2019/5/6 2:08 PM
 */
@Component
@Slf4j
public class AggPayOutLogPostFilter extends ZuulFilter {

    @Override
    public String filterType() {
        return FilterConstants.POST_TYPE;
    }

    @Override
    public int filterOrder() {
        return 999;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        String respBody = ctx.getResponseBody();
        if (respBody != null) {
            log.info("response body: {}", respBody);
        }
        ctx.setResponseBody(respBody);
        return null;
    }
}