package com.bwton.agg.channel.pre.enums;

/**
 * 银联接口类型
 *
 * @author DengQiongChuan
 * @date 2019-12-06 15:55
 */
public enum UnionInterfaceEnum {
    PLACE_ORDER("unified.trade.native", "统一下单"),
    PAY_QUERY("unified.trade.query", "查询订单"),
    CLOSE("unified.trade.close", "关闭订单"),
    REFUND("unified.trade.refund", "申请退款"),
    REFUND_QUERY("unified.trade.refundquery", "查询退款"),

    ;

    private String method;
    private String name;

    UnionInterfaceEnum(String method, String name) {
        this.method = method;
        this.name = name;
    }

    public String getMethod() {
        return method;
    }

    public String getName() {
        return name;
    }
}
