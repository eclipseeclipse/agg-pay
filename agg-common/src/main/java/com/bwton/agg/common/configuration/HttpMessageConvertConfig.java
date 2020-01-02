package com.bwton.agg.common.configuration;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.xml.internal.bind.marshaller.CharacterEscapeHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.xml.Jaxb2RootElementHttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.print.attribute.standard.MediaSize;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName
 * @Description TODO
 * @Author wuhaonan@bwton.com
 * @Date
 */
@Configuration
public class HttpMessageConvertConfig extends WebMvcConfigurerAdapter {
    @Bean
    public Gson gson() {
        final GsonBuilder builder = new GsonBuilder();
        builder.disableHtmlEscaping();
        builder.setDateFormat("yyyyMMddHHmmss");
        builder.serializeNulls();
        return builder.create();
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter< ? >> converters) {
        converters.removeIf(httpMessageConverter -> httpMessageConverter instanceof MappingJackson2HttpMessageConverter);

        StringHttpMessageConverter stringHttpMessageConverter = new StringHttpMessageConverter();
        stringHttpMessageConverter.setDefaultCharset(StandardCharsets.UTF_8);
        converters.add(stringHttpMessageConverter);

        GsonHttpMessageConverter gsonHttpMessageConverter = new GsonHttpMessageConverter();
        gsonHttpMessageConverter.setDefaultCharset(StandardCharsets.UTF_8);
        gsonHttpMessageConverter.setGson(gson());
        converters.add(gsonHttpMessageConverter);

        List<MediaType> jaxbSupportedMediaTypes = new ArrayList<>(2);
        jaxbSupportedMediaTypes.add(MediaType.APPLICATION_XML);
        jaxbSupportedMediaTypes.add(MediaType.TEXT_XML);
        Jaxb2RootElementHttpMessageConverter jaxbMsgConverter = customJaxb2RootElementHttpMessageConverter();
        jaxbMsgConverter.setDefaultCharset(StandardCharsets.UTF_8);
        jaxbMsgConverter.setSupportedMediaTypes(jaxbSupportedMediaTypes);
        converters.add(jaxbMsgConverter);
    }

    private Jaxb2RootElementHttpMessageConverter customJaxb2RootElementHttpMessageConverter() {
        return new Jaxb2RootElementHttpMessageConverter() {
            @Override
            protected void customizeMarshaller(Marshaller marshaller) {
                super.customizeMarshaller(marshaller);
                // <! 不转义
                try {
                    marshaller.setProperty(CharacterEscapeHandler.class.getName(),
                            new CharacterEscapeHandler() {
                                @Override
                                public void escape(char[] ac, int i, int j, boolean flag, Writer writer) throws IOException {
                                    writer.write(ac, i, j);
                                }
                            });
                } catch (PropertyException e) {
                    throw new HttpMessageNotWritableException("marshaller set property exception", e);
                }
            }
        };
    }

}
