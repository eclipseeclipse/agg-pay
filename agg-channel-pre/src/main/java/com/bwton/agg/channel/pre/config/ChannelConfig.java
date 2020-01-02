package com.bwton.agg.channel.pre.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author DengQiongChuan
 * @date 2019-12-10 20:34
 */
@Data
@Component
public class ChannelConfig {
    /** 响应超时时间。毫秒 */
    @Value("${channel.socket.timeout:8000}")
    private Integer socketTimeout;

    /** 连接超时时间。毫秒 */
    @Value("${channel.connect.timeout:5000}")
    private Integer connectTimeout;

    /**
     * 替换银联二维码图片地址URL为聚合支付平台URL。
     * 例如，将
     * https://qra.95516.com/pay/qrcode?uuid=xxx
     * 替换为
     * http://dev.pay.bwton.com/agg24/agg/qrcode/union?uuid=xxx
     */
    /* 银联二维码图片地址URL前缀。例如，https://qra.95516.com/pay/qrcode */
    @Value("${qrcode.url.union.source}")
    private String qrcodeUrl4UnionSource;

    /* 替换后的聚合支付平台二维码图片地址URL前缀。例如，http://dev.pay.bwton.com/agg24/agg/qrcode/union */
    @Value("${qrcode.url.union.target}")
    private String qrcodeUrl4UnionTarget;
}
