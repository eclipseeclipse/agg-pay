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
public class OrderCloseDTO extends BaseDTO implements Serializable {
    @NotBlank(message = "商户订单号不能为空")
    @SerializedName("out_trade_no")
    private String outTradeNo;
}
