package com.bwton.agg.bean.bo;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 描述: 
 * @author wjl
 * @version v2.4.9
 * @created  2019/12/14 
 */
@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "xml")
public class OrderCloseResultBO extends BaseRespBO {

}
