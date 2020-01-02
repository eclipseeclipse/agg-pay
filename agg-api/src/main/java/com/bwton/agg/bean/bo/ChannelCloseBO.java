package com.bwton.agg.bean.bo;

import lombok.Data;
import lombok.ToString;
import org.hibernate.validator.constraints.NotBlank;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author DengQiongChuan
 * @date 2019-12-06 14:34
 */
@Data
@ToString(callSuper = true)
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "xml")
public class ChannelCloseBO extends BaseBO {

    /**
     * 商户订单号
     */
    @NotBlank(message = "商户订单号不能为空")
    @XmlElement(name = "out_trade_no")
    private String outTradeNo;
}
