package com.bwton.agg.channel.pre.data.vo;

import lombok.Data;

@Data
public class UnionRefundQuerySingleVO {

    private String out_refund_no;
    /** 平台退款单号。refund_id_$n */
    private String refund_id;
    /** 第三方退款单号。out_refund_id_$n */
    private String out_refund_id;
    /** 退款渠道。ORIGINAL—原路退款，默认。refund_channel_$n */
    private String refund_channel;
    /** 退款金额。退款总金额,单位为分,可以做部分退款。refund_fee_$n */
    private String refund_fee;
    /**
     * 现金券退款金额。coupon_refund_fee_$n
     * 现金券退款金额 <= 退款金额， 退款金额-现金券退款金额为现金,单位为分
     */
    private String coupon_refund_fee;
    /**
     * 免充值优惠金额。mdiscount_$n
     * 免充值优惠金额 + 充值优惠金额=现金劵退款金额（coupon_refund_fee）,单位为分
     */
    private String mdiscount;
    /** 退款时间，yyyyMMddHHmmss。refund_time_$n */
    private String refund_time;
    /**
     * 退款状态。（退款结果以此字段为准）。refund_status_$n
     * SUCCESS—退款成功
     * FAIL—退款失败
     * PROCESSING—退款处理中
     * NOTSURE—未确定， 需要商户原退款单号重新发起
     * CHANGE—转入代发，退款到银行发现用户的卡作废或者冻结了，导致原路退款银行卡失败，资金回流到商户的现金帐号，需要商户人工干预，通过线下或者平台转账的方式进行退款。
     */
    private String refund_status;
    /** 银联交易主键。仅银联二维码交易有该字段信息。settle_key_$n */
    private String settle_key;
}
