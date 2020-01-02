package com.bwton.agg.common.enums;

/**
 * @Description tx_channel表交易状态
 * @Author wuhaonan@bwton.com
 * @Date
 */
public enum TxChannelStatusEnum {
    //todo 这是"统一下单"交易的中间态
    SAVED(0, "已保存"),
    WAIT_PAY(1, "未支付"),
    PAID(2, "成功"),
    //todo 这是"统一下单"交易的终态
    PROCESSING(3, "渠道处理中"),
    REFUND(4, "转入退款"),
    UNKNOWN(5, "渠道未确定"),
    CLOSED(6, "已关闭"),
    FAILED(7, "失败"),

    /**
     * 转入代发，退款到银行发现用户的卡作废或者冻结了，导致原路退款银行卡失败，资金回流到商户的现金帐号，
     * 需要商户人工干预，通过线下或者平台转账的方式进行退款。
     */
    THIRD_PAID(8,"转入代发"),
    CHANNEL_NOT_ACCEPT(9,"渠道前置未受理"),

    UNDEFINED(-9, "未定义的状态"),
    ;
    private Integer code;
    private String name;

    TxChannelStatusEnum(Integer code, String name){
        this.code = code;
        this.name = name;
    }

    public Integer getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static TxChannelStatusEnum get(Integer code){
        for(TxChannelStatusEnum txChannelStatusEnum : TxChannelStatusEnum.values()){
            if(txChannelStatusEnum.getCode().equals(code)){
                return txChannelStatusEnum;
            }
        }
        return UNDEFINED;
    }
}
