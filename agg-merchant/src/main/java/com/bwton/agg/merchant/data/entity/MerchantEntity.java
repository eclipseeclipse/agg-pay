package com.bwton.agg.merchant.data.entity;

import lombok.Data;

import java.util.Date;

/**
 * @author DengQiongChuan
 * @date 2019-12-03 20:43
 */
@Data
public class MerchantEntity {
    /** 商户号。前4位地区码 + 12位序号 */
    private String id;
    /** 商户名称 */
    private String name;
    /** 商户简称 */
    private String abbr;
    /** 状态。1-可用；0-初始；2-暂停；3-新增待审核；4-变更待审核；5-注销待审核；9-销户 */
    private Integer status;
    /** 省份。地址表主键 */
    private String province;
    /** 城市。地址表主键 */
    private String city;
    /** 业务类型。1-扫码支付；2-被扫支付；3-扫码与被扫支付 */
    private Integer businessType;
    /** 订单有效时间。单位：分钟。主扫默认5分钟。 */
    private Integer orderValidTime;
    /** 单笔交易限额。单位：分 */
    private Long singleLimit;
    /** 日累计限额。单位：分 */
    private Long dayLimit;
    /** 是否维护设备信息。1-维护；0-不维护 */
    private Integer hasDevice;
    /** 是否限制信用卡。1-限制；0-不限制 */
    private Integer creditCardLimit;
    /** 密钥 */
    private String secureKey;
    /** 签名方式。MD5 */
    private String signType;
    /** 对账模式。1-TVM对账；0-非TVM对账 */
    private Integer reconciliationType;
    /** 是否上送对账文件。1-上送；0-不上送 */
    private Integer uploadReconFile;
    /** 对账文件上传路径 */
    private String uploadReconUrl;
    /** 创建人 */
    private Integer createUser;
    /** 更新人 */
    private Integer modifiedUser;
    /** 创建时间 */
    private Date gmtCreate;
    /** 更新时间 */
    private Date gmtModified;
}
