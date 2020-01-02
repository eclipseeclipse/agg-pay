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
public class OrderRefundQueryDTO extends BaseDTO implements Serializable {
    @SerializedName("out_refund_no")
    private String outRefundNo;
    @SerializedName("refund_id")
    private String refundId;
}
