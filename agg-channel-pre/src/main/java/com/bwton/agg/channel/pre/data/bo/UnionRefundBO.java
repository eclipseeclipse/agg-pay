package com.bwton.agg.channel.pre.data.bo;

import com.bwton.agg.channel.pre.data.bo.UnionBaseBO;
import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 退款请求对象
 *
 * @author DengQiongChuan
 * @date 2019-12-04 14:55
 */
@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "xml")
public class UnionRefundBO extends UnionBaseBO {
    /** 商户订单号 */
    private String out_trade_no;
    /** 平台订单号, out_trade_no和transaction_id至少一个必填，同时存在时transaction_id优先 */
    private String transaction_id;
    /**
     * 商户退款单号。
     * 同个退款单号多次请求，平台当一个单处理，只会退一次款。
     * 如果出现退款不成功，请采用原退款单号重新发起，避免出现重复退款。
     */
    private String out_refund_no;
    /** 总金额。订单总金额，单位为分 */
    private Long total_fee;
    /** 退款金额。退款总金额,单位为分,可以做部分退款 */
    private Long refund_fee;
    /** 收银员。操作员帐号,默认为商户号 */
    private String op_user_id;
    /** 退款渠道。ORIGINAL-原路退款，默认 */
    private String refund_channel;
}
