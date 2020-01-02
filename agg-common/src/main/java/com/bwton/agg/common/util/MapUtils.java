package com.bwton.agg.common.util;

import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * @ClassName
 * @Description TODO
 * @Author wuhaonan@bwton.com
 * @Date
 */
public class MapUtils {

    private static void putKeyValueToMap(StringBuilder temp, boolean isKey,
                                         String key, Map<String, String> map)
            throws UnsupportedEncodingException {
        if (isKey) {
            key = temp.toString();
            if (key.length() == 0) {
                throw new RuntimeException("QString format illegal");
            }
            map.put(key, "");
        } else {
            if (key.length() == 0) {
                throw new RuntimeException("QString format illegal");
            }
            map.put(key, temp.toString());
        }
    }

    /**
     * 解析应答字符串，生成应答要素
     *
     * @param str
     *            需要解析的字符串
     * @return 解析的结果map
     * @throws UnsupportedEncodingException
     */
    public static Map<String, String> parseQString(String str)
            throws UnsupportedEncodingException {

        Map<String, String> map = new HashMap<String, String>(100);
        int len = str.length();
        StringBuilder temp = new StringBuilder();
        char curChar;
        String key = null;
        boolean isKey = true;
        //值里有嵌套
        boolean isOpen = false;
        char openName = 0;
        if(len>0){
            // 遍历整个带解析的字符串
            for (int i = 0; i < len; i++) {
                // 取当前字符
                curChar = str.charAt(i);
                // 如果当前生成的是key
                if (isKey) {
                    // 如果读取到=分隔符
                    if (curChar == '=') {
                        key = temp.toString();
                        temp.setLength(0);
                        isKey = false;
                    } else {
                        temp.append(curChar);
                    }
                    // 如果当前生成的是value
                } else  {
                    if(isOpen){
                        if(curChar == openName){
                            isOpen = false;
                        }
                    //如果没开启嵌套
                    }else{
                        //如果碰到，就开启嵌套
                        if(curChar == '{'){
                            isOpen = true;
                            openName ='}';
                        }
                        if(curChar == '['){
                            isOpen = true;
                            openName =']';
                        }
                    }
                    // 如果读取到&分割符,同时这个分割符不是值域，这时将map里添加
                    if (curChar == '&' && !isOpen) {
                        putKeyValueToMap(temp, isKey, key, map);
                        temp.setLength(0);
                        isKey = true;
                    }else{
                        temp.append(curChar);
                    }
                }

            }
            putKeyValueToMap(temp, isKey, key, map);
        }
        return map;
    }

    public static Map<String, String> convertResultStringToMap(String result) {
        Map<String, String> map =null;
        try {

            if (StringUtils.isNotBlank(result)) {
                if (result.startsWith("{") && result.endsWith("}")) {
                    result = result.substring(1, result.length() - 1);
                }
                map = parseQString(result);
            }

        } catch (UnsupportedEncodingException e) {
            return null;
        }
        return map;
    }

    /**
     * 将对象转成TreeMap,属性名为key,属性值为value
     * @param object    对象
     * @return
     * @throws IllegalAccessException
     */
    public static TreeMap<String, String> objToMap(Object object) throws IllegalAccessException {

        Class clazz = object.getClass();
        TreeMap<String, String> treeMap = new TreeMap<String, String>();

        while ( null != clazz.getSuperclass() ) {
            Field[] declaredFields1 = clazz.getDeclaredFields();

            for (Field field : declaredFields1) {
                String name = field.getName();

                // 获取原来的访问控制权限
                boolean accessFlag = field.isAccessible();
                // 修改访问控制权限
                field.setAccessible(true);
                Object value = field.get(object);
                // 恢复访问控制权限
                field.setAccessible(accessFlag);

                if (null != value && StringUtils.isNotBlank(String.valueOf(value))) {
                    treeMap.put(name, String.valueOf(value));
                }
            }
            clazz = clazz.getSuperclass();
        }
        return treeMap;
    }
}
