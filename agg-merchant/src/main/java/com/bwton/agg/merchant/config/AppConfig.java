package com.bwton.agg.merchant.config;

import com.bwton.web.filter.ApiFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

/**
 * @author DengQiongChuan
 * @date 2019-12-18 21:14
 */
@Configuration
public class AppConfig {

    @Bean
    public FilterRegistrationBean apiFilterRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(apiFilter());
        registration.addUrlPatterns("/*");
        registration.setOrder(0);
        return registration;
    }

    @Bean
    public ApiFilter apiFilter() {
        return new ApiFilter();
    }
}
