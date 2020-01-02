package com.bwton.agg.channel.pre.data.vo;

import com.bwton.agg.channel.pre.data.vo.UnionBaseVO;
import lombok.Data;

/**
 * 退款响应对象
 *
 * @author DengQiongChuan
 * @date 2019-12-04 16:39
 */
@Data
public class UnionRefundVO extends UnionBaseVO {
    /** 平台订单号 */
    private String transaction_id;
    /** 商户订单号 */
    private String out_trade_no;
    /** 商户退款单号 */
    private String out_refund_no;
    /** 平台退款单号 */
    private String refund_id;
    /** 退款渠道。ORIGINAL—原路退款，默认 */
    private String refund_channel;
    /** 退款金额。退款总金额,单位为分,可以做部分退款 */
    private String refund_fee;
    /** 现金券退款金额。现金券退款金额 <= 退款金额， 退款金额-现金券退款金额为现金,单位为分 */
    private String coupon_refund_fee;
    /** 第三方订单号 */
    private String out_transaction_id;
    /**
     * 交易类型。
     * 支付宝：pay.alipay.jspay
     * 微信：pay.weixin.jspay
     * 银联：pay.unionpay.native
     */
    private String trade_type;
}
