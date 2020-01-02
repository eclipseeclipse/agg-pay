package com.bwton.agg.bean.bo;

import lombok.Data;
import lombok.ToString;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.validation.annotation.Validated;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @ClassName
 * @Description TODO
 * @Author wuhaonan@bwton.com
 * @Date
 */
@Data
@ToString(callSuper = true)
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "xml")
@Validated
public class OrderCloseBO extends BaseReqBO {

    /**
     * 商户订单号
     */
    @NotBlank(message = "商户订单号不能为空")
    @XmlElement(name = "out_trade_no")
    private String outTradeNo;
}
