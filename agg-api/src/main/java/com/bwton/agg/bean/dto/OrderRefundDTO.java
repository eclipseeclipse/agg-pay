package com.bwton.agg.bean.dto;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;

/**
 * @ClassName
 * @Description TODO
 * @Author wuhaonan@bwton.com
 * @Date
 */
@Data
public class OrderRefundDTO extends BaseDTO implements Serializable {
    @SerializedName("out_trade_no")
    private String outTradeNo;
    @SerializedName("transaction_id")
    private String transactionId;
    @NotBlank(message = "商户退款订单号不能为空")
    @SerializedName("out_refund_no")
    private String outRefundNo;
    @NotBlank(message = "退款金额不能为空")
    @SerializedName("refund_fee")
    private String refundFee;
}
