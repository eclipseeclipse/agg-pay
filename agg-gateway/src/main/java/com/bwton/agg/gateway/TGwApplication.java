package com.bwton.agg.gateway;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author yangqing
 * @description 网关启动类
 * @create 2019/4/10 9:25 AM
 */
@Slf4j
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"com.bwton"})
@ComponentScan(basePackages={"com.bwton"})
@SpringBootApplication
@EnableZuulProxy
@EnableScheduling
@RefreshScope
public class TGwApplication {

    public static void main(String[] args) {
        SpringApplicationBuilder app = new SpringApplicationBuilder(TGwApplication.class);
        app.web(true).run(args);
        log.info("{} {} 启动成功!",Thread.currentThread().getStackTrace()[1].getClassName(),app.context().getId());
    }

}
