package com.bwton.agg.common.configuration;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RMapCache;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.concurrent.TimeUnit;

/**
 * Redisson配置类
 *
 * @author zhuzh
 * @create 2019-04-15
 */
@Slf4j
@Configuration
public class RedissonConfig {

    @Value("${spring.redis.host}")
    private String host;

    @Value("${spring.redis.port}")
    private String port;

    @Value("${spring.redis.password}")
    private String password;

    @Value("${spring.redis.database}")
    private int database;

    @Value("${spring.redis.connectTimeout}")
    private int connectTimeout;

    private static RedissonClient redisson = null;

    @Bean
    @Primary
    public RedissonClient redissonClient() {
        Config config = new Config();
        // 若不设置密码，则密码设为null
        if(StringUtils.isBlank(password)) {
            password = null;
        }
        config.useSingleServer()
                //可以用"rediss://"来启用SSL连接
                .setAddress("redis://" + host + ":" + port)
                .setPassword(password)
                .setDatabase(database)
                .setConnectTimeout(connectTimeout);

        redisson = Redisson.create(config);
        return redisson;
    }

    private synchronized RedissonClient getRedisson() {
        if(null == redisson){
            redissonClient();
        }
        return redisson;
    }

    public RLock getLock(String key) {
        RedissonClient redissonClient = getRedisson();
        return redissonClient.getLock(key);
    }

    /**
     * 释放redis锁
     *
     * @param rLock
     */
    public void unLock(RLock rLock) {
        try {
            if (rLock != null && rLock.isHeldByCurrentThread()) {
                rLock.unlock();
                log.debug("释放缓存锁成功");
            }
        } catch (Exception e) {
            log.error("释放缓存锁失败", e);
        }
    }

    public void putCacheKeyVal(String cacheName, String key, String val, long time, TimeUnit timeUnit) {
        if (StringUtils.isEmpty(key) || StringUtils.isEmpty(val)) {
            log.warn("key || val 为空！！！！！！！key={},val={}", key, val);
            return;
        }
        try {
            //log.debug("开始存储到缓存！！！！！！！key={},val={}",key,val);
            RMapCache<String, String> mapCache = redisson.getMapCache(cacheName);
            mapCache.put(key, val, time, timeUnit);
        } catch (Exception e) {
            log.error("存储到缓存失败！！！！！", e);
        }
    }

    public String getCacheKeyVal(String cacheName, String key) {
        if (StringUtils.isEmpty(key)) {
            log.warn("key 为空！！！！！！！key={}", key);
            return "";
        }
        try {
            //log.debug("开始获取缓存数据！！！！！！！cacheName={},key={}",cacheName,key);
            RMapCache<String, String> mapCache = redisson.getMapCache(cacheName);
            String redisContent = mapCache.get(key);
            //log.debug("获取到的数据={}",redisContent);
            return redisContent;
        } catch (Exception e) {
            log.error("获取缓存数据失败！！！！！", e);
            return "";
        }
    }

    public void rmCacheKeyVal(String cacheName, String key) {
        if (StringUtils.isEmpty(key)) {
            log.warn("key 为空！！！！！！！key={}", key);
            return;
        }
        try {
            //log.debug("开始删除缓存数据！！！！！！！cacheName={},key={}",cacheName,key);
            RMapCache<String, String> mapCache = redisson.getMapCache(cacheName);
            mapCache.remove(key);
        } catch (Exception e) {
            log.error("删除缓存数据失败！！！！！", e);
        }
    }

}  