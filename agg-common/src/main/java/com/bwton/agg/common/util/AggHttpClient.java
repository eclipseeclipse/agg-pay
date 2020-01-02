package com.bwton.agg.common.util;

import com.bwton.core.httpclient.HttpResult;
import com.bwton.core.logger.BwtLogger;
import com.bwton.core.util.URLUtil;
import com.bwton.exception.BusinessException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.Configurable;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.TrustStrategy;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 复制于 com.bwton.core.httpclient.CoreHttpClient
 * 因为CoreHttpClient仅支持json、form格式的请求体，且invoker方法是私有的
 *
 * @author DengQiongChuan
 * @date 2019-12-27 14:29
 */
@Slf4j
public class AggHttpClient {
    private Gson gson = new GsonBuilder().disableHtmlEscaping().setDateFormat("yyyy/MM/dd HH:mm:ss").serializeNulls().create();

    private HttpClient httpclient;
    private Configurable confClient;

    private AggHttpClient(HttpClient httpclient) {
        this.httpclient = httpclient;
        if (httpclient instanceof Configurable)
            this.confClient = (Configurable) httpclient;
    }

    public static AggHttpClient.Builder builder() {
        return new AggHttpClient.Builder();
    }

    public static class Builder {

        private Integer maxTotal = 400;

        private Integer maxPerRoute = 100;

        private Long connTimeToLive = 30000L;

        private Integer connectTimeout = 5000;
        //从连接池获取连接的超时时间
        private Integer connectionRequestTimeout = 5000;

        private Integer socketTimeout = 5000;

        private HttpClient httpclient;

        public AggHttpClient.Builder maxTotal(Integer maxTotal) {
            this.maxTotal = maxTotal;
            return this;
        }

        public AggHttpClient.Builder maxPerRoute(Integer maxPerRoute) {
            this.maxPerRoute = maxPerRoute;
            return this;
        }

        public AggHttpClient.Builder connTimeToLive(Long connTimeToLive) {
            this.connTimeToLive = connTimeToLive;
            return this;
        }

        public AggHttpClient.Builder connectTimeout(Integer connectTimeout) {
            this.connectTimeout = connectTimeout;
            return this;
        }

        public AggHttpClient.Builder socketTimeout(Integer socketTimeout) {
            this.socketTimeout = socketTimeout;
            return this;
        }

        public AggHttpClient build() {
            if (httpclient == null) {
                try{
                    httpclient = acceptsUntrustedCertsHttpClient();
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return new AggHttpClient(httpclient);
        }

        private HttpClient acceptsUntrustedCertsHttpClient() throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
            HttpClientBuilder b = HttpClientBuilder.create();

            // setup a Trust Strategy that allows all certificates.
            //
            SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
                public boolean isTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
                    return true;
                }
            }).build();
            b.setSSLContext(sslContext);

            // don't check Hostnames, either.
            //      -- use SSLConnectionSocketFactory.getDefaultHostnameVerifier(), if you don't want to weaken
            HostnameVerifier hostnameVerifier = NoopHostnameVerifier.INSTANCE;

            // here's the special part:
            //      -- need to create an SSL Socket Factory, to use our weakened "trust strategy";
            //      -- and create a Registry, to register it.
            //
            SSLConnectionSocketFactory sslSocketFactory = new SSLConnectionSocketFactory(sslContext, hostnameVerifier);
            Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                    .register("http", PlainConnectionSocketFactory.getSocketFactory())
                    .register("https", sslSocketFactory)
                    .build();

            // now, we create connection-manager using our Registry.
            //      -- allows multi-threaded use
            // 长连接保持30秒
            PoolingHttpClientConnectionManager connMgr = new PoolingHttpClientConnectionManager(socketFactoryRegistry,null,null,null, connTimeToLive, TimeUnit.MILLISECONDS);
            connMgr.setMaxTotal(maxTotal);
            connMgr.setDefaultMaxPerRoute(maxPerRoute);
            b.setConnectionManager(connMgr);

            // 保持长连接配置，需要在头添加Keep-Alive
            b.setKeepAliveStrategy(new DefaultConnectionKeepAliveStrategy());
            //设置重定向
            b.setRedirectStrategy(new LaxRedirectStrategy());
            b.setDefaultRequestConfig(RequestConfig.custom().setConnectTimeout(connectTimeout)
                    .setSocketTimeout(socketTimeout).setConnectionRequestTimeout(connectionRequestTimeout).build());
            // finally, build the HttpClient;
            //      -- done!
            CloseableHttpClient client = b.build();

