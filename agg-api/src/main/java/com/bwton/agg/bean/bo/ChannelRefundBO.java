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
public class ChannelRefundBO extends BaseBO {

    /** 商户订单号 */
    @XmlElement(name = "out_trade_no")
    private String outTradeNo;

    /** 平台订单号, out_trade_no和transaction_id至少一个必填，同时存在时transaction_id优先 */
    @XmlElement(name = "transaction_id")
    private String transactionId;

    /**
     * 商户退款单号。
     * 同个退款单号多次请求，平台当一个单处理，只会退一次款。
     * 如果出现退款不成功，请采用原退款单号重新发起，避免出现重复退款。
     */
    @XmlElement(name = "out_refund_no")
    private String outRefundNo;

    /** 总金额。订单总金额，单位为分 */
    @XmlElement(name = "total_fee")
    private Long totalFee;

    /** 退款金额。退款总金额,单位为分,可以做部分退款 */
    @XmlElement(name = "refund_fee")
    private Long refundFee;

    /** 收银员。操作员帐号,默认为商户号 */
    @XmlElement(name = "op_user_id")
    private String opUserId;

    /** 退款渠道。ORIGINAL-原路退款，默认 */
    @XmlElement(name = "refund_channel")
    private String refundChannel;
}
