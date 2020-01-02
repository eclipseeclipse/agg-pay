package com.bwton.agg.common.configuration;

import com.bwton.agg.common.util.AggHttpClient;
import com.bwton.core.httpclient.CoreHttpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * @ClassName
 * @Description TODO
 * @Author wuhaonan@bwton.com
 * @Date
 */
@Configuration
public class CommonCoreHttpClientConfig {

    @Value("${common.httpclient.connect.timeout}")
    private Integer connectTimeout;
    @Value("${common.httpclient.socket.timeout}")
    private Integer socketTimeout;
    @Value("${common.httpclient.conn.time.to.live}")
    private Long connTimeToLive;
    @Value("${common.httpclient.max.total}")
    private Integer maxTotal;
    @Value("${common.httpclient.max.per.route}")
    private Integer maxPerRoute;

    @Bean
    @Primary
    public AggHttpClient aggHttpClient() {
        return AggHttpClient.builder()
                .connectTimeout(connectTimeout)
                .socketTimeout(socketTimeout)
                .connTimeToLive(connTimeToLive)
                .maxTotal(maxTotal)
                .maxPerRoute(maxPerRoute)
                .build();
    }

}
