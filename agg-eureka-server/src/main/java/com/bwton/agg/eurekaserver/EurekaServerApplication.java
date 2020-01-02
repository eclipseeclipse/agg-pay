package com.bwton.agg.eurekaserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.bootstrap.BootstrapConfiguration;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * @author yangqing
 * @description EurekaServer启动类
 * @create 2019/4/12 14:22
 */

@SpringBootApplication
@EnableEurekaServer
@BootstrapConfiguration
public class EurekaServerApplication {
    private static Logger log = LoggerFactory.getLogger(EurekaServerApplication.class);

    public static void main(String[] args) {
        SpringApplicationBuilder app = new SpringApplicationBuilder(EurekaServerApplication.class);
        app.web(true).run(args);
        log.info("{} {} 启动成功!",Thread.currentThread().getStackTrace()[1].getClassName(),app.context().getId());
    }

}
