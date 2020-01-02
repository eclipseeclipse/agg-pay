package com.bwton.agg.common.configuration;

import com.bwton.web.filter.ApiFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.servlet.Filter;

/**
 * 描述:
 * @author wjl 架构组切面拦截器 放在各个业务模块拦截
 * @version v1.0.0
 * @created  2019/12/25
 */
// @Configuration
// @EnableWebMvc
public class WebCommonConfig {

//     @Bean
//    public FilterRegistrationBean apiFilterRegistration() {
//        FilterRegistrationBean registration = new FilterRegistrationBean();
//        registration.setFilter(apiFilter());
//        registration.setName("apiFilter");
//        registration.addUrlPatterns("/*");
//
//        // created by wjl,2019/12/26,v1.0.0 gateway 交互报文协议为XML, 不能使用该过滤器，否则过滤器内的异常不好处理。
//        registration.addInitParameter("exclusions","/agg/trade/gateway");
//        registration.setOrder(0);
//        return registration;
//    }
//
//    @Bean
//    public Filter apiFilter() {
//        return new ApiFilter();
//    }
}
