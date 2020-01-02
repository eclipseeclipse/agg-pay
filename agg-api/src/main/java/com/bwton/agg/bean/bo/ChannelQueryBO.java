package com.bwton.agg.bean.bo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author DengQiongChuan
 * @date 2019-12-06 14:34
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class ChannelQueryBO extends ChannelBaseBO {

    /** 渠道交易表流水号 */
    private String txSerialNo;

    /** 外部渠道流水号。银联或其它渠道返回的流水号 */
    private String outSerialNo;

}
