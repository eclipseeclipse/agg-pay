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
public class OrderCreateDTO extends BaseDTO implements Serializable {
    @NotBlank(message = "商户订单号不能为空")
    @SerializedName("out_trade_no")
    private String outTradeNo;
    @SerializedName("product_id")
    private String productId;
    @NotBlank(message = "商品描述不能为空")
    private String body;
    private String attach;
    @NotBlank(message = "总金额不能为空")
    @SerializedName("total_fee")
    private String totalFee;
    @NotBlank(message = "终端IP不能为空")
    @SerializedName("mch_create_ip")
    private String mchCreateIp;
    @SerializedName("notify_url")
    private String notifyUrl;
    @SerializedName("time_start")
    private String timeStart;
    @SerializedName("time_expire")
    private String timeExpire;
    @SerializedName("operation_date")
    private String operationDate;
    @SerializedName("op_user_id")
    private String opUserId;
}
