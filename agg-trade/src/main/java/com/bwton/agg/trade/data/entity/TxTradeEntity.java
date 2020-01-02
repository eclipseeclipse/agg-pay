package com.bwton.agg.trade.data.entity;

import lombok.Data;

import java.util.Date;

/**
 * @author DengQiongChuan
 * @date 2019-12-03 20:42
 */
@Data
public class TxTradeEntity {
    /** 交易流水号 */
    private String txTradeNo;
    /** 原交易流水号 */
    private String orgTxTradeNo;
    /** 聚合支付平台商户号 */
    private String merchantId;
    /** 设备号 */
    private String deviceNo;
    /** 商户订单号 */
    private String merOrderNo;
    /** 交易类型。1-主扫支付，2-被扫支付 */
    private Integer tradeType;
    /** 订单总金额 */
    private Long totalFee;
    /** 现金支付金额 */
    private Long cashFee;
    /** 现金券金额。现金券支付金额<=订单总金额，订单总金额-现金券金额为现金支付金额 */
    private Long couponFee;
    /** 货币种类。人民币：CNY */
    private String feeType;
    /** 商户订单生成时间。yyyyMMddHHmmss */
    private String merTxTime;
    /** 商户订单超时时间。yyyyMMddHHmmss */
    private String merTimeExpire;
    /** 商户订单描述 */
    private String merOrderDesc;
    /** 交易状态。1-未支付；2-成功；3-处理中；4-转入退款；5-未确定；6-已关闭；7-失败；8-转入代发，退款失败需线下退款。 */
    private Integer txStatus;
    /** 商户运营日期。yyyyMMdd */
    private String operationDate;
    /** 商户商品ID */
    private String productId;
    /** 商户订单生成的机器IP */
    private String merchantIp;
    /** 商户回调通知地址 */
    private String notifyUrl;
    /** 附加信息，即请求保留域，会原样返回商户 */
    private String reqReserved;
    /** 二维码链接 */
    private String codeUrl;
    /** 二维码图片URL */
    private String codeImgUrl;
    /** 全局跟踪号 */
    private String globalSeq;
    /** 创建时间 */
    private Date gmtCreate;
    /** 更新时间 */
    private Date gmtModified;
}
