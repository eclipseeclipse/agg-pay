package com.bwton.agg.common.util.logback;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import lombok.extern.slf4j.Slf4j;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * 
 * @Description: 日志打印IP获取
 * @author 李智鹏
 * @date 2018年6月13日 下午2:34:51
 *
 */
@Slf4j
public class IpConvert extends ClassicConverter {

	@Override
	public String convert(ILoggingEvent event) {
		String hostAddress = null;
		try {
			hostAddress = InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			log.error("get Ip error:", e);
		}
		return hostAddress;
	}

}
