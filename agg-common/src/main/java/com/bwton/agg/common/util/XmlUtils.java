package com.bwton.agg.common.util;

import com.sun.xml.internal.bind.marshaller.CharacterEscapeHandler;
import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.bind.DataBindingException;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.stream.StreamSource;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author DengQiongChuan
 * @date 2019-12-07 21:20
 */
public class XmlUtils {
    private static Map<Class<?>, JAXBContext> jaxbContextMap = new ConcurrentHashMap<>();

    public static JAXBContext getJAXBContext(Class<?> clazz) throws DataBindingException {
        JAXBContext jaxbContext = jaxbContextMap.get(clazz);
        if (jaxbContext != null) {
            return jaxbContext;
        } else {
            try {
                jaxbContext = JAXBContext.newInstance(clazz);
                jaxbContextMap.put(clazz, jaxbContext);
                return jaxbContext;
            } catch (JAXBException e) {
                throw new DataBindingException(e);
            }
        }
    }

    public static <T> String toXml(T bean, Class<T> type) throws DataBindingException {
        JAXBContext jaxbContext = getJAXBContext(type);
        StringWriter sw = new StringWriter();
        try {
            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, false);
            marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);
            marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
            // <! 不转义
            marshaller.setProperty(CharacterEscapeHandler.class.getName(),
                    new CharacterEscapeHandler() {
                        @Override
                        public void escape(char[] ac, int i, int j, boolean flag, Writer writer) throws IOException {
                            writer.write(ac, i, j);
                        }
                    });
            marshaller.marshal(bean, sw);
        } catch (JAXBException e) {
            throw new DataBindingException(e);
        }
        return sw.toString();
    }

    public static String toXml(Map<String, String> map) {
        if (map == null || map.isEmpty()) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("<xml>");
        for (Map.Entry<String, String> entry : map.entrySet()) {
            sb.append("<").append(entry.getKey()).append(">");
            sb.append("<![CDATA[").append(entry.getValue()).append("]]>");
            sb.append("</").append(entry.getKey()).append(">");
        }
        sb.append("</xml>");
        return sb.toString();
    }

    public static <T> T fromXml(String xml, Class<T> type) throws DataBindingException {
        JAXBContext jaxbContext = getJAXBContext(type);
        try {
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            JAXBElement<T> jaxbElement = unmarshaller.unmarshal(new StreamSource(new StringReader(xml)), type);
            return jaxbElement.getValue();
        } catch (JAXBException e) {
            throw new DataBindingException(e);
        }
    }

    /**
     * XML格式字符串转换为TreeMap
     *
     * @param xml XML字符串
     * @return XML数据转换后的Map
     * @throws RuntimeException
     */
    public static TreeMap<String, String> toMap(String xml) throws RuntimeException {
        if (xml == null) return null;
        TreeMap<String, String> data = new TreeMap<>();
        xmlToMap(xml, data);
        return data;
    }

    private static void xmlToMap(String xml, Map<String, String> data) throws RuntimeException {
        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            InputStream stream = new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8));
            org.w3c.dom.Document doc = documentBuilder.parse(stream);
            doc.getDocumentElement().normalize();
            NodeList nodeList = doc.getDocumentElement().getChildNodes();
            for (int idx = 0; idx < nodeList.getLength(); ++idx) {
                Node node = nodeList.item(idx);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    org.w3c.dom.Element element = (org.w3c.dom.Element) node;
                    if(StringUtils.isNotEmpty(element.getTextContent())){
                        data.put(element.getNodeName(), element.getTextContent());
                    }
                }
            }
            try {
                stream.close();
            } catch (Exception ex) {
                // do nothing
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
