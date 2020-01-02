package com.bwton.agg.common.enums;

/**
 * @Description mq延迟发送消息级别<br/>
 * @author 李智鹏<br/>
 * @date 2017年12月27日上午9:45:26<br/>
 */
public enum MqDelayTimeLevelEnum {
	// 1s 5s 10s 30s 1m 2m 3m 4m 5m 6m 7m 8m 9m 10m 20m 30m 1h 2h
	NO_DELAY(0, "即时发送mq消息"),
	DELAY_1_SECOND(1, "延迟1秒发送"),
	DELAY_5_SECOND(2, "延迟5秒发送"),
	DELAY_10_SECOND(3, "延迟10秒发送"),
	DELAY_30_SECOND(4, "延迟30秒发送"),
	DELAY_1_MINUTE(5, "延迟1分钟发送"),
	DELAY_2_MINUTE(6, "延迟2分钟发送"),
	DELAY_3_MINUTE(7, "延迟3分钟发送"),
	DELAY_4_MINUTE(8, "延迟4分钟发送"),
	DELAY_5_MINUTE(9, "延迟5分钟发送"),
	DELAY_6_MINUTE(10, "延迟6分钟发送"),
	DELAY_7_MINUTE(11, "延迟7分钟发送"),
	DELAY_8_MINUTE(12, "延迟8分钟发送"),
	DELAY_9_MINUTE(13, "延迟9分钟发送"),
	DELAY_10_MINUTE(14, "延迟10分钟发送"),
	DELAY_20_MINUTE(15, "延迟20分钟发送"),
	DELAY_30_MINUTE(16, "延迟30分钟发送"),
	DELAY_1_HOUR(17, "延迟1小时发送"),
	DELAY_2_HOUR(18, "延迟2小时发送");

	private int level;
	private String name;

	MqDelayTimeLevelEnum(int level, String name) {
		this.level = level;
		this.name = name;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public String getName() {
		return name;
	}
}
