package com.bwton.agg.bean.bo;

import com.bwton.agg.annotations.AdapterCDATA;
import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * @author DengQiongChuan
 * @date 2019-12-05 16:59
 */
@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "xml")
public class BaseRespBO {
    /** 版本号 */
    @XmlJavaTypeAdapter(AdapterCDATA.class)
    private String version;
    /** 字符集 */
    @XmlJavaTypeAdapter(AdapterCDATA.class)
    private String charset;
    /** 签名方式 */
    @XmlJavaTypeAdapter(AdapterCDATA.class)
    @XmlElement(name = "sign_type")
    private String signType;
    /** 返回状态码 */
    @XmlJavaTypeAdapter(AdapterCDATA.class)
    private String status;
    /** 返回信息 */
    @XmlJavaTypeAdapter(AdapterCDATA.class)
    private String message;

    /*以下字段在 status 为 0 的时候有返回*/

    /** 业务结果 */
    @XmlElement(name = "result_code")
    @XmlJavaTypeAdapter(AdapterCDATA.class)
    private String resultCode;
    /** 商户号 */
    @XmlElement(name = "mch_id")
    @XmlJavaTypeAdapter(AdapterCDATA.class)
    private String mchId;
    /** 设备号 */
    @XmlElement(name = "device_info")
    @XmlJavaTypeAdapter(AdapterCDATA.class)
    private String deviceInfo;
    /** 随机字符串 */
    @XmlElement(name = "nonce_str")
    @XmlJavaTypeAdapter(AdapterCDATA.class)
    private String nonceStr;
    /** 错误代码 */
    @XmlElement(name = "err_code")
    @XmlJavaTypeAdapter(AdapterCDATA.class)
    private String errCode;
    /** 错误代码描述 */
    @XmlElement(name = "err_msg")
    @XmlJavaTypeAdapter(AdapterCDATA.class)
    private String errMsg;
    /** 签名 */
    @XmlJavaTypeAdapter(AdapterCDATA.class)
    private String sign;
}
