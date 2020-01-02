package com.bwton.agg.bean.vo;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.ToString;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * 描述: 查询退款渠道返回类
 * @author wjl
 * @version v2.4.9
 * @created  2019/12/14
 */
@Data
@ToString(callSuper = true)
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "xml")
public class ChannelRefundQueryVO extends BaseVO {
    @SerializedName("trade_type")
    private String tradeType;
    @SerializedName("transaction_id")
    private String transactionId;
    @SerializedName("out_trade_no")
    private String outTradeNo;
    @SerializedName("refund_count")
    private String refundCount;
    @SerializedName("out_transaction_id")
    private String outTransactionId;
    private List<ChannelRefundSingleQueryVO> list;
}
