package com.bwton.agg.common.util;

import org.slf4j.MDC;

public class TraceIDUtils {
	private static final ThreadLocal<String> SEQUENCE_ID = new ThreadLocal<String>();

	public static final String LOG_KEY = "traceId";

	public static String getSequenceId() {
		return SEQUENCE_ID.get();
	}

	public static void setSequenceId(String sequenceId) {
		SEQUENCE_ID.set(sequenceId);
	}

	public static void setMCData(String sequence) {
		TraceIDUtils.setSequenceId(sequence);
		MDC.put(LOG_KEY, sequence);
	}
}
