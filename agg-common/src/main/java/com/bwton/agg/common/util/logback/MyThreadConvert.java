package com.bwton.agg.common.util.logback;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;

/**
 * 
 * @Description: 日志线程打印
 * @author 李智鹏
 * @date 2018年6月13日 下午2:35:11
 *
 */
public class MyThreadConvert extends ClassicConverter {

	@Override
	public String convert(ILoggingEvent event) {
		return event.getThreadName().split("\\|")[0];
	}

}
