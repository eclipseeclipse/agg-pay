package com.bwton.agg.trade.servicetest;

import com.bwton.agg.bean.bo.OrderRefundBO;
import com.bwton.agg.bean.bo.OrderRefundResultBO;
import com.bwton.agg.trade.TTradeApplication;
import com.bwton.agg.trade.base.BaseTest;
import com.bwton.agg.trade.service.JobOffsetService;
import com.bwton.agg.trade.service.OrderService;
import com.bwton.job.core.log.BwtonJobLogger;
import com.bwton.lang.Result;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * ***********************************************************
 * Copyright © 2019 八维通科技有限公司 Inc.All rights reserved.  *
 * ***********************************************************
 *
 * @company: www.bwton.com
 * @generator: cairuimin
 * @project: agg-pay
 * @package: com.bwton.agg.trade.servicetest
 * @name: RefundServiceTest
 * @author: cairuimin@bwton.com
 * @date: 2019年12月17日 10时38分
 * @description:
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {TTradeApplication.class})
@Slf4j
public class RefundServiceTest extends BaseTest {

    @Autowired
    OrderService orderService;
    @Autowired
    private JobOffsetService jobOffsetService;
    @Autowired
    Gson gson;

    @Test
    public void refundOrder () {
        OrderRefundBO orderRefundBO = new OrderRefundBO();
        //组装base信息
        getBaseRequest(orderRefundBO);
        orderRefundBO.setService("refund");
        orderRefundBO.setOutTradeNo("1576408622995");
        Result<OrderRefundResultBO> result = orderService.refund(orderRefundBO);

        System.err.println("申请退款结果>>>" + result);
    }

    @Test
    public void queryRefundOrder() {
        BwtonJobLogger.log("定时查证渠道退款处理中的订单开始");

        jobOffsetService.txChannelRefundOffset();

        BwtonJobLogger.log("定时查证渠道退款处理中的订单结束");
    }

}
