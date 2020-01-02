package com.bwton.agg.bean.vo;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.ToString;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 描述: 查询退款渠道返回类
 * @author wjl
 * @version v2.4.9
 * @created  2019/12/14
 */
@Data
@ToString(callSuper = true)
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "xml")
public class ChannelRefundSingleQueryVO{
    @SerializedName("out_refund_no")
    private String outRefundNo;
    @SerializedName("refund_id")
    private String refundId;
    @SerializedName("out_refund_id")
    private String outRefundId;
    @SerializedName("refund_channel")
    private String refundChannel;
    @SerializedName("refund_fee")
    private String refundFee;
    @SerializedName("coupon_refund_fee")
    private String couponRefundFee;
    @SerializedName("mdiscount")
    private String mdiscount;
    @SerializedName("refund_time")
    private String refundTime;
    @SerializedName("refund_status")
    private String refundStatus;
    @SerializedName("settle_key")
    private String settleKey;

}
