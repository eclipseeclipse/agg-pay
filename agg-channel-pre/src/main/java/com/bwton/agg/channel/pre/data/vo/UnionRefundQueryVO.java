package com.bwton.agg.channel.pre.data.vo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 查询订单响应对象。
 *
 * $n 表示记录的序号，取值为 0~($ refund_count -1)，
 * 例如 refund_count 指示返回的退款记录有 2 条。第一条序号为“0”，第二条序号为“1”。
 *
 * @author DengQiongChuan
 * @date 2019-12-04 17:13
 */
@Data
public class UnionRefundQueryVO extends UnionBaseVO {
    /**
     * 交易类型。
     * 支付宝：pay.alipay.jspay
     * 微信：pay.weixin.jspay
     * 银联：pay.unionpay.native
     */
    private String trade_type;
    /** 平台订单号 */
    private String transaction_id;
    /** 商户订单号 */
    private String out_trade_no;
    /** 退款笔数 */
    private String  refund_count;
    /** 第三方订单号 */
    private String out_transaction_id;

    private List<UnionRefundQuerySingleVO> list=new ArrayList<>();
}
