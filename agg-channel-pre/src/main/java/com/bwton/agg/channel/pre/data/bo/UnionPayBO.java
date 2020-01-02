package com.bwton.agg.channel.pre.data.bo;

import lombok.Data;
import lombok.ToString;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 统一下单扫码支付（主扫）请求对象
 *
 * @author DengQiongChuan
 * @date 2019-12-04 14:55
 */
@Data
@ToString(callSuper = true)
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "xml")
public class UnionPayBO extends UnionBaseBO {
    /** 商户订单号 */
    private String out_trade_no;
    /** 设备号 */
    private String device_info;
    /** 门店编号 */
    private String op_shop_id;
    /** 商户及商品名称 */
    private String body;
    /** 附加信息 */
    private String attach;
    /** 公众账号ID */
    private String sub_appid;
    /** 总金额 */
    private Long total_fee;
    /** 电子发票 */
    private String need_receipt;
    /** 订单生成的机器 IP */
    private String mch_create_ip;
    /** 通知地址 */
    private String notify_url;
    /** 订单生成时间 */
    private String time_start;
    /** 订单超时时间 */
    private String time_expire;
    /** 收银员。操作员帐号,默认为商户号 */
    private String op_user_id;
    /** 商品标记 */
    private String goods_tag;
    /** 商品 ID */
    private String product_id;
    /** 是否限制信用卡 */
    private String limit_credit_pay;
}
