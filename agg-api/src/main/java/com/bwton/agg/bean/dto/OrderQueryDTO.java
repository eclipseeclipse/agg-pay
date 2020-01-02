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
public class OrderQueryDTO extends BaseDTO implements Serializable {
    @SerializedName("transaction_id")
    private String transactionId;
    @SerializedName("out_trade_no")
    private String outTradeNo;
}
