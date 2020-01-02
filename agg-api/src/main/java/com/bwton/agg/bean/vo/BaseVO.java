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
public class BaseVO implements Serializable {
    private String version;
    private String charset;
    private String status;
    @SerializedName("err_code")
    private String errCode;
    @SerializedName("err_msg")
    private String errMsg;
    private String sign;
    @SerializedName("nonce_str")
    private String nonceStr;
    private String message;
    @SerializedName("result_code")
    private String resultCode;
    @SerializedName("mch_id")
    private String mchId;
}
