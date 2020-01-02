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
public class ChannelPayBO extends BaseBO {
    /** 商户订单号 */
    @XmlElement(name = "out_trade_no")
    private String outTradeNo;
    /** 商户及商品名称 */
    private String body;
    /** 附加信息 */
    private String attach;
    /** 总金额 */
    @XmlElement(name = "total_fee")
    private Long totalFee;
    /** 订单生成的机器 IP */
    @XmlElement(name = "mch_create_ip")
    private String mchCreateIp;
    /** 通知地址 */
    @XmlElement(name = "notify_url")
    private String notifyUrl;
    /** 订单生成时间 */
    @XmlElement(name = "time_start")
    private String timeStart;
    /** 订单超时时间 */
    @XmlElement(name = "time_expire")
    private String timeExpire;
    /** 收银员。操作员帐号,默认为商户号 */
    @XmlElement(name = "time_expire")
    private String opUserId;
    /** 商品 ID */
    @XmlElement(name = "product_id")
    private String productId;
}
