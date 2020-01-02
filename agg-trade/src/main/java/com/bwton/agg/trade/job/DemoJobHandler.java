package com.bwton.agg.trade.job;

import com.bwton.job.core.biz.model.ReturnT;
import com.bwton.job.core.handler.IJobHandler;
import com.bwton.job.core.handler.annotation.JobHandler;
import com.bwton.job.core.log.BwtonJobLogger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * 描述: Job 测试类
 * @author wjl
 * @version v1.0.0
 * @created  2019/12/17
 */
@JobHandler(value = "aggDemoJobHandler")
@Component("aggDemoJobHandler")
@Slf4j
public class DemoJobHandler extends IJobHandler {
    @Override
    public ReturnT<String> execute(String s) throws Exception {
        BwtonJobLogger.log("BWTON-JOB, Hello World.");

        for (int i = 0; i < 100; i++) {
            log.info("beat at:" + i);
            BwtonJobLogger.log("beat at:" + i);
            TimeUnit.SECONDS.sleep(2);
        }
        //返回SUCCESS视为任务执行成功，返回FAIL则表示任务执行失败
        return SUCCESS;

    }
}
