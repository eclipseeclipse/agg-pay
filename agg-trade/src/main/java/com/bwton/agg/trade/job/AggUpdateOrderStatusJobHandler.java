package com.bwton.agg.trade.job;

import com.bwton.agg.trade.service.JobOffsetService;
import com.bwton.job.core.biz.model.ReturnT;
import com.bwton.job.core.handler.IJobHandler;
import com.bwton.job.core.handler.annotation.JobHandler;
import com.bwton.job.core.log.BwtonJobLogger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 描述: 定时查证支付处理中的订单，更新订单状态
 * @author wjl
 * @version v1.0.0
 * @created  2019/12/17
 */

@JobHandler(value = "aggUpdateOrderStatusJobHandler")
@Component("aggUpdateOrderStatusJobHandler")
@Slf4j
public class AggUpdateOrderStatusJobHandler  extends IJobHandler {
    @Autowired
    private JobOffsetService jobOffsetService;

    @Override
    public ReturnT<String> execute(String s){

        BwtonJobLogger.log("定时查证渠道支付处理中的订单开始");

       jobOffsetService.txChannelPayOffset();

        BwtonJobLogger.log("定时查证渠道支付处理中的订单结束");

        return SUCCESS;

    }
}
