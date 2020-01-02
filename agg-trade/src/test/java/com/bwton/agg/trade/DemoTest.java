package com.bwton.agg.trade;

import com.bwton.agg.bean.bo.OrderQueryBO;
import com.bwton.agg.bean.bo.OrderQueryResultBO;
import com.bwton.agg.trade.service.OrderService;
import com.bwton.lang.Result;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = {TTradeApplication.class})
public class DemoTest {

    @Autowired
    private OrderService orderService;

    @Test
    public void testQueryOrder() {
        OrderQueryBO orderQueryBO=new OrderQueryBO();
        orderQueryBO.setOutTradeNo("1576408622995");
        orderQueryBO.setMchId("3501000000000000");

        Result<OrderQueryResultBO> result = orderService.query(orderQueryBO);
        System.err.println("查询订单结果>>>" + result);
    }
}