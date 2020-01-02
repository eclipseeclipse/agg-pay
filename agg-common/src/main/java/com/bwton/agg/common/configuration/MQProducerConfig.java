package com.bwton.agg.common.configuration;

import com.bwton.mq.rocketmq.RocketMQProducerFactoryBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author yangqing
 * @Description: MQ配置类
 * @create 2019/4/1610:42 AM
 */
//@Configuration
public class MQProducerConfig {
//    @Value("${rocketmq.producerGroup}")
    private String rocketmqProducerGroup;

//    @Value("${rocketmq.namesrvAddr}")
    private String rocketmqNamesrvAddr;

//    @Value("${rocketmq.vipChannelEnabled}")
    private boolean rocketmqVipChannelEnabled;

//    @Bean
    public RocketMQProducerFactoryBean rocketMQProducerFactoryBean(){
        RocketMQProducerFactoryBean rocketMQProducerFactoryBean = new RocketMQProducerFactoryBean();
        rocketMQProducerFactoryBean.setProducerGroup(rocketmqProducerGroup);
        rocketMQProducerFactoryBean.setNamesrvAddr(rocketmqNamesrvAddr);
        rocketMQProducerFactoryBean.setVipChannelEnabled(rocketmqVipChannelEnabled);

        return rocketMQProducerFactoryBean;
    }
}
