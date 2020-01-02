package com.bwton.agg.bean.vo;

import com.bwton.agg.bean.bo.BaseRespBO;
import com.bwton.agg.bean.bo.ChannelBaseBO;
import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.ToString;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 描述: 关闭订单渠道返回类
 * @author wjl
 * @version v2.4.9
 * @created  2019/12/14
 */
@Data
@ToString(callSuper = true)
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "xml")
public class ChannelRefundVO extends ChannelBaseVO {

    @XmlElement(name = "transaction_id")
    private String transactionId;
    
    @XmlElement(name = "out_trade_no")
    private String outTradeNo;

    @XmlElement(name = "out_refund_no")
    private String outRefundNo;

    @XmlElement(name = "refund_id")
    private String refundId;

    @XmlElement(name = "refund_channel")
    private String refundChannel;

    @XmlElement(name = "refund_fee")
    private String refundFee;

    @XmlElement(name = "coupon_fefund_fee")
    private String couponRefundFee;

}
