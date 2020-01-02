package com.bwton.agg.channel.pre.data.bo;

import lombok.Data;

/**
 * 银联条码支付请求对象。银联条码支付的其它接口请求对象可以继承本类。
 *
 * @author DengQiongChuan
 * @date 2019-12-04 14:55
 */
@Data
public class UnionBaseBO {
    /** 接口类型 */
    private String service;
    /** 版本号 */
    private String version;
    /** 字符集 */
    private String charset;
    /** 签名方式 */
    private String sign_type;
    /** 商户号 */
    private String mch_id;
    /** 随机字符串 */
    private String nonce_str;
    /** 签名 */
    private String sign;
    /** 授权交易机构 */
    private String sign_agentno;
    /** 连锁商户号 */
    private String groupno;
}
