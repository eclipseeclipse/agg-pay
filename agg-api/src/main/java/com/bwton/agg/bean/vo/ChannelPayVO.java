package com.bwton.agg.bean.vo;

import lombok.Data;
import lombok.ToString;

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
public class ChannelPayVO extends ChannelBaseVO {
    /** 二维码链接。商户可用此参数自定义去生成二维码后展示出来进行扫码支付 */
    @XmlElement(name = "code_url")
    private String codeUrl;
    /** 二维码图片。此参数的值即是根据code_url生成的可以扫码支付的二维码图片地址 */
    @XmlElement(name = "code_img_url")
    private String codeImgUrl;
}
