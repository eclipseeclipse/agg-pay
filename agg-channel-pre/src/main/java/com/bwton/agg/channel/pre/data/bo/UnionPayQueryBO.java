package com.bwton.agg.channel.pre.data.bo;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 查询订单请求对象
 *
 * @author DengQiongChuan
 * @date 2019-12-04 17:13
 */
@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "xml")
public class UnionPayQueryBO extends UnionBaseBO {
    /** 商户订单号 */
    private String out_trade_no;
    /** 平台订单号。out_trade_no和transaction_id至少一个必填，同时存在时transaction_id优先。 */
    private String transaction_id;
}
