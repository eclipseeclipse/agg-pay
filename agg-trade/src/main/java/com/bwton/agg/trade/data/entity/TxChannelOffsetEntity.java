package com.bwton.agg.trade.data.entity;

import lombok.Data;

import java.util.Date;

/**
 * @author DengQiongChuan
 * @date 2019-12-03 20:42
 */
@Data
public class TxChannelOffsetEntity {
    /** 交易流水号 */
    private String txSerialNo;

    /** 渠道交易时间。请求发渠道的时间 */
    private Date txChannelTime;

    /** 交易状态。1-未支付；2-成功；3-渠道处理中；4-转入退款；5-渠道未确定；6-已关闭；7-失败；8-转入代发，退款失败需线下退款；9-渠道前置未受理 */
    private Integer txStatus;

    /** 1-主扫支付，2-被扫支付，3-关闭订单，4-订单退款，5-支付查询，6-退款查询*/
    private Integer tradeType;
}
