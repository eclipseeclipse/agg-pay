package com.bwton.agg.trade.service;

import com.bwton.agg.bean.bo.*;
import com.bwton.agg.trade.data.entity.TxChannelEntity;
import com.bwton.agg.trade.data.entity.TxTradeEntity;
import com.bwton.lang.Result;

/**
 * 描述:  订单服务接口
 * @author wjl
 * @version v1.0.0
 * @created  2019/12/26
 */
public interface OrderService {
    /**
     * 统一下单交易
     * @param  orderPayBO
     * @return  OrderPayResultBO
     */
    Result<OrderPayResultBO> placeOrder(OrderPayBO orderPayBO);

    /**
     * 查询订单交易
     * @param  orderQueryBO
     * @return  OrderQueryResultBO
     */
    Result<OrderQueryResultBO> query(OrderQueryBO orderQueryBO);

    /**
     * 关闭订单交易
     * @param  orderCloseBO
     * @return  OrderCloseResultBO
     */
    Result<OrderCloseResultBO> close(OrderCloseBO orderCloseBO);

    /**
     * 退款交易
     * @param  orderRefundBO
     * @return  OrderRefundResultBO
     */
    Result<OrderRefundResultBO> refund(OrderRefundBO orderRefundBO);

    /**
     * 退款查询
     * @param  orderRefundQueryBO
     * @return  OrderRefundQueryResultBO
     */
    Result<OrderRefundQueryResultBO> refundQuery(OrderRefundQueryBO orderRefundQueryBO);

    /**
     * 定时任务 更新订单状态
     * @param
     * @return
     */
    void updateOrderStatus(TxChannelEntity txChannelEntity, TxTradeEntity txTradeEntity);

}
