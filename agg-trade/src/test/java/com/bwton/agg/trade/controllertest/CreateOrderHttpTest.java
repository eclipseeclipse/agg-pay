package com.bwton.agg.trade.controllertest;

import com.bwton.agg.bean.bo.OrderPayBO;
import com.bwton.agg.bean.bo.OrderPayResultBO;
import com.bwton.agg.common.constant.DateConstants;
import com.bwton.agg.common.util.TimeUtils;
import com.bwton.agg.trade.base.BaseTest;
import org.junit.Test;

public class CreateOrderHttpTest extends BaseTest {

    @Test
    public void createOrder() throws Exception{
        OrderPayBO orderPayBO=new OrderPayBO();
        getBaseRequest(orderPayBO);
        orderPayBO.setOutTradeNo("1576408622997");
        orderPayBO.setBody("军龙测试关单交易");
        orderPayBO.setTotalFee(1L);
        orderPayBO.setAttach("期待天明");
        orderPayBO.setMchCreateIp("192.168.20.57");
        orderPayBO.setTimeStart(TimeUtils.getCurrent(DateConstants.YYYYMMDDHHMMSS));

        String result=post(orderPayBO,OrderPayBO.class,OrderPayResultBO.class,"order/create");
        System.out.println(result);
    }
}
