package com.bwton.agg.channel.pre.data.vo;

import lombok.Data;

/**
 * 银联条码支付响应对象。银联条码支付的其它接口响应对象可以继承本类。
 *
 * @author DengQiongChuan
 * @date 2019-12-04 14:55
 */
@Data
public class UnionBaseVO {
    /** 版本号 */
    private String version;
    /** 字符集 */
    private String charset;
    /** 签名方式 */
    private String sign_type;
    /** 授权交易机构 */
    private String sign_agentno;
    /** 连锁商户号 */
    private String groupno;
    /** 返回状态码 */
    private String status;
    /** 返回信息 */
    private String message;
    /** 网关返回码 */
    private String code;

    /*以下字段在 status 为 0的时候有返回*/

    /** 业务结果 */
    private String result_code;
    /** 商户号 */
    private String mch_id;
    /** 设备号 */
    private String device_info;
    /** 随机字符串 */
    private String nonce_str;
    /** 错误代码 */
    private String err_code;
    /** 错误代码描述 */
    private String err_msg;
    /** 签名 */
    private String sign;
}
