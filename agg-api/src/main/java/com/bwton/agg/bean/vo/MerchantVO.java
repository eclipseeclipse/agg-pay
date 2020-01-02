package com.bwton.agg.bean.vo;

import lombok.Data;

import java.util.Date;

/**
 * @ClassName
 * @Description TODO
 * @Author wuhaonan@bwton.com
 * @Date
 */
@Data
public class MerchantVO {
    /** 主键 */
    private Integer id;
    /** 渠道ID。1-银联； */
    private Integer channelId;
    /** 渠道商户号 */
    private String channelMerId;
    /** 费率 */
    private Float feeRate;
    /** 安全密钥 */
    private String secureKey;
    /** 安全证书路径 */
    private String securePath;
    /** 区域代码 */
    private String region;
    /** 状态。1-生效；0-失效； */
    private Integer status;
    /** 渠道请求地址 */
    private String baseUrl;
    /** 签名类型。MD5、RSA、RSA2等。 */
    private String signType;
    /** 支付宝开放平台应用ID；微信公众号APPID */
    private String appId;
    /** 聚合支付平台私钥 */
    private String platPriKey;
    /** 聚合支付平台公钥 */
    private String platPubKey;
    /** 渠道公钥 */
    private String channelPubKey;
    /** 渠道商户名称 */
    private String channelMerName;
    /** 渠道商户简称 */
    private String channelMerAbbr;
    /** 备注 */
    private String remark;
    /** 创建人 */
    private Integer createUser;
    /** 更新人 */
    private Integer modifiedUser;
    /** 创建时间 */
    private Date gmtCreate;
    /** 更新时间 */
    private Date gmtModified;
}
