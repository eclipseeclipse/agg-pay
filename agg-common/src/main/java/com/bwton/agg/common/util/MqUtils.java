package com.bwton.agg.common.util;

import com.alibaba.fastjson.JSON;
import com.bwton.agg.common.constant.MqConstants;
import com.bwton.agg.common.enums.AggPayExceptionEnum;
import com.bwton.agg.common.enums.MqDelayTimeLevelEnum;
import com.bwton.agg.common.enums.MqTagsEnum;
import com.bwton.core.web.CommonResult;
import com.bwton.lang.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.MQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;

/**
 * @author yangqing
 * @description copy from tpsp mq消息帮助类
 * @create 2019/4/17 6:27 PM
 */
@Slf4j
public class MqUtils {

    /**
     * 异步发送mq消息
     *
     * @param mqProducer
     * @param mqTagsEnum
     * @param keys
     * @param msgs       以===连接消息，如1===2===3===，不允许为空
     * @return
     */
    public static Result<?> sendAsynMsg(MQProducer mqProducer, MqTagsEnum mqTagsEnum, String keys, Object... msgs) {
        return sendAsynMsg(MqDelayTimeLevelEnum.NO_DELAY, mqProducer, mqTagsEnum, keys, msgs);
    }

    /**
     * 延迟异步发送mq消息
     *
     * @param delayTimeLevel 延迟级别
     * @param mqProducer
     * @param mqTagsEnum
     * @param keys
     * @param msgs
     * @return
     */
    public static Result<?> sendAsynMsg(MqDelayTimeLevelEnum delayTimeLevel, MQProducer mqProducer,
                                        MqTagsEnum mqTagsEnum, String keys, Object... msgs) {
        if (msgs == null || msgs.length <= 0 || mqProducer == null || StringUtils.isAnyBlank(keys)) {
            return CommonResult.failure(AggPayExceptionEnum.MQ_PARAMS_FAIL.getMsg());
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < msgs.length; i++) {
            if (msgs[i] instanceof String) {
                sb.append(msgs[i]);
            } else {
                sb.append(JSON.toJSONString(msgs[i]));
            }
            if (msgs.length > 1 && i < msgs.length - 1) {
                sb.append(MqConstants.MSG_SEPARATOR);
            }
        }
        return sendAsynMsg(mqProducer, mqTagsEnum, keys, sb.toString(), delayTimeLevel);
    }

    private static Result<?> sendAsynMsg(MQProducer mqProducer, MqTagsEnum mqTagsEnum, String keys, String msgStr,
                                         MqDelayTimeLevelEnum delayTimeLevel) {
        Result<?> result = CommonResult.success();
        DefaultMQProducer producer = (DefaultMQProducer) mqProducer;
        int retryTimesWhenSendAsyncFailed = producer.getRetryTimesWhenSendAsyncFailed() + 1;
        try {
            Message msg = new Message(mqTagsEnum.getTopic(), mqTagsEnum.getTags(), mqTagsEnum.getTags() + ":" + keys,
                    msgStr.getBytes(RemotingHelper.DEFAULT_CHARSET));
            if (delayTimeLevel.getLevel() > 0) {
                msg.setDelayTimeLevel(delayTimeLevel.getLevel());
            }

            mqProducer.send(msg, new SendCallback() {
                int sendCount = 0;

                @Override
                public void onSuccess(SendResult sendResult) {
                    log.info("{}，异步发送消息：{}", sendResult, msg);
                }

                @Override
                public void onException(Throwable e) {
                    sendCount++;
                    if (sendCount >= retryTimesWhenSendAsyncFailed) {
                        log.error("msg={},msg body={}", msg, msgStr);
                        result.setErrcode(AggPayExceptionEnum.MQ_SEND_EXCEPTION.getCode());
                    } else {
                        log.warn(AggPayExceptionEnum.MQ_SEND_EXCEPTION.getMsg() + ":{},{}", msg, msgStr, e);
                    }
                }
            });
            return result;
        } catch (Exception e) {
            log.warn(AggPayExceptionEnum.MQ_SEND_EXCEPTION.getMsg() + ":{}", msgStr, e);
            return CommonResult.failure(e);
        }
    }

    public static boolean isSend(MQProducer mqProducer, MqTagsEnum mqTagsEnum) {
        try {
            Message msg = new Message(mqTagsEnum.getTopic(), mqTagsEnum.getTags(),
                    mqTagsEnum.getTags() + ":" + System.currentTimeMillis(),
                    "这是一条测试mq消息".getBytes(RemotingHelper.DEFAULT_CHARSET));
            SendResult sendResult = mqProducer.send(msg);
            log.info("测试成功：{}", sendResult);
        } catch (Exception e) {
            log.warn(AggPayExceptionEnum.MQ_SEND_EXCEPTION.getMsg(), e);
            return false;
        }
        return true;
    }
}
