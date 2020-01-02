package com.bwton.agg.bean.vo;

import lombok.Data;

/**
 * 交易路由
 *
 * @author DengQiongChuan
 * @date 2019-12-06 10:37
 */
@Data
public class TradeRouteVO {
    /* 商户信息 */
    /** 聚合支付平台商户号。前4位地区码 + 12位序号 */
    private String merchantId;
    /** 业务类型。1-扫码支付；2-被扫支付；3-扫码与被扫支付 */
    private Integer businessType;
    /** 订单有效时间。单位：分钟。主扫默认5分钟。 */
    private Integer orderValidTime;
    /** 单笔交易限额。单位：分 */
    private Long singleLimit;
    /** 日累计限额。单位：分 */
    private Long dayLimit;
    /** 是否限制信用卡。1-限制；0-不限制 */
    private Integer creditCardLimit;

    /* 渠道商户信息 */
    /** 渠道ID。1-银联； */
    private Integer channelId;
    /** 渠道商户号 */
    private String channelMerId;
    /** 费率 */
    private Float feeRate;
    /** 安全密钥 */
    private String secureKey;
    /** 安全证书路径 */
    private String securePath;
    /** 区域代码 */
    private String region;
    /** 渠道请求地址 */
    private String baseUrl;
    /** 签名类型。MD5、RSA、RSA2等。 */
    private String signType;
    /** 聚合支付平台私钥 */
    private String platPriKey;
    /** 聚合支付平台公钥 */
    private String platPubKey;
    /** 渠道公钥 */
    private String channelPubKey;
}
