package com.bwton.agg.enums;

/**
 * 渠道枚举
 *
 * @author DengQiongChuan
 * @date 2019-12-12 10:51
 */
public enum ChannelEnum {
    UNION(1, "银联"),

    UNKNOW(-1, "未知渠道"),
    ;

    private Integer id;
    private String name;

    ChannelEnum(Integer id, String name){
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public static ChannelEnum get(Integer id){
        for(ChannelEnum channelEnum : ChannelEnum.values()){
            if(id.equals(channelEnum.getId())){
                return channelEnum;
            }
        }
        return UNKNOW;
    }
}
