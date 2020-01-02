package com.bwton.agg.channel.pre.util;

import com.bwton.agg.channel.pre.service.IChannelPreService;
import com.bwton.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @ClassName
 * @Description TODO
 * @Author wuhaonan@bwton.com
 * @Date
 */
@Component
public class ChannelPreUtils {
    @Autowired
    private Map<String, IChannelPreService> iChannelPreServiceMap = new ConcurrentHashMap<>();

    public IChannelPreService getChannelPreService(String channelGroup){
        IChannelPreService iChannelPreService = iChannelPreServiceMap.get(channelGroup);
        if(iChannelPreService == null){
            throw new BusinessException("渠道信息不存在");
        }
        return iChannelPreService;
    }
}
