package com.bwton.agg.trade.controllertest;

import com.bwton.agg.bean.bo.OrderRefundBO;
import com.bwton.agg.bean.bo.OrderRefundResultBO;
import com.bwton.agg.trade.base.BaseTest;
import org.junit.Test;

/**
 * ***********************************************************
 * Copyright © 2019 八维通科技有限公司 Inc.All rights reserved.  *
 * ***********************************************************
 *
 * @company: www.bwton.com
 * @generator: cairuimin
 * @project: agg-pay
 * @package: com.bwton.agg.trade.controllertest
 * @name: RefundHttpTest
 * @author: cairuimin@bwton.com
 * @date: 2019年12月17日 09时53分
 * @description:
 */
public class RefundHttpTest  extends BaseTest {

    @Test
    public void refundOrder() throws Exception {

        OrderRefundBO orderRefundBO = new OrderRefundBO();
        //组装base信息
        getBaseRequest(orderRefundBO);
        orderRefundBO.setOutTradeNo("12201986181");
        orderRefundBO.setOutRefundNo("11123456156");
        orderRefundBO.setRefundFee(1L);
        orderRefundBO.setService("unified.trade.refund");

        String  result = post(orderRefundBO, OrderRefundBO.class, OrderRefundResultBO.class,"order/refund");
        System.out.println(result);
    }
}
