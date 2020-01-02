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
 * 描述: 定时查证退款处理中的订单，更新订单状态
 * @author wjl
 * @version v1.0.0
 * @created  2019/12/17
 */

@JobHandler(value = "aggUpdateRefundOrderStatusJobHandler")
@Component("aggUpdateRefundOrderStatusJobHandler")
@Slf4j
public class AggUpdateRefundOrderStatusJobHandler extends IJobHandler {
    @Autowired
    private JobOffsetService jobOffsetService;

    @Override
    public ReturnT<String> execute(String s){

        BwtonJobLogger.log("定时查证渠道退款处理中的订单开始");

       jobOffsetService.txChannelRefundOffset();

        BwtonJobLogger.log("定时查证渠道退款处理中的订单结束");

        return SUCCESS;

    }
}
