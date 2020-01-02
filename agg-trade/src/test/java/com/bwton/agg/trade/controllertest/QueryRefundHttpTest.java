package com.bwton.agg.trade.controllertest;

import com.bwton.agg.bean.bo.OrderRefundQueryBO;
import com.bwton.agg.bean.bo.OrderRefundQueryResultBO;
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
public class QueryRefundHttpTest extends BaseTest {

    @Test
    public void refundOrderQuery() throws Exception {

        OrderRefundQueryBO queryBO = new OrderRefundQueryBO();
        //组装base信息
        getBaseRequest(queryBO);
        queryBO.setOutRefundNo("157640862299500");
        queryBO.setService("unified.trade.refund");

        String  result = post(queryBO, OrderRefundQueryBO.class, OrderRefundQueryResultBO.class,"order/refund/query");
        System.err.println(result);
    }
}
