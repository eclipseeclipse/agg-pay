package com.bwton.agg.api;

import com.bwton.agg.bean.bo.*;
import com.bwton.agg.bean.vo.*;
import com.bwton.lang.Result;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @ClassName
 * @Description TODO
 * @Author wuhaonan@bwton.com
 * @Date
 */
@FeignClient("agg-channel-pre")
public interface ChannelPreRest {
    @RequestMapping(value = "/channel/place_order", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    Result<ChannelPayVO> placeOrder(ChannelPayBO channelPayBO);
}
