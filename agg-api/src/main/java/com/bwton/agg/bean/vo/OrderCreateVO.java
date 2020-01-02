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
public class OrderCreateVO extends BaseVO implements Serializable {
    @SerializedName("device_info")
    private String deviceInfo;
    @SerializedName("code_url")
    private String codeUrl;
    @SerializedName("code_img_url")
    private String codeImgUrl;
}
