package com.bwton.agg.merchant.data.entity;

import lombok.Data;

import java.util.Date;

/**
 * @author DengQiongChuan
 * @date 2019-12-03 20:43
 */
@Data
public class MerchantRouteEntity {
    private Integer id;
    /** 商户ID */
    private String merchantId;
    /** channel_merchant表主键 */
    private Integer channelMerchantId;
    /** 状态。1-生效；0-失效 */
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
