package com.bwton.agg.bean.vo;

import lombok.Data;
import lombok.ToString;

/**
 * @author DengQiongChuan
 * @date 2019-12-06 14:34
 */
@Data
@ToString(callSuper = true)
public class ChannelQueryVO extends ChannelBaseVO {

    /** 渠道交易表交易状态(tx_channel.tx_status) */
    private Integer txStatus;

    /**
     * 交易类型。
     * 支付宝：pay.alipay.jspay
     * 微信：pay.weixin.jspay
     * 银联：pay.unionpay.native
     */
    private String outTradeType;

    /** 渠道订单号 */
    private String outSerialNo;

    /** 渠道第三方订单号（支付成功后会返回，没支付则不会） */
    private String outThirdPartyNo;

    /** 聚合支付平台订单号 */
    private String txSerialNo;

    /** 总金额 */
    private Long totalFee;

    /** 现金支付金额 */
    private Long cashFee;

    /** 现金券金额。现金券支付金额<=订单总金额， 订单总金额-现金券金额为现金支付金额，以分为单位 */
    private Long couponFee;

    /** 货币种类。货币类型，符合 ISO 4217 标准的三位字母代码，默认人民币：CNY */
    private String feeType;

    /** 支付完成时间，格式为yyyyMMddHHmmss */
    private String outEndTime;

    /** 交易清算日期。yyyyMMdd */
    private String outSettleDate;
}