            return client;
        }
    }

    public static class RequestOption {
        private int connectTimeout;
        private int socketTimeout;

        RequestConfig toConfig(AggHttpClient client) {
            if (connectTimeout == 0 && socketTimeout == 0)
                return null;

            RequestConfig dft = client.confClient == null ? null : client.confClient.getConfig();
            RequestConfig.Builder builder = dft == null ? RequestConfig.custom() : dft.copy(dft);
            if (connectTimeout > 0)
                builder.setConnectTimeout(connectTimeout);
            if (socketTimeout > 0)
                builder.setSocketTimeout(socketTimeout);
            return builder.build();
        }

        public static AggHttpClient.RequestOption.RequestOptionBuilder builder() {
            return new AggHttpClient.RequestOption.RequestOptionBuilder();
        }
        public static class RequestOptionBuilder {
            private int connectTimeout;
            private int socketTimeout;

            public AggHttpClient.RequestOption.RequestOptionBuilder connectTimeout(int connectTimeout) {
                this.connectTimeout = connectTimeout;
                return this;
            }
            public AggHttpClient.RequestOption.RequestOptionBuilder socketTimeout(int socketTimeout) {
                this.socketTimeout = socketTimeout;
                return this;
            }
            public AggHttpClient.RequestOption build() {
                AggHttpClient.RequestOption ro = new AggHttpClient.RequestOption();
                ro.connectTimeout = connectTimeout;
                ro.socketTimeout = socketTimeout;
                return ro;
            }
        }
    }

    public String get(String url){
        return get(url, null);
    }

    public String get(String url, Map<String, String> headers) {
        return get(url,  null, headers);
    }


    public String get(String url, Map<String, Object> params, Map<String, String> headers){
        return getForResult(url, params, headers).getResult();
    }

    public HttpResult<String> getForResult(String url, Map<String, String> headers) {
        return getForResult(url, null, headers);
    }

    public HttpResult<String> getForResult(String url, Map<String, Object> params, Map<String, String> headers) {
        url = URLUtil.genUrl(url, params);
        HttpRequestBase requestBase = new HttpGet(url);
        return invoker(url, headers, requestBase);
    }

    public HttpResult<String> getForResult(String url, Map<String, Object> params, Map<String, String> headers,
                                           AggHttpClient.RequestOption option) {
        url = URLUtil.genUrl(url, params);
        HttpRequestBase requestBase = new HttpGet(url);
        return invoker(url, headers, requestBase, option);
    }

    public String post(String url, Map<String, Object> params) {
        return post(url, params, null);
    }



    public String post(String url, Map<String, Object> params, Map<String, String> headers){
        return post(url, params, headers, AggHttpClient.RequestContentType.JSON);
    }

    public String post(String url, Map<String, Object> params, Map<String, String> headers, AggHttpClient.RequestContentType contentType) {
        return postForResult(url, params, headers, contentType).getResult();
    }

    public HttpResult<String> postForResult(String url, Map<String, Object> params, Map<String, String> headers) {
        return postForResult(url, params, headers, AggHttpClient.RequestContentType.JSON);
    }

    public HttpResult<String> postForResult(String url, Map<String, Object> params, Map<String, String> headers, AggHttpClient.RequestContentType contentType) {
        HttpPost requestBase = new HttpPost(url);
        if (contentType == AggHttpClient.RequestContentType.FORM) {
            requestBase.setEntity(createUrlEncodedFormEntity(params));
        }else {
            requestBase.setEntity(createJSONEntity(params));
        }
        return invoker(url, headers, requestBase);
    }

    public HttpResult<String> postForResult(String url, Map<String, Object> params, Map<String, String> headers,
                                            AggHttpClient.RequestContentType contentType, AggHttpClient.RequestOption option) {
        HttpPost requestBase = new HttpPost(url);
        if (contentType == AggHttpClient.RequestContentType.FORM) {
            requestBase.setEntity(createUrlEncodedFormEntity(params));
        }else {
            requestBase.setEntity(createJSONEntity(params));
        }
        return invoker(url, headers, requestBase, option);
    }

    public String post(String url, String content, Map<String, String> headers){
        return postForResult(url, content, headers).getResult();
    }

    public HttpResult<String> postForResult(String url, String content, Map<String, String> headers) {
        HttpPost requestBase = new HttpPost(url);
        requestBase.setEntity(new StringEntity(content, ContentType.APPLICATION_JSON));
        return invoker(url, headers, requestBase);
    }

    public HttpResult<String> invoker(String url,  Map<String, String> headers, final HttpRequestBase requestBase) {
        return invoker(url, headers, requestBase, null);
    }

    public HttpResult<String> invoker(String url,  Map<String, String> headers, final HttpRequestBase requestBase,
                                       AggHttpClient.RequestOption option) {
        long startTime = System.currentTimeMillis();
        HttpResponse response = null;
        int statusCode = 0;
        try {
            if (headers != null) {
                headers.forEach((k, v) -> requestBase.addHeader(k, v));
            }
            if (option != null) {
                requestBase.setConfig(option.toConfig(this));
            }
            response = httpclient.execute(requestBase);
            statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.SC_OK) {
                HttpEntity responseEntity = response.getEntity();
                String body = EntityUtils.toString(responseEntity, StandardCharsets.UTF_8);
                return new HttpResult<String>(response.getAllHeaders(), body);
            }
            throw new BusinessException("9999", "接口调用失败，状态码："+statusCode);
        }
        catch (ConnectTimeoutException e){
            log.error("HTTP连接超时", e);
            throw new BusinessException("0008", "HTTP连接超时，目标地址："+url, e);
        }
        catch (SocketTimeoutException e) {
            log.error("HTTP接口读取超时", e);
            throw new BusinessException("0009", "HTTP接口读取超时，目标地址："+url, e);
        }
        catch (Exception e) {
            log.error("HTTP接口异常", e);
            throw new BusinessException("0007", "HTTP接口异常，目标地址："+url, e);
        }
        finally {

            if (response != null && (response instanceof CloseableHttpResponse) ) {
                try{
                    ((CloseableHttpResponse) response).close();
                }catch (IOException e) {
                    e.printStackTrace();
                }
            }
            long executeTime = System.currentTimeMillis() - startTime;
            BwtLogger.longtimeLog(url, executeTime);
        }
    }


    private HttpEntity createJSONEntity(Map<String, Object> params) {
        String content = "{}";
        if (params != null) {
            content = gson.toJson(params);
        }
        return new StringEntity(content, ContentType.APPLICATION_JSON);
    }

    private HttpEntity createUrlEncodedFormEntity(Map<String, Object> params) {
        List<NameValuePair> pairs = new ArrayList<NameValuePair>();
        if (params != null) {
            params.forEach((k, v) -> pairs.add(new BasicNameValuePair(k, v == null ? null : v.toString())));
        }
        return new UrlEncodedFormEntity(pairs, StandardCharsets.UTF_8);
    }


    public enum RequestContentType {
        JSON, FORM
    }
}
