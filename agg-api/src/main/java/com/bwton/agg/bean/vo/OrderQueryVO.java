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
public class OrderQueryVO extends BaseVO implements Serializable {
    @SerializedName("trade_state")
    private String tradeState;
    @SerializedName("trade_type")
    private String tradeType;
    private String appid;
    private String openid;
    @SerializedName("is_subscribe")
    private String isSubscribe;
    @SerializedName("transaction_id")
    private String transactionId;
    @SerializedName("out_transaction_id")
    private String outTransactionId;
    @SerializedName("out_trade_no")
    private String outTradeNo;
    @SerializedName("total_fee")
    private String totalFee;
    @SerializedName("cash_fee")
    private String cashFee;
    @SerializedName("coupon_fee")
    private String couponFee;
    @SerializedName("fee_type")
    private String feeType;
    private String attach;
    @SerializedName("bank_type")
    private String bankType;
    @SerializedName("bank_billno")
    private String bankBillno;
    @SerializedName("time_end")
    private String timeEnd;
    @SerializedName("settle_key")
    private String settleKey;
}
