package com.bwton.agg.bean.bo;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @ClassName
 * @Description TODO
 * @Author wuhaonan@bwton.com
 * @Date
 */
@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "xml")
public class OrderQueryResultBO extends BaseRespBO {

    @XmlElement(name = "trade_state")
    private String tradeState;
    @XmlElement(name = "trade_type")
    private String tradeType;
    private String appid;
    private String openid;
    @XmlElement(name = "is_subscribe")
    private String isSubscribe;
    @XmlElement(name = "transaction_id")
    private String transactionId;
    @XmlElement(name = "out_transaction_id")
    private String outTransactionId;
    @XmlElement(name = "out_trade_no")
    private String outTradeNo;
    @XmlElement(name = "total_fee")
    private String totalFee;
    @XmlElement(name = "cash_fee")
    private String cashFee;
    @XmlElement(name = "coupon_fee")
    private String couponFee;
    @XmlElement(name = "fee_type")
    private String feeType;
    private String attach;
    @XmlElement(name = "bank_type")
    private String bankType;
    @XmlElement(name = "bank_billno")
    private String bankBillno;
    @XmlElement(name = "time_end")
    private String timeEnd;
    @XmlElement(name = "settle_key")
    private String settleKey;
}
