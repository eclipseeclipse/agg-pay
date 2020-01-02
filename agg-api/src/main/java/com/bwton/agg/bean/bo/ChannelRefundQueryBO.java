package com.bwton.agg.bean.bo;

import lombok.Data;
import lombok.ToString;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author DengQiongChuan
 * @date 2019-12-06 14:34
 */
@Data
@ToString(callSuper = true)
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "xml")
public class ChannelRefundQueryBO extends BaseBO {

    /** 商户订单号 */
    @XmlElement(name = "out_trade_no")
    private String outTradeNo;

    /** 平台订单号。out_trade_no和transaction_id至少一个必填，同时存在时transaction_id优先。 */
    @XmlElement(name = "transaction_id")
    private String transactionId;

    /** 商户退款单号 */
    @XmlElement(name = "out_refund_no")
    private String outRefundNo;

    /**
     * 平台退款单号。
     * 平台退款单号refund_id、out_refund_no、out_trade_no 、transaction_id 四个参数必填一个，
     * 如果同时存在优先级为：refund_id>out_refund_no>transaction_id>out_trade_no
     */
    @XmlElement(name = "refund_id")
    private String refundId;
}
