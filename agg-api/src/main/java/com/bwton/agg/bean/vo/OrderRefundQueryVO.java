package com.bwton.agg.bean.vo;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName
 * @Description TODO
 * @Author wuhaonan@bwton.com
 * @Date
 */
@Data
public class OrderRefundQueryVO extends BaseVO implements Serializable {
    @SerializedName("transaction_id")
    private String transactionId;
    @SerializedName("out_trade_no")
    private String outTradeNo;
    @SerializedName("out_refund_no")
    private String outRefundNo;
    @SerializedName("refund_id")
    private String refundId;
    @SerializedName("refund_channel")
    private String refundChannel;
    @SerializedName("refund_fee")
    private String refundFee;
    @SerializedName("coupon_refund_fee")
    private String couponRefundFee;
    @SerializedName("refund_time")
    private String refundTime;
    @SerializedName("refund_status")
    private String refundStatus;
    @SerializedName("settle_key")
    private String settleKey;
}
