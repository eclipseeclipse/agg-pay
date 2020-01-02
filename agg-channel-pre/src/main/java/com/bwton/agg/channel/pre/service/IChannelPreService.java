package com.bwton.agg.channel.pre.service;

import com.bwton.agg.bean.bo.*;
import com.bwton.agg.bean.vo.*;
import com.bwton.lang.Result;

/**
 * @ClassName
 * @Description TODO
 * @Author wuhaonan@bwton.com
 * @Date
 */
public interface IChannelPreService {
    Result<ChannelPayVO> pay(ChannelPayBO channelPayBO);

    Result<ChannelQueryVO> query(ChannelQueryBO queryBO);

    Result<ChannelCloseVO> close(ChannelCloseBO channelCloseBO);

    Result<ChannelRefundVO> refund(ChannelRefundBO channelRefundBO);

    Result<ChannelRefundQueryVO> refundQuery(ChannelRefundQueryBO channelRefundQueryBO);
}
