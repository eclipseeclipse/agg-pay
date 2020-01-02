package com.bwton.agg.common.enums;

/**
 * @author yangqing
 * @description copy from tpsp,MQ枚举类
 * @create 2019/4/16 11:07 AM
 */
public enum MqTagsEnum {
    TAG_TEST("tag_test", "tpdc-calc", "测试mq", 0),
    TAG_CALC_ASYNC("tag_calc_async", "tpdc-calc", "优惠异步计算", 0),
    TAG_CALC_RESULT("tag_calc_result", "tpdc-calc", "优惠计算结果反馈", 0),
    TAG_CALC_RESULT_NOTIFY("tag_calc_result_notify", "tpdc-calc", "优惠计算结果通知", 0),
    ;

    private String tags;
    private String topic;
    private String name;
    private int reconsumeTimes;

    MqTagsEnum(String tags, String topic, String name, int reconsumeTimes) {
        this.tags = tags;
        this.topic = topic;
        this.name = name;
        this.reconsumeTimes = reconsumeTimes;
    }

    public String getTags() {
        return tags;
    }

    public String getTopic() {
        return topic;
    }

    public String getName() {
        return name;
    }

    public int getReconsumeTimes() {
        return reconsumeTimes;
    }

    @Override
    public String toString() {
        return "topic=" + topic + ",tags=" + tags + ",name=" + name + ",reconsumeTimes=" + reconsumeTimes;
    }

    public static MqTagsEnum getTagsEnum(String tags) {
        for (MqTagsEnum c : MqTagsEnum.values()) {
            if (c.tags.equals(tags)) {
                return c;
            }
        }
        return null;
    }
}
