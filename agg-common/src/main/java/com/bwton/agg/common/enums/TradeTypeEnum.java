package com.bwton.agg.common.enums;

/**
 * @Description 交易类型
 * @Author wuhaonan@bwton.com
 * @Date
 */
public enum TradeTypeEnum {
    ACTIVE_SCAN_QR_CODE(1, "主扫支付"),
    PASSIVE_SCAN_QR_CODE(2, "被扫支付"),
    CLOSE(3, "关闭订单"),
    REFUND(4, "订单退款"),
    QUERY(5, "支付查询"),
    REFUND_QUERY(6, "退款查询"),

    UNDEFINED(-9, "未定义的状态"),
    ;
    private Integer code;
    private String name;

    TradeTypeEnum(Integer code,String name){
        this.code = code;
        this.name = name;
    }

    public Integer getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static TradeTypeEnum get(Integer code){
        for(TradeTypeEnum tradeTypeEnum : TradeTypeEnum.values()){
            if(tradeTypeEnum.getCode().equals(code)){
                return tradeTypeEnum;
            }
        }
        return UNDEFINED;
    }
}
