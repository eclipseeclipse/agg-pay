package com.bwton.agg.common.configuration;

import com.bwton.agg.common.util.id.SnowflakeIdWorker;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * IdWorker 主键生成工具
 *
 * @author zhuzh
 * @date 2019-5-8
 */
@Configuration
public class IdWorkerConfig {

    @Value("${work.id:1}")
    private Long workId;

    @Bean
    public SnowflakeIdWorker snowflakeIdWorker() {
        return new SnowflakeIdWorker(workId);
    }
}  