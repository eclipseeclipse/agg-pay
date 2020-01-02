package com.bwton.agg.common.util;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.StaxDriver;

/**
 * @author DengQiongChuan
 * @date 2019-12-05 13:54
 */
public class XStreamUtils {

    private static class XStreamHolder {
        static XStream instance = new XStream(new StaxDriver());
    }

    public static XStream getInstance() {
        return XStreamHolder.instance;
    }

}
