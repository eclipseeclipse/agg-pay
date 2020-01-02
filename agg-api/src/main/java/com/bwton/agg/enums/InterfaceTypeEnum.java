package com.bwton.agg.enums;

/**
 * 接口类型
 *
 * @author DengQiongChuan
 * @date 2019-12-06 15:55
 */
public enum InterfaceTypeEnum {
    PLACE_ORDER("unified.trade.native", "统一下单"),
    QUERY("unified.trade.query", "查询订单"),
    CLOSE("unified.trade.close", "关闭订单"),
    REFUND("unified.trade.refund", "申请退款"),
    REFUND_QUERY("unified.trade.refundquery", "查询退款"),

    UNDEFINED("undefined", "未定义的接口"),
    ;

    private String method;
    private String name;

    InterfaceTypeEnum(String method, String name) {
        this.method = method;
        this.name = name;
    }

    public String getMethod() {
        return method;
    }

    public String getName() {
        return name;
    }

    public static InterfaceTypeEnum getEnum(String method){
        for(InterfaceTypeEnum interfaceTypeEnum : InterfaceTypeEnum.values()){
            if(interfaceTypeEnum.getMethod().equals(method)) {
                return interfaceTypeEnum;
            }
        }
        return UNDEFINED;
    }

}
