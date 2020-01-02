package com.bwton.agg.common.enums;

/**
 * @Description tx_trade交易状态枚举
 * @Author wuhaonan@bwton.com
 * @Date
 */
public enum TxTradeStatusEnum {
    WAIT_PAY(1, "未支付"),
    PAID(2, "成功"),
    PROCESSING(3, "处理中"),
    REFUND(4, "转入退款"),
    NOTSURE(5, "未确定"),
    CLOSED(6, "已关闭"),
    FAILED(7, "失败"),
    THIRD_PAID(8, "转入代发"),

    UNDEFINED(-9, "UNDEFINED"),
    ;
    private Integer code;
    private String name;

    TxTradeStatusEnum(Integer code,String name){
        this.code = code;
        this.name = name;
    }

    public Integer getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static TxTradeStatusEnum getEnum(Integer code){
        for(TxTradeStatusEnum txTradeStatusEnum : TxTradeStatusEnum.values()){
            if(code.equals(txTradeStatusEnum.getCode())){
                return txTradeStatusEnum;
            }
        }
        return UNDEFINED;
    }
}
