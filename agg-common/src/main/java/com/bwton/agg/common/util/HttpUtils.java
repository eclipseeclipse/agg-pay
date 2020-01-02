package com.bwton.agg.common.util;

import com.bwton.core.httpclient.HttpResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;

import java.nio.charset.StandardCharsets;

/**
 * 
 * @Description: Http请求帮助类
 * @author 李智鹏
 * @date 2018年4月3日 下午6:55:04
 *
 */
@Slf4j
public class HttpUtils {

    public static String post4Xml(AggHttpClient httpClient, String url, String body) {
        HttpPost httpPost = new HttpPost(url);
        httpPost.setEntity(new StringEntity(body, ContentType.create("application/xml", StandardCharsets.UTF_8)));
        log.info("http request, url: {}, body: {}", url, body);
        HttpResult<String> httpResult = httpClient.invoker(url, null, httpPost);
        String respBody = httpResult.getResult();
        log.info("http response, body: {}", respBody);
        return respBody;
    }

}
