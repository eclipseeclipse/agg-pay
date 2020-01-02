package com.bwton.agg.common.data.vo;

import lombok.Data;

/**
 * @author DengQiongChuan
 * @date 2019-12-11 17:08
 */
@Data
public class UnionNotifyVO {
    /** 版本号 */
    private String version;
    /** 字符集 */
    private String charset;
    /** 签名方式 */
    private String sign_type;
    /** 授权交易机构 */
    private String sign_agentno;
    /** 连锁商户号 */
    private String groupno;
    /** 返回状态码 */
    private String status;
    /** 返回信息 */
    private String message;
    /** 网关返回码 */
    private String code;

    /*以下字段在 status 为 0的时候有返回*/

    /** 业务结果 */
    private String result_code;
    /** 商户号 */
    private String mch_id;
    /** 设备号 */
    private String device_info;
    /** 随机字符串 */
    private String nonce_str;
    /** 错误代码 */
    private String err_code;
    /** 错误代码描述 */
    private String err_msg;
    /** 签名 */
    private String sign;

    /*以下字段在 status 和 result_code 都为 0的时候有返回*/

    /** 商户appid。受理商户appid */
    private String appid;
    /** 用户标识。用户在服务商 appid 下的唯一标识 */
    private String openid;
    /**
     * 交易类型。
     * 支付宝：pay.alipay.jspay
     * 微信：pay.weixin.jspay
     * 银联：pay.unionpay.native
     */
    private String trade_type;
    /** 是否关注公众账号。用户是否关注服务商公众账号，Y-关注，N-未关注 */
    private String is_subscribe;
    /** 支付结果。0—成功；其它—失败。支付结果以此字段为准 */
    private String pay_result;
    /** 支付结果信息，支付成功时为空*/
    private String pay_info;
    /** 平台订单号 */
    private String transaction_id;
    /** 第三方订单号 */
    private String out_transaction_id;
    /** 用户是否关注子公众账号，Y-关注，N-未关注 */
    private String sub_is_subscribe;
    /** 商户公众号appid */
    private String sub_appid;
    /** 用户openid。用户在商户公众号appid下的唯一标识 */
    private String sub_openid;
    /** 商户订单号 */
    private String out_trade_no;
    /** 总金额，以分为单位 */
    private Long total_fee;
    /** 现金支付金额 */
    private Long cash_fee;
    /** 现金券金额。现金券支付金额<=订单总金额， 订单总金额-现金券金额为现金支付金额，以分为单位 */
    private Long coupon_fee;
    /** 免充值优惠金额，以分为单位 */
    private Long mdiscount;
    /** 优惠详情【微信】 */
    private String promotion_detail;
    /** 交易支付使用的资金渠道 【支付宝】。示例值{"amount":"0.01","fundChannel":"PCREDIT"} */
    private String fund_bill_list;
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
    /** 买家支付宝账号【支付宝】   */
    private String buyer_logon_id;
    /** 买家支付宝用户ID【支付宝】 */
    private String buyer_user_id;
    /** 触发通知时间。支付宝触发通知时间 */
    private String gmt_create;
    /** 银联交易主键。仅银联二维码交易有该字段信息 */
    private String settle_key;
}
