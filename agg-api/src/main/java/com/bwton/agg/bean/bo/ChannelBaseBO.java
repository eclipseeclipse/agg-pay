package com.bwton.agg.bean.bo;

import lombok.Data;

/**
 * @author DengQiongChuan
 * @date 2019-12-06 14:34
 */
@Data
public class ChannelBaseBO {

    /** 全局跟踪号 */
    private String globalSeq;

    /** 渠道ID。1-银联； */
    private Integer channelId;

    /** 渠道请求地址 */
    private String baseUrl;

    /** 签名方式 */
    private String signType;

    /** 聚合支付平台私钥 */
    private String platPriKey;

    /** 渠道公钥 */
    private String channelPubKey;

    /** 渠道商户号 */
    private String channelMerId;

    /** 安全密钥 */
    private String secureKey;

    /** 安全证书路径 */
    private String securePath;

    /** 前置分组 */
    private String channelGroup;

}
