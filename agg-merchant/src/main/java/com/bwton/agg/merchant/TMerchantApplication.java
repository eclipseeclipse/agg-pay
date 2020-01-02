package com.bwton.agg.merchant;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author yangqing
 * @description 营销引擎启动类
 * @create 2019/4/10 9:29 AM
 */
@Slf4j
@EnableEurekaClient
@EnableFeignClients(basePackages = {"com.bwton"})
@ComponentScan(basePackages={"com.bwton"})
@SpringBootApplication(exclude = {JacksonAutoConfiguration.class})
@MapperScan("com.bwton.agg.merchant.dao")
@RestController
@RefreshScope
public class TMerchantApplication {

    public static void main(String[] args) {
        SpringApplicationBuilder app = new SpringApplicationBuilder(TMerchantApplication.class);
        app.web(true).run(args);
        log.info("{} {} 启动成功!",Thread.currentThread().getStackTrace()[1].getClassName(),app.context().getId());
    }


}
