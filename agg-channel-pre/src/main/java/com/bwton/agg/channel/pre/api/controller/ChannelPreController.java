package com.bwton.agg.channel.pre.api.controller;

import com.bwton.agg.bean.bo.ChannelCloseBO;
import com.bwton.agg.bean.bo.ChannelPayBO;
import com.bwton.agg.bean.bo.ChannelQueryBO;
import com.bwton.agg.bean.bo.ChannelRefundBO;
import com.bwton.agg.bean.bo.ChannelRefundQueryBO;
import com.bwton.agg.bean.vo.ChannelCloseVO;
import com.bwton.agg.bean.vo.ChannelPayVO;
import com.bwton.agg.bean.vo.ChannelQueryVO;
import com.bwton.agg.bean.vo.ChannelRefundQueryVO;
import com.bwton.agg.bean.vo.ChannelRefundVO;
import com.bwton.agg.channel.pre.service.IChannelPreService;
import com.bwton.agg.channel.pre.util.ChannelPreUtils;
import com.bwton.lang.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


/**
 * @ClassName
 * @Description TODO
 * @Author wuhaonan@bwton.com
 * @Date
 */
@RestController
public class ChannelPreController {
    @Autowired
    ChannelPreUtils channelPreUtil;

    @RequestMapping(value = "/channel/place_order", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Result<ChannelPayVO> placeOrder(@RequestBody ChannelPayBO channelPayBO) {
        IChannelPreService iChannelPreService = channelPreUtil.getChannelPreService(channelPayBO.getChannelGroup());
        return iChannelPreService.pay(channelPayBO);
    }

    @RequestMapping(value = "/channel/query", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Result<ChannelQueryVO> query(@RequestBody ChannelQueryBO channelQueryBO) {
        IChannelPreService iChannelPreService = channelPreUtil.getChannelPreService(channelQueryBO.getChannelGroup());
        return iChannelPreService.query(channelQueryBO);
    }

    @RequestMapping(value = "/channel/close", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Result<ChannelCloseVO> close(@RequestBody ChannelCloseBO channelCloseBO) {
        IChannelPreService iChannelPreService = channelPreUtil.getChannelPreService(channelCloseBO.getChannelGroup());
       return iChannelPreService.close(channelCloseBO);
    }

    @RequestMapping(value = "/channel/refund", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Result<ChannelRefundVO> refund(@RequestBody ChannelRefundBO channelRefundBO) {
        IChannelPreService iChannelPreService = channelPreUtil.getChannelPreService(channelRefundBO.getChannelGroup());
        return iChannelPreService.refund(channelRefundBO);
    }

    @RequestMapping(value = "/channel/refund/query", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Result<ChannelRefundQueryVO> refundQuery(@RequestBody ChannelRefundQueryBO channelRefundQueryBO) {
        IChannelPreService iChannelPreService = channelPreUtil.getChannelPreService(channelRefundQueryBO.getChannelGroup());
        return iChannelPreService.refundQuery(channelRefundQueryBO);
    }
}
