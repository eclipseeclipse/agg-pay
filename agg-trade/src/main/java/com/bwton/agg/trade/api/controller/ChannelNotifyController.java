package com.bwton.agg.trade.api.controller;

import com.bwton.agg.trade.service.ChannelNotifyService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * 接收渠道异步通知
 *
 * @author DengQiongChuan
 * @date 2019-12-11 16:52
 */
@RestController
@Slf4j
public class ChannelNotifyController {
    @Autowired
    private ChannelNotifyService notifyService;

    @RequestMapping(value = "/notify/{channel}")
    public void receiptNotification(@PathVariable String channel, HttpServletRequest req, HttpServletResponse resp) {
        try {
            String reqBody = IOUtils.toString(req.getInputStream(), StandardCharsets.UTF_8);
            if ("union".equals(channel)) {
                boolean result = notifyService.unionNotify(reqBody);
                if (result) {
                    responseMsg(resp, "success");
                } else {
                    responseMsg(resp, "fail");
                }
            }else {
                log.info("异步通知的渠道类型非法");
                responseMsg(resp, "fail");
            }
        } catch (IOException e) {
            log.error("接收渠道异步通知发生异常", e);
            responseMsg(resp, "fail");
        }
    }

    private void responseMsg(HttpServletResponse resp, String respMsg) {
        try {
            resp.getWriter().write(respMsg);
        } catch (IOException e) {
            log.error("响应渠道异步通知发生异常", e);
        }
    }

}
