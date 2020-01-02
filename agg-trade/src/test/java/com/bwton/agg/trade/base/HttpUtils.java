package com.bwton.agg.trade.base;

import com.bwton.agg.common.constant.CommonConstants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * 
 * @Description: Http请求帮助类
 * @author 李智鹏
 * @date 2018年4月3日 下午6:55:04
 *
 */
@Slf4j
public class HttpUtils {

    /**
     * 连接超时 毫秒
     */
    private static final Integer CONNECT_TIME_OUT = 5000;
    /**
     * 响应超时 毫秒
     */
    private static final Integer RESP_TIME_OUT = 8000;
    public static final String SUCCESS = "ok";

    public static String postForXML(String url, String body) throws IOException {
        return postForXML(url,body,RESP_TIME_OUT,CONNECT_TIME_OUT);
    }
    public static String postForXML(String url, String body, int socketTimeout, int connectTimeout) throws IOException {
        log.info("requestPost, url = {}, jsonData = {}", url, body);
        String respBody;
        RequestConfig defaultRequestConfig = RequestConfig.custom().setSocketTimeout(socketTimeout)
                .setConnectTimeout(connectTimeout).build();

        CloseableHttpClient httpclient = HttpClientBuilder.create().setDefaultRequestConfig(defaultRequestConfig)
                .build();
        HttpPost httppost = new HttpPost(url);

        httppost.setEntity(new StringEntity(body, StandardCharsets.UTF_8));
        httppost.setHeader(HTTP.CONTENT_TYPE, MediaType.APPLICATION_XML);
        try {
            CloseableHttpResponse response = httpclient.execute(httppost);
            int statusCode = response.getStatusLine().getStatusCode();

            if (statusCode != HttpStatus.SC_OK) {
                log.error("requestPost失败,statusCode = {}, 请求参数 = {}", statusCode, body);
                return null;
            }
            HttpEntity entity = response.getEntity();
            respBody = EntityUtils.toString(entity, StandardCharsets.UTF_8);
        } finally {
            httppost.releaseConnection();
        }
        log.info("requestPost, response = {}", respBody);
        return respBody;
    }

    public static String post(String url, String body, int socketTimeout, int connectTimeout) throws IOException {
        log.info("requestPost, url = {}, jsonData = {}", url, body);
        String respBody;
        RequestConfig defaultRequestConfig = RequestConfig.custom().setSocketTimeout(socketTimeout)
                .setConnectTimeout(connectTimeout).build();

        CloseableHttpClient httpclient = HttpClientBuilder.create().setDefaultRequestConfig(defaultRequestConfig)
                .build();
        HttpPost httppost = new HttpPost(url);

        httppost.setEntity(new StringEntity(body, StandardCharsets.UTF_8));
        httppost.setHeader(HTTP.CONTENT_TYPE, MediaType.APPLICATION_JSON);
        try {
            CloseableHttpResponse response = httpclient.execute(httppost);
            int statusCode = response.getStatusLine().getStatusCode();

            if (statusCode != HttpStatus.SC_OK) {
                log.error("requestPost失败,statusCode = {}, 请求参数 = {}", statusCode, body);
                return null;
            }
            HttpEntity entity = response.getEntity();
            respBody = EntityUtils.toString(entity, StandardCharsets.UTF_8);
        } finally {
            httppost.releaseConnection();
        }
        log.info("requestPost, response = {}", respBody);
        return respBody;
    }

    public static String requestPostException(String url, String jsonData) throws IOException {
        return post(url, jsonData, RESP_TIME_OUT, CONNECT_TIME_OUT);
    }

    public static String requestPost(String url, String jsonData) {
        try {
            return requestPostException(url, jsonData);
        } catch (Exception e) {
            log.error("发送http请求异常", e);
            return "";
        }
    }

    public static boolean isReqPostException(String url, String jsonData) throws IOException {
        String result = requestPostException(url, jsonData);
        if (SUCCESS.equals(result)) {
            return true;
        }
        return false;
    }

    public static boolean isReqPost(String url, String jsonData) {
        try {
            return isReqPostException(url, jsonData);
        } catch (Exception e) {
            log.error("发送http请求异常", e);
            return false;
        }
    }

    public static Map<String, Object> post(String url, String body, int socketTimeout, int connectTimeout,
            Map<String, String> headers) throws IOException {

        Map<String, Object> resultMap = new HashMap<>(100);

        log.debug("requestPost, url = {}, jsonData = {}", url, body);
        String respBody;
        RequestConfig defaultRequestConfig = RequestConfig.custom().setSocketTimeout(socketTimeout)
                .setConnectTimeout(connectTimeout).build();

        CloseableHttpClient httpclient = HttpClientBuilder.create().setDefaultRequestConfig(defaultRequestConfig)
                .build();
        HttpPost httppost = new HttpPost(url);

        httppost.setEntity(new StringEntity(body, StandardCharsets.UTF_8));
        httppost.setHeader(HTTP.CONTENT_TYPE, MediaType.APPLICATION_JSON);

        // 添加自定义headers
        if (headers != null && !headers.isEmpty()) {
            Set<Entry<String, String>> set = headers.entrySet();
            for (Entry<String, String> entry : set) {
                httppost.addHeader(entry.getKey(), entry.getValue());
            }
        }

        try {
            CloseableHttpResponse response = httpclient.execute(httppost);
            int statusCode = response.getStatusLine().getStatusCode();

            if (statusCode != HttpStatus.SC_OK) {
                log.error("requestPost失败,statusCode = {}, 请求参数 = {}", statusCode, body);
                return null;
            }
            HttpEntity entity = response.getEntity();
            Header[] responseHeaders = response.getAllHeaders();
            respBody = EntityUtils.toString(entity, StandardCharsets.UTF_8);
            resultMap.put(CommonConstants.HTTP_BODY, respBody);
            resultMap.put(CommonConstants.HTTP_HEADERS, responseHeaders);

        } finally {
            httppost.releaseConnection();
        }
        log.info("requestPost, response body = {}", respBody);
        return resultMap;
    }

    /**
     * 获取图片并转为Base64
     * @param url
     * @return
     * @throws IOException
     */
    public static String getImgToBase64(String url) throws IOException {
        return getImgToBase64(url, RESP_TIME_OUT, CONNECT_TIME_OUT);
    }

    public static String getImgToBase64(String url, int socketTimeout, int connectTimeout) throws IOException {
        RequestConfig defaultRequestConfig = RequestConfig.custom()
                .setSocketTimeout(socketTimeout).setConnectTimeout(connectTimeout).build();
        CloseableHttpClient httpclient = HttpClientBuilder.create().setDefaultRequestConfig(defaultRequestConfig).build();
        HttpGet httpGet = new HttpGet(url);
        log.info("requestGet, url = {}", url);
        try {
            CloseableHttpResponse response = httpclient.execute(httpGet);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != HttpStatus.SC_OK) {
                log.error("requestGet失败,statusCode = {}, 请求url = {}", statusCode, url);
                return null;
            }
            HttpEntity httpEntity = response.getEntity();
            if (httpEntity == null) {
                return null;
            }
            InputStream inputStream = httpEntity.getContent();
            if (inputStream == null) {
                return null;
            }
            byte[] bytes = IOUtils.toByteArray(inputStream);
            return Base64.getEncoder().encodeToString(bytes);
        } finally {
            httpGet.releaseConnection();
        }
    }
}
