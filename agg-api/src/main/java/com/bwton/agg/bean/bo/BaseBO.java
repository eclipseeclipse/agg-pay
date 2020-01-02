package com.bwton.agg.bean.bo;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import javax.xml.bind.annotation.XmlElement;

/**
 * @ClassName
 * @Description TODO
 * @Author wuhaonan@bwton.com
 * @Date
 */
@Data
public class BaseBO {
    private String globalSeq;
    /** 接口类型 */
    private String service;
    /** 版本号 */
    private String version;
    /** 字符集 */
    private String charset;
    /** 签名方式 */
    @SerializedName("sign_type")
    @XmlElement(name = "sign_type")
    private String signType;
    /** 商户号 */
    @SerializedName("mch_id")
    private String mchId;
    /** 随机字符串 */
    @SerializedName("nonce_str")
    @XmlElement(name = "nonce_str")
    private String nonceStr;
    /** 签名 */
    private String sign;
    /** 授权交易机构 */
    @SerializedName("sign_agent_no")
    @XmlElement(name = "sign_agent_no")
    private String signAgentNo;
    /** 连锁商户号 */
    @SerializedName("group_no")
    @XmlElement(name = "group_no")
    private String groupNo;

    /** 渠道ID。1-银联； */
    private Integer channelId;
    /** 渠道请求地址 */
    private String baseUrl;
    /** 聚合支付平台私钥 */
    @SerializedName("plat_pri_key")
    private String platPriKey;
    /** 渠道公钥 */
    @SerializedName("channel_pub_key")
    private String channelPubKey;
    /** 渠道商户号 */
    @SerializedName("channel_mer_id")
    @XmlElement(name = "mch_id")
    private String channelMerId;
    /** 安全密钥 */
    @SerializedName("secure_key")
    private String secureKey;
    /** 安全证书路径 */
    @SerializedName("secure_path")
    private String securePath;

    /** 终端设备号 */
    @SerializedName("device_info")
    @XmlElement(name = "device_info")
    private String deviceInfo;
    @SerializedName("channel_group")
    private String channelGroup;
}
