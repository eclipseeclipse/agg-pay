package com.bwton.agg.common.util;

import java.util.UUID;

/**
 * @author DengQiongChuan
 * @date 2019-12-18 20:56
 */
public class UuidUtils {

    public static String generateUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }

}
