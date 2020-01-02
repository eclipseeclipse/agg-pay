package com.bwton.agg.bean.bo;

import lombok.Data;

/**
 * 渠道商户查询
 *
 * @author DengQiongChuan
 * @date 2019-12-12 11:03
 */
@Data
public class ChannelMerchantQueryBO {
    /** 渠道ID。{@link com.bwton.agg.enums.ChannelEnum} */
    private Integer channelId;

    /** 渠道商户号 */
    private String channelMerchantId;

    public ChannelMerchantQueryBO() {}

    public ChannelMerchantQueryBO(Integer channelId, String channelMerchantId) {
        this.channelId = channelId;
        this.channelMerchantId = channelMerchantId;
    }
}
