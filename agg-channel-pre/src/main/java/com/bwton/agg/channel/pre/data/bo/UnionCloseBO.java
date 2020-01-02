package com.bwton.agg.channel.pre.data.bo;

import com.bwton.agg.channel.pre.data.bo.UnionBaseBO;
import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 关闭订单请求对象
 *
 * @author DengQiongChuan
 * @date 2019-12-04 14:55
 */
@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "xml")
public class UnionCloseBO extends UnionBaseBO {
    /** 商户订单号 */
    private String out_trade_no;
}
