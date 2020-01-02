package com.bwton.agg.bean.bo;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 描述: 
 * @author wjl
 * @version v2.4.9
 * @created  2019/12/14 
 */
@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "xml")
public class OrderRefundQueryResultBO extends BaseRespBO {
    @XmlElement(name = "transaction_id")
    private String transactionId;
    @XmlElement(name = "out_trade_no")
    private String outTradeNo;
    @XmlElement(name = "out_refund_no")
    private String outRefundNo;
    @XmlElement(name = "refund_id")
    private String refundId;
    @XmlElement(name = "refund_channel")
    private String refundChannel;
    @XmlElement(name = "refund_fee")
    private String refundFee;
    @XmlElement(name = "coupon_refund_fee")
    private String couponRefundFee;
    @XmlElement(name = "refund_time")
    private String refundTime;
    @XmlElement(name = "refund_status")
    private String refundStatus;
    @XmlElement(name = "settle_key")
    private String settleKey;

}
