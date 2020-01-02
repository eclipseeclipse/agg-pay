package com.bwton.agg.trade.controllertest;

import com.bwton.agg.bean.bo.OrderCloseBO;
import com.bwton.agg.bean.bo.OrderCloseResultBO;
import com.bwton.agg.trade.base.BaseTest;
import org.junit.Test;

public class CloseOrderHttpTest extends BaseTest {

    @Test
    public void closeOrder() throws Exception{
        OrderCloseBO orderCloseBO=new OrderCloseBO();
        getBaseRequest(orderCloseBO);
        orderCloseBO.setOutTradeNo("1576408622997");

        String result=post(orderCloseBO,OrderCloseBO.class,OrderCloseResultBO.class,"order/close");
        System.out.println(result);
    }
}
