package com.bwton.agg.channel.pre.data.vo;

import lombok.Data;

/**
 * 查询订单响应对象
 *
 * @author DengQiongChuan
 * @date 2019-12-04 17:13
 */
@Data
public class UnionPayQueryVO extends UnionBaseVO {
    /**
     * 交易状态。（支付结果以此字段为准）
     * SUCCESS—支付成功；
     * REFUND—转入退款；
     * NOTPAY—未支付；
     * CLOSED—已关闭；
     * PAYERROR—支付失败(其他原因，如银行返回失败)
     */
    private String trade_state;
    /** 交易状态描述 */
    private String trade_state_desc;
    /**
     * 交易类型。
     * 支付宝：pay.alipay.jspay
     * 微信：pay.weixin.jspay
     * 银联：pay.unionpay.native
     */
    private String trade_type;
    /** 服务商公众号appid */
    private String appid;
    /** 用户标识。用户在服务商 appid 下的唯一标识 */
    private String openid;
    /** 是否关注公众账号。Y-关注，N-未关注 */
    private String is_subscribe;
    /** 平台订单号 */
    private String transaction_id;
    /** 第三方订单号（支付成功后会返回，没支付则不会） */
    private String out_transaction_id;
    /** 商户订单号 */
    private String out_trade_no;
    /** 总金额 */
    private Long total_fee;
    /** 现金券金额。现金券支付金额<=订单总金额， 订单总金额-现金券金额为现金支付金额，以分为单位 */
    private Long coupon_fee;
    /** 免充值优惠金额，以分为单位 */
    private Long mdiscount;
    /** 货币种类。货币类型，符合 ISO 4217 标准的三位字母代码，默认人民币：CNY */
    private String fee_type;
    /** 附加信息。商家数据包，原样返回 */
    private String attach;
    /** 付款银行 */
    private String bank_type;
    /** 银行订单号，若为微信支付则为空 */
    private String bank_billno;
    /** 支付完成时间，格式为yyyyMMddHHmmss。微信支付宝交易，该字段必返回；银联二维码交易可选返回 */
    private String time_end;
    /** 开票金额【支付宝】。用户在交易中支付的可开发票的金额 */
    private String invoice_amount;
    /** 买家支付宝账号【支付宝】   */
    private String buyer_logon_id;
    /** 买家付款金额【支付宝】。单位为元，两位小数。 */
    private String buyer_pay_amount;
    /** 买家支付宝用户ID【支付宝】 */
    private String buyer_user_id;
    /** 集分宝付款金额【支付宝】，单位为元，两位小数。 */
    private String point_amount;
    /** 实收金额【支付宝】，单位为元，两位小数。 */
    private String receipt_amount;
    /** 交易支付使用的资金渠道 【支付宝】。示例值{"amount":"0.01","fundChannel":"PCREDIT"} */
    private String fund_bill_list;
    /** 优惠详情【银联】。
     * 银联二维码交易返回{"couponInfo":[{"id":"1201201901265194","desc":"宁城农商行超市二维码满50减10","type":"CP01","spnsrId":"QRA194853315031","offstAmt":"1000"}]} */
    private String unionpay_discount;
    /** 优惠详情【微信】 */
    private String promotion_detail;
    /** 银联交易主键。仅银联二维码交易有该字段信息 */
    private String settle_key;
}
