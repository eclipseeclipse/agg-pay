package com.bwton.agg.bean.vo;

import lombok.Data;

/**
 * @author DengQiongChuan
 * @date 2019-12-05 15:32
 */
@Data
public class UnionPayVO {
    /** 二维码链接。商户可用此参数自定义去生成二维码后展示出来进行扫码支付 */
    private String codeUrl;
    /** 二维码图片。此参数的值即是根据code_url生成的可以扫码支付的二维码图片地址 */
    private String codeImgUrl;

    /** 渠道响应码 */
    private String channelCode;
    /** 渠道响应码描述 */
    private String channelMsg;
}
