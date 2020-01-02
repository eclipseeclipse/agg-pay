package com.bwton.agg.common.util;

import com.google.common.base.Joiner;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author DengQiongChuan
 * @date 2019-12-05 11:10
 */
public class SignUtils {

    public static String sign4Map(String secureKey, TreeMap<String, String> treeMap) {
        if (secureKey == null || treeMap == null || treeMap.isEmpty()) {
            return "";
        }
        List<String> paramList = new ArrayList<>();
        for (Map.Entry<String, String> entry : treeMap.entrySet()) {
            if ("class".equalsIgnoreCase(entry.getKey()) || "sign".equalsIgnoreCase(entry.getKey())
                    || StringUtils.isBlank(entry.getValue()) || "null".equals(entry.getValue())) {
                continue;
            }
            paramList.add(entry.getKey() + "=" + entry.getValue());
        }
        paramList.add("key=" + secureKey);
        String paramJoin = Joiner.on("&").join(paramList);
        return DigestUtils.md5Hex(paramJoin).toUpperCase();
    }

    public static <T> String sign(String secureKey, T bean) {
        if (StringUtils.isBlank(secureKey) || bean == null) {
            return "";
        }
        TreeMap<String, String> treeMap;
        try {
            treeMap = MapUtils.objToMap(bean);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("签名时对象转Map发生异常", e);
        }
        return sign4Map(secureKey, treeMap);
    }
}
