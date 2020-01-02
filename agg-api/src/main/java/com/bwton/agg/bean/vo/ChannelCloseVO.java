package com.bwton.agg.bean.vo;

import lombok.Data;
import lombok.ToString;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 描述: 关闭订单渠道返回类
 * @author wjl
 * @version v2.4.9
 * @created  2019/12/14
 */
@Data
@ToString(callSuper = true)
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "xml")
public class ChannelCloseVO extends ChannelBaseVO {

}
