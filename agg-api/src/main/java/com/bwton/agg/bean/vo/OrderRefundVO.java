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
public class OrderRefundVO extends BaseVO implements Serializable {
    private String transactionId;
    @SerializedName("out_trade_no")
    private String outTradeNo;
    private String outRefundNo;
    private String refundId;
    private String refundChannel;
    private String refundFee;
    private String couponRefundFee;
}
