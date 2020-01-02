package com.bwton.agg.bean.bo;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * @author DengQiongChuan
 * @date 2019-12-05 16:59
 */
@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class BaseReqBO {
    /** 全局跟踪号 */
    private String globalSeq;

    /** 接口类型 */
    @NotBlank(message = "接口类型不能为空")
    private String service;
    /** 版本号 */
    @NotBlank(message = "版本信息不能为空")
    private String version;
    /** 字符集 */
    @NotBlank(message = "字符集不能为空")
    private String charset;
    /** 签名方式 */
    @XmlElement(name = "sign_type")
    @NotBlank(message = "签名类型不能为空")
    private String signType;
    /** 商户号 */
    @XmlElement(name = "mch_id")
    @NotBlank(message = "商户号不能为空")
    private String mchId;
    /** 随机字符串 */
    @XmlElement(name = "nonce_str")
    @NotBlank(message = "随机字符串不能为空")
    private String nonceStr;
    /** 签名 */
    @NotBlank(message = "签名不能为空")
    private String sign;
    /** 设备号 */
    @XmlElement(name = "device_info")
    private String deviceInfo;
}
