package com.bwton.agg.gateway.filter.error;

import com.netflix.zuul.ZuulFilter;

/**
 * @author yangqing
 * @description
 * @create  12:07 PM
 */

//@Component
public class AggPayErrorFilter extends ZuulFilter {

    @Override
    public String filterType() {
        //后置过滤器
        return "error";
    }

    @Override
    public int filterOrder() {
        //优先级，数字越大，优先级越低
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        //是否执行该过滤器，true代表需要过滤
        return true;
    }

    @Override
    public Object run() {
        return null;
    }

}