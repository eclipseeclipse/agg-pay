package com.bwton.agg.merchant.data.entity;

import lombok.Data;

import java.util.Date;

/**
 * @author DengQiongChuan
 * @date 2019-12-03 20:43
 */
@Data
public class DeviceInfoEntity {
    /** 主键 */
    private Integer id;
    /** 设备编号 */
    private String no;
    /** 商户号 */
    private String merchantId;
    /** 设备类型 */
    private Integer type;
    /** 线路。地铁线路 */
    private String lineNo;
    /** 站点编号 */
    private String siteNo;
    /** 站点名称 */
    private String siteName;
    /** 状态。1-生效；0-失效； */
    private Integer status;
    /** 创建人 */
    private Integer createUser;
    /** 更新人 */
    private Integer modifiedUser;
    /** 创建时间 */
    private Date gmtCreate;
    /** 更新时间 */
    private Date gmtModified;
}
