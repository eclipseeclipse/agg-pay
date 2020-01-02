package com.bwton.agg.common.enums;

/**
 * @ClassName
 * @Description TODO
 * @Author wuhaonan@bwton.com
 * @Date
 */
public enum ChannelIdEnum {
    //交易渠道。1-银联；
    UNION(1,"银联","unionPayService"),
    ;
    private Integer code;
    private String name;
    private String channelGroup;

    ChannelIdEnum(Integer code, String name,String channelGroup){
        this.code = code;
        this.name = name;
        this.channelGroup = channelGroup;
    }

    public Integer getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getChannelGroup() {
        return channelGroup;
    }

    public static String getChannelGroup(Integer code){
        for(ChannelIdEnum channelIdEnum : ChannelIdEnum.values()){
            if(code.equals(channelIdEnum.getCode())){
                return channelIdEnum.getChannelGroup();
            }
        }
        return "";
    }
}
