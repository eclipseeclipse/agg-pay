package com.bwton.agg.bean.vo;

import lombok.Data;

/**
 * @author DengQiongChuan
 * @date 2019-12-06 14:34
 */
@Data
public class ChannelBaseVO {

    /** 全局跟踪号 */
    private String globalSeq;

    /** 渠道响应码 */
    private String outRespCode;

    /** 渠道响应码描述 */
    private String outRespDesc;

}
