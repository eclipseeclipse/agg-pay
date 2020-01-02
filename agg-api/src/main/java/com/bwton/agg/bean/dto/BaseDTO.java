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
public class BaseDTO implements Serializable {
    @NotBlank(message = "接口类型不能为空")
    private String service;
    @NotBlank(message = "版本号不能为空")
    private String version;
    @NotBlank(message = "字符集不能为空")
    private String charset;
    @NotBlank(message = "签名不能为空")
    @SerializedName("sign_type")
    private String signType;
    @NotBlank(message = "商户号不能为空")
    @SerializedName("mch_id")
    private String mchId;
    @NotBlank(message = "随机字符串不能为空")
    @SerializedName("nonce_str")
    private String nonceStr;
    @NotBlank(message = "签名不能为空")
    private String sign;
    @SerializedName("device_info")
    private String deviceInfo;
}
