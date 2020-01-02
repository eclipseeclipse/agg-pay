package com.bwton.agg.trade.controllertest;

import com.bwton.agg.bean.bo.OrderQueryBO;
import com.bwton.agg.bean.bo.OrderQueryResultBO;
import com.bwton.agg.trade.base.BaseTest;
import org.junit.Test;

public class QueryOrderHttpTest extends BaseTest {

    @Test
    public void queryOrder() throws Exception{
        OrderQueryBO orderQueryBO=new OrderQueryBO();
        getBaseRequest(orderQueryBO);
        orderQueryBO.setOutTradeNo("1576408622995");

        String  result=post(orderQueryBO,OrderQueryBO.class,OrderQueryResultBO.class,"order/query");
        System.out.println(result);
    }

}
