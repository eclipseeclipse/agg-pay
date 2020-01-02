package com.bwton.agg.annotations;

import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * @author DengQiongChuan
 * @date 2019-12-14 15:18
 */
public class AdapterCDATA extends XmlAdapter<String, String> {

    @Override
    public String marshal(String arg) throws Exception {
        return "<![CDATA[" + arg + "]]>";
    }
    @Override
    public String unmarshal(String arg) throws Exception {
        return arg;
    }

}
