package com.bwton.agg.bean.bo;

import lombok.Data;
import lombok.ToString;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author DengQiongChuan
 * @date 2019-12-05 16:59
 */
@Data
@ToString(callSuper = true)
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "xml")
public class OrderPayBO extends BaseReqBO {
    /** 商户订单号 */
    @XmlElement(name = "out_trade_no")
    @NotBlank(message = "商户订单号不能为空")
    @Size(max =33, message = "商户订单号不能超过32位")
    private String outTradeNo;

    /** 商品ID */
    @XmlElement(name = "product_id")
    private String productId;

    /** 商品描述，即商户及商品名称 */
    @NotBlank(message = "商品描述不能为空")
    @Size(max = 128, message = "商品描述不能超过128位")
    private String body;

    /** 附加信息 */
    private String attach;

    /** 总金额 */
    @XmlElement(name = "total_fee")
    @NotBlank(message = "总金额不能为空")
    @Pattern(regexp = "^[1-9]{1}\\d*", message = "总金额，以分为单位，不允许包含任何字、符号")
    private Long totalFee;

    /** 订单生成的机器 IP */
    @NotBlank(message = "订单生成机器不能为空")
    @XmlElement(name = "mch_create_ip")
    private String mchCreateIp;

    /** 通知地址 */
    @XmlElement(name = "notify_url")
    private String notifyUrl;

    /** 订单生成时间。yyyyMMddHHmmss */
    @XmlElement(name = "time_start")
    private String timeStart;

    /** 订单超时时间。yyyyMMddHHmmss */
    @XmlElement(name = "time_expire")
    private String timeExpire;

    /** 运营日期。yyyyMMdd */
    @XmlElement(name = "operation_date")
    private String operationDate;

    /** 收银员或操作员帐号 */
    @XmlElement(name = "op_user_id")
    private String opUserId;
}
