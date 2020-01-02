package com.bwton.agg.bean.bo;

import com.bwton.agg.annotations.AdapterCDATA;
import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * @author DengQiongChuan
 * @date 2019-12-05 16:59
 */
@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "xml")
public class OrderPayResultBO extends BaseRespBO {
    /** 二维码链接 */
    @XmlElement(name = "code_url")
    @XmlJavaTypeAdapter(AdapterCDATA.class)
    private String codeUrl;

    /** 二维码图片URL */
    @XmlElement(name = "code_img_url")
    @XmlJavaTypeAdapter(AdapterCDATA.class)
    private String codeImgUrl;
}
