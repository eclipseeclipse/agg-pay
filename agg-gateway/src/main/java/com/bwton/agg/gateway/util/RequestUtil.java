package com.bwton.agg.gateway.util;

import org.springframework.util.StreamUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * @author DengQiongChuan
 * @date 2019-12-26 15:57
 */
public class RequestUtil {

    /**
     * 获得请求体内容
     */
    public static String getRequestBody(HttpServletRequest request) throws IOException {
        InputStream in = request.getInputStream();
        return StreamUtils.copyToString(in, StandardCharsets.UTF_8);
    }

}
