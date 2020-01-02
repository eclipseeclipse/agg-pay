package com.bwton.agg.trade.data.entity;

import lombok.Data;

import java.util.Date;

/**
 * @author DengQiongChuan
 * @date 2019-12-03 20:42
 */
@Data
public class TxChannelEntity {
    /** 交易流水号 */
    private String txSerialNo;
    /** 原交易流水号 */
    private String orgTxSerialNo;
    /** 平台交易流水号 */
    private String txTradeNo;
    /** 渠道交易时间。请求发渠道的时间 */
    private Date txChannelTime;
    /** 订单总金额 */
    private Long totalFee;
    /** 现金支付金额 */
    private Long cashFee;
    /** 现金券金额。现金券支付金额<=订单总金额，订单总金额-现金券金额为现金支付金额 */
    private Long couponFee;
    /** 费率 */
    private Float feeRate;
    /** 货币种类。人民币：CNY */
    private String feeType;
    /** 交易状态。1-未支付；2-成功；3-渠道处理中；4-转入退款；5-渠道未确定；6-已关闭；7-失败；8-转入代发，退款失败需线下退款；9-渠道前置未受理 */
    private Integer txStatus;
    /** 渠道ID。1-银联 */
    private Integer channelId;
    /** 渠道商户号 */
    private String channelMerId;
    /** 前置分组 */
    private String channelGroup;
    /** 外部渠道流水号 */
    private String outSerialNo;
    /** 外部渠道第三方流水号 */
    private String outThirdPartyNo;
    /** 外部渠道交易结束时间。yyyyMMddHHmmss */
    private String outEndTime;
    /** 外部渠道交易清算日期。yyyyMMdd */
    private String outSettleDate;
    /** 外部渠道返回码 */
    private String outRespCode;
    /** 外部渠道返回描述 */
    private String outRespDesc;
    /** 二维码链接 */
    private String codeUrl;
    /** 二维码图片URL */
    private String codeImgUrl;
    /**
     * 外部渠道交易类型。
     * 银联有以下交易类型：
     *   支付宝：pay.alipay.jspay
     *   微信：pay.weixin.jspay
     *   银联：pay.unionpay.native
     */
    private String outTradeType;
    /** 附加信息，即请求保留域，会原样返回商户 */
    private String reqReserved;
    /** 全局跟踪号 */
    private String globalSeq;
    /** 创建时间 */
    private Date gmtCreate;
    /** 更新时间 */
    private Date gmtModified;
    /** 交易类型。1-主扫支付，2-被扫支付 */
    private Integer tradeType;
}
